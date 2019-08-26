package com.northmeter.wartermeterdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.adapter.TaskAdapter;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.fragment.TaskBuildUserFragment;
import com.northmeter.wartermeterdemo.fragment.TaskMaintainFragment;
import com.northmeter.wartermeterdemo.fragment.TaskMeterFragment;
import com.northmeter.wartermeterdemo.fragment.TaskRepairFragment;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zz
 * @time 2016/5/9 13:52
 * @des 任务列表
 */
public class TaskActivity extends AutoLayoutActivity implements View.OnClickListener, ITaskActivityTitle {
    @BindView(R.id.rl_tasks_title)
    RelativeLayout mRlTasksTitle;
    @BindView(R.id.rl_tasks_multi_choose_state_title)
    RelativeLayout mMultiChooseStateTitle;
    @BindView(R.id.tv_multi_amount)
    TextView mTvMultiAmount;
    @BindView(R.id.tv_tasks_scale)
    TextView mTvTasksScale;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    private Unbinder unbinder;
    private ViewPager mViewPager;
    private TaskMeterFragment TMF;
    private String mLoginName;
    private TaskBuildUserFragment mTaskBuildUserFragment;
    private Fragment mCurrentFragment;
    private TaskMaintainFragment mTaskMaintainFragment;
    private TaskRepairFragment mTaskRepairFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_task);
        unbinder = ButterKnife.bind(this);
        mLoginName = getIntent().getStringExtra(HomePageActivity.EXTRA_LOGIN_NAME);

        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTvTasksScale();

    }

    public void setTvTasksScale() {
        List<DownloadTaskBeanRESPONSEXMLBean> alreadyFinish = DataSupport.where("isfinish = ? and managername = ?", Constants.ALREADY_FINISH, mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);
        List<DownloadTaskBeanRESPONSEXMLBean> totalTask = DataSupport.where("managername = ?", mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);

        mTvTasksScale.setText("" + alreadyFinish.size() + "/" + totalTask.size());
    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<Fragment>();
//        mTaskBuildUserFragment = new TaskBuildUserFragment(mLoginName);
//        fragments.add(mTaskBuildUserFragment);//立户任务
        TMF = new TaskMeterFragment(mLoginName);
        fragments.add(TMF);//抄表任务
        mTaskMaintainFragment = new TaskMaintainFragment(mLoginName);
        fragments.add(mTaskMaintainFragment);//维护任务
        mTaskRepairFragment = new TaskRepairFragment(mLoginName);
        fragments.add(mTaskRepairFragment);//维修任务
//        TMF = new TaskMeterFragment(mLoginName);
//        fragments.add(TMF);//抄表任务
        mTaskBuildUserFragment = new TaskBuildUserFragment(mLoginName);
        fragments.add(mTaskBuildUserFragment);//立户任务

        FragmentPagerAdapter adapter = new TaskAdapter(getSupportFragmentManager(), fragments);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setPageTransformer(true, new CubeOutTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                switch (position) {
                    case 0:
//                        mCurrentFragment = mTaskBuildUserFragment;
                        mCurrentFragment = TMF;
                        break;
                    case 1:
                        mCurrentFragment = mTaskMaintainFragment;
                        break;
                    case 2:
                        mCurrentFragment = mTaskRepairFragment;
                        break;
                    case 3:
//                        mCurrentFragment = TMF;
                        mCurrentFragment = mTaskBuildUserFragment;
                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
//                        mCurrentFragment = mTaskBuildUserFragment;
                        mCurrentFragment = TMF;
                        break;
                    case 1:
                        mCurrentFragment = mTaskMaintainFragment;
                        break;
                    case 2:
                        mCurrentFragment = mTaskRepairFragment;
                        break;
                    case 3:
//                        mCurrentFragment = TMF;
                        mCurrentFragment = mTaskBuildUserFragment;
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
//                        mCurrentFragment = mTaskBuildUserFragment;
                        mCurrentFragment = TMF;
                        break;
                    case 1:
                        mCurrentFragment = mTaskMaintainFragment;
                        break;
                    case 2:
                        mCurrentFragment = mTaskRepairFragment;
                        break;
                    case 3:
//                        mCurrentFragment = TMF;
                        mCurrentFragment = mTaskBuildUserFragment;
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
//                        mCurrentFragment = mTaskBuildUserFragment;
                        mCurrentFragment = TMF;
                        break;
                    case 1:
                        mCurrentFragment = mTaskMaintainFragment;
                        break;
                    case 2:
                        mCurrentFragment = mTaskRepairFragment;
                        break;
                    case 3:
//                        mCurrentFragment = TMF;
                        mCurrentFragment = mTaskBuildUserFragment;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TMF.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        this.setResult(Activity.RESULT_OK);
    }


    @Override
    public void hideTasksTitle() {
        mRlTasksTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTasksTitle() {
        mRlTasksTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMultiTitle() {
        mMultiChooseStateTitle.setVisibility(View.GONE);
    }

    @Override
    public void showMultiTitle() {
        mMultiChooseStateTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMultiAmount(int amount) {
        if (amount == 0) {
            mTvMultiAmount.setText("请选择");
        } else {
            mTvMultiAmount.setText("已选择" + amount + "项");
        }
    }

    @Override
    public void cancelButton() {
        //取消后，隐藏多选标题栏，显示任务列表标题栏
        hideMultiTitle();
        showTasksTitle();

        if (mCurrentFragment == null) {
         //TMF.hideCheckBox();
        } else if (mCurrentFragment == mTaskBuildUserFragment) {
            mTaskBuildUserFragment.hideCheckBox();
        } else if (mCurrentFragment == mTaskMaintainFragment) {
            mTaskMaintainFragment.hideCheckBox();
        } else if (mCurrentFragment == mTaskRepairFragment) {
            mTaskRepairFragment.hideCheckBox();
        } else if (mCurrentFragment == TMF) {
            TMF.hideCheckBox();
        }
    }

    @Override
    public void deleteButton() {
        //删除后，隐藏多选标题栏，显示任务列表标题栏
        hideMultiTitle();
        showTasksTitle();
//        LoggerUtil.d("deletecurrent--->" + mCurrentFragment);
        if (mCurrentFragment == null) {
//            mTaskBuildUserFragment.deleteChoiceItem();
        } else if (mCurrentFragment == mTaskBuildUserFragment) {
            mTaskBuildUserFragment.deleteChoiceItem();
        } else if (mCurrentFragment == mTaskMaintainFragment) {
            mTaskMaintainFragment.deleteChoiceItem();
        } else if (mCurrentFragment == mTaskRepairFragment) {
            mTaskRepairFragment.deleteChoiceItem();
        } else if (mCurrentFragment == TMF) {
            TMF.deleteChoiceItem();
        }

        setTvTasksScale();
    }


    @OnClick({R.id.ll_task_back, R.id.tv_multi_cancel, R.id.tv_multi_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_task_back:
                //返回
                finish();
                break;
            case R.id.tv_multi_cancel:
                //多选取消按钮
                cancelButton();
                break;
            case R.id.tv_multi_delete:
                //多选删除按钮
                deleteButton();
                break;


        }
    }


}
