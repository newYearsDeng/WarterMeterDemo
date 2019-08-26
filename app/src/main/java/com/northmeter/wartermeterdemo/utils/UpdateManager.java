package com.northmeter.wartermeterdemo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.PassResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * created by lht on 2016/6/2 10:12
 */
public class UpdateManager {
    private static final int DOWNLOAD = 1;//下载中
    private static final int DOWNLOAD_FINISH = 2;//下载完成
    private static final int GETSERVER_FINISH = 3;
    private boolean cancelUpdate = false;//是否取消更新
    //跟新进度条
    private ProgressBar mProgress;
    private Dialog mDownLoadDialog;
    private static final int SetMax = 4;
    private int progress;//进度条的数量
    private Context context;
    private String m_appNameStr;
    private String m_newVerCode;//新版本
    private boolean isUpdate;
    private Activity mActivity;
    private int contentLength;
    private TextView tv;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD:
                    mProgress.setProgress(progress);
                    tv.setText(progress + "%");
                    break;
                case DOWNLOAD_FINISH:
                    //安装apk
                    installApk();
                    ToastUtil.showShort(context, "完成");
                    break;
                case GETSERVER_FINISH:
                    checkUpdate();
                    break;
                case SetMax:
                    // mProgress.setMax(contentLength);
                    break;
                default:
                    break;


            }
        }
    };

    public UpdateManager(Context con, Activity mactivity) {
        this.context = con;
        this.mActivity = mactivity;
    }

    /**
     * 检测软件是否跟新
     */
    private void checkUpdate() {
        if (isUpdate) {
            //doNewVersionUpdate(); // 更新新版本  ;
            showNoticeDialog();
        } else {
            notNewVersionDlgShow(); // 提示当前为最新版本
        }


    }


    private void notNewVersionDlgShow() {
        // ToastUtil.showShort(context, "最新版本");
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog() {

        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String str = "当前版本：" + getVerName(context) + ", 发现新版本：" +
                 m_newVerCode + " ,是否更新？";
        showUpdateNotification(str);
        builder.setTitle("软件版本更新");
        builder.setMessage(str)
        .setCancelable(false);
        // 更新
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showUpdateNotification(String str) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(str)
                .setContentTitle("水表APP版本更新提示")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("正在下载");
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        tv = (TextView) v.findViewById(R.id.tv_update);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownLoadDialog = builder.create();
        mDownLoadDialog.setCancelable(false);
        mDownLoadDialog.show();
        // 现在文件
        downLoadApk();
    }

    private void downLoadApk() {
        ThreadUtils.runInWorkThread(new Runnable() {


            @Override
            public void run() {

                try {
                    // 判断SD卡是否存在，并且是否具有读写权限
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        // 获得存储卡的路径
                        String sdpath = Environment.getExternalStorageDirectory() + "/" + m_appNameStr;
                        String urlStr = Constants.UPDATESOFTADDRESS + m_appNameStr;
                        // String UPDATESOFTADDRESS="http://218.17.157.121:9147/APPAction/5.31_31.apk";
                        URL url = new URL(urlStr);
                        // 创建连接
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.connect();
                        // 获取文件大小
                        contentLength = conn.getContentLength();

                        mHandler.sendEmptyMessage(SetMax);

                        // 创建输入流
                        InputStream is = conn.getInputStream();
                        // 判断文件目录是否存在

                        File apkFile = new File(sdpath);
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        int count = 0;
                        // 缓存
                        byte buf[] = new byte[1024];
                        // 写入到文件中
                        do {
                            int numread = is.read(buf);
                            count += numread;
                            // 计算进度条位置
                            progress = (int) (((float) count / contentLength) * 100);
                            // 更新进度
                            mHandler.sendEmptyMessage(DOWNLOAD);
                            if (numread <= 0) {
                                // 下载完成
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                                break;
                            }
                            // 写入文件
                            fos.write(buf, 0, numread);
                        } while (!cancelUpdate);// 点击取消就停止下载.
                        fos.close();
                        is.close();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 取消下载对话框显示
                mDownLoadDialog.dismiss();
            }
        });
    }

    public void connectServer() {
        ThreadUtils.runInWorkThread(new CheckUpdate());
        ;
    }

    public class CheckUpdate implements Runnable{

        @Override
        public void run() {
            String s = NetUtils.loadStringFromURL(Constants.VERSIONNUM);

            if (s == null) {
                return;
            }
            String result = Html.fromHtml(s).toString();
            Log.i("LHT","版本更新： "+result);
            PassResult passResult = new Gson().fromJson(result, PassResult.class);
            List<PassResult.RESPONSEXMLBean> responsexml = passResult.getRESPONSEXML();
            PassResult.RESPONSEXMLBean responsexmlBean = responsexml.get(0);
            String rescode = responsexmlBean.getRESCODE();
            String resmsg = responsexmlBean.getRESMSG();
            if (!rescode.equals("1")) {
                ToastUtil.showShort(context, resmsg);
                return;
            }
            PassResult.RESPONSEXMLBean responsexmlBean1 = responsexml.get(1);
            m_newVerCode = responsexmlBean1.getVersionNumber();
            m_appNameStr = responsexmlBean1.getVersionAppName();

            String verName = getVerName(context);
            str2Int(verName);
            if (str2Int(m_newVerCode)>str2Int(verName)) {
                isUpdate = true;
            }
            mHandler.sendEmptyMessage(GETSERVER_FINISH);
        }
    }
    public int  str2Int(String version){
        StringBuffer sb=new StringBuffer();
        for (int i = 0; i < version.length(); i++) {
            String s = String.valueOf(version.charAt(i));
            if(!s.equals(".")){
                sb.append(s);
            }
        }
        return Integer.parseInt(sb.toString());
    }
    /**
     * 安装APK文件
     */
    private void installApk() {
        File apkfile = new File(Environment
                .getExternalStorageDirectory(), m_appNameStr);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
    }

    //获取版本号
    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verCode;
    }

    //获取版本名字
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("msg", e.getMessage());
        }
        return verName;
    }


}
