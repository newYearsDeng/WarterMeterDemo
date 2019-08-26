package com.northmeter.wartermeterdemo.model;

/**
 * @author zz
 * @time 2016/09/06 9:00
 * @des 立户任务model的接口
 */
public interface ITaskBuildUserModel {
    /**
     * 上传到服务器
     * @param taskID
     * @param paramNames
     * @param paramValues
     */
    void uploadServer(String taskID, String[] paramNames, String[] paramValues, TaskBuildUserModel.UploadServerCallback uploadServerCallback);
}
