package com.northmeter.wartermeterdemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zz
 * @time 2016/5/19 9:50
 * @des 水表信息查询的适配器
 */
public class SearchMessageAdapter extends BaseAdapter {


    private final List<String> mData;

    public SearchMessageAdapter(List<String> data) {
        this.mData = data;
    }

    @Override
    public int getCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null){
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
        ViewHolder holder =  null;
        if (convertView == null){
            //convertView = View.inflate(MyApplication.getContext(), R.layout.item_search_message_month,parent,false);
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_message_month,parent,false);
            AutoUtils.auto(convertView);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.tvMessage = (TextView) convertView.findViewById(R.id.tv_item_search_message_use_warter);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }


        String s = mData.get(position);
        holder.tvMessage.setText(s);

        return convertView;

    }

    private class ViewHolder{
        TextView tvMessage;
    }
}
