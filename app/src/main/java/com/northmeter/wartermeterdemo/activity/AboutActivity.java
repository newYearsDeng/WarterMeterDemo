package com.northmeter.wartermeterdemo.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.bean.LoginUserInfo;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.DataCleanManager;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.UpdateManager;
import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AboutActivity extends AutoLayoutActivity implements View.OnClickListener {
    @BindView(R.id.tv_codeName)
    TextView tv_CodeName;
    @BindView(R.id.tv_clean)
    TextView tv_clean;
    private File file;
    private ProgressDialog progressDialog;
    private InterHandler handler = new InterHandler(this);
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_about);
        unbinder = ButterKnife.bind(this);
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("关于");
        baseTitleBar.hideRightSave();
        String verName = UpdateManager.getVerName(this);
        String codeName = "版本号:" + verName;
        tv_CodeName.setText(codeName);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在删除");

        file = new File(Constants.SAVEPIC);
        tv_clean.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clean:
                createDialog();
                break;
        }
    }

    private void createDialog() {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("清空本地数据，将会退出软件，需要重新登陆!")
                    .setTitle("提示:")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.sendEmptyMessage(0);
                                dealClean();
                                handler.sendEmptyMessage(1);
                            }
                    })
                    .setCancelable(false)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

     public void dealClean() {
        //清除shared
        SharedPreferencesUtils.cleanSp(MyApplication.getContext());

        //清除数据库
       DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class);

       DataSupport.deleteAll(LoginUserInfo.class);
        //清楚图片DataCleanManager.deleteDir(file);
         if(PopularActivity.instance!=null){
             PopularActivity.instance.finish();}
         if(HomePageActivity.instance!=null){
             HomePageActivity.instance.finish();
         }
         if(SettingActivity.instance!=null){
             SettingActivity.instance.finish();}
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    private class InterHandler extends Handler {
        private WeakReference<AboutActivity> mActivity;

        public InterHandler(AboutActivity activity) {
            mActivity = new WeakReference<AboutActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    progressDialog.show();
                    break;
                case 1:
                    progressDialog.dismiss();
                    ToastUtil.showShort(MyApplication.getContext(), "清除完成");
                    break;
            }
        }
    }
}
