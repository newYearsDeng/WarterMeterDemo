package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/08/09 15:32
 * @des 任务列表下拉刷新model的接口
 */
public interface IDownloadPTRModel {
    void getDownloadMsg(DownloadPTRModel.DownloadCallBack downloadCallBack);
}
