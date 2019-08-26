package com.northmeter.wartermeterdemo.presenter;

import android.text.TextUtils;

import com.northmeter.wartermeterdemo.activity.IRegistActivity;
import com.northmeter.wartermeterdemo.bean.GetCustomerInfoBean;
import com.northmeter.wartermeterdemo.model.RegistModel;
import com.northmeter.wartermeterdemo.utils.Constants;

import java.util.List;

/**
 * @author zz
 * @time 2016/09/08 9:10
 * @des 注册的presenter
 */
public class RegistPresenter implements IRegistPresenter {
    private final IRegistActivity mIRegistActivty;
    private final RegistModel mRegistModel;
    private boolean mIsCheck = false;
    private boolean mIsActivate = false;//是否激活
    private boolean mIsNotFind = false;//手机号码是否存在，默认存在

    public RegistPresenter(IRegistActivity iRegistActivity) {
        this.mIRegistActivty = iRegistActivity;
        mRegistModel = new RegistModel();
    }

    @Override
    public void verifyPhone(String phoneNumber) {

        mIsCheck = true;//修改验证标记

        String[] paramNames = {Constants.PARAM_CUSTOMER_TELPHONE};
        String[] paramValues = {phoneNumber};

        mRegistModel.networkVerifyPhone(paramNames, paramValues, new RegistModel.RegistCallback() {
            @Override
            public void dismissDialog() {
                mIRegistActivty.dismissDialog();
            }

            @Override
            public void showToast(String toastStr) {
                mIRegistActivty.toastInfo(toastStr);
            }

            @Override
            public void setUserInfo(List<GetCustomerInfoBean.RESPONSEXMLBean> infos) {
                mIRegistActivty.showUserInfo(infos);
            }

            @Override
            public void finishView() {

            }

            @Override
            public void setIsCheck(boolean isCheck) {

            }

            @Override
            public void setFlag(boolean isActivate, boolean isNotFind) {
                mIsActivate = isActivate;
                mIsNotFind = isNotFind;
            }
        });


    }

    @Override
    public void confirmRegist(String phoneNumber, String confirmPwd, String inputPwd,
                              String userAddress, String meterNumber, String userName,
                              String problemOne, String answerOne, String problemTwo,
                              String answerTwo, String problemThree, String answerThree) {


        if (!mIsCheck) {
            mIRegistActivty.toastInfo("请先验证手机号码");
            return;
        } else if (phoneNumber.length() == 0) {
            mIRegistActivty.toastInfo("请输入手机号");
            mIsCheck = false;//修改验证编号
            return;
        } else if (mIsActivate) {
            //手机号码是否注册过
            mIRegistActivty.toastInfo("该手机号已经注册过");
            return;
        } else if (mIsNotFind) {
            mIRegistActivty.toastInfo("该手机号不存在，不能注册");
            return;
        } else if (meterNumber.length() > 16) {
            mIRegistActivty.toastInfo("水表编号长度要小于16位");
            return;
        } else if ("".equals(userAddress) || "".equals(meterNumber) || "".equals(userName)) {
            mIRegistActivty.toastInfo("信息填写不完整，不能完成注册");
            return;
        } else if (!confirmPwd.equals(inputPwd)) {
            //两次密码不一致则返回
            mIRegistActivty.toastInfo("两次密码不一致");
            return;
        } else if ("".equals(inputPwd) || "".equals(confirmPwd)) {
            mIRegistActivty.toastInfo("密码不能为空");
            return;
        } else if(inputPwd.length()>15||confirmPwd.length()>15){
            mIRegistActivty.toastInfo("密码长度不能超过15位");
            return;

        }else if (TextUtils.isEmpty(problemOne) && TextUtils.isEmpty(problemTwo) && TextUtils.isEmpty(problemThree)) {
            mIRegistActivty.showProblemDialog();
            return;
        } else if ((!TextUtils.isEmpty(problemOne) && TextUtils.isEmpty(answerOne)) ||
                (!TextUtils.isEmpty(problemTwo) && TextUtils.isEmpty(answerTwo)) ||
                (!TextUtils.isEmpty(problemThree) && TextUtils.isEmpty(answerThree))) {
            mIRegistActivty.toastInfo("密保答案不能为空");
            return;
        }


        //接口参数的字符串集合
        String[] paramNames = {Constants.PARAM_CUSTOMER_TELPHONE, Constants.PARAM_CUSTOMER_ADDR,
                Constants.PARAM_CUSTOMER_NAME, Constants.PARAM_CUSTOMER_WATERID, Constants.PARAM_CUSTOMER_PWD,
                Constants.PARAM_PWD_PROBLEM_1, Constants.PARAM_PWD_ANSWER_1, Constants.PARAM_PWD_PROBLEM_2, Constants.PARAM_PWD_ANSWER_2,
                Constants.PARAM_PWD_PROBLEM_3, Constants.PARAM_PWD_ANSWER_3};
        String[] paramValues = {phoneNumber, userAddress, userName, meterNumber, confirmPwd,
                problemOne, answerOne, problemTwo, answerTwo, problemThree, answerThree};

        mRegistModel.networkConfirmRegist(paramNames, paramValues, new RegistModel.RegistCallback() {
            @Override
            public void dismissDialog() {

            }

            @Override
            public void showToast(String toastStr) {
                mIRegistActivty.toastInfo(toastStr);
            }

            @Override
            public void setUserInfo(List<GetCustomerInfoBean.RESPONSEXMLBean> infos) {

            }

            @Override
            public void finishView() {
                mIRegistActivty.finishActivity();
            }

            @Override
            public void setIsCheck(boolean isCheck) {
                mIsCheck = isCheck;
            }

            @Override
            public void setFlag(boolean isActivate, boolean isNotFind) {

            }
        });

    }

    @Override
    public void setIsCheck(boolean isCheck) {
        this.mIsCheck = isCheck;
    }
}
