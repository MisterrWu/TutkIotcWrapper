/******************************************************************************
 *                                                                            *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 *                                                                            *
 *                                                                            *
 * Class: IOTCRDTApis.java                                                    *
 * Author: joshua ju                                                          *
 * Date: 2011-05-14                                                           *

	Revisions of IOTCRDTApis
	IOTCApis Version joined		Name		Date
	-------						----		----------
	0.5.0.0						Joshua		2011-06-30
	1.1.0.0						Joshua		2011-08-01
	1.2.0.0						Joshua      2011-08-30
	1.2.1.0						Joshua		2011-09-19

 ******************************************************************************/

package com.tutk.IOTC;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RDTAPIs {
	public static int ms_verRDTApis=0;

	public static final int  API_ER_ANDROID_NULL				=-10000;
	//RDTApis error code===================================================================================
	/** The function is performed successfully. */
	public static final int	RDT_ER_NoERROR							 =0;

	/** RDT module is not initialized yet. Please use RDT_Initialize() for initialization. */
	public static final int	RDT_ER_NOT_INITIALIZED					=-10000;

	/** RDT module is already initialized. It is not necessary to re-initialize. */
	public static final int	RDT_ER_ALREADY_INITIALIZED				=-10001;

	/** The number of RDT channels has reached maximum.
	* Please use RDT_Set_Max_Channel_Number() to set up the max number of RDT channels.
	* By default, the maximum channel number is #MAX_DEFAULT_RDT_CHANNEL_NUMBER. */
	public static final int	RDT_ER_EXCEED_MAX_CHANNEL				=-10002;

	/** Insufficient memory for allocation */
	public static final int	RDT_ER_MEM_INSUFF						=-10003;

	/** RDT module fails to create threads. Please check if OS has ability to
	* create threads for RDT module. */
	public static final int	RDT_ER_FAIL_CREATE_THREAD				=-10004;

	/** RDT module fails to create Mutexs when doing initialization. Please check
	* if OS has sufficient Mutexs for RDT module. */
	public static final int	RDT_ER_FAIL_CREATE_MUTEX				=-10005;

	/** RDT channel has been destroyed. Probably caused by local or remote site
	* calls RDT_Destroy(), or remote site has closed IOTC session. */
	public static final int	RDT_ER_RDT_DESTROYED					=-10006;

	/** The specified timeout has expired during the execution of some RDT module service.
	* For most cases, it is caused by slow response of remote site or network connection issues */
	public static final int	RDT_ER_TIMEOUT							=-10007;

	/** The specified RDT channel ID is valid */
	public static final int	RDT_ER_INVALID_RDT_ID					=-10008;

	/** ??? the meaning and which function uses this */
	public static final int	RDT_ER_RCV_DATA_END						=-10009;

	/** The remote site want to abort the RDT channel immediately and don't care data transmission.
	 * The local site will get this error code by RDT_Read(), RDT_Write(), RDT_Destroy() for handling
	 * this RDT channel to close. */
	public static final int RDT_ER_REMOTE_ABORT						=-10010;

	/** The local site called RDT_Abort() so the RDT channel is already not available. */
	public static final int RDT_ER_LOCAL_ABORT						=-10011;

	/** The specific IOTC session and channel ID is used now so can't use the same resource.
	 *  You can choose other IOTC channel for RDT use or wait RDT_Abort() to release resource automatically. */
	public static final int RDT_ER_CHANNEL_OCCUPIED					=-10012;

	/** This is a lite UID and it does not support RDT module. */
	public static final int RDT_ER_NO_PERMISSION					=-10013;

	/** The arguments passed to a function is invalid. */
	public static final int	RDT_ER_INVALID_ARG						=-10014;

	/** The local site called RDT_Create_Exit() so the RDT channel exit creating. */
	public static final int	RDT_ER_LOCAL_EXIT						=-10015;

	/** The remote site called RDT_Create_Exit() so the RDT channel exit creating. */
	public static final int	RDT_ER_REMOTE_EXIT						=-10016;

	//RDTApis interface
	public native static int RDT_GetRDTApiVer();	//save as Little endian
	public native static int RDT_Initialize();		//return max RDT_ID
	public native static int RDT_DeInitialize();
	public native static int RDT_Create(int nSessID, int TimeOut_ms, int ChID);
	public native static int RDT_Create_Exit(int nSessID, int ChID);
	public native static int RDT_Destroy(int RDT_ID);
	public native static int RDT_Write(int RDT_ID, byte[] data, int dataSize);
	public native static int RDT_Read(int RDT_ID, byte[] buf, int bufMaxSize, int Timeout_ms);
	public native static int RDT_Status_Check(int RDT_ID, St_RDT_Status status);

	public native static int  RDT_Abort(int nRDT_ID);
	public native static void RDT_Set_Max_Channel_Number(int nMaxChannelNum);
	public native static void RDT_Set_Log_Path(String path, int maxSize);
	public native static int RDT_Set_Max_Pending_ACK_Number(int RDT_ID, int nMaxNumber);
	public native static int RDT_Set_Max_SendBuffer_Size(int nRDTChannelID, int nMaxSendBufferSize);

	/* Obsoleted in 2011.10.3  */
	//public native int RDT_BufSizeInQueue(int RDT_ID);


	private static final String TAG = "RDTAPIs";
	private static List<String> rdtIds = new ArrayList<>();
	private static volatile int mInited = -1;

	public static int LM_RDT_Destroy(int rdtId,int sid,int chid){
		int ret = 0;
		if(sid >= 0 && chid >=0 ) {
			ret = RDTAPIs.RDT_Create_Exit(sid, chid);
			Log.e(TAG, "Connection RDTWriteAndRead RDT_Create_Exit: " + ret);
		}
        if (rdtId >= 0) {
            long time = System.currentTimeMillis();
            //RDT_Destroy 与网络速度有关,主线程调用可能出现 anr
            ret = RDTAPIs.RDT_Destroy(rdtId);
			rdtIds.remove(String.valueOf(rdtId));
			Log.e(TAG, "Connection RDTWriteAndRead RDT_Destroy: " + ret + ",time: " + (System.currentTimeMillis() - time));
		}
        return 0;
    }

	public static int LM_RDT_Create(int nSessID, int TimeOut_ms, int ChID){
        int mRet = RDT_Create(nSessID, TimeOut_ms, ChID);
        rdtIds.add(String.valueOf(mRet));
        return mRet;
    }

    public static synchronized int LM_RDT_DeInitialize(){
		if(mInited < 0){
			return 0;
		}
		int ret = -1;
		if(rdtIds.isEmpty() && mInited >= 0) {
			ret = RDTAPIs.RDT_DeInitialize();
			mInited = -1;
			Log.e(TAG, "Connection RDTWriteAndRead RDT_DeInitialize: " + ret);
		}
		return ret;
	}

	public static synchronized int LM_RDT_Initialize(){
	    if(mInited >= 0){
	        return mInited;
        }
	    int mRet = RDT_Initialize();
	    if(mRet >= 0){
	        mInited = mRet;
        }
        return mRet;
    }

	static { try {System.loadLibrary("RDTAPIs");}
			 catch(UnsatisfiedLinkError ule){
		       System.exit(0);
			   System.out.println("loadLibrary(RDTAPIs),"+ule.getMessage());
			 }
	}
}
