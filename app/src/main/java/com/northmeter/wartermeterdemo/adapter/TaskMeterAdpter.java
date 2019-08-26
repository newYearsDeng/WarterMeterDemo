package com.northmeter.wartermeterdemo.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * created by lht on 2016/6/23 15:51
 */
public class TaskMeterAdpter extends BaseAdapter {
    private List<DownloadTaskBeanRESPONSEXMLBean> mData;
    private boolean mIsMultiFlag = false;

    public TaskMeterAdpter(List<DownloadTaskBeanRESPONSEXMLBean> mData) {
        this.mData = mData;
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

    public void updateAdapter(List<DownloadTaskBeanRESPONSEXMLBean> newData) {
        // mData.clear();
        // mData.addAll(newData);
        this.mData = newData;
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
           // convertView = View.inflate(MyApplication.getContext(), R.layout.item_task_meter, null);
            convertView= LayoutInflater.from(parent.getContext()).inflate( R.layout.item_task_meter,parent,false);
            // 2. holder
            holder = new ViewHolder();
            // 3. tag
            convertView.setTag(holder);

            AutoUtils.auto(convertView);
            // 4. 找view
            holder.tvTitleAddress = (TextView) convertView.findViewById(R.id.tv_item_task_meter_address_id);
            holder.tvTitleSpecific = (TextView) convertView.findViewById(R.id.tv_item_task_meter_specific);
            holder.tvTaskName = (TextView) convertView.findViewById(R.id.tv_tasks_name);
            holder.ivTitlePhoto = (ImageView) convertView.findViewById(R.id.iv_item_task_meter_photo);
            holder.cbChoose = (CheckBox) convertView.findViewById(R.id.cb_item_task_meter_choose_delete);
        } else {
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


        // 设置数据906812

        DownloadTaskBeanRESPONSEXMLBean bean = mData.get(position);
        holder.tvTitleAddress.setText(bean.getCustomerName() + "/" + bean.getAreaName());
        holder.tvTitleSpecific.setText(bean.getCustomerAddress());
        String taskType = bean.getTaskType();
        holder.tvTaskName.setText(taskType);
        String analysis = bean.getIsAnalysis();
        //Log.i("LHT","ananlysis  "+position+"==> "+analysis);
        if (analysis.equals(Constants.ALREADY_FINISH)) {
            holder.ivTitlePhoto.setImageResource(R.drawable.already_photo);
        } else {
            holder.ivTitlePhoto.setImageResource(R.drawable.no_already);
        }
        return convertView;
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
        TextView tvTaskName;
        ImageView ivTitlePhoto;
        CheckBox cbChoose;
    }
}

