package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/08/26 14:13
 * @des 用水账单Model的接口
 */
public interface ISearchMessageModel {
    void getUseWaterMsg(SearchMessageModel.SearchMsgCallback searchMsgCallback);
}
