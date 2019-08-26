package com.northmeter.wartermeterdemo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.AnalysisResultActivity_HP;
import com.northmeter.wartermeterdemo.activity.IDownloadPTR;
import com.northmeter.wartermeterdemo.activity.TakePhotoActivity_HP;
import com.northmeter.wartermeterdemo.activity.TaskActivity;
import com.northmeter.wartermeterdemo.adapter.TasksListAdapter;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.bean.Uploading;
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
 * @des 抄表事务的fragment
 */

@SuppressLint("ValidFragment")
public class TaskMeterFragment extends Fragment implements IDownloadPTR {
    private String mLoginName;
    @BindView(R.id.lv_task)
    ListView mListView;
    @BindView(R.id.iv_data_empty)
    ImageView mIvDataEmpty;
    private List<DownloadTaskBeanRESPONSEXMLBean> mData;
    private TasksListAdapter listAdapter;
    private boolean isMultiFlag = false;
    private TaskActivity mTaskActivity;
    private int count = 0;
    private CheckBox checkBox;
    private List<Integer> mDeleteItem = new ArrayList<>();
    private Unbinder unbinder;

    public TaskMeterFragment() {
    }

    public TaskMeterFragment(String loginName) {
        this.mLoginName = loginName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_task_meter, container, false);
        unbinder = ButterKnife.bind(this, inflate);

        //初始化presenter
        DownloadPTRPresenter downloadPTRPresenter = new DownloadPTRPresenter(this, mLoginName, Constants.TASK_TYPE_MERTER, inflate, getActivity());
        downloadPTRPresenter.transmitDownloadMsg();

        mTaskActivity = (TaskActivity) getActivity();

        initData();
        initEvent();


        return inflate;
    }

    private void initListView() {
        listAdapter = new TasksListAdapter(getContext(), mData,0);
        mListView.setAdapter(listAdapter);
        mListView.setEmptyView(mIvDataEmpty);
    }

    private void initData() {
        //找到抄表任务类型的数据
        try {
            mData = DataSupport.where("tasktype = ?  and managername = ?", Constants.TASK_TYPE_MERTER, mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initListView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!getUserVisibleHint()) {
            if (listAdapter != null) {
                //如果当前的fragment不可见就取消多选状态
                isMultiFlag = false;
                listAdapter.notifyData(isMultiFlag);
                //切换标题栏的显示
                mTaskActivity.hideMultiTitle();
                mTaskActivity.showTasksTitle();
                //选择数量清空
                count = 0;
            }
        }
    }

    private void initEvent() {
       /* //listview长按时事件监听
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("LHT", "long position " + position);
                checkBox = (CheckBox) view.findViewById(R.id.cb_item_tasks_choose_delete);
                if (checkBox.isChecked()) {
                    //如果是选中的状态就取消选中
                    checkBox.setChecked(false);
                    //数量-1
                    count -= 1;
                    mDeleteItem.remove(mDeleteItem.get(mDeleteItem.indexOf(position)));
                    mTaskActivity.showMultiAmount(count);
                } else {
                    //选中被长按的item
                    checkBox.setChecked(true);
                    //设置多选状态
                    isMultiFlag = true;
                    //通知adapter更新
                    listAdapter.notifyData(isMultiFlag);

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
        });*/

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

                                                     transfor(position);

                                                 }
                                             }

                                         }

        );

    }

    private void transfor(int position) {
        DownloadTaskBeanRESPONSEXMLBean bean = mData.get(position);
        long beanId = bean.getId();
        String analysis = bean.getIsAnalysis();
        String imageAddress = bean.getIMAGEADDRESS();
        String name = bean.getCustomerName();
        String addr = bean.getCustomerAddress();
        String userId = bean.getCustomerID();
        String water_customerid = bean.getMeterCode();
        String taskOperateDate = bean.getTaskOperateDate();
        String taskId = bean.getTaskID();
        String numArea = bean.getNumArea();
        if (analysis.equals(Constants.ALREADY_FINISH)) {
            tansforToAnalysis(beanId, imageAddress, name, addr, userId, water_customerid, taskOperateDate, taskId, numArea);
        } else {
            transforToPhoto(beanId, userId);
        }
    }


    private void transforToPhoto(long _id, String userId) {
        Intent intent = new Intent(getActivity(), TakePhotoActivity_HP.class);
        intent.putExtra("_id", _id);
        intent.putExtra("userId", userId);
        intent.putExtra(Constants.TAKEPHOTOSRC, "TMF");
        getActivity().startActivityForResult(intent, 100);
    }

    private void tansforToAnalysis(long id, String imageAddress, String name, String addr, String userId, String water_customerid, String taskOperateDate, String taskId, String numArea) {
        Uploading uploading = new Uploading(addr, name, id, imageAddress, numArea, taskId, taskOperateDate, userId, water_customerid);
        Intent intent = new Intent(getActivity(), AnalysisResultActivity_HP.class);
        intent.putExtra(Constants.ANALYSISSRC, "TMF");
        intent.putExtra(Uploading.UPLOADNAME, uploading);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.i("LHT", "fragment onResume");
        updataList();
    }

    private void updataList() {
        mData = DataSupport.where("tasktype = ?  and managername = ?", Constants.TASK_TYPE_MERTER, mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);
        listAdapter.updateAdapter(mData);
    }


    @Override
    public void showDownloadMsg(String downloadResultString) {
        //切换标题栏的显示
        mTaskActivity.hideMultiTitle();
        mTaskActivity.showTasksTitle();
        count = 0;
        mDeleteItem.clear();
        ToastUtil.showShort(MyApplication.getContext(), downloadResultString);
    }

    @Override
    public void notifyListView() {
       /* mData = DataSupport.where("tasktype = ?  and managername = ?", Constants.TASK_TYPE_MERTER, mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);
        listAdapter.updateAdapter(mData);*/
        initData();
    }

    /**
     * 隐藏checkbox
     */
    public void hideCheckBox() {
        //清空选中的item数量，修改多选的标记
        listAdapter.notifyData(false);
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
        mData = DataSupport.where("tasktype = ? and isfinish = ? and managername = ?", Constants.TASK_TYPE_MERTER, Constants.NOT_FINISH, mLoginName).find(DownloadTaskBeanRESPONSEXMLBean.class);
        initListView();
    }

}
