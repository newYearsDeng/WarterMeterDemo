package com.northmeter.wartermeterdemo.activity;

/**
 * @author zz
 * @time 2016/08/09 15:25
 * @des 任务列表下拉更新的接口
 */
public interface IDownloadPTR {
    void showDownloadMsg(String downloadResultString);
    void notifyListView();
}
