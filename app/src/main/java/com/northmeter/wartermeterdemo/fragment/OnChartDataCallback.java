package com.northmeter.wartermeterdemo.fragment;

import java.util.ArrayList;

/**
 * @author zz
 * @time 2016/08/27 14:06
 * @des ${TODO}
 */
public interface OnChartDataCallback {
    void setMoneyData(ArrayList<String> money);
    void setUseValueData(ArrayList<String> useValue);
    void setMonthData(ArrayList<String> month);

}
