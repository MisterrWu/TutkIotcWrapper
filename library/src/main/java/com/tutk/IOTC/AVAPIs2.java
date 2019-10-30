/******************************************************************************
 *                                                                            *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 * Class: AVAPIs2.java                                                        *
 *                                                                            *
 * Author: Ferando                                                            *
 * Date: 2016-04-14                                                           *
 *                                                                            *
 ******************************************************************************/

package com.tutk.IOTC;


public class AVAPIs2 {

	public static final int MAX_ANALYTICS_DATA_SLOT_NUMBER = 5;
	// weird stuff
	public static final int TIME_DELAY_DELTA				=1;		//ms
	public static final int TIME_DELAY_MIN					=4;		//ms
	public static final int TIME_DELAY_MAX					=500;	//ms
	public static final int TIME_DELAY_INITIAL				=0;	//ms
	public static final int TIME_SPAN_LOSED					=1000;	//ms

	//--{{inner iotype-----------------------------------------------------
	public static final int IOTYPE_INNER_SND_DATA_DELAY		=0xFF;	//C--->D: avClient(AP) change time interval of sending packets by avSendFrameData(avServer)

	//--}}inner iotype-----------------------------------------------------

	public static final int API_ER_ANDROID_NULL					= -10000;
	//AVApis error code	===================================================================================
	/** The function is performed successfully. */
	public static final int		AV_ER_NoERROR						 =0;

	/** The passed-in arguments for the function are incorrect */
	public static final int		AV_ER_INVALID_ARG					=-20000;

	/** The buffer to receive frame is too small to store one frame */
	public static final int		AV_ER_BUFPARA_MAXSIZE_INSUFF		=-20001;

	/** The number of AV channels has reached maximum.
	* The maximum number of AV channels is determined by the passed-in
	* argument of avInitialize() */
	public static final int		AV_ER_EXCEED_MAX_CHANNEL			=-20002;

	/** Insufficient memory for allocation */
	public static final int		AV_ER_MEM_INSUFF					=-20003;

	/** AV fails to create threads. Please check if OS has ability to create threads for AV. */
	public static final int		AV_ER_FAIL_CREATE_THREAD			=-20004;

	/** A warning error code to indicate that the sending queue of video frame of an AV server
	* is almost full, probably caused by slow handling of an AV client or network
	* issue. Please note that this is just a warning, the video frame is actually
	* put in the queue. */
	public static final int		AV_ER_EXCEED_MAX_ALARM				=-20005;

	/** The frame to be sent exceeds the currently remaining video frame buffer.
	* The maximum of video frame buffer is controlled by avServSetMaxBufSize() */
	public static final int		AV_ER_EXCEED_MAX_SIZE				=-20006;

	/** The specified AV server has no response */
	public static final int		AV_ER_SERV_NO_RESPONSE				=-20007;

	/** An AV client does not call avClientStart() yet */
	public static final int		AV_ER_CLIENT_NO_AVLOGIN				=-20008;

	/** The client fails in authentication due to incorrect view account or password */
	public static final int		AV_ER_WRONG_VIEWACCorPWD			=-20009;

	/** The IOTC session of specified AV channel is not valid */
	public static final int		AV_ER_INVALID_SID					=-20010;

	/** The specified timeout has expired during some operation */
	public static final int		AV_ER_TIMEOUT						=-20011;

	/** The data is not ready for receiving yet. */
	public static final int		AV_ER_DATA_NOREADY					=-20012;

	/** Some parts of a frame are lost during receiving */
	public static final int		AV_ER_INCOMPLETE_FRAME				=-20013;

	/** The whole frame is lost during receiving */
	public static final int		AV_ER_LOSED_THIS_FRAME				=-20014;

	/** The remote site already closes the IOTC session.
	* Please call IOTC_Session_Close() to release local IOTC session resource */
	public static final int		AV_ER_SESSION_CLOSE_BY_REMOTE		=-20015;

	/** This IOTC session is disconnected because remote site has no any response
	* after a specified timeout expires. */
	public static final int		AV_ER_REMOTE_TIMEOUT_DISCONNECT		=-20016;

	/** Users exit starting AV server process */
	public static final int		AV_ER_SERVER_EXIT		    		=-20017;

	/** Users exit starting AV client process */
	public static final int		AV_ER_CLIENT_EXIT		    		=-20018;

