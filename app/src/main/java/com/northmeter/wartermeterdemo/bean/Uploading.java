package com.northmeter.wartermeterdemo.bean;

/**
 * created by lht on 2016/8/26 17:17
 */
 /*     intent.putExtra(Constants.ANALYSISSRC, "TMF");
        intent.putExtra("name", name);
        intent.putExtra("addr", addr);
        intent.putExtra("_id", _id);
        intent.putExtra("water_customerid", water_customerid);
        intent.putExtra("path", imageaddress);
        intent.putExtra("userId", userId);
        intent.putExtra("NumArea",numArea);
        intent.putExtra(Constants.PARAM_TASKOPERATEDATE, taskOperateDate);
        intent.putExtra(Constants.PARAM_TASKID, taskId);*/

import java.io.Serializable;

/**
 * 上传数据的实体类
 */
public class Uploading implements Serializable {
    public static String UPLOADNAME="upLoadName";
    //用户名
    private String customName;
    private String address;
    //如果是数据库中的数据的话，会存在id
    private long id;
    private String waterCustomerId;
    //图片的相对路径
    private String imageAddr;
    private String userId;
    private String numArea;
    private String taskOperateData;
    private String taskId;

    public Uploading() {

    }

    public Uploading(String address, String customName, long id, String imageAddr, String numArea, String taskId, String taskOperateData, String userId, String waterCustomerId) {
        this.address = address;
        this.customName = customName;
        this.id = id;
        this.imageAddr = imageAddr;
        this.numArea = numArea;
        this.taskId = taskId;
        this.taskOperateData = taskOperateData;
        this.userId = userId;
        this.waterCustomerId = waterCustomerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageAddr() {
        return imageAddr;
    }

    public void setImageAddr(String imageAddr) {
        this.imageAddr = imageAddr;
    }

    public String getNumArea() {
        return numArea;
    }

    public void setNumArea(String numArea) {
        this.numArea = numArea;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskOperateData() {
        return taskOperateData;
    }

    public void setTaskOperateData(String taskOperateData) {
        this.taskOperateData = taskOperateData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWaterCustomerId() {
        return waterCustomerId;
    }

    public void setWaterCustomerId(String waterCustomerId) {
        this.waterCustomerId = waterCustomerId;
    }
}
