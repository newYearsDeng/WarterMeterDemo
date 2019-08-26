package com.northmeter.wartermeterdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * @author zz
 * @time 2016/5/11 15:39
 * @des 抄表任务
 */
public class TaskMeterActivity extends AutoLayoutActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_task_meter);
        initView();
    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(TaskMeterActivity.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("抄表任务");
    }
}
