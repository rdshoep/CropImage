package com.rdshoep.android.image;
/*
 * @description
 *   Please write the BackgroundTask module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(12/29/2015)
 */

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

abstract class BackgroundTask<T> implements Handler.Callback {

    static final int MESSAGE_FINISH = 1;
    static final int MESSAGE_TASK_START = 2;
    static final int MESSAGE_TASK_END = 3;

    HandlerThread mBackgroundThread;
    Handler mBackgroundHandler;
    Handler mUiHandler;

    public BackgroundTask() {
        mBackgroundThread = new HandlerThread("ImageCalculate", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper(), this);
        mUiHandler = new Handler(Looper.getMainLooper(), this);
    }

    /**
     * 开始执行
     */
    public final void execute() {
        mBackgroundHandler.sendEmptyMessage(MESSAGE_TASK_START);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MESSAGE_FINISH:
                //noinspection unchecked
                onFinish((T) msg.obj);
                mBackgroundHandler.sendEmptyMessage(MESSAGE_TASK_END);
                return true;
            case MESSAGE_TASK_START:
                T data = doInBackgroundThread();
                mUiHandler.sendMessage(mUiHandler.obtainMessage(MESSAGE_FINISH, data));
                return true;
            case MESSAGE_TASK_END:
                try {
                    mBackgroundThread.quit();
                } catch (Exception ignored) {
                    //保证子线程正常退出
                }
                break;
        }
        return false;
    }

    /**
     * 执行完成 UiThread
     *
     * @param data 后台处理后的数据
     */
    public abstract void onFinish(T data);

    /**
     * 后台处理任务
     *
     * @return 处理结果
     */
    public abstract T doInBackgroundThread();
}
