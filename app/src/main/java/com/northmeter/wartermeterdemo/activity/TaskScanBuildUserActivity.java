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
 * @des 人工扫描添加的立户任务
 */
public class TaskScanBuildUserActivity extends AutoLayoutActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_scan_build_user);
       // StatusBarCompat.compat(this);
        initView();
    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(TaskScanBuildUserActivity.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("立户任务");
    }
}
