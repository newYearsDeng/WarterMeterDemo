package com.northmeter.wartermeterdemo.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * @author zz
 * @time 2016/5/10 17:24
 * @des 顶部标题栏的基类
 */
public class BaseTitleBar extends AutoLayoutActivity implements View.OnClickListener {
    private final AppCompatActivity mActivity;
    private LinearLayout mLlLeftBack;
    private final TextView mTvTitleBarCenterText;
    private final TextView mTvRightSave;

    /**
     * @param activity 传activity的实例 XxxActivty.this
     * @param view 传activty自身的view，getWindow().getDecorView()
     */
    public BaseTitleBar(AppCompatActivity activity,View view) {
        this.mActivity = activity;
        mLlLeftBack = (LinearLayout) view.findViewById(R.id.ll_title_bar_back);
        mTvTitleBarCenterText = (TextView) view.findViewById(R.id.tv_title_bar_center_text);
        mTvRightSave = (TextView) view.findViewById(R.id.tv_title_bar_right_text);
        initEvent();
    }

    private void initEvent() {
        mLlLeftBack.setOnClickListener(this);
       // mTvRightSave.setOnClickListener(this);
    }

    /**
     * 设置中间标题文字
     * @param centerText
     */
    public void setCenterText(String centerText){
        mTvTitleBarCenterText.setText(centerText);
    }



    /**
     * 隐藏右侧保存按钮
     */
    public void hideRightSave(){
        mTvRightSave.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_bar_back:
                mActivity.finish();
                break;
            case R.id.tv_title_bar_right_text://保存让自己去实现
                break;
        }
    }
}
