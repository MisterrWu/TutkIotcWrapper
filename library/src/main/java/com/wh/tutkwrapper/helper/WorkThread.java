package com.wh.tutkwrapper.helper;


import android.util.Log;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * wait、notify、notifyAll都必须在synchronized中执行，否则会抛出异常 IllegalMonitorStateException,
 *
 * synchronized关键字和ReentrantLock锁都是辅助线程同步使用的,sleep方法没有释放锁，而wait方法释放了锁,
 *
 * 如果暂停 SUSPEND 使用wait,恢复 setResume 使用notifyAll。
 *
 * 问题一：在doInitial中有使用Thread.sleep 并且时间很长,
 * 那么调用 setResume 唤醒线程时会等待sleep执行完才能获取到锁synchronized,从而出现 anr。
 *
 * 问题二：在doRepeatWork中有调用LinkedBlockingQueue的put函数，如果队列已经满了，
 * 那么调用 setResume 唤醒线程时notifyAll会等待队列中putLock.unlock()的调用，从而出现 anr.
 *
 * 1.使用reentrantlock可以完成同样的功能,需要注意的是，必须要必须要必须要手动释放锁（重要的事情说三遍）
 *
 * 2.使用reentrantlock可以进行“尝试锁定”tryLock，这样无法锁定，或者在指定时间内无法锁定，线程可以决定是否继续等待
 *
 * 3.使用ReentrantLock还可以调用lockInterruptibly方法，可以对线程interrupt方法做出响应；在一个线程等待锁的过程中，可以被打断
 *
 * 4.ReentrantLock还可以指定为公平锁
 *
 * @author wuhan
 * @date 2019/3/6 21:09
 */
public abstract class WorkThread extends Thread {

    private static final String TAG = "WorkThread";

    private final ReentrantLock pauseLock = new ReentrantLock();

    private final Condition notPause = pauseLock.newCondition();

    public final static int STOP = -1;
    public final static int SUSPEND = 0;
    public final static int RUNNING = 1;
    public volatile int status = STOP;

    public WorkThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        int mRet = this.doInitial();
        if(mRet >= 0) {
            while (status != STOP) {
                if (status == SUSPEND) {
                    await();
                } else {
                    try {
                        this.doRepeatWork();
                    } catch (IllegalStateException var2) {
                        var2.printStackTrace();
                    }
                }
            }
        }
        Log.e(TAG, getName()+" doRelease.. ");
        this.doRelease();
    }

    private void await(){
        final ReentrantLock putLock = this.pauseLock;
        try {
            putLock.lockInterruptibly();
            Log.e(TAG, getName() + " await ");
            notPause.await();
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, getName() + " 线程异常终止.. ");
        } finally {
            Log.e(TAG, getName() + " unlock ");
            try {
                putLock.unlock();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void signal(){
        final ReentrantLock putLock = this.pauseLock;
        try {
            putLock.lock();
            Log.e(TAG, getName() + " signal ");
            notPause.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                putLock.unlock();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected abstract int doRepeatWork();

    protected abstract int doInitial();

    protected abstract void doRelease();

    @Override
    public synchronized void start() {
        if(getState() == State.NEW) {
            try {
                super.start();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        status = RUNNING;
    }

    public void stopThread() {
        setStop();
    }

    public void setResume() {
        status = RUNNING;
        signal();
    }

    public void setSuspend() {
        status = SUSPEND;
    }

    public void setStop() {
        status = STOP;
        signal();
    }
    public int getStatus() {
        return this.status;
    }
}
