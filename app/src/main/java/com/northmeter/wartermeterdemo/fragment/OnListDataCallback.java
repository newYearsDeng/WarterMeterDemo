package com.northmeter.wartermeterdemo.fragment;

/**
 * @author zz
 * @time 2016/08/27 14:06
 * @des ${TODO}
 */
public interface OnListDataCallback {
    void getMoneyData(String money);
    void getUseValueData(String useValue);

    void getMonth(String month);
}
