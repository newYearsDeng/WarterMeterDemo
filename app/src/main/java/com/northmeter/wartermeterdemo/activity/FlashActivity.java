package com.northmeter.wartermeterdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.AD;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.service.LoadAdService;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.view.RoudProgressBar;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import cn.jpush.android.api.JPushInterface;

public class FlashActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isOnLine;
    private boolean isFirst;
    private Intent intent;
    private String aActivity;
    private Class<?> aClass;
    private ImageView ivAd;
    private Intent loadIntent;
    private RoudProgressBar rpb;
    private int progress;
    private boolean jump;
    private Bitmap bitmap;
    private String adUrl;
    private Handler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_flash);
        loadIntent = new Intent(this, LoadAdService.class);
        initSetting();
        initData();
        initView();
        if (isFirst) {
            intent = new Intent(this, GuideActivity.class);
            SharedPreferencesUtils.setParam(MyApplication.getContext(), "isFirst", false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    finish();
                }
            }, 1000);

        } else {
            if (isOnLine) {
                aActivity = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.LOGINEDACTIVITY, "LoginActivity");

                switch (aActivity) {
                    case "LoginActivity":
                        aClass = LoginActivity.class;
                        break;
                    case "HomePageActivity":
                        aClass = HomePageActivity.class;
                        break;
                    case "PopularActivity":
                        aClass = PopularActivity.class;
                        break;
                }
                intent = new Intent(FlashActivity.this, aClass);

            } else {
                intent = new Intent(FlashActivity.this, LoginActivity.class);
            }

//            ThreadUtils.runInWorkThread(new Runnable() {
//                @Override
//                public void run() {
//                    while (progress < 100) {
//                        progress += 2;
//                        rpb.setProgress(progress);
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    jumpOtherActivity();
//                }
//            });
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    jumpOtherActivity();
                }
            },500);
        }
    }


    private void initView() {
       // ivAd = (ImageView) findViewById(R.id.iv_ad);
       // ivAd.setImageBitmap(bitmap);
       // ivAd.setOnClickListener(this);
       // rpb = (RoudProgressBar) findViewById(R.id.rpb);
       // rpb.setOnClickListener(this);
//        if (isFirst) {

//            rpb.setVisibility(View.GONE);
//        }


    }

    private void initData() {
        isOnLine = (boolean) SharedPreferencesUtils.getParam(MyApplication.getContext(), "isOnLine", false);
        isFirst = (boolean) SharedPreferencesUtils.getParam(MyApplication.getContext(), "isFirst", true);
//        AD first = DataSupport.findFirst(AD.class);
//        if (first == null) {
//            return;
//        }
//        byte[] pic = first.getPic();
//        bitmap = BitmapFactory.decodeByteArray(pic, 0, pic.length);
//        adUrl=first.getUrl();

    }

    private void initSetting() {

      /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //减少图片占用内存
        RelativeLayout rlBackground = (RelativeLayout) findViewById(R.id.rl_flash_bg);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = getResources().openRawResource(+R.drawable.flash1);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        rlBackground.setBackgroundDrawable(bd);*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rpb:
                jumpOtherActivity();
                break;
            case R.id.iv_ad:
                if(isNetworkAvailable()){
                    jump = !jump;
                Intent intent=new Intent(this,WebActivity.class);
                intent.putExtra(Constants.AD_LINKEDURL,adUrl);
                startActivity(intent);
                finish();
                }
                break;
        }

    }

    private synchronized void jumpOtherActivity() {
        if (!jump) {
            jump = !jump;
            startActivity(intent);
             overridePendingTransition(R.anim.fade, R.anim.hold);
            finish();
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<FlashActivity> mActivity;

        MyHandler(FlashActivity mActivity1) {
            mActivity = new WeakReference<FlashActivity>(mActivity1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
      //  startService(loadIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
    }
    /**
     * 检测网络是否连接
     *
     * @return
     */
    private boolean isNetworkAvailable() {
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            return manager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
