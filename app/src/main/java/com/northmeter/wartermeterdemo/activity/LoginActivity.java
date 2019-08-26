package com.northmeter.wartermeterdemo.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.GetCustomerInfoBean;
import com.northmeter.wartermeterdemo.bean.LoginUserInfo;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.GetPhoneInfo;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author zz
 * @time 2016/5/4 14:05
 * @des 登录界面
 */
public class LoginActivity extends AutoLayoutActivity implements View.OnClickListener {

    public static final String RESCODE_SUCCESS = "1";
    public static final String RESCODE_FAIL = "0";
    public static final String RESMSG_ID_PWS_FAIL = "登录账号或密码有误";
    public static final String RESMSG_SUCCESS = "成功";
    private static final String CONNECT_FAIL = "连接异常";
    private static final String OPER_TYPE1 = "超级管理员";
    private static final String OPER_TYPE2 = "系统管理员";
    private static final String OPER_TYPE3 = "抄表员";
    private static final String OPER_TYPE4 = "区域维护人员";
    private static final String OPER_TYPE5="用户";

    private EditText mEt_id;
    private EditText mEt_password;
    private Button mBtn_login;
    private TextView mTvRegist;
    private ImageButton ib_drop;
    private PopupWindow popupWindow;
    private RelativeLayout relativeLayout;
    private Boolean isMemory;//isMemory变量用来判断SharedPreferences有没有数据，包括上面的YES和NO
    //标记是否退出
    private static boolean isExit = false;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private String mName;
    private String mPassword;
    private CheckBox mCbRempass;
    private TextView mTvRemberPwd;
    private long mLastClick = 0L;
    private ListView popListView;
    private Dialog dialog;
    private LinearLayout mLlForgetPassword;
    private int navigationBarHeight;
    private boolean showSoftKeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_login);

        MyApplication.getInstance().addActivity(this);
        //减少图片占用内存
        LinearLayout llBackground = (LinearLayout) findViewById(R.id.ll_login_bg);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = getResources().openRawResource(+R.drawable.homepage_bg);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        llBackground.setBackgroundDrawable(bd);
        initView();
        initData();
        initListener();
        navigationBarHeight = GetPhoneInfo.getNavigationBarHeight(this);
        if (showSoftKeys) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) llBackground.getLayoutParams();
            layoutParams.bottomMargin = navigationBarHeight;
            llBackground.setLayoutParams(layoutParams);
        }
    }



    private void initData() {
        isMemory = (Boolean) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_ISMEMORY_KEY, false);
        mName = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_NAME_KEY, "");
        mEt_id.setText(mName);
        if (isMemory) {
            mPassword = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_PASSWORD_KEY, "");
            mCbRempass.setChecked(true);
            mEt_password.setText(mPassword);
        } else {
            mEt_id.setText(mName);
        }
    }

    // remenber方法用于判断是否记住密码，checkBox1选中时，提取出EditText里面的内容，放到SharedPreferences里面的name和password中
    public void remenber() {
        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_NAME_KEY, mEt_id.getText().toString());
        if (mCbRempass.isChecked()) {
            SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_PASSWORD_KEY, mEt_password.getText().toString());
            SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_ISMEMORY_KEY, true);
        } else if (!mCbRempass.isChecked()) {
            SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_ISMEMORY_KEY, false);
        }
    }


    private void initView() {
        mEt_id = (EditText) findViewById(R.id.et_login_id);
        mEt_password = (EditText) findViewById(R.id.et_login_password);
        mBtn_login = (Button) findViewById(R.id.btn_login);
        mTvRegist = (TextView) findViewById(R.id.tv_login_regist);
        mCbRempass = (CheckBox) findViewById(R.id.cb_login_rempass);
        mTvRemberPwd = (TextView) findViewById(R.id.tv_login_remember_pwd);
        ib_drop = (ImageButton) findViewById(R.id.ib_login_id_xiala);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_acount);
        mLlForgetPassword = (LinearLayout) findViewById(R.id.ll_login_forget_pass);
        dialog = ToastUtil.createLoadingDialog(this, "正在登陆中...", Color.WHITE);
    }

    private void initListener() {
        mBtn_login.setOnClickListener(this);
        mTvRegist.setOnClickListener(this);
        mTvRemberPwd.setOnClickListener(this);
        ib_drop.setOnClickListener(this);
        mLlForgetPassword.setOnClickListener(this);

        mEt_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEt_password.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_login:
                //大于一秒方可通过
                if (System.currentTimeMillis() - mLastClick <= 1000) {
                    return;
                }
                mLastClick = System.currentTimeMillis();

                //获取输入框的数据
                String userID = mEt_id.getText().toString().trim();
                String userPwd = mEt_password.getText().toString().trim();
                //登录按钮
                if (userID.length() == 0) {
                    ToastUtil.showShort(MyApplication.getContext(), "请输入账号");
                    return;
                } else if ("".equals(userPwd)) {
                    ToastUtil.showShort(MyApplication.getContext(), "密码不能为空");
                    return;
                }
                dialog.show();
                mName = userID;
                mPassword = userPwd;
                remenber();
                connect(userID, userPwd);

                break;
            case R.id.tv_login_regist:
                startActivity(new Intent(MyApplication.getContext(), RegistActivity.class));
                break;
            case R.id.tv_login_remember_pwd:
                if (mCbRempass.isChecked()) {
                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_ISMEMORY_KEY, false);
                    mCbRempass.setChecked(false);
                } else if (!mCbRempass.isChecked()) {
                    mCbRempass.setChecked(true);
                }
                break;
            case R.id.ib_login_id_xiala:
                if (popupWindow != null) {
                    if (!popupWindow.isShowing()) {
                        popupWindow.showAsDropDown(relativeLayout);
                    } else {
                        popupWindow.dismiss();
                    }
                } else {
                    createPopWindow();

                }
                break;
            case R.id.ll_login_forget_pass:
                startActivity(new Intent(MyApplication.getContext(),ForgetPasswordActivty.class));
                break;
        }
    }

    private void createPopWindow() {
        //如果已经有登陆过账号
        List<LoginUserInfo> infos = DataSupport.findAll(LoginUserInfo.class);
        if (infos.size() > 0) {
            initPopWindow(infos);
            if (!popupWindow.isShowing()) {
                popupWindow.showAsDropDown(relativeLayout);
            } else {
                popupWindow.dismiss();
            }

        } else {
            ToastUtil.showShort(this, "无记录");
        }
    }

    private void connect(final String userID, final String userPwd) {
        WebServiceUtils webServiceUtils = new WebServiceUtils();
        String[] paramNames = {Constants.PARAM_LOGIN_NAME, Constants.PARAM_LOGIN_PWD};
        String[] paremValues = {userID, userPwd};
        webServiceUtils.getWebServiceInfo(Constants.METHOD_LOGIN_CHECK,
                paramNames, paremValues, new WebServiceUtils.CallBack() {
                    @Override
                    public String result(String result) {
                        // LoggerUtil.json(result);
                        dialog.dismiss();
                        if (CONNECT_FAIL.equals(result)) {
                            ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请稍后重试");
                            return result;
                        }

                        LoggerUtil.d("loginResult--->" + result);
                        //解析json
                        Gson gson = new Gson();
                        GetCustomerInfoBean getCustomerInfoBean = gson.fromJson(result.toString(), GetCustomerInfoBean.class);
                        final List<GetCustomerInfoBean.RESPONSEXMLBean> infos = getCustomerInfoBean.getRESPONSEXML();
                        final GetCustomerInfoBean.RESPONSEXMLBean responseCode = infos.get(0);//第一次返回的状态码

                        if (RESCODE_SUCCESS.equals(responseCode.getRESCODE())) {
//                            if (RESMSG_ID_PWS_FAIL.equals(responseCode.getRESMSG())) {
//
//                            } else
                            if (RESMSG_SUCCESS.equals(responseCode.getRESMSG())) {

                                ToastUtil.showShort(MyApplication.getContext(), "登录成功");

                                String customer_name = infos.get(1).getCUSTOMER_NAME();//用户姓名
                                String customer_id = infos.get(1).getCUSTOMER_ID();//客户编号
                                String water_customerid = infos.get(1).getWATER_CUSTOMERID();//水表子用户号
                                String customer_addr = infos.get(1).getCUSTOMER_ADDR();//用户地址
                                String oper_type = infos.get(1).getOPER_TYPE();//操作员类型

                                if (OPER_TYPE1.equals(oper_type) || OPER_TYPE2.equals(oper_type)
                                        || OPER_TYPE3.equals(oper_type) || OPER_TYPE4.equals(oper_type)||OPER_TYPE5.equals(oper_type)) {
                                    //跳转到管理员界面
                                    customer_name = mEt_id.getText().toString().trim();
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_NAME, customer_name);
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, "");
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ADDR, "");
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_WATER_CUSTOMERID, "");
                                    //登记成功后，用来标记下次直接进入的activity
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.LOGINEDACTIVITY, "HomePageActivity");
                                    startActivity(new Intent(MyApplication.getContext(), HomePageActivity.class));
                                    finish();
                                } else {
                                    //跳转到普通用户界面
                                    //存储客户姓名、客户编号和水表子用户号
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_NAME, customer_name);
                                    //Log.i("LHT","Login "+customer_id);
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, customer_id);
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ADDR, customer_addr);
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_GET_WATER_CUSTOMERID, water_customerid);
                                    //登记成功后，用来标记下次直接进入的activity
                                    SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.LOGINEDACTIVITY, "PopularActivity");
                                    //登录成功
                                    startActivity(new Intent(MyApplication.getContext(), PopularActivity.class));
                                    finish();
                                }
                                saveToSqlite(userID, userPwd);
                            } else {

                                ToastUtil.showShort(MyApplication.getContext(), "登录账号或密码有误");
                                return result;
                            }
                        } else if (RESCODE_FAIL.equals(responseCode.getRESCODE())) {
                            ToastUtil.showShort(MyApplication.getContext(), "登录失败");
                        }

                        return result;
                    }
                });
    }

    private void saveToSqlite(String userID, String userPwd) {
        LoginUserInfo userinfo = new LoginUserInfo();
        userinfo.setUserName(userID);
        if (mCbRempass.isChecked()) {
            userinfo.setSave("保存");
            userinfo.setPassWord(userPwd);
        } else {
            userinfo.setSave("不保存");
            userinfo.setPassWord("");
        }
        List<LoginUserInfo> loginUserInfos = DataSupport.where("username = ?", userID).
                find(LoginUserInfo.class);
        if (loginUserInfos.size() != 0) {
            int update = userinfo.update(loginUserInfos.get(0).getId());
        } else {
            boolean save = userinfo.save();
        }


    }

    private void initPopWindow(final List<LoginUserInfo> infos) {
        final List<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            HashMap<String, Object> map = new HashMap<>();
            String userName = infos.get(i).getUserName();
            map.put("name", userName);
            map.put("drawable", R.drawable.icon_delect_item);
            list.add(map);
        }
        SimpleAdapter dropDwonAdpater = new MySimpleAdapter(this, list, R.layout.dropdown_item, new String[]{"name", "drawable"}, new int[]{R.id.tv_drop_item, R.id.delete_drop_item});
        popListView = new ListView(this);
        popListView.setAdapter(dropDwonAdpater);
        popupWindow = new PopupWindow(popListView, relativeLayout.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5CC2ED")));
    }

    public class MySimpleAdapter extends SimpleAdapter {
        private List<HashMap<String, Object>> data;

        public MySimpleAdapter(Context context, List<HashMap<String, Object>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dropdown_item, null);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_drop_item);
                viewHolder.btn = (ImageButton) convertView.findViewById(R.id.delete_drop_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final String name = data.get(position).get("name").toString();
            viewHolder.tv.setText(name);

            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (name.equals(mEt_id.getText().toString())) {
                        mEt_id.setText("");
                        mEt_password.setText("");
                        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_NAME_KEY, "");
                        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_PASSWORD_KEY, "");
                        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SP_ISMEMORY_KEY, false);
                    }
                    List<LoginUserInfo> infos = DataSupport.findAll(LoginUserInfo.class);
                    if (infos.size() > 0) {
                        int i = DataSupport.deleteAll(LoginUserInfo.class, "username = ? ", name);
                    }
                    List<LoginUserInfo> infos1 = DataSupport.findAll(LoginUserInfo.class);
                    if (infos1.size() > 0) {
                        popupWindow.dismiss();
                        initPopWindow(infos1);
                        popupWindow.showAsDropDown(relativeLayout);
                    } else {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                }
            });
            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<LoginUserInfo> loginUserInfos = DataSupport.where("username = ?", name).
                            find(LoginUserInfo.class);
                    LoginUserInfo loginUserInfo = loginUserInfos.get(0);
                    mEt_id.setText(loginUserInfo.getUserName());
                    mEt_password.setText("");
                    if (loginUserInfo.getIsSave().equals("保存")) {
                        mEt_password.setText(loginUserInfo.getPassWord());
                    }
                    popupWindow.dismiss();
                }
            });

            return convertView;
        }

        class ViewHolder {
            private TextView tv;
            private ImageButton btn;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.showShort(getApplicationContext(), "再按一次退出程序");
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
//            ActivityManager manager = (ActivityManager)MyApplication.getContext().getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器
//            manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().finishActivity(this);
    }


    @Override
    protected void onStart() {
        //用户名栏获取焦点
        mEt_id.setFocusable(true);
        mEt_id.setFocusableInTouchMode(true);
        mEt_id.requestFocus();
        super.onStart();
    }
}
