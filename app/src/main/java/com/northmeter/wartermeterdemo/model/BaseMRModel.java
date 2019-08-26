package com.northmeter.wartermeterdemo.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBean;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author zz
 * @time 2016/08/06 9:23
 * @des 维修维护详情的model
 */
public class BaseMRModel implements IBaseMRModel {
    private final String mTaskID;
    private static final String CONNECT_FAIL = "连接异常";
    private static final String DATA_UNDEFINED = "数据异常";
    public static final String RESMSG_SUCCESS = "成功";
    public static final String RESMSG_SUCCESS_INVALID_ID = "无效的任务ID";
    public static final String RESCODE_FAIL = "0";
    public static final String RESCODE_SUCCESS = "1";

    public BaseMRModel(String taskID) {
        this.mTaskID = taskID;
    }


    @Override
    public void getUploadMsg(final UploadMsgCallback uploadMsgCallback) {
        List<DownloadTaskBeanRESPONSEXMLBean> bean = DataSupport.where("taskid = ?", mTaskID).find(DownloadTaskBeanRESPONSEXMLBean.class);
        String isFinish = bean.get(0).getIsFinish();
        String taskType = bean.get(0).getTaskType();
        String taskOperateDate = bean.get(0).getTaskOperateDate();
        String feedbackContent = bean.get(0).getFeedbackContent();
        String imageaddress = bean.get(0).getIMAGEADDRESS();

        if (Constants.NOT_FINISH.equals(isFinish)) {
//            ToastUtil.showShort(MyApplication.getContext(), "请先保存再上传");
            uploadMsgCallback.onFailCallback("请先保存再上传");
            return;
        }

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
//                String toBase64 = ImageUtil.picPathToStr(imagePath);
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
            }
        }

//        System.out.println(taskPic1);

        String[] paramNames = {Constants.PARAM_TASKID, Constants.PARAM_TASKTYPE, Constants.PARAM_TASKRETURN,
                Constants.PARAM_TASKPIC1, Constants.PARAM_TASKPIC2, Constants.PARAM_TASKPIC3, Constants.PARAM_TASKPIC4,
                Constants.PARAM_TASKPIC1NAME, Constants.PARAM_TASKPIC2NAME, Constants.PARAM_TASKPIC3NAME, Constants.PARAM_TASKPIC4NAME,
                Constants.PARAM_TASKOPERATEDATE};

        String[] paramValues = {mTaskID, taskType, feedbackContent,
                taskPic1, taskPic2, taskPic3, taskPic4,
                taskPic1Name, taskPic2Name, taskPic3Name, taskPic4Name,
                taskOperateDate};


        uploadMsgCallback.onDialogProgress0();
        uploadMsgCallback.onDialogProgress(50);

        WebServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_UPLOADTASK, paramNames, paramValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {

                if (CONNECT_FAIL.equals(result)) {
                    uploadMsgCallback.onFailCallback("访问服务器异常，请稍后重试");
                    uploadMsgCallback.onDialogProgress(-1);
                    return result;
                } else if (DATA_UNDEFINED.equals(result)) {
                    uploadMsgCallback.onDialogProgress(-1);
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
                    downloadTaskBeanRESPONSEXMLBean.updateAll("taskid = ?", mTaskID);
                    int deleteCount = DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class, "isupload = ?", Constants.ALREADY_UPLOAD);
                    if (deleteCount > 0) {
                        uploadMsgCallback.onSucceedCallback("上传成功");
                        uploadMsgCallback.onDialogProgress(100);
//                        finish();
                        uploadMsgCallback.onMRFinish();
                    } else {
                        uploadMsgCallback.onFailCallback("上传失败");
                        uploadMsgCallback.onDialogProgress(-1);
                    }
                } else if (RESCODE_SUCCESS.equals(responseCode.getRESCODE()) &&
                        RESMSG_SUCCESS_INVALID_ID.equals(responseCode.getRESMSG())) {
                    uploadMsgCallback.onFailCallback("上传失败");
                    DownloadTaskBeanRESPONSEXMLBean bean = new DownloadTaskBeanRESPONSEXMLBean();
                    bean.setIsUpload(Constants.ALREADY_UPLOAD);
                    bean.updateAll("taskid = ?", mTaskID);

                    uploadMsgCallback.onMRFinish();
                } else {
                    uploadMsgCallback.onFailCallback("上传失败");
                    uploadMsgCallback.onDialogProgress(-1);
                }

                uploadMsgCallback.onDialogProgress(-1);
                return result;
            }
        });
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public interface UploadMsgCallback {
        void onSucceedCallback(String succeedString);

        void onFailCallback(String failString);

        void onMRFinish();

        void onDialogProgress(int progress);

        void onDialogProgress0();
    }
}
