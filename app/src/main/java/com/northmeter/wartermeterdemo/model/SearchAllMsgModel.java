package com.northmeter.wartermeterdemo.model;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.SearchAllUserMsgBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author zz
 * @time 2016/09/02 9:19
 * @des 查询所有用户月份用水信息的model
 */
public class SearchAllMsgModel implements ISearchAllMsgModel {
    private final PtrClassicFrameLayout mPTRAllMsg;

    public SearchAllMsgModel(PtrClassicFrameLayout ptrAllMsg) {
        this.mPTRAllMsg = ptrAllMsg;
    }

    @Override
    public void internetSearchData(final String loginName, final SearchAllMsgCallback searchAllMsgCallback) {

        mPTRAllMsg.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                ExpandableListView listview = (ExpandableListView) content.findViewById(R.id.elv_search_all_msg);
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
                //开始刷新
                getData(loginName, searchAllMsgCallback);
            }
        });

        getData(loginName, searchAllMsgCallback);
    }

    private void getData(String loginName, final SearchAllMsgCallback searchAllMsgCallback) {
        WebServiceUtils webServiceUtils = new WebServiceUtils();
        //设置参数
        String[] paramNames = {Constants.PARAM_LOGINNAME};
        String[] paramValues = {loginName};
        webServiceUtils.getWebServiceInfo(Constants.METHOD_NAME_GETALLUSERINFO, paramNames, paramValues, new WebServiceUtils.CallBack() {

            @Override
            public String result(String result) {
                //得到json字符串
                LoggerUtil.d(result);

                if ("连接异常".equals(result)) {
                    searchAllMsgCallback.setEmpty("访问网络异常，请稍后重试");
                    searchAllMsgCallback.dialogDismiss();
                    mPTRAllMsg.refreshComplete();
                    return result;
                } else if ("数据异常".equals(result)) {
                    searchAllMsgCallback.setEmpty("数据返回异常，请重新刷新");
                    searchAllMsgCallback.dialogDismiss();
                    mPTRAllMsg.refreshComplete();
                    return result;
                }

                //解析所有用户用水信息的json
                Gson gson = new Gson();
                SearchAllUserMsgBean bean = gson.fromJson(result, SearchAllUserMsgBean.class);
                String rescode = bean.getRESCODE();
                String resmsg = bean.getRESMSG();
                if ("1".equals(rescode) && "成功".equals(resmsg)) {
                    //返回成功的信息
                    List<SearchAllUserMsgBean.NameBean> name = bean.getName();
                    searchAllMsgCallback.successMsg(name);
                    searchAllMsgCallback.dialogDismiss();
                } else {
                    searchAllMsgCallback.setEmpty("该抄表员暂时没有用户的信息");
                    searchAllMsgCallback.dialogDismiss();
                }

                mPTRAllMsg.refreshComplete();
                return result;
            }
        });
    }


    public interface SearchAllMsgCallback {
        void setEmpty(String failString);

        void successMsg(List<SearchAllUserMsgBean.NameBean> name);

        void dialogDismiss();
    }
}
