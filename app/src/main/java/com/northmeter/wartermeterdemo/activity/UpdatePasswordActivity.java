package com.northmeter.wartermeterdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.LoginUserInfo;
import com.northmeter.wartermeterdemo.bean.PassResult;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.northmeter.wartermeterdemo.view.ClearEditText;
import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class UpdatePasswordActivity extends AutoLayoutActivity {

    @BindView(R.id.tv_title_bar_right_text)
    TextView tvSaveReadMeter;
    @BindView(R.id.et_oldPwd_UpdatePwd)
    ClearEditText etOldPwdUpdatePwd;
    @BindView(R.id.et_newPwd_UpdatePwd)
    ClearEditText etNewPwdUpdatePwd;
    @BindView(R.id.et_confirm_UpdatePwd)
    ClearEditText et_confirm;
    private static final String CONNECT_FAIL = "连接异常";
    private String[] paramsName = new String[]{"Login_name", "Login_Opwd", "Login_Npwd"};
    private String[] paramsValues;
    private Handler handler = new Handler();
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_update_password);
        unbinder = ButterKnife.bind(this);
        initView();

        initlistener();


    }

    private void initlistener() {

        etNewPwdUpdatePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  if (s.length()>15){
                      etNewPwdUpdatePwd.setError("密码不能超过15位");
                  }
            }
        });
        et_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>15){
                    et_confirm.setError("密码不能超过15位");
                }
            }
        });
    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("密码修改");
        tvSaveReadMeter.setText("提交");

    }
    private void savePwd() {
        String oldPwd = etOldPwdUpdatePwd.getText().toString().trim();
        String newPwd = etNewPwdUpdatePwd.getText().toString().trim();
        String confirmPwd = et_confirm.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd)) {
            etOldPwdUpdatePwd.setError("密码不能为空");
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            etNewPwdUpdatePwd.setError("修改密码不能为空");
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            et_confirm.setError("两次密码不一样，请重新输入");
            return;
        }
        if(newPwd.length()>15||confirmPwd.length()>15){
            return;
        }

        String userName = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_NAME_KEY, "");
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showShort(this, "用户名为空");
            return;
        }
        paramsValues = new String[]{userName, oldPwd, newPwd};
        //联网操作
        newWork();

    }

    private void newWork() {
        WebServiceUtils.getWebServiceInfo("UpdatePWD", paramsName, paramsValues, new WebServiceUtils.CallBack() {
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
                Log.i("LHT", result);
                PassResult passResult = new Gson().fromJson(result, PassResult.class);
                PassResult.RESPONSEXMLBean responsexmlBean = passResult.getRESPONSEXML().get(0);
                String rescode = responsexmlBean.getRESCODE();
                String resmsg = responsexmlBean.getRESMSG();
                if (rescode.equals("1")) {
                    if(PopularActivity.instance!=null){
                    PopularActivity.instance.finish();}
                    if(SettingActivity.instance!=null){
                    SettingActivity.instance.finish();}
                    ToastUtil.showShort(MyApplication.getContext(), "修改成功");
                    SharedPreferencesUtils.setParam(MyApplication.getContext(), "isOnLine", false);
                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_PASSWORD_KEY, "");
                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_ISMEMORY_KEY, false);
                    //密码修改成功后,改变数据库中密码的保存状态
                    List<LoginUserInfo> loginUserInfos = DataSupport.where("username = ?", paramsValues[0]).
                            find(LoginUserInfo.class);
                    long id = loginUserInfos.get(0).getId();
                    Log.i("LHT","id "+id);
                    Log.i("LHT","username "+paramsValues[0]);
                    LoginUserInfo loginUserInfos1=new LoginUserInfo();
                    loginUserInfos1.setSave("不保存");

                   // boolean save = loginUserInfos1.save();
                    //  int i = loginUserInfos1.updateAll("id = ?", id + "");

                    int update = loginUserInfos1.update(id);
                  Log.i("LHT","update set "+ update);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(UpdatePasswordActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, 1000);

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
        if (handler != null) {
            handler = null;
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

    @OnClick({R.id.tv_title_bar_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_bar_right_text:
                savePwd();
                break;
        }

    }

    private void dealEdit(boolean mdis, EditText et_Text, ImageButton ib_Button) {

        if(!mdis){
//显示密码
            et_Text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ib_Button.setBackgroundResource(R.drawable.pwd_show);
        }else{

//隐藏密码
            et_Text.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ib_Button.setBackgroundResource(R.drawable.pwd_dismiss);
        }
    }


}
