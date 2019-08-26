package com.northmeter.wartermeterdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.PayInfo;
import com.northmeter.wartermeterdemo.service.PrintService;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.northmeter.wartermeterdemo.view.ClearEditText;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 *  created by lht on 2016/5/9
 */
public class PayActivity extends AutoLayoutActivity {

    @BindView(R.id.et_company_pay)
    ClearEditText etCompanyPay;
    @BindView(R.id.et_userNum_pay)
    ClearEditText etUserNumPay;
    @BindView(R.id.btn_query_pay)
    CircularProgressButton cpb;
    private  Intent pServiceIntent;//启动打印服务
    private boolean startService;//是否启动服务
    public static  boolean isConnected;
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_pay);
       // StatusBarCompat.compat(this);
        unbinder = ButterKnife.bind(this);
        initView();


    }
    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("缴费查询");
        baseTitleBar.hideRightSave();
        cpb.setIndeterminateProgressMode(true);
    }

    @OnClick({ R.id.btn_query_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_query_pay:
                if(cpb.getProgress()==0){
                    query();
                }

                break;
        }
    }

    private void query() {
        String areaName=etCompanyPay.getText().toString().trim();
        String customerID=etUserNumPay.getText().toString().trim();
        if(!TextUtils.isEmpty(areaName)&&!TextUtils.isEmpty(customerID)){
            cpb.setProgress(50);
            netWork(areaName,customerID);
        }else{
            ToastUtil.showShort(this,"查询单位和用户号不能为空");
        }
    }

    private void netWork(String areaName, String customerID) {
        WebServiceUtils.getWebServiceInfo("SelectPayInfo", new String[]{"AreaName", "CustomerID"}, new String[]{areaName,customerID}, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                if (result == null) {
                    ToastUtil.showShort(MyApplication.getContext(), "访问失败...");
                    cpb.setProgress(100);
                    return result;
                }
                if ("连接异常".equals(result)) {
                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
                    cpb.setProgress(0);
                    return result;
                }

                dealResult(result);
                return result;
            }
        });
    }

    private void dealResult(String result) {
        cpb.setProgress(0);
        PayInfo payInfo = new Gson().fromJson(result, PayInfo.class);
        String rescode = payInfo.getRESPONSEXML().get(0).getRESCODE();
        String resmsg = payInfo.getRESPONSEXML().get(0).getRESMSG();
        if(rescode.equals("1")){
            if("成功".equals(resmsg)){
                PayInfo.RESPONSEXMLBean responsexmlBean = payInfo.getRESPONSEXML().get(1);
                Intent intent=new Intent(PayActivity.this,PayInfoActivity.class);
                intent.putExtra("printInfo", responsexmlBean);
                startActivity(intent);
            }else{
                ToastUtil.showShort(this,resmsg);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        stopService(pServiceIntent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(!startService){
            startService=!startService;
            ThreadUtils.runInWorkThread(new Runnable() {
                @Override
                public void run() {
                    //启动服务
                    String address = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SECLETBLUETOOTHMAC, "");
                    pServiceIntent = new Intent(MyApplication.getContext(), PrintService.class);
                    pServiceIntent.putExtra("address", address);
                    startService(pServiceIntent);
                }
            });
        }

    }


    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
