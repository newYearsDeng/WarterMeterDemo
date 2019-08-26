package com.northmeter.wartermeterdemo.presenter;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.northmeter.wartermeterdemo.activity.IDownloadPTR;
import com.northmeter.wartermeterdemo.model.DownloadPTRModel;

/**
 * @author zz
 * @time 2016/08/09 15:27
 * @des 任务列表下拉刷新的presenter
 */
public class DownloadPTRPresenter implements IDownloadPTRPresenter {

    private final String mTasktype;
    private final DownloadPTRModel mDownloadPTRModel;
    private final IDownloadPTR mIDownloadPTR;
    private final String mLoginName;

    public DownloadPTRPresenter(IDownloadPTR iDownloadPTR, String loginName, String taskType, View viewGroupPTR, FragmentActivity activity) {
        this.mIDownloadPTR = iDownloadPTR;
        this.mLoginName = loginName;
        this.mTasktype = taskType;
        mDownloadPTRModel = new DownloadPTRModel(mLoginName,mTasktype, viewGroupPTR,activity);
    }

    @Override
    public void transmitDownloadMsg() {
        mDownloadPTRModel.getDownloadMsg(new DownloadPTRModel.DownloadCallBack() {
            @Override
            public void onDownloadResultCallback(String downloadResultString) {

                mIDownloadPTR.showDownloadMsg(downloadResultString);
            }

            @Override
            public void notifyListView() {
                mIDownloadPTR.notifyListView();
            }
        });
    }
}
