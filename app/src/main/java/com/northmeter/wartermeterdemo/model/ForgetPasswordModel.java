package com.northmeter.wartermeterdemo.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.bean.ForgetPwdBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import java.util.List;

/**
 * @author zz
 * @time 2016/09/09 14:32
 * @des 忘记密码的model
 */
public class ForgetPasswordModel implements IForgetPasswordModel {
    @Override
    public void visitNetVerify(String[] paramsNames, String[] paramsValues, final ForgetPwdCallback forgetPwdCallback) {

        //访问服务器
        WebServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_GETPASSWORDPROBLEM, paramsNames, paramsValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                LoggerUtil.d(result);

                if ("连接异常".equals(result)) {
                    forgetPwdCallback.showToast("访问服务器异常，请稍后重试");
                    return result;
                }

                //解析json数据
                Gson gson = new Gson();
                ForgetPwdBean forgetPwdBean = gson.fromJson(result, ForgetPwdBean.class);
                List<ForgetPwdBean.RESPONSEXMLBean> bean = forgetPwdBean.getRESPONSEXML();
                String rescode = bean.get(0).getRESCODE();
                String resmsg = bean.get(0).getRESMSG();
                if ("1".equals(rescode) && "成功".equals(resmsg)) {
                    //成功返回
//                    if (TextUtils.isEmpty(bean.get(1).getPasswordProblem1()) &&
//                            TextUtils.isEmpty(bean.get(1).getPasswordProblem2()) &&
//                            TextUtils.isEmpty(bean.get(1).getPasswordProblem3())) {
//                        //找不到密保问题
//                        forgetPwdCallback.successData(bean);
//                    } else {
                        forgetPwdCallback.successData(bean);
//                    }


                } else {
                    //错误返回
                    forgetPwdCallback.showToast("验证失败");
                }

                forgetPwdCallback.setIsCheckPhone(false);
                return result;
            }
        });
    }

    @Override
    public void visitNetConfirm(String[] paramsNames, String[] paramsValues, final ForgetPwdCallback forgetPwdCallback) {

        WebServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_VERIFYPASSWORDPROBLEM, paramsNames, paramsValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                LoggerUtil.d(result);

                if ("连接异常".equals(result)) {
                    forgetPwdCallback.showToast("访问服务器异常，请稍后重试");
                    return result;
                }

                //解析json数据
                Gson gson = new Gson();
                ForgetPwdBean forgetPwdBean = gson.fromJson(result, ForgetPwdBean.class);
                List<ForgetPwdBean.RESPONSEXMLBean> bean = forgetPwdBean.getRESPONSEXML();
                String rescode = bean.get(0).getRESCODE();
                String resmsg = bean.get(0).getRESMSG();
//                if ("1".equals(rescode) && "成功".equals(resmsg)) {
                if ("1".equals(rescode) && "密码重置成功".equals(resmsg)) {
                    //成功返回

                    forgetPwdCallback.showToast("重置密码成功");
                    forgetPwdCallback.finishView();
                } else if ("1".equals(rescode) && "密保答案不正确".equals(resmsg)) {
                    //验证密保失败
                    forgetPwdCallback.showToast("密保答案不正确");
                } else {
                    //错误返回
                    forgetPwdCallback.showToast("重置密码失败");
                }

                return result;
            }
        });
    }

    public interface ForgetPwdCallback {
        void showToast(String toastStr);

        void successData(List<ForgetPwdBean.RESPONSEXMLBean> bean);

        void setIsCheckPhone(boolean isCheckPhone);

        void finishView();
    }
}
