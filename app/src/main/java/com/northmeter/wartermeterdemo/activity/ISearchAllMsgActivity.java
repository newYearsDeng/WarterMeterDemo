package com.northmeter.wartermeterdemo.activity;

import com.northmeter.wartermeterdemo.bean.SearchAllUserMsgBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zz
 * @time 2016/09/02 9:03
 * @des 查询所有用户每月用水信息的view接口
 */
public interface ISearchAllMsgActivity {
    /**
     * 显示空视图
     */
    void showEmptyView(String failString);

    /**
     * 显示2级listview
     * @param groupName
     * @param childMonth
     * @param childItemAllDetail
     * @param userID
     */
    void showExpandableListView(ArrayList<String> groupName, ArrayList<ArrayList<String>> childMonth, HashMap<Integer, HashMap<Integer, List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean>>> childItemAllDetail, HashMap<Integer, String> userID);
    /**
     * 隐藏dialog
     */
    void hideDialog();
}
