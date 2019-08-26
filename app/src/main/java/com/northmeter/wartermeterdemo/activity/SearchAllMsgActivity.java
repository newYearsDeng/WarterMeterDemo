package com.northmeter.wartermeterdemo.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.adapter.AllUserMsgAdapter;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.SearchAllUserMsgBean;
import com.northmeter.wartermeterdemo.presenter.SearchAllMsgPresenter;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * @author zz
 * @time 2016/08/29 15:15
 * @des 所有用户月份用水信息
 */
public class SearchAllMsgActivity extends AutoLayoutActivity implements ISearchAllMsgActivity {
    @BindView(R.id.elv_search_all_msg)
    ExpandableListView mElvSearchAllMsg;
    @BindView(R.id.iv_search_all_msg_empty)
    ImageView mIvEmpty;
    @BindView(R.id.ptr_all_msg)
    PtrClassicFrameLayout mPtrAllMsg;
    private String mLoginName;
    private Unbinder unbinder;

    private SearchAllMsgPresenter mSearchAllMsgPresenter;
    private Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_search_all_msg);
        unbinder = ButterKnife.bind(this);

        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("用户每月账单");
        baseTitleBar.hideRightSave();

        mLoginName = getIntent().getStringExtra(HomePageActivity.EXTRA_LOGIN_NAME);

        //初始化presenter
        mSearchAllMsgPresenter = new SearchAllMsgPresenter(this, mLoginName,mPtrAllMsg);
        mSearchAllMsgPresenter.getSearchAllMsgData();

        //设置dialog
        mDialog = ToastUtil.createLoadingDialog(this, "正在读取数据", 0);
        mDialog.show();

    }


    @Override
    public void showEmptyView(String failString) {
        mIvEmpty.setVisibility(View.VISIBLE);
        mElvSearchAllMsg.setVisibility(View.GONE);
        ToastUtil.showShort(MyApplication.getContext(), failString);
    }

    @Override
    public void showExpandableListView(final ArrayList<String> groupName,
                                       final ArrayList<ArrayList<String>> childMonth,
                                       final HashMap<Integer, HashMap<Integer, List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean>>> childItemAllDetail,
                                       final HashMap<Integer, String> userID) {
        mElvSearchAllMsg.setVisibility(View.VISIBLE);
        mIvEmpty.setVisibility(View.GONE);
        //设置adapter
        mElvSearchAllMsg.setAdapter(new AllUserMsgAdapter(groupName, childMonth));

        //二级目录点击事件
        mElvSearchAllMsg.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                LoggerUtil.d("--->"+groupPosition+"---"+childPosition);
                Intent intent = new Intent(MyApplication.getContext(), SearchAllMsgDetailActivity.class);
                //通过键值对得到用户每月的详细信息
                HashMap<Integer, List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean>> itemMonthDetail = childItemAllDetail.get(groupPosition);
                List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean> detailBean = itemMonthDetail.get(childPosition);

                String clearingValueTotal = detailBean.get(0).getClearingValueTotal();
                String basisPrice = detailBean.get(0).getBasisPrice();
                String customerAddress = detailBean.get(0).getCustomerAddress();
                String ecMoneySumTotal = detailBean.get(0).getECMoneySumTotal();
                String eDataItemValue = detailBean.get(0).getEDataItemValue();
                String sDataItemValue = detailBean.get(0).getSDataItemValue();
                intent.putExtra(Constants.INTENT_EXTRA_CLEARINGVALUETOTAL, clearingValueTotal);
                intent.putExtra(Constants.INTENT_EXTRA_BASISPRICE, basisPrice);
                intent.putExtra(Constants.INTENT_EXTRA_CUSTOMERADDRESS, customerAddress);
                intent.putExtra(Constants.INTENT_EXTRA_ECMONEYSUMTOTAL, ecMoneySumTotal);
                intent.putExtra(Constants.INTENT_EXTRA_EDATAITEMVALUE, eDataItemValue);
                intent.putExtra(Constants.INTENT_EXTRA_SDATAITEMVALUE, sDataItemValue);
                intent.putExtra(Constants.INTENT_EXTRA_CUSTOMERNAME, groupName.get(groupPosition));
                intent.putExtra(Constants.INTENT_EXTRA_CUSTOMERID, userID.get(groupPosition));
                intent.putExtra(Constants.INTENT_EXTRA_MONTH_TITLE, childMonth.get(groupPosition).get(childPosition));
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void hideDialog() {
        mDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
