package com.wh.tutkwrapper;

import android.util.Log;

import com.tutk.IOTC.Packet;
import com.tutk.IOTC.RDTAPIs;
import com.wh.tutkwrapper.helper.BaseThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class OperationRDT extends BaseThread {

    private static final String TAG = "OperationRDT";
    private RdtCallBack rdtCallBack;

    private volatile int mRdtId = -1;

    public OperationRDT() {
        super("RDTOperation");
    }

    public void setRdtId(int rdtId) {
        this.mRdtId = rdtId;
    }

    @Override
    protected int doRepeatWork() {
        if(mRdtId < 0){
            return 0;
        }
        int nRetWrite = rdtWrite();
        Log.e(TAG, "RDTWriteAndRead: RDT_Write: " + nRetWrite);
        if (nRetWrite >= 0) {
            Log.e(TAG, getName() + " rdtRead: " + nRetWrite);
            rdtRead();
        } else {
            return 0;
        }
        try {
            Log.e(TAG, getName() + " sleep: " + nRetWrite);
            //一分钟一次
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public interface RdtCallBack {
        void onRdtData(byte[] result, boolean isEnd);
    }

    private int rdtWrite() {
        Calendar calendar = Calendar.getInstance();
        long startTime = calendar.getTimeInMillis() / 1000;
        long endTime = System.currentTimeMillis() / 1000;
        JSONObject obj = new JSONObject();
        try {
            obj.put("time_start", startTime);
            obj.put("time_end", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        byte[] buf = obj.toString().getBytes();
        byte[] type = Packet.intToByteArray_Little(TUTKConstant.MHLUMI_RDT_COMMOND_GETRECORDLIST_REQ);
        byte[] len = Packet.intToByteArray_Little(buf.length);
        byte[] data = new byte[buf.length + type.length + len.length];
        System.arraycopy(type, 0, data, 0, type.length);
        System.arraycopy(len, 0, data, type.length, len.length);
        System.arraycopy(buf, 0, data, type.length + len.length, buf.length);
        return RDTAPIs.RDT_Write(mRdtId, data, data.length);
    }

    private byte[] rdtCtrlBuf = new byte[2048];

    private void rdtRead() {
        int readType = -1;
        int readLen = 0;
        byte[] headBytes = new byte[8];
        while (this.status != STOP) {
            //read head
            int nRead = RDTAPIs.RDT_Read(mRdtId, headBytes, headBytes.length, 0);
            if (nRead == RDTAPIs.RDT_ER_TIMEOUT) {
                continue;
            }
            Log.e(TAG, "RDTWriteAndRead: RDT_Read_Head: " + nRead);
            if (nRead > 0) {
                byte[] result = new byte[nRead];
                System.arraycopy(headBytes, 0, result, 0, nRead);
                readType = Packet.byteArrayToInt_Little(result, 0);
                readLen = Packet.byteArrayToInt_Little(result, 4);
                if (readType > 100) {
                    readType = -1;
                    continue;
                }
                Log.e(TAG, "RDTWriteAndRead: readType: " + readType + ",readLen: " + readLen);
                break;
            } else if (nRead < 0) {
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (readLen > 0 && readType > 0) {
            int destPos = 0;
            while (this.status != STOP) {
                //read content
                int nRead = RDTAPIs.RDT_Read(mRdtId, rdtCtrlBuf, rdtCtrlBuf.length, 0);
                if (nRead != RDTAPIs.RDT_ER_TIMEOUT) {
                    Log.e(TAG, "RDTWriteAndRead: RDT_Read_Content: " + nRead);
                }
                if (nRead > 0) {
                    byte[] result = new byte[nRead];
                    System.arraycopy(rdtCtrlBuf, 0, result, 0, nRead);
                    destPos += nRead;
                    if(rdtCallBack != null){
                        rdtCallBack.onRdtData(result, false);
                    }
                    Log.e(TAG, "RDTWriteAndRead: RDT_Read destPos: " + destPos);
                } else {
                    if (destPos >= readLen) {
                        if(rdtCallBack != null){
                            rdtCallBack.onRdtData(new byte[1], true);
                        }
                        break;
                    }
                }
            }
        } else if (readLen == 0) {
            if (rdtCallBack != null) {
                rdtCallBack.onRdtData(new byte[1], true);
            }
        }
    }

    void setCallBack(RdtCallBack callBack) {
        this.rdtCallBack = callBack;
    }
}
