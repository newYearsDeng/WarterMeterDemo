package com.northmeter.wartermeterdemo.bean;

import org.litepal.crud.DataSupport;

/**
 * @author zz
 * @time 2016/6/17/0017 10:03
 * @des ${TODO}
 */
public class DownloadTaskBeanRESPONSEXMLBean extends DataSupport {
    private long id;//数据库唯一id
    private String RESCODE;//返回码
    private String RESMSG;//返回码对应的信息
    private String TaskID;     //任务编号
    private String TaskType;   //任务类型
    private String AreaName;   //区域名称
    private String CustomerID;     //用户编号
    private String CustomerName;   //用户名称
    private String CustomerAddress;   //用户地址
    private String MeterCode;//用户水表号
    private String TaskDate;   //任务产生时间
    private String TaskContent;//任务内容
    private String isFinish;//任务是否完成
    private String FeedbackContent;//反馈内容
    private String isUpload;//是否上传
    private String IMAGEADDRESS;//图片地址
    private String isAnalysis;//图片是否拍照
    private String CustomerTel;//手机号码
    private String TaskOperateDate;//任务处理时间
    private String ManagerName;//抄表、管理员的登录名称
    private String NumArea;//图片矩形区域的位置   “ 50,100,480,200 ”
    private Float sDataItemValue;//上个月用水表底
    private String  DataItemValueTime;//上月表底时间
    private Float AvgValue;//平均用水量

    public String getManagerName() {
        return ManagerName;
    }

    public Float getAvgValue() {
        return AvgValue;
    }

    public void setAvgValue(Float avgValue) {
        AvgValue = avgValue;
    }

    public String getDataItemValueTime() {
        return DataItemValueTime;
    }

    public void setDataItemValueTime(String dataItemValueTime) {
        DataItemValueTime = dataItemValueTime;
    }

    public Float getsDataItemValue() {
        return sDataItemValue;
    }

    public void setsDataItemValue(Float sDataItemValue) {
        this.sDataItemValue = sDataItemValue;
    }

    public void setManagerName(String managerName) {
        ManagerName = managerName;
    }

    public String getNumArea() {
        return NumArea;
    }

    public void setNumArea(String numArea) {
        NumArea = numArea;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String upload) {
        isUpload = upload;
    }


    public String getTaskOperateDate() {
        return TaskOperateDate;
    }

    public void setTaskOperateDate(String taskOperateDate) {
        TaskOperateDate = taskOperateDate;
    }

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getMeterCode() {
        return MeterCode;
    }

    public void setMeterCode(String meterCode) {
        MeterCode = meterCode;
    }

    public long getId() {
        return id;
    }

    public String getIMAGEADDRESS() {
        return IMAGEADDRESS;
    }


    public String getFeedbackContent() {
        return FeedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        FeedbackContent = feedbackContent;
    }

    public void setIMAGEADDRESS(String IMAGEADDRESS) {
        this.IMAGEADDRESS = IMAGEADDRESS;
    }

    public String getIsAnalysis() {
        return isAnalysis;
    }

    public void setAnalysis(String analysis) {
        isAnalysis = analysis;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getRESCODE() {
        return RESCODE;
    }

    public void setRESCODE(String RESCODE) {
        this.RESCODE = RESCODE;
    }

    public String getRESMSG() {
        return RESMSG;
    }

    public void setRESMSG(String RESMSG) {
        this.RESMSG = RESMSG;
    }

    public String getTaskID() {
        return TaskID;
    }

    public void setTaskID(String taskID) {
        TaskID = taskID;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getCustomerTel() {
        return CustomerTel;
    }

    public void setCustomerTel(String customerTel) {
        CustomerTel = customerTel;
    }

    public String getTaskDate() {
        return TaskDate;
    }

    public void setTaskDate(String taskDate) {
        TaskDate = taskDate;
    }

    public String getTaskContent() {
        return TaskContent;
    }

    public void setTaskContent(String taskContent) {
        TaskContent = taskContent;
    }

    @Override
    public String toString() {
        return "DownloadTaskBeanRESPONSEXMLBean{" +
                "AreaName='" + AreaName + '\'' +
                ", id=" + id +
                ", RESCODE='" + RESCODE + '\'' +
                ", RESMSG='" + RESMSG + '\'' +
                ", TaskID='" + TaskID + '\'' +
                ", TaskType='" + TaskType + '\'' +
                ", CustomerID='" + CustomerID + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", CustomerAddress='" + CustomerAddress + '\'' +
                ", MeterCode='" + MeterCode + '\'' +
                ", TaskDate='" + TaskDate + '\'' +
                ", TaskContent='" + TaskContent + '\'' +
                ", isFinish=" + isFinish +
                ", FeedbackContent='" + FeedbackContent + '\'' +
                ", IMAGEADDRESS='" + IMAGEADDRESS + '\'' +
                ", isAnalysis=" + isAnalysis +
                '}';
    }
}

