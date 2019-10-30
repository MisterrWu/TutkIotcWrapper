package com.wh.tutkwrapper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;
import com.tutk.IOTC.RDTAPIs;
import com.tutk.IOTC.St_RDT_Status;
import com.wh.tutkwrapper.helper.WorkThread;


public class TuTkClient {

    private static final String TAG = "TuTkClient";

    private static final int MSG_DIRECTION = 1; // 云台设置
    private static final int MSG_DISCONNECT = MSG_DIRECTION + 1;
    private static final int MSG_CONNECT = MSG_DISCONNECT + 1;
    private static final int MSG_RELEASE = MSG_CONNECT + 1;
    private static final int MSG_SEND_IO_MESSAGE_REQ_MSG = MSG_RELEASE + 1;
    private static final int MSG_PLAYBACK = MSG_SEND_IO_MESSAGE_REQ_MSG + 1;
    private static final int MSG_LIVE = MSG_PLAYBACK + 1;
    private static final int MSG_PAUSE = MSG_LIVE + 1;
    private static final int MSG_RESUME = MSG_PAUSE + 1;
    private static final int MSG_START_TALK = MSG_RESUME + 1;
    private static final int MSG_STOP_TALK = MSG_START_TALK + 1;
    private static final int MSG_DEFINITION = MSG_STOP_TALK + 1;

    private static final int PLAY_STATUS_LIVE = 0;
    private static final int PLAY_STATUS_BACK = 1;

    private static final int STATUS_CONNECTING = 0;
    private static final int STATUS_CONNECTED = 1;
    private static final int STATUS_UNCONNECTED = 2;

    private volatile int mPlayStatus = PLAY_STATUS_LIVE;
    private volatile int mConnectStatus = STATUS_UNCONNECTED;

