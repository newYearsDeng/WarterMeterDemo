package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/08/06 9:25
 * @des 维修维护详情model的接口
 */
public interface IBaseMRModel {
    void getUploadMsg(final BaseMRModel.UploadMsgCallback uploadMsgCallback);
}
