package com.northmeter.wartermeterdemo.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBean;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author zz
 * @time 2016/08/05 14:25
 * @des 主页的Model
 */
public class HomepageModel implements IHomepageModel {

    private final String mLoginName;
    private static final String CONNECT_FAIL = "连接异常";
    public static final String RESMSG_SUCCESS = "成功";
    public static final String RESMSG_SUCCESS_INVALID_ID = "无效的任务ID";
    public static final String RESCODE_FAIL = "0";
    public static final String RESCODE_SUCCESS = "1";

    public HomepageModel(String loginName) {
        this.mLoginName = loginName;
    }

    @Override
    public void getDownloadMsg(final DownloadMsgCallback downloadMsgCallback) {


        //设置进度条
        downloadMsgCallback.showDialog("正在下载数据");

        WebServiceUtils webServiceUtils = new WebServiceUtils();
        String[] paramNames = {Constants.PARAM_LOGIN_NAME};
        String[] paramValues = {mLoginName};
        webServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_DOWNLOADTASK, paramNames, paramValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                LoggerUtil.d(result);

                if (CONNECT_FAIL.equals(result)) {
                    downloadMsgCallback.onFailResult("访问服务器异常，请稍后重试");
                    downloadMsgCallback.hideDialog();
                    return result;
                }

                //解析json
                Gson gson = new Gson();
                DownloadTaskBean downloadTaskBean = gson.fromJson(result.toString(),
                        DownloadTaskBean.class);
                List<DownloadTaskBeanRESPONSEXMLBean> infos =
                        downloadTaskBean.getRESPONSEXML();
                final DownloadTaskBeanRESPONSEXMLBean responseCode =
                        infos.get(0);//第一次返回的状态码

                if (RESCODE_SUCCESS.equals(responseCode.getRESCODE()) &&
                        RESMSG_SUCCESS.equals(responseCode.getRESMSG())) {

                    //存储前先把本地数据库未完成的删除
                    int i1 = DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "isfinish = ?", Constants.NOT_FINISH);
                    //删除已上传的任务
                    int i2 = DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "isupload = ?", Constants.ALREADY_UPLOAD);
                    LoggerUtil.d("deleteAll--->" + i1 + "---" + i2);

                    //循环读取服务器的数据，一个个的取出来并保存到本地数据库
                    for (int taskNum = 1; taskNum < infos.size(); taskNum++) {
                        //从1开始是因为0是状态码，不是有效的信息

                        String taskID = infos.get(taskNum).getTaskID();//任务编号
                        String taskType = infos.get(taskNum).getTaskType();//任务类型
                        String areaName = infos.get(taskNum).getAreaName();//区域名称
                        String userid = infos.get(taskNum).getCustomerID();//用户编号
                        String username = infos.get(taskNum).getCustomerName();//用户名称
                        String useraddr = infos.get(taskNum).getCustomerAddress();//用户地址
                        String taskDate = infos.get(taskNum).getTaskDate();//任务产生时间
                        String taskContent = infos.get(taskNum).getTaskContent();//任务内容
                        String meterCode = infos.get(taskNum).getMeterCode();//水表号
                        String phoneNumber = infos.get(taskNum).getCustomerTel();//手机号码


                        //如果在本地数据库已完成未上传就不重新存储了
                        List<DownloadTaskBeanRESPONSEXMLBean> isFinishList = DataSupport.where("taskid = ? and isfinish = ?", taskID, Constants.ALREADY_FINISH).
                                find(DownloadTaskBeanRESPONSEXMLBean.class);
//                        LoggerUtil.d("isFinishList--->" + isFinishList.size());
                        if (isFinishList.size() > 0 || TextUtils.isEmpty(taskID)) {
                            continue;
                        }

                        //存储到本地数据库.
                        DownloadTaskBeanRESPONSEXMLBean downloadTaskBeanRESPONSEXMLBean =
                                new DownloadTaskBeanRESPONSEXMLBean();
                        downloadTaskBeanRESPONSEXMLBean.setTaskID(taskID);
                        downloadTaskBeanRESPONSEXMLBean.setTaskType(taskType);
                        downloadTaskBeanRESPONSEXMLBean.setAreaName(areaName);
                        downloadTaskBeanRESPONSEXMLBean.setCustomerID(userid);
                        downloadTaskBeanRESPONSEXMLBean.setCustomerName(username);
                        downloadTaskBeanRESPONSEXMLBean.setCustomerAddress(useraddr);
                        downloadTaskBeanRESPONSEXMLBean.setTaskDate(taskDate);
                        downloadTaskBeanRESPONSEXMLBean.setMeterCode(meterCode);
                        downloadTaskBeanRESPONSEXMLBean.setTaskContent(taskContent);
                        downloadTaskBeanRESPONSEXMLBean.setIsFinish(Constants.NOT_FINISH);
                        downloadTaskBeanRESPONSEXMLBean.setAnalysis(Constants.NOT_FINISH);
                        downloadTaskBeanRESPONSEXMLBean.setIsUpload(Constants.NOT_UPLOAD);
                        downloadTaskBeanRESPONSEXMLBean.setCustomerTel(phoneNumber);
                        downloadTaskBeanRESPONSEXMLBean.setManagerName(mLoginName);

                        boolean isSave = downloadTaskBeanRESPONSEXMLBean.save();//保存数据
                    }
                    //更改未完成任务数量
                    List<DownloadTaskBeanRESPONSEXMLBean> data = DataSupport.where("taskid > ? and  isfinish =? and managername = ?", "0", Constants.NOT_FINISH, mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);
                    if (data.size() > 0) {
                        downloadMsgCallback.showUnreadNum(data.size() + "");
                    }

                    downloadMsgCallback.onSucceedResult("下载数据成功");

                } else if (RESCODE_FAIL.equals(responseCode.getRESCODE())&&
                        "操作员账号错误".equals(responseCode.getRESMSG())) {

                    downloadMsgCallback.onFailResult("操作员账号错误");
                }else {

                    downloadMsgCallback.onFailResult("下载数据失败");
                }

                downloadMsgCallback.hideDialog();
                return result;
            }
        });

    }

    public interface DownloadMsgCallback {
        void onSucceedResult(String succeedString);

        void onFailResult(String failString);

        void showDialog(String showDialog);

        void hideDialog();

        void showUnreadNum(String unreadNum);

    }
}
