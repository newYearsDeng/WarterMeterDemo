package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/08/05 14:26
 * @des 主页model的接口
 */
public interface IHomepageModel {
    void getDownloadMsg(HomepageModel.DownloadMsgCallback downloadMsgCallback);
}
