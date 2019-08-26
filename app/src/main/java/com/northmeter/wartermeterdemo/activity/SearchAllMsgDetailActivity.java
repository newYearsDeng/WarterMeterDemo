package com.northmeter.wartermeterdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zz
 * @time 2016/08/30 9:41
 * @des 用户每月的用水详单
 */
public class SearchAllMsgDetailActivity extends AutoLayoutActivity {

    @BindView(R.id.tv_all_msg_detail_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_all_msg_detail_user_id)
    TextView mTvUserId;
    @BindView(R.id.tv_all_msg_detail_address)
    TextView mTvAddress;
    @BindView(R.id.tv_all_msg_detail_last_month)
    TextView mTvLastMonth;
    @BindView(R.id.tv_all_msg_detail_this_month)
    TextView mTvThisMonth;
    @BindView(R.id.tv_all_msg_detail_use_amount)
    TextView mTvUseAmount;
    @BindView(R.id.tv_all_msg_detail_basis_price)
    TextView mTvBasisPrice;
    @BindView(R.id.tv_all_msg_detail_pay)
    TextView mTvPay;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_search_all_msg_detail);
        unbinder = ButterKnife.bind(this);

        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.hideRightSave();

        String clearingValueTotal = getIntent().getStringExtra(Constants.INTENT_EXTRA_CLEARINGVALUETOTAL);
        String basisPrice = getIntent().getStringExtra(Constants.INTENT_EXTRA_BASISPRICE);
        String customerAddress = getIntent().getStringExtra(Constants.INTENT_EXTRA_CUSTOMERADDRESS);
        String ecMoneySumTotal = getIntent().getStringExtra(Constants.INTENT_EXTRA_ECMONEYSUMTOTAL);
        String eDataItemValue = getIntent().getStringExtra(Constants.INTENT_EXTRA_EDATAITEMVALUE);
        String sDataItemValue = getIntent().getStringExtra(Constants.INTENT_EXTRA_SDATAITEMVALUE);
        String username = getIntent().getStringExtra(Constants.INTENT_EXTRA_CUSTOMERNAME);
        String customerid = getIntent().getStringExtra(Constants.INTENT_EXTRA_CUSTOMERID);
        String monthtitle = getIntent().getStringExtra(Constants.INTENT_EXTRA_MONTH_TITLE);

        mTvUseAmount.setText(clearingValueTotal+ " ㎥");
        mTvBasisPrice.setText(basisPrice + " 元/㎥");
        mTvAddress.setText(customerAddress);
        mTvPay.setText(ecMoneySumTotal + " 元");
        mTvThisMonth.setText(eDataItemValue+ " ㎥");
        mTvLastMonth.setText(sDataItemValue+ " ㎥");
        mTvUserName.setText(username);
        mTvUserId.setText(customerid);

        //截取年份，月份
        String year = monthtitle.substring(0, 4);
        String month = monthtitle.substring(5, 7);
        //截取掉单数月份后面的- （如 2015-9-1   2015-11-1）
        if (month.contains("-")) {
            month = month.replace("-", "");
        }
        baseTitleBar.setCenterText(year+"年"+month+"月");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
