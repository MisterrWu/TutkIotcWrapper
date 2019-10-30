package com.wh.tutkwrapper;

import org.json.JSONException;
import org.json.JSONObject;

public class IOCtrlMessage {

    public int ioType;
    public byte[] ioCtrlBuf;

    public IOCtrlMessage() {
    }

    public IOCtrlMessage(int ioType) {
        this();
        this.ioType = ioType;
        ioCtrlBuf = new byte[1];
    }

    public int getIoCtrlBufSize() {
        if (ioCtrlBuf != null) {
            return ioCtrlBuf.length;
        }
        return 0;
    }

    public static IOCtrlMessage createVideoStopMsg(int definition) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_VIDEO_STOP);
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", definition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = obj.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createVideoStartMsg(String uuid, int definition) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_VIDEO_START);
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", definition);
            obj.put("uuid", uuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = obj.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createDefinitionMsg(int definition) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_STREAM_INFO_REQ);
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", definition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = obj.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createRecordDefinitionMsg(int definition) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_SET_PLAYBACK_CH_REQ);
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", definition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = obj.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createPlayRecordMsg(long relativeTime){
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_PLAYRECORDSTART_REQ1);
        JSONObject obj = new JSONObject();
        try {
            obj.put("start_time", String.valueOf(relativeTime));
            obj.put("keep_on", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = obj.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createVideoSpeedMsg(int speedType) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IIOTYPE_USER_IPCAM_SET_VIDEOSPEED_REQ);
        JSONObject object = new JSONObject();
        try {
            object.put("speed", speedType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = object.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createSpeakerStartMsg(int mFreeChannel) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_SPEAKERSTART);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", mFreeChannel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = jsonObject.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createSpeakerStopMsg(int mFreeChannel) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IOTYPE_USER_IPCAM_SPEAKERSTOP);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channel", mFreeChannel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = jsonObject.toString().getBytes();
        return message;
    }

    public static IOCtrlMessage createSendRdtMsg(int chiD) {
        IOCtrlMessage message = new IOCtrlMessage(TUTKConstant.IIOTYPE_USER_IPCAM_SEND_RDT_CHANNDEL_REQ);
        JSONObject obj = new JSONObject();
        try {
            obj.put("channel", chiD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        message.ioCtrlBuf = obj.toString().getBytes();
        return message;
    }
}
