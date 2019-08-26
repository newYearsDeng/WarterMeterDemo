package com.northmeter.wartermeterdemo.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBean;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EstimateActivity extends AutoLayoutActivity {
    @BindView(R.id.tv_avgValue)
    TextView tvAvgValue;
    @BindView(R.id.tv_lastWater)
    TextView tvLastWater;
    @BindView(R.id.et_estimate)
    EditText etEstimate;
    @BindView(R.id.cpb_submit_estimate)
    CircularProgressButton cpbSubmitEstimate;
    //用户号
    private String customerId;
    //水表号
    private String MeterCode;
    //任务ID
    private String TaskID;
    private long longId;
    Dialog progressDialog;
    private Handler handler = new InnerHandler(this);
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_estimate);
        unbinder = ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("预估");
        baseTitleBar.hideRightSave();

        progressDialog = ToastUtil.createLoadingDialog(this, "正在获取数据..", 0);
        longId = getIntent().getLongExtra("longId", -1);
        if (longId != -1) {

            DownloadTaskBeanRESPONSEXMLBean bean = DataSupport.find(DownloadTaskBeanRESPONSEXMLBean.class, longId);
            customerId = bean.getCustomerID();
            MeterCode = bean.getMeterCode();
            TaskID=bean.getTaskID();
        }
        progressDialog.show();
        WebServiceUtils.getWebServiceInfo("AvgValue", new String[]{"CustomerID"}, new String[]{customerId}, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                if (result == null) {
                    handler.sendEmptyMessage(0);
                    return result;
                }
                if ("连接异常".equals(result)) {
                    handler.sendEmptyMessage(1);
                }
                Log.i("LHT", result);
                DownloadTaskBean downloadTaskBean = new Gson().fromJson(result, DownloadTaskBean.class);
                DownloadTaskBeanRESPONSEXMLBean bean1 = downloadTaskBean.getRESPONSEXML().get(0);
                DownloadTaskBeanRESPONSEXMLBean bean2 = downloadTaskBean.getRESPONSEXML().get(1);
                if (bean1.getRESCODE().equals("1")) {
                    Float lastWaterCode = bean2.getsDataItemValue();
                    String dataItemValueTime = bean2.getDataItemValueTime();
                    Float avgValue = bean2.getAvgValue();
                    calculator(lastWaterCode, avgValue);
                }
                return result;
            }
        });
    }

    private void calculator(Float lastWaterCode, Float avgValue) {
        if (avgValue != 0) {
            tvLastWater.setText(lastWaterCode + "");
            tvAvgValue.setText(avgValue + "");
            float thisCode = lastWaterCode + avgValue;

            etEstimate.setText(thisCode + "");
            etEstimate.setEnabled(false);
        } else {
            tvLastWater.setText(lastWaterCode + "");
            tvAvgValue.setText("无用水记录");
            etEstimate.setEnabled(true);
        }
        progressDialog.dismiss();

    }

    @OnClick(R.id.cpb_submit_estimate)
    public void onClick() {
        String msg = etEstimate.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            ToastUtil.showShort(this, "请输入本月用水预估值");
            return;
        } else {
            //上传预估值
            commit(msg);
        }

    }

    private void commit(String msg) {
        progressDialog.show();
        WebServiceUtils.getWebServiceInfo("UpdateDiscreetValue", new String[]{"CustomerID", "MeterCode", "DiscreetValue","TaskID"}, new String[]{customerId, MeterCode, msg,TaskID}, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                if (result == null) {
                    handler.sendEmptyMessage(0);
                    return result;
                }
                if ("连接异常".equals(result)) {
                    handler.sendEmptyMessage(1);
                }
                progressDialog.dismiss();
                Log.i("LHT", "预估上传结果 " + result);
                DownloadTaskBean downloadTaskBean = new Gson().fromJson(result, DownloadTaskBean.class);
                DownloadTaskBeanRESPONSEXMLBean bean1 = downloadTaskBean.getRESPONSEXML().get(0);
                if (bean1.getRESCODE().equals("1")) {
                    DataSupport.delete(DownloadTaskBeanRESPONSEXMLBean.class, longId);
                    ToastUtil.showShort(MyApplication.getContext(), "预估成功");
                    finish();
                } else {
                    handler.sendEmptyMessage(0);
                }
                return result;
            }
        });
    }
    class InnerHandler extends Handler {
        WeakReference<Activity> mActivity;

        public InnerHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
            switch (msg.what) {
                case 0:
                    ToastUtil.showShort(MyApplication.getContext(), "上传失败");
                    break;
                case 1:
                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
