package com.northmeter.wartermeterdemo.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.presenter.ForgetPasswordPresenter;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zz
 * @time 2016/09/09 11:01
 * @des 忘记密码的view
 */
public class ForgetPasswordActivty extends AutoLayoutActivity implements IForgetPasswordActivity {
    @BindView(R.id.tv_title_bar_right_text)
    TextView mRightSave;
    @BindView(R.id.et_forget_pwd_phone_num)
    EditText mEtPhoneNum;
    @BindView(R.id.tv_forget_pwd_problem)
    TextView mTvProblem;
    @BindView(R.id.et_forget_pwd_answer)
    EditText mEtAnswer;
    @BindView(R.id.et_forget_pwd_input_password)
    EditText mEtInputPassword;
    @BindView(R.id.et_forget_pwd_confirm_password)
    EditText mEtConfirmPassword;
    @BindView(R.id.ll_forget_pwd_problem_and_answer)
    LinearLayout mLlProblemAndAnswer;
    private ForgetPasswordPresenter mForgetPwdPresenter;
    private String[] mProblemArr;
    private boolean isReset = false;
    private Dialog mVerifyDialog;
    private Dialog mResetDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_forget_password);
        unbinder = ButterKnife.bind(this);

        //初始化presenter
        mForgetPwdPresenter = new ForgetPasswordPresenter(this);

        //监听电话号码输入框是否变化
        mEtPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mForgetPwdPresenter.setCheckFlag(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        initTitle();
    }

    private void initTitle() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(ForgetPasswordActivty.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("重置密码");
        mRightSave.setText("完成");
//        mRightSave.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_forget_pwd_phone_verify, R.id.rl_forget_pwd_problem_bg, R.id.tv_title_bar_right_text})
    public void onClick(View view) {
        //获取输入框信息
        String phoneNumber = mEtPhoneNum.getText().toString().trim();
        String problem = mTvProblem.getText().toString();
        String answer = mEtAnswer.getText().toString();
        String inputPwd = mEtInputPassword.getText().toString().trim();
        String confirmPwd = mEtConfirmPassword.getText().toString().trim();

        switch (view.getId()) {
            case R.id.btn_forget_pwd_phone_verify:
                //清空输入栏
                mTvProblem.setText("");
                mEtAnswer.setText("");
                mEtInputPassword.setText("");
                mEtConfirmPassword.setText("");

                mVerifyDialog = ToastUtil.createLoadingDialog(ForgetPasswordActivty.this, "正在验证手机号码", 0);
                mVerifyDialog.show();
                //验证手机号码
                mForgetPwdPresenter.verifyPhoneNum(phoneNumber);
                break;
            case R.id.rl_forget_pwd_problem_bg:

                if (isReset) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    //设置选择密保问题的dialog
                    builder.setSingleChoiceItems(mProblemArr, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //密保问题显示在textview上
                            mTvProblem.setText(mProblemArr[which]);
                            mTvProblem.setTextColor(Color.parseColor("#000000"));
                            dialog.dismiss();
                        }
                    }).show();
                }

                break;
            case R.id.tv_title_bar_right_text:

                mResetDialog = ToastUtil.createLoadingDialog(ForgetPasswordActivty.this, "正在重置密码", 0);
                mResetDialog.show();
                //完成重置密码
                mForgetPwdPresenter.confirmResetPwd(phoneNumber, problem, answer, inputPwd, confirmPwd);
                break;
        }
    }


    @Override
    public void showToastInfo(String toastStr) {
        ToastUtil.showShort(MyApplication.getContext(), toastStr);
    }

    @Override
    public void showPwdProtectLayout(String[] pwdProblemArr, boolean isReset) {

        if (isReset) {

            //修改标记
            this.isReset = isReset;
            //可以重置
            this.mProblemArr = pwdProblemArr;
        }else {
//            this.mProblemArr = null;
            //修改标记
            this.isReset = false;
        }
    }

    @Override
    public void finishAcitivty() {
        finish();
    }

    @Override
    public void hideVerifyDialog() {
        mVerifyDialog.dismiss();
    }

    @Override
    public void hideConfirmDialog() {
        mResetDialog.dismiss();
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