	/** AV module has not been initialized */
	public static final int		AV_ER_NOT_INITIALIZED	    		=-20019;

	/** By design, an AV client cannot send frame and audio data to an AV server */
	public static final int		AV_ER_CLIENT_NOT_SUPPORT	   		=-20020;

	/** The AV channel of specified AV channel ID is already in sending IO control process */
	public static final int		AV_ER_SENDIOCTRL_ALREADY_CALLED	   	=-20021;

	/** The sending IO control process is terminated by avSendIOCtrlExit() */
	public static final int		AV_ER_SENDIOCTRL_EXIT		    	=-20022;

	/** The UID is a lite UID */
	public static final int		AV_ER_NO_PERMISSION					=-20023;

	/** The length of password is wrong */
	public static final int		AV_ER_WRONG_ACCPWD_LENGTH           =-20024;

	public int authCB(byte[] szViewAccount, byte[] szViewPassWord)
	{
		System.out.println("authCB is called");
		return 0;
	};

	public int ioCtrlRecvCB(int nAVCanal, int nIoCtrlType, byte[] pIoCtrlBuf, int nIoCtrlBufLen)
	{
		System.out.println("ioCtrlRecvCB is called");
		return 0;
	};

	public int videoRecvCB(int nAVCanal, int ret, byte[] pFrameData, int nActualFrameSize, int nExpectedFrameSize, byte[] pFrameInfo, int nFrameInfoSize, int frmNo)
	{
		System.out.println("videoRecvCB is called");
		return 0;
	};

	public int audioRecvCB(int nAVCanal, int ret, byte[] pAudioData, int nAudioSize, byte[] pAudioInfo, int frmNo)
	{
		System.out.println("audioRecvCB is called");
		return 0;
	};

	public int metaRecvCB(int nAVCanal, int ret, byte[] pMetaData, int nActualMetaSize, int nExpectedMetaSize, byte[] pMetaInfo, int nMetaInfoSize, int frmNo)
	{
		System.out.println("metaRecvCB is called");
		return 0;
	};

	public int canalStatusCB(int nAVCanal, int nError, int nChannelID, St_SInfo pStSInfo)
	{
		System.out.println("canalStatusCB is called");
		return 0;
	};

	public int serverStatusCB(int nStatus, int nError, int nAVCanal, int nChannelID, St_SInfo pStSInfo)
	{
		System.out.println("serverStatusCB is called");
		return 0;
	};

	public int clientStatusCB(int nStatus, int nError, int nAVCanal, int nChannelID, St_SInfo pStSInfo)
	{
		System.out.println("clientStatusCB is called");
		return 0;
	};

