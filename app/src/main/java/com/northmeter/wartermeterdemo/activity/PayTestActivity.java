package com.northmeter.wartermeterdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class PayTestActivity extends AppCompatActivity {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_test);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api= WXAPIFactory.createWXAPI(this, Constants.APP_ID_WX,true);
        // 将该app注册到微信
        api.registerApp(Constants.APP_ID_WX);

        et= (EditText) findViewById(R.id.et_pay_test);
    }


    //核心支付方法
    private void sendPayReq(WXPayData info) {
        api = WXAPIFactory.createWXAPI(this, info.getAppid());
        PayReq req = new PayReq();
        req.appId = info.getAppid();
        req.partnerId = info.getPartnerid();
        req.prepayId = info.getPrepayid();//预支付id
        req.nonceStr = info.getNoncestr();//32位内的随机串，防重发
        req.timeStamp = String.valueOf(info.getTimestamp());//时间戳，为 1970 年 1 月 1 日 00:00 到请求发起时间的秒数
        req.packageValue = info.getPackage1();
        req.sign = info.getApp_signature();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    public void pay(View view) {

        final String value = et.getText().toString();

    }
}
