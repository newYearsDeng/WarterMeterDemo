package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/09/02 9:19
 * @des 查询所有用户用水信息model的接口
 */
public interface ISearchAllMsgModel {
    /**
     * 网络获取json数据
     * @param loginName
     */
    void internetSearchData(String loginName,SearchAllMsgModel.SearchAllMsgCallback searchAllMsgCallback);
}
