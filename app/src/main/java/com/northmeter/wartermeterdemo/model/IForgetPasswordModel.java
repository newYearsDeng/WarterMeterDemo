package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/09/09 14:32
 * @des 忘记密码model的接口
 */
public interface IForgetPasswordModel {
    /**
     * 访问服务器验证
     * @param paramsNames
     * @param paramsValues
     */
    void visitNetVerify(String[] paramsNames, String[] paramsValues,ForgetPasswordModel.ForgetPwdCallback forgetPwdCallback);

    /**
     * 访问服务器确认完成重置密码
     * @param paramsNames
     * @param paramsValues
     */
    void visitNetConfirm(String[] paramsNames, String[] paramsValues,ForgetPasswordModel.ForgetPwdCallback forgetPwdCallback);
}