	public native static int AVAPI2_SetCanalLimit(int nSessionLimit, int nChannelLimit);
	public static int lmAVAPI2_SetCanalLimit(int nSessionLimit, int nChannelLimit){
		try {
			return AVAPI2_SetCanalLimit(nSessionLimit,nChannelLimit);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetVersion(byte[] szVersion, int nLen);
	public static int lmAVAPI2_GetVersion(byte[] szVersion, int nLen){
		try {
			return AVAPI2_GetVersion(szVersion,nLen);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_SendFrameData(int nAVCanal, int usCodecID, int bFrameFlags, byte[]pFramebuf, int nFrameSize);
	public static int lmAVAPI2_SendFrameData(int nAVCanal, int usCodecID, int bFrameFlags, byte[]pFramebuf, int nFrameSize){
		try {
			return AVAPI2_SendFrameData(nAVCanal,usCodecID,bFrameFlags,pFramebuf,nFrameSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_SendAudioData(int nAVCanal, int usCodecID, int bFrameFlags, byte[] pAudioData, int nAudioSize);
	public static int lmAVAPI2_SendAudioData(int nAVCanal, int usCodecID, int bFrameFlags, byte[] pAudioData, int nAudioSize){
		try {
			return AVAPI2_SendAudioData(nAVCanal,usCodecID,bFrameFlags,pAudioData,nAudioSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_SendMetaData(int nAVCanal, byte[] pMetaData, int nMetaSize, byte[] cabMetaInfo, int nMetaInfoSize);
	public static int lmAVAPI2_SendMetaData(int nAVCanal, byte[] pMetaData, int nMetaSize, byte[] cabMetaInfo, int nMetaInfoSize){
		try {
			return AVAPI2_SendMetaData(nAVCanal,pMetaData,nMetaSize,cabMetaInfo,nMetaInfoSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_SendIOCtrl(int nAVCanal, int nIOCtrlType, byte[] cabIOCtrlData, int nIOCtrlDataSize);
	public static int lmAVAPI2_SendIOCtrl(int nAVCanal, int nIOCtrlType, byte[] cabIOCtrlData, int nIOCtrlDataSize){
		try {
			return AVAPI2_SendIOCtrl(nAVCanal,nIOCtrlType,cabIOCtrlData,nIOCtrlDataSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetAVCanalBySIDChannel(int nSID, int ChID);
	public static int lmAVAPI2_GetAVCanalBySIDChannel(int nSID, int ChID){
		try {
			return AVAPI2_GetAVCanalBySIDChannel(nSID,ChID);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetAVCanalByUIDChannel(String szUID, int nChannel);
	public static int lmAVAPI2_GetAVCanalByUIDChannel(String szUID, int nChannel){
		try {
			return AVAPI2_GetAVCanalByUIDChannel(szUID,nChannel);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetSessionIDByAVCanal(int nAVCanal);
	public static int lmAVAPI2_GetSessionIDByAVCanal(int nAVCanal){
		try {
			return AVAPI2_GetSessionIDByAVCanal(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetChannelByAVCanal(int nAVCanal);
	public static int lmAVAPI2_GetChannelByAVCanal(int nAVCanal){
		try {
			return AVAPI2_GetChannelByAVCanal(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetUIDByAVCanal(int nAVCanal, String szUID);
	public static int lmAVAPI2_GetUIDByAVCanal(int nAVCanal, String szUID){
		try {
			return AVAPI2_GetUIDByAVCanal(nAVCanal,szUID);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	
	/* set callback: canalStatusCB */
	public native static int AVAPI2_ClientConnect(int SID, byte[] szUser, byte[] szPass, int timeout_sec, int[] pservType, int nChannelID, int[] pnResend, Object obj);
	public static int lmAVAPI2_ClientConnect(int SID, byte[] szUser, byte[] szPass, int timeout_sec, int[] pservType, int nChannelID, int[] pnResend, Object obj){
		try {
			return AVAPI2_ClientConnect(SID,szUser,szPass,timeout_sec,pservType,nChannelID,pnResend,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ClientDisconnect(int nAVCanal);
	public static int lmAvServResetBuffer(int nAVCanal){
		try {
			return AVAPI2_ClientDisconnect(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}

	/* set callback: canalStatusCB, clientStatusCB */
	public native static int AVAPI2_ClientConnectByUID(String szUID, byte[] szUser, byte[] szPass, int timeout_sec, int nChannelID, Object obj);
	public static int lmAVAPI2_ClientConnectByUID(String szUID, byte[] szUser, byte[] szPass, int timeout_sec, int nChannelID, Object obj){
		try {
			return AVAPI2_ClientConnectByUID(szUID,szUser,szPass,timeout_sec,nChannelID,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ClientDisconnectAndCloseIOTC(int nAVCanal);
	public static int lmAVAPI2_ClientDisconnectAndCloseIOTC(int nAVCanal){
		try {
			return AVAPI2_ClientDisconnectAndCloseIOTC(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static void AVAPI2_ClientStop();
	public static void lmAVAPI2_ClientStop(){
		try {
			AVAPI2_ClientStop();
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
	}
	public native static void AVAPI2_ClientExit(int nSID, int nChannelID);
	public static void lmAVAPI2_ClientExit(int nSID, int nChannelID){
		try {
			AVAPI2_ClientExit(nSID,nChannelID);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
	}

	/* set callback: clientStatusCB */
	public native static int AVAPI2_RegClientStatusCB(int nAVCAnal, Object obj);
	public static int lmAVAPI2_RegClientStatusCB(int nAVCAnal, Object obj){
		try {
			return AVAPI2_RegClientStatusCB(nAVCAnal,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	
	/* set callback: metaRecvCB */
	public native static int AVAPI2_RegRecvMeta(int nAVCanal, Object obj);
	public static int lmAVAPI2_RegRecvMeta(int nAVCanal, Object obj){
		try {
			return AVAPI2_RegRecvMeta(nAVCanal,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}

	/* set callback: ioCtrlRecvCB */
	public native static int AVAPI2_RecvIoCtrl(int nAVCanal, Object obj);
	public static int lmAVAPI2_RecvIoCtrl(int nAVCanal, Object obj){
		try {
			return AVAPI2_RecvIoCtrl(nAVCanal,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}

	/* set callback: videoRecvCB */
	public native static int AVAPI2_StartRecvFrame(int nAVCanal, int nChannelID, Object obj);
	public static int lmAVAPI2_StartRecvFrame(int nAVCanal, int nChannelID, Object obj){
		try {
			return AVAPI2_StartRecvFrame(nAVCanal,nChannelID,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}

	public native static int AVAPI2_StopRecvFrame(int nAVCanal, int nChannelID);
	public static int lmAVAPI2_StopRecvFrame(int nAVCanal, int nChannelID){
		try {
			return AVAPI2_StopRecvFrame(nAVCanal,nChannelID);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}

	/* set callback: audioRecvCB */
	public native static int AVAPI2_StartRecvAudio(int nAVCanal, int nChannelID, Object obj);
	public static int lmAVAPI2_StartRecvAudio(int nAVCanal, int nChannelID, Object obj){
		try {
			return AVAPI2_StartRecvAudio(nAVCanal,nChannelID,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}


	public native static int AVAPI2_StopRecvAudio(int nAVCanal, int nChannelID);
	public static int lmAVAPI2_StopRecvAudio(int nAVCanal, int nChannelID){
		try {
			return AVAPI2_StopRecvAudio(nAVCanal,nChannelID);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_SetAVTimeSync(int nAVCanal, int nEnable);
	public static int lmAVAPI2_SetAVTimeSync(int nAVCanal, int nEnable){
		try {
			return AVAPI2_SetAVTimeSync(nAVCanal,nEnable);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	
	/* set callback: canalStatusCB */
	public native static int AVAPI2_CreateChannelForSend(int nSID, int nTimeout, int nServType, int nChannel, int Resend, Object obj);
	public static int lmAVAPI2_CreateChannelForSend(int nSID, int nTimeout, int nServType, int nChannel, int Resend, Object obj){
		try {
			return AVAPI2_CreateChannelForSend(nSID,nTimeout,nServType,nChannel,Resend,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ReleaseChannelForSend(int nAVCanal);
	public static int lmAVAPI2_ReleaseChannelForSend(int nAVCanal){
		try {
			return AVAPI2_ReleaseChannelForSend(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}

	/* set callback: clientStatusCB, canalStatusCB, ioCtrlRecvCB, videoRecvCB, audioRecvCB */
	public native static int AVAPI2_CreateChannelForReceive(int nSID, int nChannelID, int nResend, int nTimeout, Object obj);
	public static int lmAVAPI2_CreateChannelForReceive(int nSID, int nChannelID, int nResend, int nTimeout, Object obj){
		try {
			return AVAPI2_CreateChannelForReceive(nSID,nChannelID,nResend,nTimeout,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ReleaseChannelForReceive(int nAVCanal);
	public static int lmAVAPI2_ReleaseChannelForReceive(int nAVCanal){
		try {
			return AVAPI2_ReleaseChannelForReceive(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerInitial(int nSessionLimit, int nChannelLimit, int nUDPPort);
	public static int lmAVAPI2_ServerInitial(int nSessionLimit, int nChannelLimit, int nUDPPort){
		try {
			return AVAPI2_ServerInitial(nSessionLimit,nChannelLimit,nUDPPort);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerStart(String szUID, int nServerInitChannel, int nServerType, Object obj); //CB
	public static int lmAVAPI2_ServerStart(String szUID, int nServerInitChannel, int nServerType, Object obj){
		try {
			return AVAPI2_ServerStart(szUID,nServerInitChannel,nServerType,obj);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerStopCanal(int nAVCanal);
	public static int lmAVAPI2_ServerStopCanal(int nAVCanal){
		try {
			return AVAPI2_ServerStopCanal(nAVCanal);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static void AVAPI2_ServerExitCanal(int nSessionID, int nChannelID);
	public static void lmAVAPI2_ServerExitCanal(int nSessionID, int nChannelID){
		try {
			AVAPI2_ServerExitCanal(nSessionID,nChannelID);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
	}
	public native static void AVAPI2_ServerStop();
	public static void lmAVAPI2_ServerStop(){
		try {
			AVAPI2_ServerStop();
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
	}
	public native static int AVAPI2_ServerSetVideoPreBufSize(int nAVCanal, int nVideoPreBufSize);
	public static int lmAVAPI2_ServerSetVideoPreBufSize(int nAVCanal, int nVideoPreBufSize){
		try {
			return AVAPI2_ServerSetVideoPreBufSize(nAVCanal,nVideoPreBufSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerSetAudioPreBufSize(int nAVCanal, int nAudioPreBufSize);
	public static int lmAVAPI2_ServerSetAudioPreBufSize(int nAVCanal, int nAudioPreBufSize){
		try {
			return AVAPI2_ServerSetAudioPreBufSize(nAVCanal,nAudioPreBufSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerSetIoCtrlBufSize(int nAVcanal, int nIOCtrlBufSize);
	public static int lmAVAPI2_ServerSetIoCtrlBufSize(int nAVcanal, int nIOCtrlBufSize){
		try {
			return AVAPI2_ServerSetIoCtrlBufSize(nAVcanal,nIOCtrlBufSize);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerSetCongestionCtrlMode(int nAVCanal, int nMode);
	public static int lmAVAPI2_ServerSetCongestionCtrlMode(int nAVCanal, int nMode){
		try {
			return AVAPI2_ServerSetCongestionCtrlMode(nAVCanal,nMode);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerCleanBuf(int nAVCanal, int nTimeout);
	public static int lmAVAPI2_ServerCleanBuf(int nAVCanal, int nTimeout){
		try {
			return AVAPI2_ServerCleanBuf(nAVCanal,nTimeout);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerCleanVideoBuf(int nAVCanal, int nTimeout);
	public static int lmAVAPI2_ServerCleanVideoBuf(int nAVCanal, int nTimeout){
		try {
			return AVAPI2_ServerCleanVideoBuf(nAVCanal,nTimeout);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ServerCleanAudioBuf(int nAVCanal, int nTimeout);
	public static int lmAVAPI2_ServerCleanAudioBuf(int nAVCanal, int nTimeout){
		try {
			return AVAPI2_ServerCleanAudioBuf(nAVCanal,nTimeout);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ClientCleanBuf(int nAVCanal, int nTimeout, int nCleanServerBuf);
	public static int lmAVAPI2_ClientCleanBuf(int nAVCanal, int nTimeout, int nCleanServerBuf){
		try {
			return AVAPI2_ClientCleanBuf(nAVCanal,nTimeout,nCleanServerBuf);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ClientCleanVideoBuf(int nAVcanal, int nTimeout, int nCleanServerBuf);
	public static int lmAVAPI2_ClientCleanVideoBuf(int nAVcanal, int nTimeout, int nCleanServerBuf){
		try {
			return AVAPI2_ClientCleanVideoBuf(nAVcanal,nTimeout,nCleanServerBuf);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_ClientCleanAudioBuf(int nAVCanal, int nCleanServerBuf);
	public static int lmAVAPI2_ClientCleanAudioBuf(int nAVCanal, int nCleanServerBuf){
		try {
			return AVAPI2_ClientCleanAudioBuf(nAVCanal,nCleanServerBuf);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetAnalyticsData(int nAVcanal, St_AnalyticsDataSlot analyticsDataSlot);
	public static int lmAVAPI2_GetAnalyticsData(int nAVcanal, St_AnalyticsDataSlot analyticsDataSlot){
		try {
			return AVAPI2_GetAnalyticsData(nAVcanal,analyticsDataSlot);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}
	public native static int AVAPI2_GetAnalyticsClientData(int nAVCanal, St_AnalyticsClientDataSlot analyticsClientDataSlot);
	public static int lmAVAPI2_GetAnalyticsClientData(int nAVCanal, St_AnalyticsClientDataSlot analyticsClientDataSlot){
		try {
			return AVAPI2_GetAnalyticsClientData(nAVCanal,analyticsClientDataSlot);
		} catch (UnsatisfiedLinkError e) {
			System.exit(0);
		}
		return -1;
	}


	static { try {System.loadLibrary("AVAPIs");}
			 catch(UnsatisfiedLinkError ule){
			   System.out.println("loadLibrary(AVAPIs),"+ule.getMessage());
			 }
	}
}
