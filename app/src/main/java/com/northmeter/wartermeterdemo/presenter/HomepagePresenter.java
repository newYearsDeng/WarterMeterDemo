package com.northmeter.wartermeterdemo.presenter;

import com.northmeter.wartermeterdemo.activity.IHomepage;
import com.northmeter.wartermeterdemo.model.HomepageModel;

/**
 * @author zz
 * @time 2016/08/05 14:19
 * @des 主页的presenter
 */
public class HomepagePresenter implements IHomepagePresenter{

    private final IHomepage mIHomepage;
    private final HomepageModel mHomepageModel;
    private final String mLoginName;

    public HomepagePresenter(IHomepage iHomepage, String login_name) {
        this.mIHomepage = iHomepage;
        this.mLoginName = login_name;
        mHomepageModel = new HomepageModel(mLoginName);
    }

    @Override
    public void transmitDownloadMsg() {
        mHomepageModel.getDownloadMsg(new HomepageModel.DownloadMsgCallback() {
            @Override
            public void onSucceedResult(String succeedString) {
                mIHomepage.showDownloadSucceedMsg(succeedString);
            }

            @Override
            public void onFailResult(String failString) {
                mIHomepage.showDownloadFailMsg(failString);
            }

            @Override
            public void showDialog(String showDialog) {
                mIHomepage.showDialog(showDialog);
            }

            @Override
            public void hideDialog() {
                mIHomepage.hideDialog();
            }

            @Override
            public void showUnreadNum(String unreadNum) {
                mIHomepage.showUnreadNum(unreadNum);
            }

        });
    }
}
