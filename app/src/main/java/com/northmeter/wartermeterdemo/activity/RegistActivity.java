package com.northmeter.wartermeterdemo.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.GetCustomerInfoBean;
import com.northmeter.wartermeterdemo.presenter.RegistPresenter;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @author zz
 * @time 2016/5/18 15:28
 * @des 注册界面
 */
public class RegistActivity extends AutoLayoutActivity implements IRegistActivity {
    @BindView(R.id.et_regist_phone_num)
    EditText mEtPhoneNum;
    @BindView(R.id.tv_regist_user_id)
    TextView mTvUserId;
    @BindView(R.id.et_regist_user_name)
    EditText mEtUserName;
    @BindView(R.id.et_regist_user_address)
    EditText mEtUserAddress;
    @BindView(R.id.et_regist_meter_number)
    EditText mEtMeterNumber;
    @BindView(R.id.et_regist_input_password)
    EditText mEtInputPassword;
    @BindView(R.id.et_regist_confirm_password)
    EditText mEtConfirmPassword;
    @BindView(R.id.et_regist_problem_one)
    TextView mTvProblemOne;
    @BindView(R.id.tv_regist_problem_two)
    TextView mTvProblemTwo;
    @BindView(R.id.tv_regist_problem_three)
    TextView mTvProblemThree;
    @BindView(R.id.et_regist_answer_one)
    EditText mEtAnswerOne;
    @BindView(R.id.et_regist_answer_two)
    EditText mEtAnswerTwo;
    @BindView(R.id.et_regist_answer_three)
    EditText mEtAnswerThree;

    private Dialog mProgressDialog;
    private RegistPresenter mRegistPresenter;
    private int mCheckItemOne = 0;//第一个问题被选中的条目
    private int mCheckItemTwo = 0;//第二个问题被选中的条目
    private int mCheckItemThree = 0;//第三个问题被选中的条目
    private Unbinder unbinder;

    private HashMap<Integer, Integer> mCheckProblem = new HashMap<>();//通过键值对保存密保问题
    private String[] mProblemArr = new String[]{"您配偶的生日是？", "您母亲的姓名是？", "您学号(或工号)是？", "您母亲的生日是？",
            "您高中班主任的名字是？", "您父亲的姓名是？", "您小学班主任的名字是？", "您配偶的姓名是？", "您初中班主任的名字是？", "对您影响最大的人是？"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_regist);
        unbinder = ButterKnife.bind(this);
        //初始化presenter
        mRegistPresenter = new RegistPresenter(this);

