package com.northmeter.wartermeterdemo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.LoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * created by lht on 2016/5/31 11:39
 */
public class MyViewPageAdapter extends PagerAdapter {
    private List<View>  list;
    public MyViewPageAdapter(List<View> list, final Activity activity){
        this.list=list;
    }
    public myViewPagerAapterListener listener;
    public void setmyViewPagerAdapterListener(myViewPagerAapterListener listener){
        this.listener=listener;
    }
    public interface  myViewPagerAapterListener{
        void setActivityFinish(int i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(list.get(position));
        View view = list.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setActivityFinish(position);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }
}
