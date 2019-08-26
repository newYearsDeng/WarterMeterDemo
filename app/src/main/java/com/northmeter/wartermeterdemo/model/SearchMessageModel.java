package com.northmeter.wartermeterdemo.model;

import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.adapter.SearchMessageAdapter;
import com.northmeter.wartermeterdemo.bean.GetCustomerInfoBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author zz
 * @time 2016/08/26 14:13
 * @des 查询用水账单的model
 */
public class SearchMessageModel implements ISearchMessageModel {
    public static final String RESCODE_SUCCESS = "1";
    public static final String RESCODE_FAIL = "0";
    public static final String RESMSG_NO_FIND_RECORD = "暂时没有记录";
    public static final String RESMSG_SUCCESS = "成功";
    private static final String CONNECT_FAIL = "连接异常";
    private final PtrClassicFrameLayout mPTR;

    public SearchMessageModel(PtrClassicFrameLayout ptrSearchMessage) {
        this.mPTR = ptrSearchMessage;
    }

    @Override
    public void getUseWaterMsg(final SearchMsgCallback searchMsgCallback) {

        mPTR.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                ListView listview = (ListView) content.findViewById(R.id.listview_search_message);
//                if (null == listview.getChildAt(0)||
//                        (0 == listview.getFirstVisiblePosition() && 0 == listview.getChildAt(0).getTop())) {
//                    //如果listview的第一个item为空表示是没有数据，也可以开始刷新
//                    //完全见listview的顶部就返回true，表示可以开始刷新
//                    return true;
//                } else {
//                    return false;
//                }
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                networkData(searchMsgCallback);
            }
        });
        networkData(searchMsgCallback);
    }

    private void networkData(final SearchMsgCallback searchMsgCallback) {
        String customer_id = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, "");
        String water_customer_id = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_WATER_CUSTOMERID, "");

        String[] paramNames = {Constants.PARAM_CUSTOMER_ID, Constants.PARAM_WATER_CUSTOMERID};
        String[] paramValues = {customer_id, water_customer_id};
        WebServiceUtils.getWebServiceInfo(Constants.METHOD_GET_CUSTOMER_USE_RECORD,
                paramNames, paramValues, new WebServiceUtils.CallBack() {
                    @Override
                    public String result(String result) {
                        LoggerUtil.json(result);

                        if (CONNECT_FAIL.equals(result)) {
//                            ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
                            searchMsgCallback.failMsg("访问服务器异常，请稍后重试");
//                            mNotFindRecord.setVisibility(View.VISIBLE);
                            searchMsgCallback.showEmptyView();
//                            mProgressDialog.dismiss();
                            searchMsgCallback.dismissDialog();
                            mPTR.refreshComplete();
                            return result;
                        }

                        Gson gson = new Gson();
                        //解析json
                        GetCustomerInfoBean getCustomerInfoBean = gson.fromJson(result, GetCustomerInfoBean.class);
                        List<GetCustomerInfoBean.RESPONSEXMLBean> beanList = getCustomerInfoBean.getRESPONSEXML();
                        //先获得返回码和返回码的信息
                        GetCustomerInfoBean.RESPONSEXMLBean responseCode = beanList.get(0);
                        //判断条件
                        if (RESCODE_FAIL.equals(responseCode.getRESCODE())) {
//                            ToastUtil.showShort(MyApplication.getContext(), "数据异常，无法获取数据");
                            searchMsgCallback.failMsg("数据异常，无法获取数据");
//                            mNotFindRecord.setVisibility(View.VISIBLE);
                            searchMsgCallback.showEmptyView();
//                            mProgressDialog.dismiss();
                            searchMsgCallback.dismissDialog();

                            mPTR.refreshComplete();
                            return result;
                        } else if (RESCODE_SUCCESS.equals(responseCode.getRESCODE()) && RESMSG_NO_FIND_RECORD.equals(responseCode.getRESMSG())) {
                            //未找到 暂时没有记录
//                            mNotFindRecord.setVisibility(View.VISIBLE);
                            searchMsgCallback.showEmptyView();
//                            mProgressDialog.dismiss();
                            searchMsgCallback.dismissDialog();
                            mPTR.refreshComplete();
                            return result;
                        }
                        //返回成功的数据信息
                        searchMsgCallback.successMsg(result);
//                        mListView.setVisibility(View.VISIBLE);
                        //遍历json字符串
//                        getJson(result);
//                        mListView.setAdapter(new SearchMessageAdapter(mMonthData));

//                        mProgressDialog.dismiss();//取消进度条
                        searchMsgCallback.dismissDialog();

                        mPTR.refreshComplete();
                        return result;
                    }
                });
    }


    public interface SearchMsgCallback {
        void dismissDialog();

        void showEmptyView();

        void successMsg(String result);

        void failMsg(String failStr);
    }
}
