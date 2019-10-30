package com.wh.tutkwrapper;

import com.tutk.IOTC.AVAPIs;
import com.wh.tutkwrapper.helper.BaseThread;

class OperationMessage extends BaseThread {

    private int[] ioCtrlType = new int[1];
    private byte[] ioCtrlBuf = new byte[2048];
    private volatile int mAvIndex = -1;
    private OnCtrlDataListener ctrlDataListener;

    OperationMessage() {
        super("MessageOperation");
    }

    void setAvIndex(int avIndex) {
        this.mAvIndex = avIndex;
    }

    @Override
    protected int doRepeatWork() {
        if(mAvIndex < 0){
            return 0;
        }
        int recvRet = AVAPIs.lmAvRecvIOCtrl(mAvIndex,
                ioCtrlType, ioCtrlBuf, ioCtrlBuf.length, 1000);
        if (recvRet >= 0) {
            byte[] result = new byte[recvRet];
            System.arraycopy(ioCtrlBuf, 0, result, 0, recvRet);
            IOCtrlMessage msg = new IOCtrlMessage();
            msg.ioType = ioCtrlType[0];
            msg.ioCtrlBuf = result;
            if (ctrlDataListener != null) {
                ctrlDataListener.onCtrlData(msg);
            }
        } else if (recvRet == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE
                || recvRet == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT
                || recvRet == AVAPIs.AV_ER_INVALID_SID
                || recvRet == AVAPIs.AV_ER_NOT_INITIALIZED) {
            if(ctrlDataListener != null){
                ctrlDataListener.onError(recvRet, TuTkManager.instance().getErrorInfo(recvRet));
            }
        }
        return 0;
    }

    void setCtrlDataListener(OnCtrlDataListener ctrlDataListener) {
        this.ctrlDataListener = ctrlDataListener;
    }

    interface OnCtrlDataListener{
        void onCtrlData(IOCtrlMessage msg);
        void onError(int code, String info);
    }
}
