package com.wh.tutkwrapper;

public class TuTkException extends Exception {

    int mError;

    public TuTkException(int error, String detailMessage) {
        super(detailMessage);
        mError = error;
    }

    public int getError() {
        return mError;
    }
}
