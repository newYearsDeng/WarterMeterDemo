package com.northmeter.wartermeterdemo.presenter;

import com.northmeter.wartermeterdemo.activity.ISearchAllMsgActivity;
import com.northmeter.wartermeterdemo.activity.SearchAllMsgActivity;
import com.northmeter.wartermeterdemo.bean.SearchAllUserMsgBean;
import com.northmeter.wartermeterdemo.model.SearchAllMsgModel;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * @author zz
 * @time 2016/09/02 9:07
 * @des 查询所有用户月份用水信息的presenter
 */
public class SearchAllMsgPresenter implements ISearchAllMsgPresenter {
    private final ISearchAllMsgActivity mSearchAllMsgActivity;
    private final String mLoginName;
    private final SearchAllMsgModel mSearchAllMsgModel;

    public SearchAllMsgPresenter(ISearchAllMsgActivity iSearchAllMsgActivity, String loginName, PtrClassicFrameLayout ptrAllMsg) {
        this.mSearchAllMsgActivity = iSearchAllMsgActivity;
        this.mLoginName = loginName;
        mSearchAllMsgModel = new SearchAllMsgModel(ptrAllMsg);
    }

    @Override
    public void getSearchAllMsgData() {

        mSearchAllMsgModel.internetSearchData(mLoginName, new SearchAllMsgModel.SearchAllMsgCallback() {
            @Override
            public void setEmpty(String failString) {
                mSearchAllMsgActivity.showEmptyView(failString);
            }

            @Override
            public void successMsg(List<SearchAllUserMsgBean.NameBean> name) {
                //一级用户姓名目录集合
                ArrayList<String> groupName = new ArrayList<>();
                //二级用户月份信息目录
                ArrayList<ArrayList<String>> childMonth = new ArrayList<>();
                //通过键值对来保存用户对应月份对应的详细信息
                HashMap<Integer, HashMap<Integer, List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean>>>
                        childItemAllDetail = new HashMap<>();
                //用户姓名对应的用户id
                HashMap<Integer, String> userID = new HashMap<>();

                for (int i = 0; i < name.size(); i++) {
                    //得到一级目录的用户姓名和用户id
                    String customerID = name.get(i).getCustomerID();
                    String customerName = name.get(i).getCustomerName();
                    userID.put(i, customerID);
                    groupName.add(customerName);

                    //得到二级目录的月份信息
                    List<SearchAllUserMsgBean.NameBean.MonthBean> month = name.get(i).getMonth();
                    //创建月份集合
                    ArrayList<String> childItemMonth = new ArrayList<>();
                    //通过键值对来保存对应月份的信息
                    HashMap<Integer, List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean>>
                            itemMonthDetail = new HashMap<>();

                    for (int j = 0; j < month.size(); j++) {
                        //得到每月的详细信息
                        List<SearchAllUserMsgBean.NameBean.MonthBean.DetailBean>
                                detail = month.get(j).getDetail();
                        //存储月份
                        childItemMonth.add(month.get(j).getOperationTime());
                        //保存每月的详细信息
                        itemMonthDetail.put(j, detail);
                    }
                    //保存用户的月份信息
                    childItemAllDetail.put(i, itemMonthDetail);

                    childMonth.add(childItemMonth);
                }
                //把信息传递给acitivity
                mSearchAllMsgActivity.showExpandableListView(groupName, childMonth, childItemAllDetail, userID);
            }

            @Override
            public void dialogDismiss() {
                mSearchAllMsgActivity.hideDialog();
            }
        });
    }

}
