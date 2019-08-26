package com.northmeter.wartermeterdemo.service;

import android.app.Dialog;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.wpx.IBluetoothPrint;
import com.wpx.WPXMain;
import com.wpx.util.GeneralAttributes;

import org.greenrobot.eventbus.EventBus;

/**
 * created by lht on 2016/7/8 10:52
 */
public class PrintService extends Service implements WPXMain.SearchCallBack {
    private PintABroadCastReciver reciver;
    private boolean isConnected;
    private String address;
    @Nullable


    @Override
    public void onCreate() {
        super.onCreate();
        //动态注册广播接收器；
        reciver=new PintABroadCastReciver();
        registerReceiver(reciver,new IntentFilter(Constants.SERVICE_BROADCAST_ACTION));
          Log.i("LHT","服务创建");
        WPXMain.addSerachDeviceCallBack(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*address = intent.getStringExtra("address");
        if(!TextUtils.isEmpty(address)){
            sendBroadcastMsg("正在连接设备");
            isConnected=WPXMain.connectDevice(address);
            if(isConnected){
                sendBroadcastMsg("连接成功");
            }else  if(!isConnected){
                sendBroadcastMsg("连接失败");
            }
        }*/
         Log.i("LHT","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LHT","打印服务销毁");
        unregisterReceiver(reciver);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void startSearch() {

    }

    @Override
    public void searching(BluetoothDevice bluetoothDevice) {

    }

    @Override
    public void stopSearch() {

    }

    @Override
    public void onStateChange() {
        if(isConnected){
            isConnected=false;
            //sendBroadcastMsg("连接断开");
            EventBus.getDefault().post("连接断开");
        }
    }

    //广播接收器
    class PintABroadCastReciver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
             final String msg = intent.getStringExtra("msg");
              address=intent.getStringExtra("address");
            Log.i("LHT","接受来自activity 的信息");
            ThreadUtils.runInWorkThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected){
                        // sendBroadcastMsg("正在连接设备");
                        EventBus.getDefault().post("正在连接设备");

                        isConnected= WPXMain.connectDevice(address);
                        if(isConnected){
                            //  sendBroadcastMsg("连接成功");
                            EventBus.getDefault().post("连接成功");
                        }else  if(!isConnected){
                            // sendBroadcastMsg("连接失败");
                            EventBus.getDefault().post("连接失败");
                        }
                    }else {
                        WPXMain.printCommand(GeneralAttributes.INSTRUCTIONS_ESC_INIT);
                        final IBluetoothPrint bp = WPXMain.getBluetoothPrint();
                        bp.printText(msg);
                    }
                }
            });


        }
    }
}
