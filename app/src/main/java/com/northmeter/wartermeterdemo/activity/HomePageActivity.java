package com.northmeter.wartermeterdemo.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.presenter.HomepagePresenter;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.UpdateManager;
import com.northmeter.wartermeterdemo.utils.UploadAsyncUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author zz
 * @time 2016/5/4 15:03
 * @des $首页页面
 */
public class HomePageActivity extends AutoLayoutActivity implements View.OnClickListener, IHomepage {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    @BindView(R.id.ibtn_homepage_search)
    ImageButton mIbtnSearch;
    @BindView(R.id.ibtn_homepage_setting)
    ImageButton mIbtnSetting;
    /**
     * 显示扫描结果
     */
    private TextView mTextView;
    /**
     * 显示扫描拍的图片
     */
    private ImageView mImageView;

    private ImageButton mBtn_task;
    private ImageButton mBtn_pay;
    private ImageButton mBtn_meter;
    private ImageButton mBtn_builduser;
    private ImageButton mBtn_download;
    private ImageButton mBtn_upload;
    private ImageButton mBtn_user;
    private LinearLayout rlPopularBackground;
    private long mLastClick = 0L;
    public static final String EXTRA_LOGIN_NAME = "extra_login_name";
    private Unbinder unbinder;

    //标记是否退出
    private static boolean isExit = false;
    private Handler handler = new MyHandler(this);
    public static HomePageActivity instance;
    private String mLogin_name;
    private TextView mTvUnRead;
    private Dialog mProgressDialog;
    private HomepagePresenter mHomepagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_homepage);
        unbinder = ButterKnife.bind(this);

        instance = this;
         UpdateManager manager = new UpdateManager(HomePageActivity.this, this);
        //检查软件更新
        manager.connectServer();

        //得到存储到sp的账号名
        mLogin_name = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_NAME_KEY, "");
        LoggerUtil.d(mLogin_name);

        //创建presenter对象
        mHomepagePresenter = new HomepagePresenter(this, mLogin_name);

        initView();
