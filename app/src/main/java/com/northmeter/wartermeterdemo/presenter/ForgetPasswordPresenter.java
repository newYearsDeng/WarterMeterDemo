package com.northmeter.wartermeterdemo.presenter;

import android.text.TextUtils;

import com.northmeter.wartermeterdemo.activity.IForgetPasswordActivity;
import com.northmeter.wartermeterdemo.bean.ForgetPwdBean;
import com.northmeter.wartermeterdemo.model.ForgetPasswordModel;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zz
 * @time 2016/09/09 14:28
 * @des 忘记密码的presenter
 */
public class ForgetPasswordPresenter implements IForgetPassswordPresenter {
    private final IForgetPasswordActivity mIForgetPwdActivity;
    private final ForgetPasswordModel mForgetPwdModel;
    private boolean isReset = false;//是否可以重置密码
    private boolean mIsCheckPhone = true;//是否需要验证手机号

    public ForgetPasswordPresenter(IForgetPasswordActivity iForgetPasswordActivity) {
        this.mIForgetPwdActivity = iForgetPasswordActivity;
        mForgetPwdModel = new ForgetPasswordModel();
    }

    @Override
    public void verifyPhoneNum(String phoneNumber) {

        if (TextUtils.isEmpty(phoneNumber)) {
            mIForgetPwdActivity.showToastInfo("请输入手机号");
            mIForgetPwdActivity.hideVerifyDialog();
            return;
        } else if (phoneNumber.length() < 11) {
            mIForgetPwdActivity.showToastInfo("手机号码长度不正确");
            mIForgetPwdActivity.hideVerifyDialog();
            return;
        }

        String[] paramsNames = {Constants.PARAM_CUSTOMER_TELPHONE};
        String[] paramsValues = {phoneNumber};


        mForgetPwdModel.visitNetVerify(paramsNames, paramsValues, new ForgetPasswordModel.ForgetPwdCallback() {
            @Override
            public void showToast(String toastStr) {
                mIForgetPwdActivity.showToastInfo(toastStr);
                mIForgetPwdActivity.hideVerifyDialog();
            }

            @Override
            public void successData(List<ForgetPwdBean.RESPONSEXMLBean> bean) {

                ArrayList<String> pwdProblemArr = new ArrayList<>();
                //判断密保问题是否为空，不为空就添加到集合里面
                if (!TextUtils.isEmpty(bean.get(1).getPasswordProblem1()) && !("null".equals(bean.get(1).getPasswordProblem1()))) {
                    String passwordProblem1 = bean.get(1).getPasswordProblem1();
                    pwdProblemArr.add(passwordProblem1);
                }
                if (!TextUtils.isEmpty(bean.get(1).getPasswordProblem2()) && !("null".equals(bean.get(1).getPasswordProblem2()))) {
                    String passwordProblem2 = bean.get(1).getPasswordProblem2();
                    pwdProblemArr.add(passwordProblem2);
                }
                if (!TextUtils.isEmpty(bean.get(1).getPasswordProblem3()) && !("null".equals(bean.get(1).getPasswordProblem3()))) {
                    String passwordProblem3 = bean.get(1).getPasswordProblem3();
                    pwdProblemArr.add(passwordProblem3);
                }

                String[] problemArr = new String[pwdProblemArr.size()];
                if (pwdProblemArr.size() != 0) {
                    //有密保问题就显示出来
                    //把集合数据转成数组数据
                    for (int i = 0; i < pwdProblemArr.size(); i++) {
                        problemArr[i] = pwdProblemArr.get(i);
                    }
                    //设置重置标记
                    isReset = true;
                    mIForgetPwdActivity.showToastInfo("该手机号可以重置密码，请选择密保问题");
                } else {
                    //没有密保问题
                    mIForgetPwdActivity.showToastInfo("该手机号不能重置密码");
                    //设置重置标记
                    isReset = false;
                }
                mIForgetPwdActivity.showPwdProtectLayout(problemArr, isReset);
                mIForgetPwdActivity.hideVerifyDialog();
            }

            @Override
            public void setIsCheckPhone(boolean isCheckPhone) {
                mIsCheckPhone = isCheckPhone;
            }

            @Override
            public void finishView() {

            }
        });
    }

    @Override
    public void confirmResetPwd(String phoneNumber, String problem, String answer, String inputPwd, String confirmPwd) {

        if (mIsCheckPhone) {
            mIForgetPwdActivity.showToastInfo("请先验证手机号");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        } else if (!isReset) {
            mIForgetPwdActivity.showToastInfo("该手机号不能重置密码");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        } else if (TextUtils.isEmpty(problem)) {
            mIForgetPwdActivity.showToastInfo("请选择密保问题");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        } else if (TextUtils.isEmpty(answer)) {
            mIForgetPwdActivity.showToastInfo("请输入密保答案");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        } else if (TextUtils.isEmpty(inputPwd) || TextUtils.isEmpty(confirmPwd)) {
            mIForgetPwdActivity.showToastInfo("密码不能为空");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        } else if (!inputPwd.equals(confirmPwd)) {
            mIForgetPwdActivity.showToastInfo("两次密码不一致");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        }else if(inputPwd.length()>15||confirmPwd.length()>15){
            mIForgetPwdActivity.showToastInfo("密码长度不能超过15位");
            mIForgetPwdActivity.hideConfirmDialog();
            return;
        }

        String[] paramsNames = new String[]{Constants.PARAM_CUSTOMER_TELPHONE, Constants.PARAM_PASSWORD, Constants.PARAM_PASSWORDPROBLEM, Constants.PARAM_PASSWORDANSWER};
        String[] paramsValues = new String[]{phoneNumber, confirmPwd, problem, answer};
        mForgetPwdModel.visitNetConfirm(paramsNames, paramsValues, new ForgetPasswordModel.ForgetPwdCallback() {
            @Override
            public void showToast(String toastStr) {
                mIForgetPwdActivity.showToastInfo(toastStr);
                mIForgetPwdActivity.hideConfirmDialog();
            }

            @Override
            public void successData(List<ForgetPwdBean.RESPONSEXMLBean> bean) {
            }

            @Override
            public void setIsCheckPhone(boolean isCheckPhone) {

            }

            @Override
            public void finishView() {
                mIForgetPwdActivity.finishAcitivty();
            }
        });
    }

    @Override
    public void setCheckFlag(boolean isCheckPhone) {
        this.mIsCheckPhone = isCheckPhone;
    }
}
