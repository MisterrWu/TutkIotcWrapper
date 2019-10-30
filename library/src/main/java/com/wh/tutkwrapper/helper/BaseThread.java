package com.wh.tutkwrapper.helper;


public abstract class BaseThread extends WorkThread {

    public BaseThread(String name) {
        super(name);
    }

    @Override
    protected int doInitial() {
        return 0;
    }

    @Override
    protected void doRelease() {}
}
