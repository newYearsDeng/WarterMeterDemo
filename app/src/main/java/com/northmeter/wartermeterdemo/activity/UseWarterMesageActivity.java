package com.northmeter.wartermeterdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.fragment.SearchMsgCListFragment;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author zz
 * @time 2016/5/19 10:31
 * @des 用水信息详情
 */
public class UseWarterMesageActivity extends AutoLayoutActivity {

    @BindView(R.id.tv_user_warter_message_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_use_warter_message_user_id)
    TextView mTvUserId;
    @BindView(R.id.tv_use_warter_message_address)
    TextView mTvAddress;
    @BindView(R.id.tv_use_warter_message_last_month_warter_id)
    TextView mTvLastMonthWarterId;
    @BindView(R.id.tv_use_warter_message_this_month_warter_id)
    TextView mTvThisMonthWarterId;
    @BindView(R.id.tv_use_warter_message_use_amount)
    TextView mTvUseAmount;
    @BindView(R.id.tv_use_warter_message_basis_price)
    TextView mTvBasisPrice;
    @BindView(R.id.tv_use_warter_message_pay)
    TextView mTvPay;
    @BindView(R.id.btn_use_warter_message_pay)
    CircularProgressButton mBtnPay;

    private String mTitlename;
    private String mStartValue;
    private String mEndValue;
    private String mUseValue;
    private String mUseMoney;
    private String mBasisPrice;
    private int mFirstposition;
    private Unbinder unbinder;

    private static final String URL = Constants.YOUR_URL;
    //微信支付渠道
    private static final String CHANNEL_WECHAT = "wx";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_use_warter_message);
        unbinder = ButterKnife.bind(this);

        mFirstposition = getIntent().getIntExtra(SearchMsgCListFragment.FIRST_POSITION, -1);

        mTitlename = getIntent().getStringExtra(Constants.SEARCH_MONTH_MESSAGE);

        mStartValue = getIntent().getStringExtra(Constants.SEARCH_MSG_STARTVALUE);
        mEndValue = getIntent().getStringExtra(Constants.SEARCH_MSG_ENDVALUE);
        mUseValue = getIntent().getStringExtra(Constants.SEARCH_MSG_USEVALUE);
        mUseMoney = getIntent().getStringExtra(Constants.SEARCH_MSG_USEMONEY);
        mBasisPrice = getIntent().getStringExtra(Constants.SEARCH_MSG_BASISPRICE);

        initView();
        initData();
    }


    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(UseWarterMesageActivity.this, getWindow().getDecorView());
        baseTitleBar.hideRightSave();
        baseTitleBar.setCenterText(mTitlename);

        //只有第一个可见的item才显示缴费按钮
        //微信支付暂时不做
        if (0 == mFirstposition) {
            //mBtnPay.setVisibility(View.VISIBLE);
            // mBtnPay.setOnClickListener(this);
        }
    }

    private void initData() {
        //读取sp里面的用户姓名，用户编号
        String customer_name = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_NAME, "");
        String customer_id = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, "");
        String customer_address = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ADDR, "");

        mTvUserName.setText(customer_name);
        mTvUserId.setText(customer_id);
        mTvAddress.setText(customer_address);
        mTvLastMonthWarterId.setText(mStartValue+ " ㎥");
        mTvThisMonthWarterId.setText(mEndValue+ " ㎥");
        mTvUseAmount.setText(mUseValue + " ㎥");
        mTvBasisPrice.setText(mBasisPrice + " 元/㎥");
        mTvPay.setText(mUseMoney + " 元");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
