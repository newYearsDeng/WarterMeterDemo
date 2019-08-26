package com.northmeter.wartermeterdemo.presenter;

/**
 * @author zz
 * @time 2016/09/08 9:11
 * @des 注册presenter的借口
 */
public interface IRegistPresenter {
    /**
     * 认证手机号
     * @param phoneNumber
     */
    void verifyPhone(String phoneNumber);

    /**
     * 确认注册
     * @param phoneNumber
     * @param confirmPwd
     * @param inputPwd
     * @param userAddress
     * @param meterNumber
     * @param userName
     * @param problemOne
     * @param answerOne
     * @param problemTwo
     * @param answerTwo
     * @param problemThree
     * @param answerThree
     */
    void confirmRegist(String phoneNumber, String confirmPwd, String inputPwd,
                       String userAddress, String meterNumber, String userName,
                       String problemOne, String answerOne, String problemTwo,
                       String answerTwo, String problemThree, String answerThree);

    /**
     * 设置check标记
     * @param isCheck
     */
    void setIsCheck(boolean isCheck);
}
