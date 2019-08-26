package com.northmeter.wartermeterdemo.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

import com.northmeter.wartermeterdemo.activity.ITaskBuildUserActivity;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.model.TaskBuildUserModel;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * @author zz
 * @time 2016/09/06 8:55
 * @des 立户任务的presenter
 */
public class TaskBuildUserPresenter implements ITaskBuildUserPresenter{

    private final ITaskBuildUserActivity mBuildUserActivity;
    private final TaskBuildUserModel mBuildUserModel;
    private final String mTaskID;
    private String[] mParamNames;
    private String[] mParamValues;

    public TaskBuildUserPresenter(ITaskBuildUserActivity iTaskBuildUserActivity, String taskID) {
        this.mBuildUserActivity = iTaskBuildUserActivity;
        mBuildUserModel = new TaskBuildUserModel();
        this.mTaskID = taskID;
    }


    @Override
    public void uploadTask() {

        List<DownloadTaskBeanRESPONSEXMLBean> bean = DataSupport.where("taskid = ?", mTaskID).find(DownloadTaskBeanRESPONSEXMLBean.class);
        String isFinish = bean.get(0).getIsFinish();
        String meterCode = bean.get(0).getMeterCode();
        String taskType = bean.get(0).getTaskType();
        String customerTel = bean.get(0).getCustomerTel();
        String taskOperateDate = bean.get(0).getTaskOperateDate();
        String customerAddress = bean.get(0).getCustomerAddress();
        String customerName = bean.get(0).getCustomerName();
        String customerID = bean.get(0).getCustomerID();
        String imageaddress = bean.get(0).getIMAGEADDRESS();

        LoggerUtil.d("任务ID->" + mTaskID + "-用户编号->" + customerID + "-用户姓名->"
                + customerName + "-用户地址->" + customerAddress + "-保存时间->" + taskOperateDate +
                "-用户手机->" + customerTel + "-任务类型->" + taskType + "-水表编号->" + meterCode + "-完成->" + isFinish+
                "-图片地址->"+imageaddress);

        if (Constants.NOT_FINISH.equals(isFinish)) {
            mBuildUserActivity.toastMsg("请先保存再上传");
            return;
        } else if (TextUtils.isEmpty(customerTel) || TextUtils.isEmpty(customerName)
                || TextUtils.isEmpty(customerAddress) || TextUtils.isEmpty(meterCode)) {
            mBuildUserActivity.toastMsg("请把信息填写完整，然后保存再上传");
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


        mParamNames = new String[]{Constants.PARAM_TASKID, Constants.PARAM_TASKTYPE,Constants.PARAM_CUSTOMERNAME,
                Constants.PARAM_TASKPIC1, Constants.PARAM_TASKPIC2, Constants.PARAM_TASKPIC3, Constants.PARAM_TASKPIC4,
                Constants.PARAM_TASKPIC1NAME, Constants.PARAM_TASKPIC2NAME, Constants.PARAM_TASKPIC3NAME, Constants.PARAM_TASKPIC4NAME,
                Constants.PARAM_CUSTOMERADDR, Constants.PARAM_CUSTOMERTEL,
                Constants.PARAM_METERCODE, Constants.PARAM_TASKOPERATEDATE};

        mParamValues = new String[]{mTaskID, taskType, customerName,
                taskPic1, taskPic2, taskPic3, taskPic4,
                taskPic1Name, taskPic2Name, taskPic3Name, taskPic4Name,
                customerAddress, customerTel, meterCode, taskOperateDate};

        mBuildUserActivity.showDialogProgress0();
        mBuildUserActivity.showDialogProgress(50);
        mBuildUserModel.uploadServer(mTaskID,mParamNames, mParamValues, new TaskBuildUserModel.UploadServerCallback() {
            @Override
            public void onFinishView() {
                mBuildUserActivity.finishAvtivity();
            }

            @Override
            public void onToastStr(String toastStr) {
                mBuildUserActivity.toastMsg(toastStr);
            }

            @Override
            public void onDialogPregress(int progress) {
                mBuildUserActivity.showDialogProgress(progress);
            }
        });
    }

    @Override
    public void searchDBPicture(String taskID) {
        List<DownloadTaskBeanRESPONSEXMLBean> bean = DataSupport.where("taskid = ?", mTaskID).find(DownloadTaskBeanRESPONSEXMLBean.class);
        String imageaddress = bean.get(0).getIMAGEADDRESS();
        if (!TextUtils.isEmpty(imageaddress)) {
            String[] split = imageaddress.split(",");
            mBuildUserActivity.showDBPicture(split);
        }
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
