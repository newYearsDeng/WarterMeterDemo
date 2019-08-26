package com.northmeter.wartermeterdemo.activity;

/**
 * @author zz
 * @time 2016/08/05 14:11
 * @des 主页activity的接口
 */
public interface IHomepage {
    void showDownloadSucceedMsg(String succeedString);
    void showDownloadFailMsg(String failString);
    void showDialog(String showDialog);
    void hideDialog();

    void showUnreadNum(String unreadNum);
}
