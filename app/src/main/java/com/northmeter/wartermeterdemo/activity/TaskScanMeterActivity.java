package com.northmeter.wartermeterdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * @author zz
 * @time 2016/5/11 15:39
 * @des 人工扫描添加的抄表任务
 */
public class TaskScanMeterActivity extends AutoLayoutActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_scan_meter);
       // StatusBarCompat.compat(this);
        initView();
    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(TaskScanMeterActivity.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("抄表任务");
    }
}
