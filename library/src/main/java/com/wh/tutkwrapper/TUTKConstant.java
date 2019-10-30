package com.wh.tutkwrapper;

public final class TUTKConstant {

    public static int TYPE_IMAGE = 0;
    public static int TYPE_VIDEO = 1;
    public static int TYPE_ALARM = 2;

    public static int IOTYPE_USER_IPCAM_VIDEO_START = 0x1FF;//开启视频
    public static int IOTYPE_USER_IPCAM_VIDEO_STOP = 0x02FF;//关闭视频

    public static int IOTYPE_USER_IPCAM_SPEAKERSTART = 0x0302;//开启对讲
    public static int IOTYPE_USER_IPCAM_SPEAKERSTOP = 0x0303;//关闭对讲

    public static int IOTYPE_USER_IPCAM_CLBRT_PLAY_RATE = 0x0309;//发送命令给设备端用于调节stream发送的速率

    public static int IOTYPE_USER_IPCAM_SET_VIDEO_MODE = 0x600;//设置摄像头模式
    public static int IOTYPE_USER_IPCAM_EPTZ = 0x602;//设置摄像头旋转
    public static int IOTYPE_USER_IPCAM_START_EX_REQ = 0x603;//开启视频和获取录像时间一起
    public static int IOTYPE_USER_IPCAM_START_EX_RESP = 0x604;//开启视频和获取录像时间一起
    public static int IOTYPE_USER_IPCAM_STREAM_INFO_REQ = 0x0605;//码流切换
    public static int IOTYPE_USER_IPCAM_STREAM_INFO_RESP = 0x0606;//码流切换
    public static int IOTYPE_USER_IPCAM_PAUSE_REQ = 0x0610;//开启暂停
    public static int IOTYPE_USER_IPCAM_PAUSE_RESP = 0x0611;//开启暂停回应

    public static int IOTYPE_USER_IPCAM_PLAYRECORDSTART_REQ0 = 0x0304;//开启回看0
    public static int IOTYPE_USER_IPCAM_PLAYRECORDSTART_RESP0 = 0X608;//开启回看0
    public static int IOTYPE_USER_IPCAM_PLAYRECORDSTOP_REQ = 0x0305;//关闭回看
    public static int IOTYPE_USER_IPCAM_PLAYRECORDSTOP_RESP = 0x0306;//关闭回看

    public static int IOTYPE_USER_IPCAM_PLAYRECORDSTART_REQ1 = 0X607;//开启回看1
    public static int IOTYPE_USER_IPCAM_PLAYRECORDSTART_RESP1 = 0X609;//开启回看1


    /**
     * 设置回放视频通道
     * 请求的key: [Channel]  0： 1080p，1：vga
     * GetIpcProp方法对应的key是：playback_stream
     */
    public static int IOTYPE_USER_IPCAM_SET_PLAYBACK_CH_REQ           = 0x70C;
    public static int IOTYPE_USER_IPCAM_SET_PLAYBACK_CH_RESP          = 0x70D;
    /**
     * 拉取回放视频 List
     */
    public static int MHLUMI_RDT_COMMOND_GETRECORDLIST_REQ    = 0x0001;
    public static int MHLUMI_RDT_COMMOND_GETRECORDLIST_RESP   = 0x0002;
    /**
     * 发送 RDT Channel
     */
    public static int IIOTYPE_USER_IPCAM_SEND_RDT_CHANNDEL_REQ         = 0x700;
    public static int IIOTYPE_USER_IPCAM_SEND_RDT_CHANNDEL_RESP        = 0x701;
    //说明：打开 RDT 通道的时候，需要给设备端发送 RDT 的 channdel，具体请看 RDT 通道的初始化

    /**
     * 设置视频速率
     */
    public static int IIOTYPE_USER_IPCAM_SET_VIDEOSPEED_REQ            = 0x702;
    public static int IIOTYPE_USER_IPCAM_SET_VIDEOSPEED_RESP           = 0x703;

    /**
     * 视频帧接受成功
     */
    public static int IIOTYPE_USER_IPCAM_SEND_GET_FRAME_SUCCEED_REQ     = 0x704;
    public static int IIOTYPE_USER_IPCAM_SEND_GET_FRAME_SUCCEED_RESP    = 0x705;
    // 说明：当前端收到第一帧I帧的时候，给设备端发送 0x704 命令，表示开图成功，设备会根据设置把码流调回正常

    /**
     * 交换AEC key(Android消回声专用)
     * @return
     */
    public static int IOTYPE_USER_IPCAM_SEND_AEC_KEY_REQ           = 0x706;
    public static int IOTYPE_USER_IPCAM_SEND_AEC_KEY_RESP          = 0x707;

    /**
     * 回放暂停
     */
    public static int IIOTYPE_USER_IPCAM_PAUSE_PLAYBACK_REQ           = 0x708;
    public static int IIOTYPE_USER_IPCAM_PAUSE_PLAYBACK_RESP          = 0x709;
    //说明：放回的情况下，切后台，切设置页面，需要发送回放暂停。

    /**
     * 回放重新开始
     */
    public static int IIOTYPE_USER_IPCAM_RESUME_PLAYBACK_REQ           = 0x70A;
    public static int IIOTYPE_USER_IPCAM_RESUME_PLAYBACK_RESP          = 0x70B;

    /**
     * 视频帧接受成功
     * 说明：当前端收到第一帧I帧的时候，给设备端发送 0x704 命令，表示开图成功，设备会根据设置把码流调回正常
     */
    public static int IOTYPE_USER_IPCAM_SEND_GET_FRAME_SUCCEED_REQ     = 0x704;
    public static int IOTYPE_USER_IPCAM_SEND_GET_FRAME_SUCCEED_RESP    = 0x705;

    public static int TYPE_SERVER_STREAMING = 16;

}
