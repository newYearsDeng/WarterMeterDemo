package com.northmeter.wartermeterdemo.activity;

/**
 * @author zz
 * @time 2016/08/26 14:08
 * @des 用水账单查询的接口
 */
public interface ISearchMessageActivity {
    /**
     * 显示用水账单信息
     * @param result
     */
    void showUseWaterMessage(String result);

    /**
     * 隐藏dialog
     */
    void hideDialog();

    /**
     * 显示空视图
     */
    void showEmptyView();

    /**
     * 弹出错误信息
     * @param failStr
     */
    void toastFailMsg(String failStr);

}
