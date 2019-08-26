package com.northmeter.wartermeterdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.fragment.TaskBuildUserFragment;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author zz
 * @time 2016/6/22/0022 10:48
 * @des 任务列表的适配器
 */
public class TasksListAdapter extends BaseAdapter {
    private boolean mIsMultiFlag = false;
    private int type;
    private List<DownloadTaskBeanRESPONSEXMLBean> mData;
    private Context mContext;
    private HashMap<Integer, View> mHashMap = new HashMap<>();

    public TasksListAdapter(Context context, List<DownloadTaskBeanRESPONSEXMLBean> data, int type) {
        this.mData = data;
        this.mContext = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (convertView == null) {
        if (mHashMap.get(position) == null) {
            //convertView = View.inflate(MyApplication.getContext(), R.layout.item_tasks, null);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks, parent, false);

            AutoUtils.auto(convertView);
            // 2. holder
            holder = new ViewHolder();
            // 3. tag
            mHashMap.put(position, convertView);
            convertView.setTag(holder);
            // 4. 找view
            holder.tvTitleAddress = (TextView) convertView.findViewById(R.id.tv_item_tasks_address_id);
            holder.tvTitleSpecific = (TextView) convertView.findViewById(R.id.tv_item_tasks_specific);
            holder.tvIsFinish = (TextView) convertView.findViewById(R.id.tv_item_tasks_isfinish);
            holder.ivIsFinishIcon = (ImageView) convertView.findViewById(R.id.iv_item_tasks_isfinish_icon);
            holder.llAlreadyUpload = (LinearLayout) convertView.findViewById(R.id.ll_item_task_already_upload);
            holder.cbChoose = (CheckBox) convertView.findViewById(R.id.cb_item_tasks_choose_delete);
            holder.tvTaskName = (TextView) convertView.findViewById(R.id.tv_tasks_name);
        } else {
            convertView = mHashMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }

        //判断是否是多选的标记
        if (mIsMultiFlag) {
            //显示多选状态，并取消checkbox的焦点
            holder.cbChoose.setVisibility(View.VISIBLE);
            holder.cbChoose.setFocusable(false);
        } else {
            //隐藏checkbox，清空checkbox的选中状态，
            holder.cbChoose.setVisibility(View.GONE);
            holder.cbChoose.setChecked(false);
        }

        // 设置数据
        DownloadTaskBeanRESPONSEXMLBean bean = mData.get(position);
        holder.tvTitleAddress.setText(bean.getCustomerName() + "/" + bean.getAreaName());
        holder.tvTitleSpecific.setText(bean.getCustomerAddress());
        String taskType = bean.getTaskType();
        holder.tvTaskName.setText(taskType);
        if (type == 1) {
            //修改完成的状态
            if (Constants.ALREADY_FINISH.equals(bean.getIsFinish())) {
//        holder.ivIsFinishIcon.setTag(position);
//            holder.tvIsFinish.setTag(position);
                holder.tvIsFinish.setText("已完成");
                holder.ivIsFinishIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.task_already_finish));
            }else {
                holder.tvIsFinish.setText("未完成");
                holder.ivIsFinishIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.task_not_finish));
            }
            if (Constants.ALREADY_UPLOAD.equals(bean.getIsUpload())) {
                holder.llAlreadyUpload.setVisibility(View.VISIBLE);
            }
        }else if(type==0){
            String analysis = bean.getIsAnalysis();
            //Log.i("LHT","ananlysis  "+position+"==> "+analysis);
            if (analysis.equals(Constants.ALREADY_FINISH)) {
                holder.ivIsFinishIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.already_photo));
                holder.tvIsFinish.setText("已拍照");
            } else {
                holder.ivIsFinishIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_already));
                holder.tvIsFinish.setText("未拍照");
            }
        }
        return convertView;
    }

    public void updateAdapter(List<DownloadTaskBeanRESPONSEXMLBean> newData) {
        this.mData = newData;
        notifyDataSetChanged();

    }

    /**
     * 更新adapter并修改多选状态
     *
     * @param isMultiFlag
     */
    public void notifyData(boolean isMultiFlag) {
        this.mIsMultiFlag = isMultiFlag;
        this.notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvTitleAddress;
        TextView tvTitleSpecific;
        TextView tvIsFinish;
        ImageView ivIsFinishIcon;
        LinearLayout llAlreadyUpload;
        CheckBox cbChoose;
        TextView tvTaskName;
    }
}
