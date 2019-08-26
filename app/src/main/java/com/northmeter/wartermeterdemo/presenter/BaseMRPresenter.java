package com.northmeter.wartermeterdemo.presenter;

import android.text.TextUtils;

import com.northmeter.wartermeterdemo.base.IBaseMaintainRepairActivity;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.model.BaseMRModel;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * @author zz
 * @time 2016/08/06 9:19
 * @des 维修维护详情的presenter
 */
public class BaseMRPresenter implements IBaseMRPresenter {

    private final IBaseMaintainRepairActivity mIBaseMRActivity;
    private final BaseMRModel mBaseMRModel;
    private final String mTaskID;

    public BaseMRPresenter(IBaseMaintainRepairActivity iBaseMaintainRepairActivity, String taskID) {
        this.mIBaseMRActivity = iBaseMaintainRepairActivity;
        mBaseMRModel = new BaseMRModel(taskID);
        this.mTaskID = taskID;
    }

    /**
     * 传递点击拍照的提示信息
     */
    @Override
    public void transmitUploadMsg() {
        mBaseMRModel.getUploadMsg(new BaseMRModel.UploadMsgCallback() {
            @Override
            public void onSucceedCallback(String succeedString) {

                mIBaseMRActivity.showSucceedUploadMsg(succeedString);
            }

            @Override
            public void onFailCallback(String failString) {
                mIBaseMRActivity.showFailUploadMsg(failString);

            }

            @Override
            public void onMRFinish() {
                mIBaseMRActivity.finishActivity();
            }

            @Override
            public void onDialogProgress(int progress) {
                mIBaseMRActivity.showDialogProgress(progress);
            }

            @Override
            public void onDialogProgress0() {
                mIBaseMRActivity.showDialogProgress0();
            }
        });
    }

    @Override
    public void searchDBPicture(String taskID) {
        List<DownloadTaskBeanRESPONSEXMLBean> bean = DataSupport.where("taskid = ?", mTaskID).find(DownloadTaskBeanRESPONSEXMLBean.class);
        String imageaddress = bean.get(0).getIMAGEADDRESS();
        if (!TextUtils.isEmpty(imageaddress)) {
            String[] split = imageaddress.split(",");
            mIBaseMRActivity.showDBPicture(split);
        }
    }
}
