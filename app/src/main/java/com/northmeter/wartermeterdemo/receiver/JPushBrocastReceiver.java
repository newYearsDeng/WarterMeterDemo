package com.northmeter.wartermeterdemo.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.northmeter.wartermeterdemo.activity.FlashActivity;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author zz
 * @time 2016/6/3/0003 14:59
 * @des 接收推送的广播
 */
public class JPushBrocastReceiver extends BroadcastReceiver {
    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

//        //设置标签
//        Set<String> set = new LinkedHashSet<>();
//        set.add("a");
//        JPushInterface.setTags(context, set, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                LoggerUtil.d(i+"----" + s+"----"+set);
//            }
//        });

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            LoggerUtil.d("JPush用户注册成功");
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LoggerUtil.d( "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LoggerUtil.d( "接受到推送下来的通知");
            receivingNotification(context,bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LoggerUtil.d( "用户点击打开了通知");
            openNotification(context,bundle);
        } else {
            LoggerUtil.d( "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle){
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        LoggerUtil.d( " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        LoggerUtil.d( "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LoggerUtil.d( "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle){
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myValue = "";
        try {
            //当前的url的值是由，极光推送的服务传递过来的，http://www.itheima.com
            JSONObject extrasJson = new JSONObject(extras);
            myValue = extrasJson.optString("url");

        } catch (Exception e) {
            LoggerUtil.d("Exception  --  >" + e.toString());
            return;
        }
        /**
         * 接收到消息后要点击要跳转的页面
         * 这里是跳转到另一个Activity进行页面信息的显示
         */
        Intent mIntent = new Intent(context, FlashActivity.class);
//        mIntent.putExtra("url", myValue);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
        System.exit(0);
    }
}
