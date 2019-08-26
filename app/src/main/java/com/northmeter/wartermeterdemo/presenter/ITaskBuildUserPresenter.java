package com.northmeter.wartermeterdemo.presenter;

/**
 * @author zz
 * @time 2016/09/06 8:55
 * @des 立户任务presenter的接口
 */
public interface ITaskBuildUserPresenter {
    /**
     * 上传任务
     */
    void uploadTask();

    /**
     * 查找数据库保存的图片
     * @param taskID
     */
    void searchDBPicture(String taskID);
}
