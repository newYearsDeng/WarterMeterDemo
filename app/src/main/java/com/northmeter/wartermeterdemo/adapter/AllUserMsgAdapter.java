package com.northmeter.wartermeterdemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

/**
 * @author zz
 * @time 2016/09/02 9:49
 * @des 查询徐偶有用户用水信息二级目录的adapter
 */
public class AllUserMsgAdapter extends BaseExpandableListAdapter {
    private final ArrayList<ArrayList<String>> mChildMonth;
    private final ArrayList<String> mGroupName;

    public AllUserMsgAdapter(ArrayList<String> groupName, ArrayList<ArrayList<String>> childMonth) {
        this.mGroupName = groupName;
        this.mChildMonth = childMonth;
    }

    @Override
    public int getGroupCount() {
        return mGroupName.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildMonth.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupName.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildMonth.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_search_all_msg, parent, false);
            holder = new ItemViewHolder();
            AutoUtils.autoSize(convertView);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_all_user_name);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mGroupName.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_search_all_msg, parent, false);
            holder = new ItemViewHolder();
            AutoUtils.autoSize(convertView);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_item_all_user_name);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }


        String childItemMonth = mChildMonth.get(groupPosition).get(childPosition);
        //截取年份，月份
        String year = childItemMonth.substring(0, 4);
        String month = childItemMonth.substring(5, 7);
        //截取掉单数月份后面的- （如 2015-9-1   2015-11-1）
        if (month.contains("-")) {
            month = month.replace("-", "");
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(80, 0, 0, 0);
        holder.tvName.setLayoutParams(lp);
        holder.tvName.setText(year+"年"+month+"月");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class ItemViewHolder {
        public TextView tvName;
    }
}
