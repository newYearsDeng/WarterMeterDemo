package com.northmeter.wartermeterdemo.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.DataCleanManager;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.view.CircularImage;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lht on 2016/5/9
 */
public class SettingActivity extends AutoLayoutActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.ll_title_bar_back)
    LinearLayout back;
    @BindView(R.id.tv_title_bar_center_text)
    TextView tv_Title;
    @BindView(R.id.tv_title_bar_right_text)
    TextView tv_right;
    @BindView(R.id.listView_setting)
    ListView listView;
    @BindView(R.id.image_head_setting)
    CircularImage imageHeadSetting;
    @BindView(R.id.tv_name_setting)
    TextView tvNameSetting;
    @BindView(R.id.btn_exit_setting)
    CircularProgressButton btnExitSetting;

    private Unbinder unbinder;
    public static SettingActivity instance;
    private boolean isOnLine=true;
    private List<Integer> ivPics ;
    private List<String>  tvNames;
    private Class<?> activities[] ;
    private List<Map<String, Object>> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_setting);
        instance=this;
        unbinder = ButterKnife.bind(this);
        String fromSrc = getIntent().getStringExtra("fromSrc");
        initData(fromSrc);
        initView();

    }

    private void initData(String fromSrc) {
        if(fromSrc.equals("PA")){
            ivPics= Arrays.asList(R.drawable.pws_setting, R.drawable.code_setting, R.drawable.advice, R.drawable.about_setting);
            tvNames= Arrays.asList("密码修改", "二维码推广", "意见反馈", "关于");
            activities = new Class<?>[]{UpdatePasswordActivity.class, CodeActivity.class, AdviceActivity.class, AboutActivity.class};
        }else if(fromSrc.equals("HPA")){
            ivPics= Arrays.asList(R.drawable.pws_setting, R.drawable.code_setting, R.drawable.advice,R.drawable.print_setting, R.drawable.about_setting);
            tvNames= Arrays.asList("密码修改", "二维码推广", "意见反馈","蓝牙打印机设置", "关于");
            activities = new Class<?>[]{UpdatePasswordActivity.class, CodeActivity.class, AdviceActivity.class,PrintActivity.class, AboutActivity.class};
        }
    }

    private void initView() {
        tv_Title.setText("设置");
        tv_right.setVisibility(View.INVISIBLE);
       String  userName = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_NAME, "");
        if(userName!=null){
            tvNameSetting.setText(userName);
        }
        for (int i = 0; i < ivPics.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("ivName", tvNames.get(i));
            map.put("ivPic", ivPics.get(i));
            data.add(map);
        }
        SimpleAdapter adpter = new SimpleAdapter(this, data, R.layout.item_listview_setting, new String[]{"ivPic", "ivName"}, new int[]{R.id.iv_item_listView_setting, R.id.tv_item_listView_setting});
        listView.setAdapter(adpter);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    @OnClick({R.id.ll_title_bar_back,R.id.image_head_setting, R.id.tv_name_setting, R.id.btn_exit_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_head_setting:
                break;
            case R.id.tv_name_setting:
                break;
            case R.id.ll_title_bar_back:
                if(btnExitSetting.getProgress()==100){
                    backToLogin();
                }else{
                    this.finish();
                }
                break;
            case R.id.btn_exit_setting:
                if (btnExitSetting.getProgress() == 0) {
                    transforBtn();
                } /*else {
                    backToLogin();
                }*/
                break;
        }
    }

    private void transforBtn() {
        btnExitSetting.setProgress(50);
        if(PopularActivity.instance!=null){
            PopularActivity.instance.finish();
        }
        if(HomePageActivity.instance!=null){
            HomePageActivity.instance.finish();
        }
        btnExitSetting.setProgress(100);
        tvNameSetting.setText("暂无登陆");
        imageHeadSetting.setImageResource(R.drawable.unonline);

        SharedPreferencesUtils.setParam(MyApplication.getContext(),"isOnLine",false);
        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.LOGINEDACTIVITY, "LoginActivity");
        isOnLine=false;
        backToLogin();
        /*SharedPreferencesUtils.setParam(MyApplication.getContext(),"isOnLine",false);
        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.LOGINEDACTIVITY, "LoginActivity");
        Intent loginIntent = new Intent(MyApplication.getContext(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);*/
    }




    private void backToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_left,
                R.anim.slide_right);
        this.finish();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        Class<?> activity = activities[position];
        /*if(activity==PrintActivity.class){
            intent=new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        }else {*/
         intent= new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!isOnLine){
                backToLogin();
            }else{
                finish();
            }
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
