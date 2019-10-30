package com.wh.tutkwrapper;

import android.util.Log;

import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.IOTCAPIs;
import com.tutk.IOTC.RDTAPIs;

import java.util.concurrent.ConcurrentHashMap;

public class TuTkManager {

    public static final int ERR_CODE_RECV_TIME_OUT = -2;
    public static final int ERR_CODE_RECV_DISCONNECT = -3;

    private static final String TAG = "TuTkManager";
    public static final Object DEINIT_LOCK = new Object();

    private static final TuTkManager INSTANCE = new TuTkManager();
    private static final int MAX_NUM = 12;
    private static final int INIT_OK = 0;

    private volatile int initIOTC = -1;
    private ConcurrentHashMap<String, TuTkClient> clients = new ConcurrentHashMap<>();

    private TuTkManager() {
    }

    public static TuTkManager instance() {
        return INSTANCE;
    }

    public void putClient(String key, TuTkClient client){
        clients.put(key, client);
    }

    public void removeClient(String key){
        clients.remove(key);
    }

    public TuTkClient getClient(String key){
        return clients.get(key);
    }

    public int clientSize(){
        return clients.size();
    }

    /**
     * 初始化IOTC,多次连接只需调用一次
     *
     * @return
     */
    public int initIOTC() {
        synchronized (DEINIT_LOCK) {
            if (initIOTC == INIT_OK) {
                return INIT_OK;
            }
            IOTCAPIs.IOTC_Setup_Session_Alive_Timeout(16);
            int port = (int) (10000L + System.currentTimeMillis() % 10000L);
            int mRet = IOTCAPIs.lmIOTC_Initialize2(port);
            Log.e(TAG, "Connection lmIOTC_Initialize2 " + mRet);
            if(mRet != IOTCAPIs.IOTC_ER_ALREADY_INITIALIZED && mRet != IOTCAPIs.IOTC_ER_NoERROR){
                return mRet;
            }
            mRet = AVAPIs.lmAvInitialize(MAX_NUM);
            AVAPIs.lmAvClientSetMaxBufSize(5000);
            Log.e(TAG, "Connection lmAvInitialize " + mRet);
            if(mRet < 0){
                return mRet;
            }
            mRet = RDTAPIs.RDT_Initialize();
            Log.e(TAG, "Connection RDT_Initialize " + mRet);
            if(mRet != RDTAPIs.RDT_ER_ALREADY_INITIALIZED && mRet != RDTAPIs.RDT_ER_NoERROR){
                return mRet;
            }
            initIOTC = INIT_OK;
            return INIT_OK;
        }
    }

    /**
     * 去掉初始化,必须在所有连接断开后调用
     */
    public void deInitIOTC() {
        synchronized (DEINIT_LOCK) {
            int clientSize = clients.size();
            Log.e(TAG, "Connection deInitIOTC " + clientSize);
            if (clientSize <= 0) {
                int mRet = AVAPIs.lmAvDeInitialize();
                Log.e(TAG, "Connection lmAvDeInitialize " + mRet);
                mRet = IOTCAPIs.lmIOTC_DeInitialize();
                Log.e(TAG, "Connection lmIOTC_DeInitialize " + mRet);
                mRet = RDTAPIs.RDT_DeInitialize();
                Log.e(TAG, "Connection RDT_DeInitialize " + mRet);
                initIOTC = -1;
            }
        }
    }

    public String getErrorInfo(int code) {
        return "error: " + code;
    }

}
