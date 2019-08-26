package com.northmeter.wartermeterdemo.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.fragment.OnListDataCallback;
import com.northmeter.wartermeterdemo.fragment.SearchMsgCListFragment;
import com.northmeter.wartermeterdemo.fragment.SearchMsgChartFragment;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zz
 * @time 2016/5/19 9:37
 * @des 用户水表信息查询
 */
public class SearchMessageActivity extends AutoLayoutActivity implements OnListDataCallback {

    @BindView(R.id.tv_change)
    TextView mTvChange;

    private Unbinder unbinder;
    private SearchMsgCListFragment mListFragment;
    private SearchMsgChartFragment mChartFragment;
    private int currentPosition = 0;
    private ArrayList<String> moneyArr = new ArrayList<>();
    private ArrayList<String> useValueArr = new ArrayList<>();
    private ArrayList<String> monthArr = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_search_message);
        unbinder = ButterKnife.bind(this);
        mListFragment = new SearchMsgCListFragment(this);
        mChartFragment = new SearchMsgChartFragment();


        //设置默认第一个的fragment
        setDefaultFragment();

        initView();
    }


    private void setDefaultFragment() {
        //创建fragment的管理器
        FragmentManager fragmentManager = getFragmentManager();
        //开启fragment事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_fragment, mListFragment);
        //提交事务
        fragmentTransaction.commit();

    }

    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(SearchMessageActivity.this, getWindow().getDecorView());
        baseTitleBar.hideRightSave();
        baseTitleBar.setCenterText("用水账单查询");
        mTvChange.setVisibility(View.VISIBLE);
        mTvChange.setText("信息列表");
    }


    @OnClick(R.id.tv_change)
    public void onClick() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentPosition == 0) {
            //第一个fragment页面
            if (mChartFragment.isAdded()) {
                //如果图表fragment已经添加过不用再次添加
                fragmentTransaction.show(mChartFragment).hide(mListFragment);
            } else {
                //没添加过就添加
                fragmentTransaction.add(R.id.fl_fragment, mChartFragment).hide(mListFragment);
            }

            mChartFragment.setMoneyData(moneyArr);
            mChartFragment.setUseValueData(useValueArr);
            mChartFragment.setMonthData(monthArr);
            currentPosition = 1;

            mTvChange.setText("统计图表");
        } else {
            //第二个fragment页面
            fragmentTransaction.show(mListFragment).hide(mChartFragment);
            currentPosition = 0;

            mTvChange.setText("信息列表");
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    /**
     * 从List那里得到数据传给图表
     *
     * @param money
     */
    @Override
    public void getMoneyData(String money) {
        moneyArr.add(money);
    }

    /**
     * 从List那里得到数据传给图表
     *
     * @param useValue
     */
    @Override
    public void getUseValueData(String useValue) {
//        mChartFragment.setUseValueData(useValue);
        useValueArr.add(useValue);
    }

    @Override
    public void getMonth(String month) {
        monthArr.add(month);
    }
}
