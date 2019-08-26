package com.northmeter.wartermeterdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * @author zz
 * @time 2016/5/9 10:35
 * @des 立户
 */
public class BuildUserActivity extends AutoLayoutActivity implements View.OnClickListener {
    private Button mBtnChange;
    private Button mBtnUpload;
    private TextView mTvSave;
    private LinearLayout mLlBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_builduser);
        //StatusBarCompat.compat(this);
        initView();
        initEvent();
    }

    public void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(BuildUserActivity.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("立户");
        mBtnChange = (Button) findViewById(R.id.btn_builderuser_change);
        mBtnUpload = (Button) findViewById(R.id.btn_builderuser_upload);
    }

    private void initEvent() {
        mBtnChange.setOnClickListener(this);
        mBtnUpload.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_builderuser_change:
//                String qr_code = getIntent().getStringExtra("qr_code");
                ToastUtil.showShort(MyApplication.getContext(), "修改");
                break;
            case R.id.btn_builderuser_upload:
                ToastUtil.showShort(MyApplication.getContext(), "上传");
                break;
        }
    }
}
