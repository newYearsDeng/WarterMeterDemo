package com.northmeter.wartermeterdemo.presenter;

/**
 * @author zz
 * @time 2016/09/09 14:29
 * @des 忘记密码presenter的接口
 */
public interface IForgetPassswordPresenter {
    /**
     * 验证手机号
     * @param phoneNumber
     */
    void verifyPhoneNum(String phoneNumber);
    /**
     * 保存确定重置密码
     * @param phoneNumber
     * @param problem
     * @param answer
     * @param inputPwd
     * @param confirmPwd
     */
    void confirmResetPwd(String phoneNumber, String problem, String answer, String inputPwd, String confirmPwd);
    /**
     * 修改验证标记
     * @param isCheckPhone
     */
    void setCheckFlag(boolean isCheckPhone);
}
