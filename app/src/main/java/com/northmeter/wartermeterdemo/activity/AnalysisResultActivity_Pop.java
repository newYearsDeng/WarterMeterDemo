package com.northmeter.wartermeterdemo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.PassResult;
import com.northmeter.wartermeterdemo.bean.PicData;
import com.northmeter.wartermeterdemo.bean.Uploading;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.ImageUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.NetUtils;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.northmeter.wartermeterdemo.view.numTextView;
import com.northmeter.wartermeterdemo.view.numViewGroup;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;

/**
 * @author Lht
 * @time 2017/1/17 15:49
 * @des: 普通用户版，分析界面
 */
public class AnalysisResultActivity_Pop extends AutoLayoutActivity implements View.OnClickListener {
    @BindView(R.id.numViewGroup)
    numViewGroup numViewGroup;
    @BindView(R.id.btn_upload_analysis)
    CircularProgressButton cpb;
    @BindView(R.id.tv_title_bar_right_text)
    TextView tv_right;
    @BindView(R.id.tv_result_ananlysis)
    TextView tv_result;
    @BindView(R.id.iv_result_analysis)
    ImageView iv_result;
    @BindView(R.id.pv_analysis)
    PhotoView pv;
    @BindView(R.id.linearlayout_upload_ok_analysis)
    LinearLayout llResult;
    private Unbinder unbinder;
    private BaseTitleBar baseTitleBar;
    private Button btn_no_dialog;
    private Button btn_ok_dialog;
    private Dialog dialog;
    //识别返回码
    private String resultCode;

    private PicData picData;
    private numTextView[] numTextViews = new numTextView[10];
    //图片的绝对路径
    private String picAbPath;
    //图片的相对路径
    private String picRelativePath;
    private boolean isFinish;
    private Bitmap showBitmap;
    private static final String CONNECT_FAIL = "连接异常";
    //客户代码
    private String CUSTOMER_ID;
    //抄表表底
    private String READVALUE;
    //图片名称
    private String READPHOTE;
    //图片Base64字符串
    private String ImgCode;
    //用户水表号
    private String WATER_CUSTOMERID;
    private String[] paramsName = new String[]{"CUSTOMER_ID", "WATER_CUSTOMERID", "GAS_CUSTOMERID", "READVALUE", "READPHOTE", "IMAGE_CODE"};
    ;
    private String paramValues[];
    private String name, addr;//用户名，用户地址
    private String NumArea;//矩形框的信息   “ 50,100,480,200 ”
    /**
     * TP:来自TakePhotoActivity
     * TMF：来自TakeMeterFragment
     */
    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor afd;
    private Dialog progressDialog_anaylysis, progressDialog_pass;
    private Handler handler = new InnerHandler(this);

    private void drawData() {
        ViewGroup.LayoutParams layoutParams = pv.getLayoutParams();
        layoutParams.height = 600;
        pv.setLayoutParams(layoutParams);
        READVALUE = ImageUtil.makeRectangle(picData, numViewGroup, numTextViews, true);
        baseTitleBar.setCenterText("数据");
        if (!resultCode.equals("0")) {
            cpb.setProgress(-1);
        } else {
            cpb.setProgress(50);
            cpb.setProgress(100);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_analysis_result);
        unbinder =  ButterKnife.bind(this);

        initData();
        initView();
        initMethod();
    }

    private void initData() {
        name = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_NAME, "");
        addr = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ADDR, "");
        CUSTOMER_ID = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, "-1");
        WATER_CUSTOMERID = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_WATER_CUSTOMERID, "");

        Uploading data = (Uploading) getIntent().getSerializableExtra(Uploading.UPLOADNAME);

        picRelativePath = data.getImageAddr();
        NumArea = data.getNumArea();
        picAbPath = Constants.SAVEPIC + picRelativePath;
        READPHOTE = picRelativePath;
