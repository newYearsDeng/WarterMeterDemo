package com.northmeter.wartermeterdemo.activity;

import java.util.ArrayList;

/**
 * @author zz
 * @time 2016/09/09 14:26
 * @des 忘记密码view的接口
 */
public interface IForgetPasswordActivity {
    /**
     * toast信息
     */
    void showToastInfo(String toastStr);
    /**
     * 显示密保布局
     * @param pwdProblemArr
     * @param isReset
     */
    void showPwdProtectLayout(String[] pwdProblemArr, boolean isReset);
    /**
     * 退出activity
     */
    void finishAcitivty();
    /**
     * 隐藏验证dialog
     */
    void hideVerifyDialog();
    /**
     * 隐藏确认dialog
     */
    void hideConfirmDialog();
}
