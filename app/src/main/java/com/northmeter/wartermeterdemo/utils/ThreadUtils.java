package com.northmeter.wartermeterdemo.utils;

import android.os.Handler;

/**
 * Created by lht on 2016/5/10.
 */
public class ThreadUtils {
    /**
     *
     * @param task 线程
     */
    public static void runInWorkThread(Runnable task){
        new Thread(task).start();
    }

    public static Handler handler=new Handler();

    /**
     *
     * @param task  线程
     */
    public static void runInUiThread(Runnable task){
        handler.post(task);
    }
}
