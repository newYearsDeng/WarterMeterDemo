package com.northmeter.wartermeterdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class WebActivity extends AutoLayoutActivity {

    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.tv_title_bar_left_text)
    TextView tvTitleBarLeftText;
    @BindView(R.id.ll_title_bar_back)
    LinearLayout llTitleBarBack;
    private boolean isOnLine, isFirst;
    private Intent intent;
    private String aActivity;
    private Class<?> aClass;
    private Unbinder unbinder;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        unbinder = ButterKnife.bind(this);
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("");
        baseTitleBar.hideRightSave();


        String stringExtra = getIntent().getStringExtra(Constants.AD_LINKEDURL);
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        WebSettings webSettings = web.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        //webSettings.setPluginsEnabled(true);//support flash
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
        web.loadUrl(stringExtra);

    }

    @OnClick({R.id.tv_title_bar_left_text, R.id.ll_title_bar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_bar_left_text:
            case R.id.ll_title_bar_back:
                isOnLine = (boolean) SharedPreferencesUtils.getParam(MyApplication.getContext(), "isOnLine", false);
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
                    intent = new Intent(WebActivity.this, aClass);

                } else {
                    intent = new Intent(WebActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