        initView();
        initData();
    }

    private void initData() {
        mEtPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                isCheck = false;//文本发生变化就改变验证标记
                mRegistPresenter.setIsCheck(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(RegistActivity.this, getWindow().getDecorView());
        baseTitleBar.hideRightSave();
        baseTitleBar.setCenterText("注册");
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

    @OnClick({R.id.btn_regist_confirm, R.id.btn_regist_phone_verify,
            R.id.rl_regist_problem_one_bg, R.id.rl_regist_problem_two_bg, R.id.rl_regist_problem_three_bg})
    public void onClick(View v) {
        final String phoneNumber = mEtPhoneNum.getText().toString().trim();
        String confirmPwd = mEtConfirmPassword.getText().toString().trim();//确认密码
        String inputPwd = mEtInputPassword.getText().toString();//输入密码
        //获取输入框的字符串
        String userAddress = mEtUserAddress.getText().toString().trim();//用户地址
        String meterNumber = mEtMeterNumber.getText().toString().trim();//水表编号
        String userName = mEtUserName.getText().toString().trim();//客户名称
        //获取密保问题和答案
        String problemOne = mTvProblemOne.getText().toString();
        String answerOne = mEtAnswerOne.getText().toString();
        String problemTwo = mTvProblemTwo.getText().toString();
        String answerTwo = mEtAnswerTwo.getText().toString();
        String problemThree = mTvProblemThree.getText().toString();
        String answerThree = mEtAnswerThree.getText().toString();
        switch (v.getId()) {
            case R.id.btn_regist_confirm:
                //确定按钮
                mRegistPresenter.confirmRegist(phoneNumber, confirmPwd, inputPwd, userAddress, meterNumber, userName,
                        problemOne, answerOne,problemTwo,answerTwo,problemThree,answerThree);
                break;
            case R.id.btn_regist_phone_verify:
                //认证按钮
                //清空输入栏
                mTvUserId.setText("");
                mEtUserName.setText("");
                mEtUserAddress.setText("");
                mEtMeterNumber.setText("");
                mEtInputPassword.setText("");
                mEtConfirmPassword.setText("");

                if (phoneNumber.length() == 0) {
                    ToastUtil.showShort(MyApplication.getContext(), "请输入手机号");
                    return;
                }

                //设置进度条
                mProgressDialog = ToastUtil.createLoadingDialog(RegistActivity.this, "正在验证手机号码", 0);
                mProgressDialog.show();

                mRegistPresenter.verifyPhone(phoneNumber);
                break;
            case R.id.rl_regist_problem_one_bg:
                setProblemText(mTvProblemOne);
                break;
            case R.id.rl_regist_problem_two_bg:
                setProblemText(mTvProblemTwo);
                break;
            case R.id.rl_regist_problem_three_bg:
                setProblemText(mTvProblemThree);
                break;
        }
    }


    /**
     * 把选中的问题显示在textview上
     *
     * @param problemText
     */
    public void setProblemText(final TextView problemText) {

        //设置默认选中的问题条目为0
        int defaultCheckItem = 0;
        if (problemText == mTvProblemOne) {
            //如果点击的是第一个问题，就选第一个问题被选择的条目
            defaultCheckItem = mCheckItemOne;
        } else if (problemText == mTvProblemTwo) {
            //如果点击的是第二个问题，就选第二个问题被选择的条目
            defaultCheckItem = mCheckItemTwo;
        } else if (problemText == mTvProblemThree) {
            //如果点击的是第三个问题，就选第三个问题被选择的条目
            defaultCheckItem = mCheckItemThree;
        }


        //设置密保问题多选条目dialog
        new AlertDialog.Builder(this).setSingleChoiceItems(mProblemArr, defaultCheckItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //判断密保问题是否在集合中存在
                if (!mCheckProblem.containsValue(which)) {
                    //问题序号做键，密保问题集合序号做值；给各自选中的密保问题条目赋值
                    if (problemText == mTvProblemOne) {
                        mCheckProblem.put(1, which);
                        mCheckItemOne = mCheckProblem.get(1);
                    } else if (problemText == mTvProblemTwo) {
                        mCheckProblem.put(2, which);
                        mCheckItemTwo = mCheckProblem.get(2);
                    } else if (problemText == mTvProblemThree) {
                        mCheckProblem.put(3, which);
                        mCheckItemThree = mCheckProblem.get(3);
                    }

                    problemText.setText(mProblemArr[which]);
                    problemText.setTextColor(Color.parseColor("#000000"));

                    dialog.dismiss();
                } else {
                    ToastUtil.showShort(MyApplication.getContext(), "该问题已被选择");
                }

            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void toastInfo(String toastStr) {
        ToastUtil.showShort(MyApplication.getContext(), toastStr);
    }

    @Override
    public void dismissDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showUserInfo(List<GetCustomerInfoBean.RESPONSEXMLBean> infos) {
        final GetCustomerInfoBean.RESPONSEXMLBean responseInfo = infos.get(1);//第二次返回的是用户信息
        mTvUserId.setText(responseInfo.getCUSTOMER_ID());
        mEtUserName.setText(responseInfo.getCUSTOMER_NAME());
        mEtUserAddress.setText(responseInfo.getCUSTOMER_ADDR());
        mEtMeterNumber.setText(responseInfo.getCUSTOMER_WATERID());
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showProblemDialog() {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this)
                .setTitleText("温馨提示")
                .setContentText("你没有设置任何密保问题，请至少设置一个密保问题，方便以后找回密码")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
        sweetAlertDialog.show();

    }

}