//        initData();
        intEvent();
    }


    private void initView() {

//        mBtn_user = (ImageButton) findViewById(R.id.ibtn_homepage_user);
        mBtn_upload = (ImageButton) findViewById(R.id.ibtn_homepage_upload);
        mBtn_download = (ImageButton) findViewById(R.id.ibtn_homepage_download);
//        mBtn_builduser = (ImageButton) findViewById(R.id.ibtn_homepage_builduser);
//        mBtn_meter = (ImageButton) findViewById(R.id.ibtn_homepage_meter);
        mBtn_pay = (ImageButton) findViewById(R.id.ibtn_homepage_pay);
        mBtn_task = (ImageButton) findViewById(R.id.ibtn_homepage_task);
        rlPopularBackground = (LinearLayout) findViewById(R.id.linear_bg_homepage);
        mTvUnRead = (TextView) findViewById(R.id.tv_homepage_unread_message);

        //减少图片占用内存
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //   opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = getResources().openRawResource(+R.drawable.homepage_bg);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        rlPopularBackground.setBackgroundDrawable(bd);
        SharedPreferencesUtils.setParam(MyApplication.getContext(), "isOnLine", true);
    }

    private void initData() {

        //未完成的数目
        List<DownloadTaskBeanRESPONSEXMLBean> data = DataSupport.where("taskid > ? and isfinish = ? and managername = ?", "0", Constants.NOT_FINISH, mLogin_name).find(DownloadTaskBeanRESPONSEXMLBean.class);
        if (data.size() > 0) {
            mTvUnRead.setVisibility(View.VISIBLE);
            mTvUnRead.setText(data.size() + "");

        }else{
            //这是我加的，清除数据库，以后执行
            mTvUnRead.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void intEvent() {
//        mBtn_user.setOnClickListener(this);
        mIbtnSetting.setOnClickListener(this);
        mIbtnSearch.setOnClickListener(this);
        mBtn_upload.setOnClickListener(this);
        mBtn_download.setOnClickListener(this);
//        mBtn_builduser.setOnClickListener(this);
//        mBtn_meter.setOnClickListener(this);
        mBtn_pay.setOnClickListener(this);
        mBtn_task.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ibtn_homepage_user:
            case R.id.ibtn_homepage_setting:
                Intent intent1 = new Intent(MyApplication.getContext(), SettingActivity.class);
                intent1.putExtra("fromSrc", "HPA");
                startActivity(intent1);
                break;
            case R.id.ibtn_homepage_upload:
                //上传任务数据

                //从服务器获取数据
                //大于一秒才可以再次点击
                if (System.currentTimeMillis() - mLastClick <= 1000) {
                    return;
                }
                mLastClick = System.currentTimeMillis();
                List<DownloadTaskBeanRESPONSEXMLBean> uploadData =
                        DataSupport.where("isfinish = ? and isupload = ? and (tasktype = ? or tasktype = ? or tasktype = ?)",
                                Constants.ALREADY_FINISH, Constants.NOT_UPLOAD,Constants.TASK_TYPE_MAINTAIN,Constants.TASK_TYPE_REPAIR,Constants.TASK_TYPE_BUILDER_USER)
                                .find(DownloadTaskBeanRESPONSEXMLBean.class);
                UploadAsyncUtil uploadUtilsAsyncTest = new UploadAsyncUtil(uploadData, this);
                uploadUtilsAsyncTest.execute();

                break;
            case R.id.ibtn_homepage_download:
                //下载任务数据

                //从服务器获取数据
                //大于一秒才可以再次点击
                if (System.currentTimeMillis() - mLastClick <= 1000) {
                    return;
                }
                mLastClick = System.currentTimeMillis();

                mHomepagePresenter.transmitDownloadMsg();

                break;
//            case R.id.ibtn_homepage_builduser: //点击立户跳转到二维码扫描界面
//                Intent intent = new Intent();
//                intent.setClass(MyApplication.getContext(), CaptureActivity.class);
//                intent.putExtra(Constants.EXTRA_BUILD_USER, Constants.BUILD_USER);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//                break;
//            case R.id.ibtn_homepage_meter:
//                // Intent intent1 = new Intent();
//                //startActivity(intent1);
//                // startActivity(new Intent(this, ReadMeterActivity.class));
//                ToastUtil.showShort(this, "先扫描二维码");
//                break;
            case R.id.ibtn_homepage_search:
                Intent intent = new Intent(MyApplication.getContext(), SearchAllMsgActivity.class);
                intent.putExtra(EXTRA_LOGIN_NAME,mLogin_name);
                startActivity(intent);
            break;
            case R.id.ibtn_homepage_pay:
                startActivity(new Intent(this, PayActivity.class));

                break;
            case R.id.ibtn_homepage_task:
                Intent taskActivityIntent = new Intent(MyApplication.getContext(), TaskActivity.class);
                taskActivityIntent.putExtra(EXTRA_LOGIN_NAME, mLogin_name);
                startActivityForResult(taskActivityIntent, 101);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
//                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
//                    //显示
//                    mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
            case 101:
                updateText();
                break;

        }
    }

    private void updateText() {
        List<DownloadTaskBeanRESPONSEXMLBean> data1 = DataSupport.where("isfinish = ? and managername = ?", Constants.NOT_FINISH, mLogin_name).find(DownloadTaskBeanRESPONSEXMLBean.class);
        // Log.i("LHT","data1.size "+data1.size());
        if (data1.size() == 0) {
            mTvUnRead.setVisibility(View.GONE);
            return;
        }
        mTvUnRead.setText(data1.size() + "");
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
        }
    }

    /**
     * 下载成功的回调
     *
     * @param succeedString
     */
    @Override
    public void showDownloadSucceedMsg(String succeedString) {
        ToastUtil.showShort(MyApplication.getContext(), succeedString);
    }

    /**
     * 现在失败的回调
     *
     * @param failString
     */
    @Override
    public void showDownloadFailMsg(String failString) {
        ToastUtil.showShort(MyApplication.getContext(), failString);
    }

    /**
     * 显示dialog
     *
     * @param showDialog
     */
    @Override
    public void showDialog(String showDialog) {
        mProgressDialog = ToastUtil.createLoadingDialog(HomePageActivity.this, showDialog, Color.WHITE);
        mProgressDialog.show();
    }

    /**
     * 隐藏dialog
     */
    @Override
    public void hideDialog() {
        mProgressDialog.dismiss();
    }

    /**
     * 显示未完成的任务数量
     *
     * @param unreadNum
     */
    @Override
    public void showUnreadNum(String unreadNum) {
        mTvUnRead.setVisibility(View.VISIBLE);
        mTvUnRead.setText(unreadNum);
    }


    private static class MyHandler extends Handler {
        WeakReference<HomePageActivity> popularActivity;

        MyHandler(HomePageActivity homePageActivity) {
            popularActivity = new WeakReference<HomePageActivity>(homePageActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
