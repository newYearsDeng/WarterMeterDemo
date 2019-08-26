package com.northmeter.wartermeterdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.northmeter.wartermeterdemo.utils.LoggerUtil;

import java.util.List;

/**
 * @author zz
 * @time 2016/5/11 14:03
 * @des 事务的适配器
 */
public class TaskAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private static final String[] CONTENT = new String[]{"抄表任务", "维护任务", "维修任务", "立户任务"};

    public TaskAdapter(FragmentManager fm) {
        super(fm);
    }

    public TaskAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {
        if(mFragments != null){
            return mFragments.get(position);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

}
