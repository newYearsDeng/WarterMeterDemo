package com.northmeter.wartermeterdemo.activity;

import com.northmeter.wartermeterdemo.bean.GetCustomerInfoBean;

import java.util.List;

/**
 * @author zz
 * @time 2016/09/08 9:06
 * @des 注册view的接口
 */
public interface IRegistActivity  {
    /**
     * 显示弹出提示信息
     */
    void toastInfo(String toastStr);
    /**
     * 隐藏dialog
     */
    void dismissDialog();
    /**
     * 显示用户信息
     * @param infos
     */
    void showUserInfo(List<GetCustomerInfoBean.RESPONSEXMLBean> infos);
    /**
     * finish activity
     */
    void finishActivity();
    /**
     * 弹出警告框
     */
    void showProblemDialog();
}
