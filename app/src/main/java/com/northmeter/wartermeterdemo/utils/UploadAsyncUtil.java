package com.northmeter.wartermeterdemo.utils;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.HomePageActivity;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBean;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author zz
 * @time 2016/7/5/0005 9:20
 * @des 批量上传的asyncTask
 */
public class UploadAsyncUtil extends AsyncTask<String, String, Integer> {
    private static final String CONNECT_FAIL = "连接异常";
    public static final String RESMSG_SUCCESS = "成功";
    public static final String RESMSG_SUCCESS_INVALID_ID = "无效的任务ID";
    public static final String RESCODE_FAIL = "0";
    public static final String RESCODE_SUCCESS = "1";
    public static final String UPLOAD_FINISH = "上传完成";
    public static final String UPLOAD_FAIL = "上传异常";
    private final List<DownloadTaskBeanRESPONSEXMLBean> uploadData;
    private final AppCompatActivity activity;
    private ProgressDialog mDialog;
    private int mProgress;

    public UploadAsyncUtil(List<DownloadTaskBeanRESPONSEXMLBean> uploadData, AppCompatActivity activity) {
        this.uploadData = uploadData;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //设置默认起始的进度值
        mProgress = 1;
        //设置进度条
        mDialog = new ProgressDialog(activity);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMessage("正在上传数据，请等待");
        mDialog.setCancelable(false);
        mDialog.setMax(uploadData.size());
        mDialog.show();
    }

    @Override
    protected Integer doInBackground(String... params) {

        //for (DownloadTaskBeanRESPONSEXMLBean bean : uploadData) {
        for (int tempData = 0; tempData < uploadData.size(); tempData++) {
            final String taskID = uploadData.get(tempData).getTaskID();
            String taskType = uploadData.get(tempData).getTaskType();
            String feedbackContent = uploadData.get(tempData).getFeedbackContent();
            String imageaddress = uploadData.get(tempData).getIMAGEADDRESS();
            String customerName = uploadData.get(tempData).getCustomerName();
            String customerAddress = uploadData.get(tempData).getCustomerAddress();
            String customerTel = uploadData.get(tempData).getCustomerTel();
            String meterCode = uploadData.get(tempData).getMeterCode();
            String taskOperateDate = uploadData.get(tempData).getTaskOperateDate();
            String isFinish = uploadData.get(tempData).getIsFinish();
            String isUpload = uploadData.get(tempData).getIsUpload();

//            if (Constants.TASK_TYPE_MERTER.equals(taskType) || Constants.NOT_FINISH.equals(isFinish)) {
//                //如果是抄表任务或者未完成的任务就跳过，不上传
//                continue;
//            }
            LoggerUtil.d("任务ID->" + taskID + "-任务反馈->" + feedbackContent
                    + "-任务地址->" + customerAddress + "-处理时间->" + taskOperateDate
                    + "-任务类型->" + taskType + "-任务图片->" + imageaddress
                    + "-用户名称->" + customerName + "-用户电话->" + customerTel
                    + "-水表编号->" + meterCode + "-是否完成->" + isFinish);

            String taskPic1 = "";
            String taskPic2 = "";
            String taskPic3 = "";
            String taskPic4 = "";
            String taskPic1Name = "";
            String taskPic2Name = "";
            String taskPic3Name = "";
            String taskPic4Name = "";
            if (!TextUtils.isEmpty(imageaddress)) {
                String[] split = imageaddress.split(",");
                for (int temp = 0; temp < split.length; temp++) {
                    String imagePath = split[temp];
                    String toBase64 = imagePathToBase64(imagePath);
//                            LoggerUtil.d("imagePath    " + imagePath);

                    if (temp == 0) {
                        taskPic1 = toBase64;
                        taskPic1Name = imagePath.substring(Constants.SAVEPIC.length());
                    } else if (temp == 1) {
                        taskPic2 = toBase64;
                        taskPic2Name = imagePath.substring(Constants.SAVEPIC.length());
                    } else if (temp == 2) {
                        taskPic3 = toBase64;
                        taskPic3Name = imagePath.substring(Constants.SAVEPIC.length());
                    } else if (temp == 3) {
                        taskPic4 = toBase64;
                        taskPic4Name = imagePath.substring(Constants.SAVEPIC.length());
                    }

//                            LoggerUtil.d("base64----" + toBase64);
//                            System.out.println("-----------------------------------------------------------------------------------");
//                            System.out.println(toBase64);
                }
            }

//                    LoggerUtil.d("taskPic1--->" + taskPic1 + "---taskPic2--->" + taskPic2
//                            + "---taskPic3--->" + taskPic3 + "---taskPic4--->" + taskPic4);

//                    LoggerUtil.d("path->" + taskPic1Name + "->" +
//                            taskPic2Name + "->" + taskPic3Name + "->" + taskPic4Name);

            String[] paramNames = {Constants.PARAM_TASKID, Constants.PARAM_TASKTYPE, Constants.PARAM_TASKRETURN,
                    Constants.PARAM_TASKPIC1, Constants.PARAM_TASKPIC2, Constants.PARAM_TASKPIC3, Constants.PARAM_TASKPIC4,
                    Constants.PARAM_TASKPIC1NAME, Constants.PARAM_TASKPIC2NAME, Constants.PARAM_TASKPIC3NAME, Constants.PARAM_TASKPIC4NAME,
                    Constants.PARAM_CUSTOMERNAME, Constants.PARAM_CUSTOMERADDR, Constants.PARAM_CUSTOMERTEL,
                    Constants.PARAM_METERCODE, Constants.PARAM_TASKOPERATEDATE};

            String[] paramValues = {taskID, taskType, feedbackContent, taskPic1, taskPic2, taskPic3, taskPic4,
                    taskPic1Name, taskPic2Name, taskPic3Name, taskPic4Name, customerName,
                    customerAddress, customerTel, meterCode, taskOperateDate};

            WebServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_UPLOADTASK, paramNames, paramValues, new WebServiceUtils.CallBack() {
                @Override
                public String result(String result) {

                    if (CONNECT_FAIL.equals(result)) {
//                        ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
//                        mDialog.dismiss();
                        publishProgress(CONNECT_FAIL);
                        return null;
                    }

                    LoggerUtil.d("上传是否成功     " + result);

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

                        DownloadTaskBeanRESPONSEXMLBean downloadTaskBeanRESPONSEXMLBean =
                                new DownloadTaskBeanRESPONSEXMLBean();
                        downloadTaskBeanRESPONSEXMLBean.setIsUpload(Constants.ALREADY_UPLOAD);
                        downloadTaskBeanRESPONSEXMLBean.updateAll("taskid = ?", taskID);
                        int deleteCount = DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "isupload = ?", Constants.ALREADY_UPLOAD);

//                        publishProgress(UPLOAD_FINISH);
                    } else if (RESCODE_SUCCESS.equals(responseCode.getRESCODE()) &&
                            RESMSG_SUCCESS_INVALID_ID.equals(responseCode.getRESMSG())) {
//                        ToastUtil.showShort(MyApplication.getContext(), "无效的任务ID");
//                        DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "taskid = ?", taskID);

                        DownloadTaskBeanRESPONSEXMLBean bean = new DownloadTaskBeanRESPONSEXMLBean();
                        bean.setIsUpload(Constants.ALREADY_UPLOAD);
                        bean.updateAll("taskid = ?", taskID);

//                        publishProgress(UPLOAD_FINISH);
                    }
//                    else {
//                        publishProgress(UPLOAD_FAIL);
//                    }

                    publishProgress(UPLOAD_FINISH);
                    return null;
                }
            });
