package com.northmeter.wartermeterdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.PassResult;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AdviceActivity extends AutoLayoutActivity implements TextWatcher {
    @BindView(R.id.tv_title_bar_right_text)
    TextView tvSubmit;
    @BindView(R.id.et_advice_Advice)
    EditText etAdviceAdvice;
    @BindView(R.id.tv_num_Advice)
    TextView tv_num;
    private static final String CONNECT_FAIL = "连接异常";
    private String[] paramsName = new String[]{"Login_name", "CONTENT"};
    private String[] paramsValues;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_advice);
        unbinder = ButterKnife.bind(this);
        initView();


    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("意见反馈");
        tvSubmit.setText("提交");
        etAdviceAdvice.addTextChangedListener(this);

    }

    @OnClick({R.id.tv_title_bar_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_bar_right_text:
                updateData();
                break;
        }
    }

    private void updateData() {

        String userAdv = etAdviceAdvice.getText().toString().trim();
        String userName = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_NAME_KEY, "");
        if (TextUtils.isEmpty(userAdv)) {
            etAdviceAdvice.setError("内容不能为空");
            return;
        }
        if(userAdv.length()>100){
            ToastUtil.showShort(this,"长度超出限制，请重新输入");
            return;
        }
        paramsValues = new String[]{userName, userAdv};

        //发送用户意见的网络请求
        WebServiceUtils.getWebServiceInfo("UpLoadAdivce", paramsName, paramsValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                if (result == null) {
                    ToastUtil.showShort(MyApplication.getContext(), "访问失败...");
                    return result;
                }
                if (CONNECT_FAIL.equals(result)) {
                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
                    return result;
                }
                PassResult passResult = new Gson().fromJson(result, PassResult.class);
                PassResult.RESPONSEXMLBean responsexmlBean = passResult.getRESPONSEXML().get(0);
                String rescode = responsexmlBean.getRESCODE();
                String resmsg = responsexmlBean.getRESMSG();
                if (rescode.equals("1")) {
                    finish();
                    ToastUtil.showShort(MyApplication.getContext(), "提交成功");
                } else {
                    ToastUtil.showLong(MyApplication.getContext(), resmsg);
                }
                return result;
            }
        });

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int num;
        int length = s.length();
        num=length;
       // tv_num.setText("数字");
        tv_num.setText(num+"/100");
        if (length > 100) {
            tv_num.setTextColor(Color.RED);
            etAdviceAdvice.setError("字数超出了100");
           // etAdviceAdvice.setEnabled(false);
        } else {
            tv_num.setTextColor(Color.BLACK);
           // etAdviceAdvice.setEnabled(true);

        }
    }
}
