package com.northmeter.wartermeterdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.IDownloadPTR;
import com.northmeter.wartermeterdemo.activity.TaskActivity;
import com.northmeter.wartermeterdemo.activity.TaskBuildUserActivity;
import com.northmeter.wartermeterdemo.adapter.TasksListAdapter;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.presenter.DownloadPTRPresenter;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zz
 * @time 2016/5/11 10:54
 * @des 立户事务的fragment
 */

@SuppressLint("ValidFragment")
public class TaskBuildUserFragment extends Fragment implements IDownloadPTR {
    private String mLoginName;
    @BindView(R.id.lv_task)
    ListView mListView;
    @BindView(R.id.iv_data_empty)
    ImageView mIvDataEmpty;
    private List<DownloadTaskBeanRESPONSEXMLBean> mData;
    private DownloadPTRPresenter mDownloadPTRPresenter;
    private TasksListAdapter mTasksListAdapter;
    private boolean isMultiFlag = false;
    private TaskActivity mTaskActivity;
    private int count = 0;
    private List<Integer> mDeleteItem = new ArrayList<>();
    private CheckBox mCheckBox;
    public TaskBuildUserFragment(){}
    public TaskBuildUserFragment(String loginName ) {
        this.mLoginName = loginName;
    }
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_task_build_user, container, false);
        unbinder = ButterKnife.bind(this, inflate);

        //初始化presenter
        mDownloadPTRPresenter = new DownloadPTRPresenter(this, mLoginName, Constants.TASK_TYPE_BUILDER_USER, inflate, getActivity());
        mDownloadPTRPresenter.transmitDownloadMsg();

        mTaskActivity = (TaskActivity) getActivity();

        initEvent();


        return inflate;
    }


    @Override
    public void onStart() {
        super.onStart();
        initData();

    }

    private void initData() {
        //找到立户任务类型的数据
        mData = DataSupport.where("tasktype = ? and managername = ?", Constants.TASK_TYPE_BUILDER_USER, mLoginName).
                find(DownloadTaskBeanRESPONSEXMLBean.class);
        if (mData.size() > 0) {
            //有数据就把listview展示出来
            mListView.setVisibility(View.VISIBLE);
            mIvDataEmpty.setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            mIvDataEmpty.setVisibility(View.VISIBLE);
        }

        mTasksListAdapter = new TasksListAdapter(MyApplication.getContext(), mData,1);
        mListView.setAdapter(mTasksListAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!getUserVisibleHint()) {
            if (mTasksListAdapter != null) {
                //如果当前的fragment不可见就取消多选状态
                isMultiFlag = false;
                mTasksListAdapter.notifyData(isMultiFlag);
                //切换标题栏的显示
                mTaskActivity.hideMultiTitle();
                mTaskActivity.showTasksTitle();
                //选择数量清空
                count = 0;
            }
        }
    }

    private void initEvent() {
     /*   //listview长按时事件监听
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mCheckBox = (CheckBox) view.findViewById(R.id.cb_item_tasks_choose_delete);
                if (mCheckBox.isChecked()) {
                    //如果是选中的状态就取消选中
                    mCheckBox.setChecked(false);
                    //数量-1
                    count -= 1;
                    mDeleteItem.remove(mDeleteItem.get(mDeleteItem.indexOf(position)));
                    mTaskActivity.showMultiAmount(count);
                } else {
                    //选中被长按的item
                    mCheckBox.setChecked(true);
                    //设置多选状态
                    isMultiFlag = true;
                    //通知adapter更新
                    mTasksListAdapter.notifyData(isMultiFlag);

                    //长按后隐藏标题栏，变成多行状态栏
                    mTaskActivity.hideTasksTitle();
                    mTaskActivity.showMultiTitle();

                    //数量+1
                    count += 1;
                    mDeleteItem.add(position);
                    mTaskActivity.showMultiAmount(count);
                }
                //返回true拦截长按事件，避免长按完继续出发条目点击事件
                return true;
            }
        });
*/

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //如果进入了选择状态就开始多选，否则就跳转页面
                if (isMultiFlag) {
                    CheckBox cbChoose = (CheckBox) view.findViewById(R.id.cb_item_tasks_choose_delete);
                    if (cbChoose.isChecked()) {
                        //如果选中了再次点击就取消选中
                        cbChoose.setChecked(false);

                        //数量-1
                        count -= 1;
                        mDeleteItem.remove(mDeleteItem.get(mDeleteItem.indexOf(position)));
                        mTaskActivity.showMultiAmount(count);
                    } else {
                        //如果没选中点击就选中
                        cbChoose.setChecked(true);

                        //数量+1
                        count += 1;
                        mDeleteItem.add(position);
                        mTaskActivity.showMultiAmount(count);
                    }
                } else {
                    Intent intent = new Intent(MyApplication.getContext(), TaskBuildUserActivity.class);
                    intent.putExtra(Constants.PUT_EXTRA_TASK_ID, mData.get(position).getTaskID());
                    intent.putExtra(Constants.PUT_EXTRA_TASK_NAME, mData.get(position).getCustomerName());
                    intent.putExtra(Constants.PUT_EXTRA_TASK_CUSTOMER_ID, mData.get(position).getCustomerID());
                    intent.putExtra(Constants.PUT_EXTRA_TASK_ADDRESS, mData.get(position).getCustomerAddress());
                    intent.putExtra(Constants.PUT_EXTRA_TASK_METER_CODE, mData.get(position).getMeterCode());
                    intent.putExtra(Constants.PUT_EXTRA_TASK_PHONE_NUMBER, mData.get(position).getCustomerTel());
//                intent.putExtra(Constants.PUT_EXTRA_TASK_AREA_NAME, mData.get(position).getAreaName());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 下载反馈提示
     *
     * @param downloadResultString
     */
    @Override
    public void showDownloadMsg(String downloadResultString) {
        //切换标题栏的显示
        mTaskActivity.hideMultiTitle();
        mTaskActivity.showTasksTitle();
        count = 0;
        mDeleteItem.clear();
        ToastUtil.showShort(MyApplication.getContext(), downloadResultString);
    }

    /**
     * 刷新listview
     */
    @Override
    public void notifyListView() {
        initData();
    }


    /**
     * 隐藏checkbox
     */
    public void hideCheckBox() {
        //清空选中的item数量，修改多选的标记
        mTasksListAdapter.notifyData(false);
        count = 0;
        isMultiFlag = false;
        mDeleteItem.clear();
    }

    /**
     * 删除被选中的item
     */
    public void deleteChoiceItem() {




        //根据找到的任务id来删除数据库里面的数据
        for (int i = 0; i < mDeleteItem.size(); i++) {
            String taskID = mData.get(mDeleteItem.get(i)).getTaskID();
            DataSupport.deleteAll(DownloadTaskBeanRESPONSEXMLBean.class,
                    "taskid = ?", taskID);
        }

        //清空选中的item数量，清空需要删除的集合数据
        count = 0;
        isMultiFlag = false;
        mDeleteItem.clear();
        initData();
    }
}
