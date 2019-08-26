package com.northmeter.wartermeterdemo.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.activity.ISearchMessageActivity;
import com.northmeter.wartermeterdemo.activity.UseWarterMesageActivity;
import com.northmeter.wartermeterdemo.adapter.SearchMessageAdapter;
import com.northmeter.wartermeterdemo.presenter.SearchMessagePresenter;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * @author zz
 * @time 2016/08/26 15:01
 * @des 用水账单列表的fragment
 */
@SuppressLint({"NewApi", "ValidFragment"})
public class SearchMsgCListFragment extends Fragment implements ISearchMessageActivity, AdapterView.OnItemClickListener {
    private OnListDataCallback mCallback;
    @BindView(R.id.listview_search_message)
    ListView mListView;
    @BindView(R.id.iv_search_data_empty)
    ImageView mNotFindRecord;
    @BindView(R.id.ptr_search_message)
    PtrClassicFrameLayout mPtrSearchMessage;
    private Dialog mProgressDialog;
    private Unbinder unbinder;

    private List<String> mMonthData;
    private Map<String, List<String>> mMapMsg;
    private List<String> mUseMsg;
    public static final String FIRST_POSITION = "firstposition";

    public SearchMsgCListFragment() {
    }

    public SearchMsgCListFragment(OnListDataCallback onListDataCallback) {
        this.mCallback = onListDataCallback;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_msg_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        //初始化presenter
        SearchMessagePresenter presenter = new SearchMessagePresenter(this, mPtrSearchMessage);
        presenter.transmitUseWaterMsg();


        //设置进度条
        mProgressDialog = ToastUtil.createLoadingDialog(getActivity(), "正在读取数据", 0);
        mProgressDialog.show();
        mListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 遍历json字符串 并把json数据放进数组里面
     *
     * @param jsonString
     * @return
     */
    public void getJson(String jsonString) {
        //遍历jsonString
        // Create a JSONObject
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            //通过Json头，解析创建json数组
            JSONArray jsonArray = jsonObject.getJSONArray(Constants.JSON_RESPONSEXML);
            //创建集合数组存储月份
            mMonthData = new ArrayList<>();
            //创建集合数组，存储月份对应的信息
            mMapMsg = new HashMap<String, List<String>>();

            //要从 1 开始，因为0是返回的状态码和状态信息
            for (int i = 1; i < jsonArray.length(); i++) {
                //每次都重新new一个新的数据存储
                mUseMsg = new ArrayList<String>();
                //得到每个json数组的数据
                JSONObject oneObject = jsonArray.getJSONObject(i);
                //获得json数组里面的详细信息
                //读取成功的数据信息
                //用水年 月份
                String date = oneObject.getString(Constants.SEARCH_MSG_USEDATE);
                //结算金额
                String money = oneObject.getString(Constants.SEARCH_MSG_USEMONEY);
                //本月实际使用值
                String usevalue = oneObject.getString(Constants.SEARCH_MSG_USEVALUE);
                //水价单价
                String basisprice = oneObject.getString(Constants.SEARCH_MSG_BASISPRICE);
                //本月表底
                String endvalue = oneObject.getString(Constants.SEARCH_MSG_ENDVALUE);
                //上月表底
                String startvalue = oneObject.getString(Constants.SEARCH_MSG_STARTVALUE);

                //传递金额与使用值数据
                mCallback.getMoneyData(money);
                mCallback.getUseValueData(usevalue);

                //截取年份，月份
                String year = date.substring(0, 4);
                String month = date.substring(5, 7);
                //截取掉单数月份后面的- （如 2015-9-1   2015-11-1）
                if (month.contains("-")) {
                    month = month.replace("-", "");
                }

                //传递月份
                mCallback.getMonth(year.substring(2,4)+"年"+month+"月");
//                mCallback.getMonth(month+"月");

                //月份集合添加月份用水信息标题
                String yearMonth = year + "年" + month + "月用水账单";
                mMonthData.add(yearMonth);
                //添加对应月份的用水详细信息
                mUseMsg.add(money);
                mUseMsg.add(usevalue);
                mUseMsg.add(basisprice);
                mUseMsg.add(endvalue);
                mUseMsg.add(startvalue);
                //用月份用水信息标题做key，存储对应的详细信息
                mMapMsg.put(yearMonth, mUseMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MyApplication.getContext(), UseWarterMesageActivity.class);
        //点击得到position位置的  用水信息 title
        String yearMonthTitle = mMonthData.get(position);
        intent.putExtra(Constants.SEARCH_MONTH_MESSAGE, yearMonthTitle);

        //通过 用水信息title key值 得到详细信息value值
        List<String> userMsg = mMapMsg.get(yearMonthTitle);
        String sMoney = userMsg.get(0);
        String sUsevalue = userMsg.get(1);
        String sBasisprice = userMsg.get(2);
        String sEndvalue = userMsg.get(3);
        String sStartvalue = userMsg.get(4);

        intent.putExtra(Constants.SEARCH_MSG_STARTVALUE, sStartvalue);
        intent.putExtra(Constants.SEARCH_MSG_ENDVALUE, sEndvalue);
        intent.putExtra(Constants.SEARCH_MSG_USEVALUE, sUsevalue);
        intent.putExtra(Constants.SEARCH_MSG_USEMONEY, sMoney);
        intent.putExtra(Constants.SEARCH_MSG_BASISPRICE, sBasisprice);
        //把第一个位置的item传递过去
        intent.putExtra(FIRST_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showUseWaterMessage(String result) {
        mListView.setVisibility(View.VISIBLE);
        mNotFindRecord.setVisibility(View.GONE);
        getJson(result);
        mListView.setAdapter(new SearchMessageAdapter(mMonthData));
    }

    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showEmptyView() {
        mNotFindRecord.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }

    @Override
    public void toastFailMsg(String failStr) {
        ToastUtil.showShort(MyApplication.getContext(), failStr);
    }


}