//
        }

//        LoggerUtil.d("size--->" + uploadData.size());
//        LoggerUtil.d("doInBackground  " + params[1]);
        return uploadData.size();
    }

    /**
     * 图片转base64
     *
     * @param imagePath
     * @return
     */
    private String imagePathToBase64(String imagePath) {

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    @Override
    protected void onPostExecute(Integer s) {
        super.onPostExecute(s);
        LoggerUtil.d("onPostExecute  " + s); // 最后执行
        if (0 == s) {
            ToastUtil.showShort(MyApplication.getContext(), "没有需要上传的数据");
            mDialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
//        LoggerUtil.d("onProgressUpdate0  " + values[0]);//进度条

        if (UPLOAD_FINISH.equals(values[0])) {

            mDialog.setProgress(mProgress);
//            mProgress += 1;
            LoggerUtil.d("FINISH---->   " + mProgress);
            if (mProgress >= uploadData.size()) {
                mDialog.dismiss();
                ToastUtil.showShort(MyApplication.getContext(), "上传完成");
            }
        } else if (CONNECT_FAIL.equals(values[0])) {
            mDialog.setProgress(mProgress);
//            mProgress += 1;
            LoggerUtil.d("CONNECT_FAIL---->   " + mProgress);
            if (mProgress >= uploadData.size()) {
                mDialog.dismiss();
                ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请稍后重试");
            }
        }
        mProgress += 1;

//        else if (UPLOAD_FAIL.equals(values[0])) {

//            mDialog.setProgress(mProgress);
//            mProgress += 1;
//            LoggerUtil.d("FAIL---->   " + mProgress);
//            if (mProgress > uploadData.size()) {
//                mDialog.dismiss();
////                ToastUtil.showShort(MyApplication.getContext(), "上传完成");
//            }
//        }

    }
}
