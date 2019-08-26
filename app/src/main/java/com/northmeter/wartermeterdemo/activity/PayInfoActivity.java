package com.northmeter.wartermeterdemo.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.PayInfo;
import com.northmeter.wartermeterdemo.service.PrintService;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.wpx.WPXMain;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * created by lht on lht
 */
public class PayInfoActivity extends AutoLayoutActivity {

    @BindView(R.id.tv_customerName_pay_result)
    TextView tvCustomerNamePayResult;
    @BindView(R.id.tv_customerCode_pay_result)
    TextView tvCustomerCodePayResult;
    @BindView(R.id.tv_customerAddr_pay_result)
    TextView tvCustomerAddrPayResult;
    @BindView(R.id.tv_LastWaterCoder_pay_result)
    TextView tvLastWaterCoderPayResult;
    @BindView(R.id.tv_nowWaterCode_pay_result)
    TextView tvNowWaterCodePayResult;
    @BindView(R.id.tv_applyWater_pay_result)
    TextView tvApplyWaterPayResult;
    @BindView(R.id.tv_unitPrice_pay_result)
    TextView tvUnitPricePayResult;
    @BindView(R.id.tv_waterPrice_pay_result)
    TextView tvWaterPricePayResult;
    @BindView(R.id.tv_printName)
    TextView tvPrintName;
    @BindView(R.id.tv_title_bar_left_text)
    TextView tvTitleBarLeftText;
    @BindView(R.id.ll_title_bar_back)
    LinearLayout llTitleBarBack;
    @BindView(R.id.tv_title_bar_center_text)
    TextView tvTitleBarCenterText;
    @BindView(R.id.tv_title_bar_right_text)
    TextView tvTitleBarRightText;
    private String msg;
    private Intent pServiceIntent;
    private Dialog mDialog;
    private String address;
    private PayInfo.RESPONSEXMLBean printInfo;
    //= "8C:8B:83:49:00:AB";//要连接的设备的mac地址
    private Unbinder unbinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_pay_info);
        unbinder = ButterKnife.bind(this);
       // StatusBarCompat.compat(this);
        EventBus.getDefault().register(this);
        initData();
        initView();
    }

    //启动打印服务
    private void startPrintService(String address) {
        pServiceIntent = new Intent(this, PrintService.class);
        pServiceIntent.putExtra("address", address);
        startService(pServiceIntent);
    }


    private void initView() {
        tvTitleBarCenterText.setText("缴费信息");
        tvTitleBarRightText.setText("打印");
        mDialog = ToastUtil.createLoadingDialog(this, "正在连接蓝牙打印机",0);
        tvCustomerNamePayResult.setText(printInfo.getCustomerName());
        tvCustomerCodePayResult.setText(printInfo.getCustomerID());
        tvCustomerAddrPayResult.setText(printInfo.getCustomerAddress());
        tvLastWaterCoderPayResult.setText(printInfo.getsDataItemValue());
        tvNowWaterCodePayResult.setText(printInfo.geteDataItemValue());
        tvApplyWaterPayResult.setText(printInfo.getClearingValueTotal());
        tvUnitPricePayResult.setText(printInfo.getECMoneySumTotal());
        tvWaterPricePayResult.setText(printInfo.getECMoneySumTotal());


    }


    private void initData() {
        printInfo = (PayInfo.RESPONSEXMLBean) getIntent().getSerializableExtra("printInfo");
        msg =printInfo.toString();

    }

    @OnClick({R.id.tv_title_bar_left_text, R.id.ll_title_bar_back, R.id.tv_title_bar_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_bar_left_text:

            case R.id.ll_title_bar_back:
                finish();
                break;
            case R.id.tv_title_bar_right_text:
                if (!WPXMain.isBluetoothEnabled()) {
                    //WPXMain.openBluetooth();
                    startActivityForResult(new Intent(PayInfoActivity.this, PrintActivity.class), 103);
                    return;
                }
                address = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SECLETBLUETOOTHMAC, "");
                if (TextUtils.isEmpty(address)) {
                    ToastUtil.showShort(MyApplication.getContext(), "请连接打印机");
                    startActivityForResult(new Intent(PayInfoActivity.this, PrintActivity.class), 103);
                    return;
                }
                if (!PayActivity.isConnected) {
                    mDialog.show();
                }
                ThreadUtils.runInWorkThread(new Runnable() {
                    @Override
                    public void run() {
                        sendBroadcast();
                    }
                });
                break;
        }
    }

    private void sendBroadcast() {
        //发送广播
        Intent intent = new Intent(Constants.SERVICE_BROADCAST_ACTION);
        intent.putExtra("msg", msg);
        intent.putExtra("address", address);
        sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void deal(String str) {
        if (!str.equals("正在连接设备")) {
            if (str.equals("连接成功")) {
                PayActivity.isConnected = true;
            }
            mDialog.dismiss();
            ToastUtil.showShort(PayInfoActivity.this, str);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            address = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SECLETBLUETOOTHMAC, "");
        }
    }
}
