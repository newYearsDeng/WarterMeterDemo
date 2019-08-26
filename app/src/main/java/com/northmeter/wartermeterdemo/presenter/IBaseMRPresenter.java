package com.northmeter.wartermeterdemo.presenter;

/**
 * @author zz
 * @time 2016/08/06 9:21
 * @des 维修维护详情presenter的接口
 */
public interface IBaseMRPresenter {
    void transmitUploadMsg();
    /**
     * 查找数据库保存的图片
     * @param taskID
     */
    void searchDBPicture(String taskID);
}
