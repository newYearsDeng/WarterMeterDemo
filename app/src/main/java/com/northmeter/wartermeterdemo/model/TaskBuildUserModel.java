package com.northmeter.wartermeterdemo.model;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBean;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author zz
 * @time 2016/09/06 8:59
 * @des 立户任务的model
 */
public class TaskBuildUserModel implements ITaskBuildUserModel {

    private static final String CONNECT_FAIL = "连接异常";
    public static final String RESMSG_SUCCESS = "成功";
    public static final String RESMSG_SUCCESS_INVALID_ID = "无效的任务ID";
    public static final String RESCODE_FAIL = "0";
    public static final String RESCODE_SUCCESS = "1";


    @Override
    public void uploadServer(final String taskID, String[] paramNames, String[] paramValues, final UploadServerCallback uploadServerCallback) {
        WebServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_UPLOADTASK, paramNames, paramValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {

                if (CONNECT_FAIL.equals(result)) {
//                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
//                    mBtnUpload.setProgress(-1);
                    uploadServerCallback.onToastStr("访问服务器异常，请稍后重试");
                    uploadServerCallback.onDialogPregress(-1);
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
                    if (deleteCount > 0) {
                        uploadServerCallback.onToastStr("上传成功");
//                        finish();
                        uploadServerCallback.onFinishView();
                    } else {
                        uploadServerCallback.onToastStr("上传失败");
                        uploadServerCallback.onDialogPregress(-1);
                    }
                } else if (RESCODE_SUCCESS.equals(responseCode.getRESCODE()) &&
                        RESMSG_SUCCESS_INVALID_ID.equals(responseCode.getRESMSG())) {
                    uploadServerCallback.onToastStr("上传失败");
                    DownloadTaskBeanRESPONSEXMLBean bean = new DownloadTaskBeanRESPONSEXMLBean();
                    bean.setIsUpload(Constants.ALREADY_UPLOAD);
                    bean.updateAll("taskid = ?", taskID);
//                    finish();
                    uploadServerCallback.onFinishView();
                } else {
                    uploadServerCallback.onToastStr("上传失败");
                    uploadServerCallback.onDialogPregress(-1);
                }

                return null;
            }
        });
    }

    public interface UploadServerCallback {
        void onFinishView();

        void onToastStr(String toastStr);

        void onDialogPregress(int progress);
    }
}
