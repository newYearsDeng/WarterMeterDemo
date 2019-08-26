package com.northmeter.wartermeterdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wpx.WPXMain;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * @author lht
 * @time 2016/5/5 16:55
 * @des ${TODO}
 */
public class MyApplication extends LitePalApplication {
    private static Context mContext;
    //记录当前栈里所有的activity
    private List<Activity> activities = new ArrayList<>();

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;
        //初始化JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //初始化Bugly
     //   CrashReport.initCrashReport(getApplicationContext(), "900033480", false);
        //初始化LitePal，创建数据表
        SQLiteDatabase liteDatabase = Connector.getDatabase();
        // 初始化蓝牙适配器
        try {
            WPXMain.init(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化fresco
        Fresco.initialize(this);

        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    public static Context getContext() {

        return mContext;
    }

    public static MyApplication getInstance() {
        return instance;
    }


    /**
     * 新建一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 应用退出，结束所有的acitivity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    ;

}
