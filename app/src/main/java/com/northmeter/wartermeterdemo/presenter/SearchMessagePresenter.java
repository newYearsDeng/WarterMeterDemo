package com.northmeter.wartermeterdemo.presenter;

import com.northmeter.wartermeterdemo.activity.ISearchMessageActivity;
import com.northmeter.wartermeterdemo.activity.SearchMessageActivity;
import com.northmeter.wartermeterdemo.model.SearchMessageModel;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * @author zz
 * @time 2016/08/26 14:10
 * @des 用水账单查询的presenter
 */
public class SearchMessagePresenter implements ISearchMessagePresenter{
    private final ISearchMessageActivity mSearchMessageActivity;
    private final SearchMessageModel mSearchMessageModel;

    public SearchMessagePresenter(ISearchMessageActivity iSearchMessageActivity, PtrClassicFrameLayout ptrSearchMessage) {
        this.mSearchMessageActivity = iSearchMessageActivity;
        mSearchMessageModel = new SearchMessageModel(ptrSearchMessage);
    }

    @Override
    public void transmitUseWaterMsg() {
        mSearchMessageModel.getUseWaterMsg(new SearchMessageModel.SearchMsgCallback() {
            @Override
            public void dismissDialog() {
                mSearchMessageActivity.hideDialog();
            }

            @Override
            public void showEmptyView() {
                mSearchMessageActivity.showEmptyView();
            }

            @Override
            public void successMsg(String result) {
                mSearchMessageActivity.showUseWaterMessage(result);
            }

            @Override
            public void failMsg(String failStr) {
                mSearchMessageActivity.toastFailMsg(failStr);
            }
        });
    }
}
