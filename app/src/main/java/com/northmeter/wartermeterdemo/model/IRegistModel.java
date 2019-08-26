package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/09/08 9:32
 * @des 注册model的接口
 */
public interface IRegistModel  {
    /**
     * 访问网络验证手机号
     * @param paramNames
     * @param paramValues
     */
    void networkVerifyPhone(String[] paramNames, String[] paramValues,RegistModel.RegistCallback registCallback);
    /**
     * 访问网络确认注册
     * @param paramNames
     * @param paramValues
     */
    void networkConfirmRegist(String[] paramNames, String[] paramValues,RegistModel.RegistCallback registCallback);
}
