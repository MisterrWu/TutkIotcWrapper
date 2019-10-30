package com.wh.tutkwrapper;

import android.util.Log;

import com.tutk.IOTC.AVAPIs;
import com.wh.tutkwrapper.helper.BaseThread;

final class OperationRecvVideo extends BaseThread {

    private static final int VIDEO_BUF_SIZE = 400000;
    private static final int FRAME_INFO_SIZE = 64;

    private static final String TAG = "OpRecvVideo";

    private byte[] mFrameInfo = new byte[FRAME_INFO_SIZE];
    private byte[] mVideoBuffer = new byte[VIDEO_BUF_SIZE];
    private int mAvIndex = -1;

    private OnAvRecvFrameListener recvFrameListener;

    OperationRecvVideo() {
        super("OperationRecvVideo");
    }

    void setAvIndex(int avIndex) {
        this.mAvIndex = avIndex;
    }

    @Override
    protected int doRepeatWork() {
        if(mAvIndex < 0){
            return 0;
        }
        int ret = avRecvFrameData2();
        if(ret < 0 && recvFrameListener != null){
            recvFrameListener.onAvRecvFrameFailed(ret);
        }
        return 0;
    }

    /**
     * 接收数据计数器,一千次没有接收到数据就超时处理
     */
    private int recvDataCount = 0;

    /**
     * 接收数据是否超过超时次数
     *
     * @return
     */
    private boolean isRecvDataTimeOutCount() {
        recvDataCount++;
        /**
         * 超时次数
         */
        return recvDataCount >= 1000;
    }

    private int avRecvFrameData2() {
        int[] frameIndex = new int[1];
        final int ret = AVAPIs.lmAvRecvFrameData2(mAvIndex, mVideoBuffer, VIDEO_BUF_SIZE
                , new int[1], new int[1], mFrameInfo, FRAME_INFO_SIZE, new int[1], frameIndex);
        if (isRecvDataTimeOutCount()) {
            recvDataCount = 0;
            return AVAPIs.AV_ER_TIMEOUT;
        }
        Log.e(TAG, "avRecvFrameData2: recvDataCount " + recvDataCount + ",ret " + ret);
        if (ret > 0) {
            recvDataCount = 0;
            if(recvFrameListener != null){
                byte[] info = new byte[mFrameInfo.length];
                System.arraycopy(mFrameInfo, 0, info, 0, info.length);
                byte[] buffer = new byte[ret];
                System.arraycopy(mVideoBuffer, 0, buffer, 0, buffer.length);
                recvFrameListener.onAvRecvFrameData(info, buffer);
            }
        } else if (ret == AVAPIs.AV_ER_DATA_NOREADY) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
            return 0;
        } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
            return 0;
        } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE
                || ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT
                || ret == AVAPIs.AV_ER_INVALID_SID
                || ret == AVAPIs.AV_ER_NOT_INITIALIZED) {
            return ret;
        } else {
            return 0;
        }
        return 0;
    }

    void setRecvFrameListener(OnAvRecvFrameListener recvFrameListener) {
        this.recvFrameListener = recvFrameListener;
    }

    interface OnAvRecvFrameListener{
        /**
         * 获取数据
         *
         * @param info 数据信息
         * @param buffer  音频或视频数据
         */
        void onAvRecvFrameData(byte[] info, byte[] buffer);

        /**
         * 接收视频数据失败
         *
         * @param errCode 错误码
         */
        void onAvRecvFrameFailed(int errCode);
    }
}
