package com.northmeter.wartermeterdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.northmeter.wartermeterdemo.base.BaseMaintainRepairActivity;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.Constants;

import butterknife.ButterKnife;

/**
 * @author zz
 * @time 2016/5/11 15:39
 * @des 维修任务
 */
public class TaskRepairActivity extends BaseMaintainRepairActivity {

    private String mCustomerID;
    private String mTaskID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StatusBarCompat.compat(this);
        initView();
        initData();
    }


    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(TaskRepairActivity.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("维修任务");
    }
    private void initData() {
        //得到任务详情信息
        String taskUserName =  getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_NAME);
        String taskContent = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_DETAILS);
        mTaskID = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_ID);
        mCustomerID = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_CUSTOMER_ID);

        getMessage(taskUserName,taskContent,mTaskID,mCustomerID);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