//获取声音资源
        try {
            afd = getAssets().openFd("alarm.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 重置页面显示的照片
     */
    private void initMethod() {
        showBitmap = BitmapFactory.decodeFile(picAbPath);
        pv.setImageBitmap(showBitmap);
        //开始解析数据
        handler.sendEmptyMessage(3);
    }

    /**
     * 发送图像识别的网络请求
     *
     * @param picAbPath
     */
    private void PostRequest(final String picAbPath) {
        ThreadUtils.runInWorkThread(new Runnable() {
            @Override
            public void run() {
                String resultMessage = NetUtils.doPostPic(picAbPath, NumArea);

                if (resultMessage == null) {
                    handler.sendEmptyMessage(0);
                    return;
                }
                if (resultMessage.equals("")) {
                    handler.sendEmptyMessage(0);
                    return;
                }
                if (resultMessage != null) {
                    if (isFinish) {
                        return;
                    }
                    Log.i("LHT", resultMessage);
                    picData = new Gson().fromJson(resultMessage, PicData.class);
                    resultCode = picData.getCode();
                    handler.sendEmptyMessage(1);
                }

            }
        });


    }

    private void initView() {
        baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("抄表");
        tv_right.setText("重拍");
        tv_right.setOnClickListener(this);
        cpb.setIndeterminateProgressMode(true);
        cpb.setOnClickListener(this);

        progressDialog_anaylysis = ToastUtil.createLoadingDialog(this, "正在解析数据..", 0);
        progressDialog_pass = ToastUtil.createLoadingDialog(this, "正在上传中..", 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_bar_right_text:
                againPhoto();
                break;
            case R.id.btn_ok_dialog:

                handler.sendEmptyMessage(2);
                break;
            case R.id.btn_no_dialog:
                dialog.dismiss();
                break;
            case R.id.btn_upload_analysis:
                if (cpb.getProgress() == -1) {
                    //重新拍照
                    againPhoto();
                } else if (cpb.getProgress() == 0) {
                    handler.sendEmptyMessage(3);
                } else if (cpb.getProgress() == 100) {
                    createDialog();
                }
                break;
        }
    }

    private void againPhoto() {
        Intent intent = new Intent(this, TakePhoteActivity_Pop.class);
        intent.putExtra(Constants.TAKEPHOTOSRC, "ARA");
        startActivityForResult(intent, Constants.PHOTOGRAPH_ANALYSIS);
        cpb.setProgress(0);
    }


    /**
     * 上传数据
     */
    private void UpLoadCBData() {

        ImgCode = ImageUtil.picPathToStr(picAbPath);
        paramValues = new String[]{CUSTOMER_ID, WATER_CUSTOMERID, "", READVALUE, READPHOTE, ImgCode};
       /* Log.i("LHT","--------------------------------------------");
        Log.i("LHT","CUSTOMER_ID "+CUSTOMER_ID);
        Log.i("LHT","WATER_CUSTOMERID "+WATER_CUSTOMERID);
        Log.i("LHT","READVALUE "+READVALUE);
        Log.i("LHT","READPHOTE "+READPHOTE);
        Log.i("LHT","ImgCode "+ImgCode);*/
        //隐藏下面的按钮
        cpb.setVisibility(View.GONE);
        WebServiceUtils.getWebServiceInfo("UpLoadCBData", paramsName, paramValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
                if (result == null) {

                    handler.sendEmptyMessage(4);
                    return result;
                }
                if (CONNECT_FAIL.equals(result)) {

                    handler.sendEmptyMessage(5);
                    return result;
                }
                progressDialog_pass.dismiss();
                Log.i("LHT", "上传结果。。 " + result);
                PassResult passResult = new Gson().fromJson(result, PassResult.class);
                PassResult.RESPONSEXMLBean responsexmlBean = passResult.getRESPONSEXML().get(0);
                String resmsg = responsexmlBean.getRESMSG();
                showPrompt(resmsg);
                return result;
            }
        });
    }

    /**
     * 结果提示信息
     *
     * @param msg
     */
    private void showPrompt(String msg) {
        String info = "";
        int resultDraw;
        String color;
        switch (msg) {
            case "成功":
                info = "数据上传成功";
                resultDraw = R.drawable.ok_bg;
                color = "#01B2EB";
                break;
            case "该用户本月抄表数据已上传":
                info = "该用户本月抄表数据已上传";
                resultDraw = R.drawable.failure_bg;
                color = "#E2635C";
                break;
            default:
                info = "上传信息失败";
                resultDraw = R.drawable.failure_bg;
                color = "#E2635C";
                break;
        }
        numViewGroup.setVisibility(View.GONE);
        llResult.setVisibility(View.VISIBLE);
        iv_result.setImageResource(resultDraw);
        tv_result.setTextColor(Color.parseColor(color));
        tv_result.setText(info);
        ObjectAnimator.ofFloat(tv_result, "translationX", -360F, 0F).setDuration(1000).start();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(iv_result, "scaleX", 0.2f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(iv_result, "scaleY", 0.2f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000).play(animatorX).with(animatorY);
        set.start();
        cpb.setVisibility(View.INVISIBLE);
        tv_right.setVisibility(View.INVISIBLE);
    }

    /**
     * 信息确认提示框
     */
    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.view_dialog_analysis, null);
        btn_no_dialog = ((Button) view.findViewById(R.id.btn_no_dialog));
        btn_ok_dialog = ((Button) view.findViewById(R.id.btn_ok_dialog));
        ((TextView) view.findViewById(R.id.tv_name_dialog)).setText(name);
        ((TextView) view.findViewById(R.id.tv_addr_dialog)).setText(addr);
        String[] split = picData.getValue().split(",");
        String readValue = split[0];
        ((TextView) view.findViewById(R.id.tv_num_dialog)).setText(readValue);
        //ImageUtil.makeRectangle(picData, mViewGroupD, numTextViews, false);
        btn_no_dialog.setOnClickListener(this);
        btn_ok_dialog.setOnClickListener(this);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        WindowManager windowManager = getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        params.width = (int) (width * 0.9);
        params.height = (int) (width * 0.9);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 警报提示框
     *
     * @param info
     */
    private void createAlarmDialog(String info) {
        progressDialog_pass.dismiss();
        View view = getLayoutInflater().inflate(R.layout.view_dialog_alarm, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_alarm);
        tv.setText(info);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.show();
        WindowManager.LayoutParams params =
                dialog.getWindow().getAttributes();
        WindowManager windowManager = getWindowManager();
        int width = windowManager.getDefaultDisplay().getWidth();
        params.width = (int) (width * 0.75);
        params.height = (int) (width * 0.75);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 播放提示音
     */

    public void plauAlarm() {
        ThreadUtils.runInWorkThread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        isFinish = true;
        if (showBitmap != null) {
            showBitmap.recycle();
            showBitmap = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.PHOTOGRAPH_ANALYSIS && resultCode == Activity.RESULT_OK) {
            String path = intent.getStringExtra("path");
            this.picAbPath = Constants.SAVEPIC + path;
            cpb.setEnabled(true);
            numViewGroup.setVisibility(View.INVISIBLE);
            initMethod();
        }

    }

    private class InnerHandler extends Handler {
        WeakReference<Activity> mActivity;

        public InnerHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    progressDialog_anaylysis.dismiss();
                    ToastUtil.showShort(AnalysisResultActivity_Pop.this, "访问服务器异常，请检查网络");
                    break;
                case 1:
                    progressDialog_anaylysis.dismiss();
                    //设置显示解析数据
                    drawData();
                    String pos = picData.getPos();
                    showBitmap = ImageUtil.setMbitmap(showBitmap, pos);
                    pv.setImageBitmap(showBitmap);
                    break;
                case 2:
                    // cpb.setProgress(0);
                    cpb.setVisibility(View.GONE);
                    dialog.dismiss();
                    progressDialog_pass.show();
                    UpLoadCBData();
                    break;
                case 3:
                    //解析图片，显示进度框
                    // cpb.setProgress(50);
                    progressDialog_anaylysis.show();
                    PostRequest(picAbPath);
                    break;
                case 4:
                    ToastUtil.showShort(MyApplication.getContext(), "访问失败...");
                    cpb.setVisibility(View.VISIBLE);
                    cpb.setProgress(100);
                    progressDialog_pass.dismiss();
                    break;
                case 5:

                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
                    cpb.setVisibility(View.VISIBLE);
                    cpb.setProgress(100);
                    if (progressDialog_pass.isShowing())
                        progressDialog_pass.dismiss();
                    if (progressDialog_anaylysis.isShowing()) {
                        progressDialog_anaylysis.dismiss();
                    }
                    break;

            }
        }

    }

}
