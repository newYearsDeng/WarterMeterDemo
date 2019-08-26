package com.northmeter.wartermeterdemo.model;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.TaskActivity;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBean;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author zz
 * @time 2016/08/09 15:31
 * @des 任务列表下拉刷新的model
 */
public class DownloadPTRModel implements IDownloadPTRModel {
    private static final String CONNECT_FAIL = "连接异常";
    public static final String RESMSG_SUCCESS = "成功";
    public static final String RESMSG_SUCCESS_INVALID_ID = "无效的任务ID";
    public static final String RESCODE_FAIL = "0";
    public static final String RESCODE_SUCCESS = "1";
    private final View mViewGroupPTR;
    private final String mLoginName;
    private final String mTaskType;
    private final TaskActivity mActivity;

    public DownloadPTRModel(String loginName, String taskType, View viewGroupPTR, FragmentActivity activity) {
        this.mLoginName = loginName;
        this.mTaskType = taskType;
        this.mViewGroupPTR = viewGroupPTR;
        this.mActivity = (TaskActivity)activity;
    }


    /**
     * 从网络获取任务信息
     */
    @Override
    public void getDownloadMsg(final DownloadCallBack downloadCallBack) {
        //找到PTR的id控件
        final PtrClassicFrameLayout ptrClassicFrameLayout =
                (PtrClassicFrameLayout) mViewGroupPTR.findViewById(R.id.pull_to_refresh);
        //设置PTR的handler
        ptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //简单返回是否更新的条件（详细的条件需要自己写）
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);

                ListView listview = (ListView) content.findViewById(R.id.lv_task);
                if (null == listview.getChildAt(0)||
                        (0 == listview.getFirstVisiblePosition() && 0 == listview.getChildAt(0).getTop())) {
                    //如果listview的第一个item为空表示是没有数据，也可以开始刷新
                    //完全见listview的顶部就返回true，表示可以开始刷新
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新开始，开始访问网络


                WebServiceUtils webServiceUtils = new WebServiceUtils();
                String[] paramNames = {Constants.PARAM_LOGIN_NAME};
                String[] paramValues = {mLoginName};
                webServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_DOWNLOADTASK,
                        paramNames, paramValues, new WebServiceUtils.CallBack() {
                            @Override
                            public String result(String result) {
                                LoggerUtil.d(result);

                                if (CONNECT_FAIL.equals(result)) {
//                            LoggerUtil.d("访问服务器异常，请检查网络");
                                    downloadCallBack.onDownloadResultCallback("访问服务器异常，请稍后重试");
                                    ptrClassicFrameLayout.refreshComplete();
                                    mActivity.setTvTasksScale();
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
                                    int i1 = DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "isfinish = ? and tasktype = ?", Constants.NOT_FINISH, mTaskType);
//                            //删除已上传的任务
                                    int i2 = DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "isupload = ? and tasktype = ?", Constants.ALREADY_UPLOAD, mTaskType);
                                    LoggerUtil.d("deleteAll--->" + i1 + "---" + i2);
//
                                    LoggerUtil.d("infos.size()    " + infos.size());
//                            //循环读取服务器的数据，一个个的取出来并保存到本地数据库
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
//                                List<DownloadTaskBeanRESPONSEXMLBean> taskIDList =
//                                        DataSupport.where("taskid = ?", taskID).
//                                                find(DownloadTaskBeanRESPONSEXMLBean.class);
//
//                                LoggerUtil.d("taskid  " + taskID);
//                                if (taskIDList.size() > 0 || TextUtils.isEmpty(taskID)) {
//                                    //如果在本地数据库找到存在的任务ID就不再重复保存到本地数据库
//                                    continue;
//                                }

                                        //任务类型要符合当前页面的任务类型
                                        if (mTaskType.equals(taskType)) {
                                            LoggerUtil.d(mTaskType);
                                            //如果在本地数据库已完成未上传就不重新存储了
                                            List<DownloadTaskBeanRESPONSEXMLBean> isFinishList = DataSupport
                                                    .where("taskid = ? and isfinish = ?", taskID, Constants.ALREADY_FINISH).
                                                            find(DownloadTaskBeanRESPONSEXMLBean.class);
                                            LoggerUtil.d("isFinishList--->" + isFinishList.size());
                                            if (isFinishList.size() > 0 || TextUtils.isEmpty(taskID)) {
                                                continue;
                                            }

                                            //存储到本地数据库
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
                                    }

//                            LoggerUtil.d("下载数据成功");
                                    downloadCallBack.onDownloadResultCallback("下载数据成功");
                                    downloadCallBack.notifyListView();
                                    ptrClassicFrameLayout.refreshComplete();
                                    mActivity.setTvTasksScale();


                                } else if (RESCODE_FAIL.equals(responseCode.getRESCODE())) {
//                            LoggerUtil.d("下载数据失败");
                                    downloadCallBack.onDownloadResultCallback("下载数据失败");
                                    ptrClassicFrameLayout.refreshComplete();
                                    mActivity.setTvTasksScale();
                                }

                                return result;
                            }
                        });


            }
        });

    }

    public interface DownloadCallBack {
        void onDownloadResultCallback(String downloadResultString);

        void notifyListView();
    }
}