    private final HandlerThread mTuTkHandlerThread;
    private final TuTkHandler mTuTkHandler;
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());
    private IClientListener mClientListener;

    // tutk 链接
    private PwdKey mPwdKey;
    private int definition;
    private String key;
    private int sid = -1;
    private int mAvIndex = -1;

    // 对讲
    private volatile int mAudioIndex = -1;
    private volatile int mFreeChannel = -1;
    // 回看数据
    private volatile int mRdtChiD = -1;
    private volatile int mRdtId = -1;

    private boolean needRecvMsg;
    private boolean needRecordData;

    private OperationRecvVideo operationRecvVideo;
    private OperationMessage operationMessage;
    private OperationRDT operationRDT;

    private TuTkClient(String key, boolean needRecvMsg, boolean needRecordData) {
        this.key = key;
        this.needRecordData = needRecordData;
        this.needRecvMsg = needRecvMsg;
        mTuTkHandlerThread = new HandlerThread("TuTkClient ");
        mTuTkHandlerThread.start();
        mTuTkHandler = new TuTkHandler(mTuTkHandlerThread.getLooper());
    }

    public static final class Builder {

        private String tag;
        private boolean needRecvMsg;
        private boolean needRecordData;

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder needRecvMsg(boolean needRecvMsg) {
            this.needRecvMsg = needRecvMsg;
            return this;
        }

        public Builder needRecordData(boolean needRecordData) {
            this.needRecordData = needRecordData;
            return this;
        }

        public TuTkClient build() {
            if (TextUtils.isEmpty(tag)) {
                tag = "Empty";
            }
            return new TuTkClient(tag, needRecvMsg, needRecordData);
        }
    }

    private void releaseThread() {
        mTuTkHandler.removeCallbacksAndMessages(null);
        mTuTkHandlerThread.quit();
    }

    private class TuTkHandler extends Handler {

        TuTkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CONNECT:
                    try {
                        doConnectIOTC();
                        doInitIOTCRDT();
                        doInitOperation();
                        onConnected();
                    } catch (TuTkException e) {
                        mConnectStatus = STATUS_UNCONNECTED;
                        onError(e.getError(), e.getMessage());
                    }
                    break;
                case MSG_DISCONNECT:
                    doStopOperation();
                    doCloseTalk();
                    doStopIOTCRDT();
                    doStopConnectIOTC();
                    onDisConnected();
                    break;
                case MSG_PAUSE:
                    try {
                        pauseReceiveDataThread();
                        doPauseVideo();
                        onPaused();
                    } catch (TuTkException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_RESUME:
                    try {
                        resumeReceiveDataThread();
                        doResumeVideo();
                        onStarted();
                    } catch (TuTkException e) {
                        onError(e.getError(), e.getMessage());
                    }
                    break;
                case MSG_LIVE:
                    try {
                        resumeReceiveDataThread();
                        doStartLive();
                        onStarted();
                    } catch (TuTkException e) {
                        onError(e.getError(), e.getMessage());
                    }
                    break;
                case MSG_PLAYBACK:
                    try {
                        resumeReceiveDataThread();
                        doStartPlayBack((Long) msg.obj);
                        onStarted();
                    } catch (TuTkException e) {
                        onError(e.getError(), e.getMessage());
                    }
                    break;
                case MSG_RELEASE:
                    break;
                case MSG_DEFINITION:
                    try {
                        doChangeDefinition(msg.arg1);
                    } catch (TuTkException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_SEND_IO_MESSAGE_REQ_MSG:
                    try {
                        doAvSendIOCtrl((IOCtrlMessage) msg.obj);
                    } catch (TuTkException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_START_TALK:
                    try {
                        doOpenTalk((IOCtrlMessage) msg.obj);
                        onStartTalk();
                    } catch (TuTkException e) {
                        onError(e.getError(), e.getMessage());
                    }
                    break;
                case MSG_STOP_TALK:
                    doCloseTalk();
                    onStopTalk();
                    break;
                default:
            }
        }
    }

    /*********** 提供给外部的接口 **************************************/
    private void connect() {
        mConnectStatus = STATUS_CONNECTING;
        sendEmptyMessage(MSG_CONNECT);
    }

    private void disConnect() {
        mConnectStatus = STATUS_UNCONNECTED;
        sendEmptyMessage(MSG_DISCONNECT);
    }

    public void reConnect(PwdKey pwdKey,int definition){
        this.mPwdKey = pwdKey;
        this.definition = definition;
        disConnect();
        connect();
    }

    public void reConnect(PwdKey pwdKey){
        this.reConnect(pwdKey,definition);
    }

    public boolean isConnecting(){
        return mConnectStatus == STATUS_CONNECTING;
    }

    public boolean isConnected(){
        return mConnectStatus == STATUS_CONNECTED;
    }

    public boolean isUnconnected(){
        return mConnectStatus == STATUS_UNCONNECTED;
    }

    public void startPlayLive() {
        sendEmptyMessage(MSG_LIVE);
    }

    public void startPlayRecord(long relativeTime) {
        Message msg = mTuTkHandler.obtainMessage(MSG_PLAYBACK);
        msg.obj = relativeTime;
        msg.sendToTarget();
    }

    public void resume() {
        sendEmptyMessage(MSG_RESUME);
    }

    public void pause(){
        sendEmptyMessage(MSG_PAUSE);
    }

    public void stop() {
        sendEmptyMessage(MSG_RELEASE);
    }

    public void interruptRDT() {
        if(operationRDT != null){
            operationRDT.interrupt();
        }
    }

    public void setDefinition(int definition) {
        this.definition = definition;
        Message msg = mTuTkHandler.obtainMessage(MSG_DEFINITION);
        msg.arg1 = definition;
        msg.sendToTarget();
    }

    public void sendIOCtrl(IOCtrlMessage ctrlMessage) {
        Message msg = mTuTkHandler.obtainMessage(MSG_SEND_IO_MESSAGE_REQ_MSG);
        msg.obj = ctrlMessage;
        msg.sendToTarget();
    }

    public void startTalk(IOCtrlMessage aecMsg) {
        Message msg = mTuTkHandler.obtainMessage(MSG_START_TALK);
        msg.obj = aecMsg;
        msg.sendToTarget();
    }

    public void stopTalk() {
        sendEmptyMessage(MSG_STOP_TALK);
    }

    public void sendAudioData(byte[] data, int dataSize, byte[] pFrmInfo, int frmInfoSize) {
        if (mAudioIndex >= 0) {
            AVAPIs.lmAvSendAudioData(mAudioIndex, data, dataSize, pFrmInfo, frmInfoSize);
        }
    }

    private void sendEmptyMessage(int type) {
        if (mTuTkHandler != null) {
            mTuTkHandler.sendEmptyMessage(type);
        }
    }

    /********** 实现接口，运行在 HandlerThread ****************/

    private void resumeReceiveDataThread() {
        if(operationRecvVideo != null){
            if(operationRecvVideo.getStatus() == WorkThread.STOP){
                operationRecvVideo.start();
            } else if(operationRecvVideo.getStatus() == WorkThread.SUSPEND){
                operationRecvVideo.setResume();
            }
        }
    }

    private void pauseReceiveDataThread() {
        if(operationRecvVideo != null){
            if(operationRecvVideo.getStatus() == WorkThread.RUNNING){
                operationRecvVideo.setSuspend();
            }
        }
    }

    private void doConnectIOTC() throws TuTkException {
        if(mPwdKey == null){
            return;
        }
        Log.e(TAG, "Connection lmIOTC_Session_Check SID " + sid + ",mAvIndex " + mAvIndex);
        onProgress(17);
        int mRet = TuTkManager.instance().initIOTC();
        if (mRet < 0) {
            throw new TuTkException(mRet, TuTkManager.instance().getErrorInfo(mRet));
        }
        onProgress(22);
        mRet = IOTCAPIs.lmIOTC_Get_SessionID();
        Log.e(TAG, "Connection lmIOTC_Get_SessionID " + mRet);
        if (mRet < 0) {
            throw new TuTkException(mRet, TuTkManager.instance().getErrorInfo(mRet));
        }
        onProgress(36);
        sid = mRet;
        mRet = IOTCAPIs.lmIOTC_Connect_ByUID_Parallel(mPwdKey.p2pId, sid);
        Log.e(TAG, "Connection lmIOTC_Connect_ByUID_Parallel " + mRet);
        if (mRet < 0) {
            throw new TuTkException(mRet, TuTkManager.instance().getErrorInfo(mRet));
        }
        onProgress(40);
        int[] srvType = new int[1];
        int[] bResend = new int[1];
        String account = mPwdKey.mAccount;
        onProgress(46);
        mRet = AVAPIs.lmAvClientStart2(sid, account, mPwdKey.mPwd, 10, srvType, 0, bResend);
        Log.e(TAG, "Connection lmAvClientStart2 " + mAvIndex);
        if (mRet < 0) {
            throw new TuTkException(mRet, TuTkManager.instance().getErrorInfo(mRet));
        }
        mAvIndex = mRet;
        onProgress(52);
        int ret = AVAPIs.lmAvSendIOCtrl(mAvIndex, AVAPIs.IOTYPE_INNER_SND_DATA_DELAY,
                new byte[2], 2);
        Log.e(TAG, "Connection IOTYPE_INNER_SND_DATA_DELAY " + ret);
        onProgress(60);
    }

    private void doInitIOTCRDT() throws TuTkException {
        if(!needRecordData){
            return;
        }
        int mRet = IOTCAPIs.lmIOTC_Session_Get_Free_Channel(sid);
        if(mRet < 0){
            throw new TuTkException(mRet,TuTkManager.instance().getErrorInfo(mRet));
        }
        mRdtChiD = mRet;
        doAvSendIOCtrl(IOCtrlMessage.createSendRdtMsg(mRdtChiD));

        mRet = RDTAPIs.LM_RDT_Create(sid, 10000, mRdtChiD);
        Log.e(TAG, "RDTWriteAndRead: RDT_Create: " + mRet);
        if(mRet < 0){
            throw new TuTkException(mRet,TuTkManager.instance().getErrorInfo(mRet));
        }
        mRdtId = mRet;
        St_RDT_Status status = new St_RDT_Status();
        RDTAPIs.RDT_Status_Check(mRet, status);
    }

    private void doInitOperation() {
        doInitOperationRDT();
        doInitOperationMessage();
        doInitOperationRecvVideo();
    }

    private void doInitOperationRDT(){
        if(operationRDT != null){
            operationRDT.setRdtId(mRdtId);
        } else if(needRecordData) {
            operationRDT = new OperationRDT();
            operationRDT.setRdtId(mRdtId);
            operationRDT.setCallBack(this::onRdtData);
            operationRDT.start();
        }
    }

    private void doInitOperationMessage(){
        if(operationMessage != null){
            operationMessage.setAvIndex(mAvIndex);
        } else if(needRecvMsg){
            operationMessage = new OperationMessage();
            operationMessage.setAvIndex(mAvIndex);
            operationMessage.setCtrlDataListener(new OperationMessage.OnCtrlDataListener() {
                @Override
                public void onCtrlData(IOCtrlMessage msg) {
                    TuTkClient.this.onCtrlData(msg);
                }

                @Override
                public void onError(int code, String info) {
                    TuTkClient.this.onError(code,info);
                }
            });
            operationMessage.start();
        }
    }

    private void doInitOperationRecvVideo(){
        if(mPwdKey == null){
            return;
        }
        if(operationRecvVideo != null){
            operationRecvVideo.setAvIndex(mAvIndex);
        } else {
            operationRecvVideo = new OperationRecvVideo();
            operationRecvVideo.setAvIndex(mAvIndex);
            operationRecvVideo.setRecvFrameListener(new OperationRecvVideo.OnAvRecvFrameListener() {
                @Override
                public void onAvRecvFrameData(byte[] info, byte[] buffer) {
                    if (mClientListener != null) {
                        mClientListener.onFrameData(TuTkClient.this, info, buffer);
                    }
                }

                @Override
                public void onAvRecvFrameFailed(int errCode) {
                    onError(errCode, TuTkManager.instance().getErrorInfo(errCode));
                }
            });
        }
    }

    private void doStopOperation(){
        if(operationRecvVideo != null){
            operationRecvVideo.setAvIndex(-1);
            operationRecvVideo.setStop();
            operationRecvVideo.interrupt();
            operationRecvVideo = null;
        }
        if(operationMessage != null){
            operationMessage.setAvIndex(-1);
            operationMessage.setStop();
            operationMessage.interrupt();
            operationMessage = null;
        }

        if(operationRDT != null){
            operationRDT.setRdtId(-1);
            operationRDT.setStop();
            operationRDT.interrupt();
            operationRDT = null;
        }
    }

    private void doStopIOTCRDT(){
        if(mRdtId > -1){
            RDTAPIs.RDT_Abort(mRdtId);
            RDTAPIs.RDT_Create_Exit(sid,mRdtChiD);
            RDTAPIs.RDT_Destroy(mRdtId);
            mRdtChiD = -1;
            mRdtId = -1;
        }
    }

    private void doStopConnectIOTC() {

        Log.e(TAG, "Connection stopConnectIOTC: mAvIndex " + mAvIndex + ",sid " + sid);
        if (mAvIndex > -1) {
            try {
                doAvSendIOCtrl(new IOCtrlMessage(TUTKConstant.IIOTYPE_USER_IPCAM_PAUSE_PLAYBACK_REQ));
                doAvSendIOCtrl(IOCtrlMessage.createVideoStopMsg(definition));
            } catch (TuTkException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Connection quitIOTC: lmAvClientStop  mAvIndex " + mAvIndex);
            AVAPIs.lmAvClientCleanVideoBuf(mAvIndex);
            AVAPIs.lmAvClientCleanAudioBuf(mAvIndex);
            AVAPIs.lmAvClientStop(mAvIndex);
            AVAPIs.lmAvClientExit(sid, mAvIndex);
            mAvIndex = -1;
        }

        if (sid > -1) {
            Log.e(TAG, "Connection quitIOTC: lmIOTC_Session_Close  " + sid);
            IOTCAPIs.lmIOTC_Session_Close(sid);
            sid = -1;
        }
    }

    private void doPauseVideo() throws TuTkException {
        if (mPlayStatus == PLAY_STATUS_LIVE) {
            doAvSendIOCtrl(IOCtrlMessage.createVideoStopMsg(definition));
        } else {
            doAvSendIOCtrl(new IOCtrlMessage(TUTKConstant.IIOTYPE_USER_IPCAM_PAUSE_PLAYBACK_REQ));
        }
    }

    private void doResumeVideo() throws TuTkException {
        if (mPlayStatus == PLAY_STATUS_LIVE) {
            if (mPwdKey != null) {
                doAvSendIOCtrl(IOCtrlMessage.createVideoStartMsg(mPwdKey.uuid, definition));
            }
        } else {
            doAvSendIOCtrl(new IOCtrlMessage(TUTKConstant.IIOTYPE_USER_IPCAM_RESUME_PLAYBACK_REQ));
        }
    }

    private void doStartPlayBack(long relativeTime) throws TuTkException {
        // 如果是直播先关掉直播
        if (mPlayStatus == PLAY_STATUS_LIVE) {
            doAvSendIOCtrl(IOCtrlMessage.createVideoStopMsg(definition));
        }
        doAvSendIOCtrl(IOCtrlMessage.createPlayRecordMsg(relativeTime));
        mPlayStatus = PLAY_STATUS_BACK;
    }

    private void doStartLive() throws TuTkException {
        // 如果是回看先关掉回看
        if (mPlayStatus == PLAY_STATUS_BACK) {
            doAvSendIOCtrl(new IOCtrlMessage(TUTKConstant.IIOTYPE_USER_IPCAM_PAUSE_PLAYBACK_REQ));
        }
        if (mPwdKey != null) {
            doAvSendIOCtrl(IOCtrlMessage.createVideoStartMsg(mPwdKey.uuid, definition));
        }
        mPlayStatus = PLAY_STATUS_LIVE;
    }

    private void doChangeDefinition(int definition) throws TuTkException {
        // 摄像头重启时会阻塞线程，无法返回获取结果
        doAvSendIOCtrl(IOCtrlMessage.createDefinitionMsg(definition));
    }

    private void doAvSendIOCtrl(IOCtrlMessage ctrlMessage) throws TuTkException {
        if (mAvIndex >= 0) {
            if (ctrlMessage.ioCtrlBuf == null) {
                ctrlMessage.ioCtrlBuf = new byte[1];
            }
            int ret = AVAPIs.lmAvSendIOCtrl(mAvIndex, ctrlMessage.ioType, ctrlMessage.ioCtrlBuf, ctrlMessage.getIoCtrlBufSize());
            if (ret < 0) {
                throw new TuTkException(ret, TuTkManager.instance().getErrorInfo(ret));
            }
        }
    }

    private void doOpenTalk(IOCtrlMessage aecMsg) throws TuTkException {
        int mRet = mFreeChannel;
        if (mRet < 0) {
            mRet = IOTCAPIs.lmIOTC_Session_Get_Free_Channel(sid);
        }
        if (mRet < 0) {
            throw new TuTkException(mRet, TuTkManager.instance().getErrorInfo(mRet));
        }
        mFreeChannel = mRet;

        doAvSendIOCtrl(IOCtrlMessage.createSpeakerStartMsg(mFreeChannel));

        mRet = AVAPIs.lmAvServStart(sid, null, null, 5, TUTKConstant.TYPE_SERVER_STREAMING
                , mFreeChannel);
        if (mRet < 0) {
            Log.e(TAG, "lmAvServStart mAudioIndex=" + mRet + ",SID:" + sid);
            throw new TuTkException(mRet, TuTkManager.instance().getErrorInfo(mRet));
        }
        mAudioIndex = mRet;
        if (aecMsg != null) {
            // 回音消除相关
            doAvSendIOCtrl(aecMsg);
        }
    }

    private void doCloseTalk() {
        if (mFreeChannel >= 0) {
            try {
                doAvSendIOCtrl(IOCtrlMessage.createSpeakerStopMsg(mFreeChannel));
            } catch (TuTkException e) {
                e.printStackTrace();
            }
        }
        if (mAudioIndex >= 0) {
            AVAPIs.lmAvServStop(mAudioIndex);
            mAudioIndex = -1;
        }
        if (mFreeChannel >= 0) {
            AVAPIs.lmAvServExit(sid, mFreeChannel);
            mFreeChannel = -1;
        }
    }

    /*********** 回调接口,运行在 MainThread *****************************/
    private void onProgress(final int progress) {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onProgress(TuTkClient.this, progress);
            }
        });
    }

    private void onError(int code, String info) {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onError(TuTkClient.this, code, info);
            }
        });
    }

    private void onConnected() {
        mMainHandler.post(() -> {
            mConnectStatus = STATUS_CONNECTED;
            if (mClientListener != null) {
                mClientListener.onConnected(TuTkClient.this);
            }
        });
    }

    private void onDisConnected() {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onDisConnected(TuTkClient.this);
            }
        });
    }

    private void onStarted() {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onStarted(TuTkClient.this);
            }
        });
    }

    private void onCtrlData(IOCtrlMessage msg) {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onCtrlData(TuTkClient.this, msg);
            }
        });
    }

    private void onPaused() {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onPaused(TuTkClient.this);
            }
        });
    }

    private void onStartTalk() {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onStartTalk(TuTkClient.this);
            }
        });
    }

    private void onStopTalk() {
        mMainHandler.post(() -> {
            if (mClientListener != null) {
                mClientListener.onStopTalk(TuTkClient.this);
            }
        });
    }

    private void onRdtData(byte[] buffer, boolean isEnd){
        mMainHandler.post(() -> {
            if(mClientListener != null){
                mClientListener.onRdtData(buffer, isEnd);
            }
        });
    }

    /*********** 辅助类以及接口 ****************************************/
    public void setClientListener(IClientListener clientListener) {
        this.mClientListener = clientListener;
    }

    public interface IClientListener {

        default void onError(TuTkClient client, int code, String info) {
        }

        default void onProgress(TuTkClient client, int progress) {
        }

        default void onConnected(TuTkClient client) {
        }

        default void onDisConnected(TuTkClient client) {
        }

        default void onStarted(TuTkClient client) {
        }

        default void onPaused(TuTkClient client) {
        }

        default void onStartTalk(TuTkClient client) {
        }

        default void onStopTalk(TuTkClient client) {
        }

        //type = -1 session data,type==-2 rdt data,tye>0 avctrl data
        default void onCtrlData(TuTkClient client, IOCtrlMessage msg) {
        }

        default void onFrameData(TuTkClient client, byte[] info, byte[] buffer) {
        }

        default void onRdtData(byte[] buffer, boolean isEnd){}
    }
}
