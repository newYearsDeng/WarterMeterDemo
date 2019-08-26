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
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.bean.PassResult;
import com.northmeter.wartermeterdemo.bean.PicData;
import com.northmeter.wartermeterdemo.bean.Uploading;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.ImageUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.NetUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.WebServiceUtils;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.northmeter.wartermeterdemo.view.numTextView;
import com.northmeter.wartermeterdemo.view.numViewGroup;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;

/**
 *@author  Lht
 *@time    2017/1/17 15:42
 *@des:   管理员版分析界面
 */
public class AnalysisResultActivity_HP extends AutoLayoutActivity implements View.OnClickListener {
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
    private String resultCode;
    private numViewGroup mViewGroupD;
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
    private String[] paramsName = new String[]{"CUSTOMER_ID", "WATER_CUSTOMERID", "GAS_CUSTOMERID", "READVALUE", "READPHOTE", "IMAGE_CODE", "TaskID", "TaskOperateDate"};
    ;
    private String paramValues[];
    private TextView tv_name, tv_addr;
    private String name, addr;//用户名，用户地址
    private String taskOperateDate;//拍照时间
    private String NumArea;//矩形框的信息   “ 50,100,480,200 ”
    //数据库中的id
    private long id;
    /**
     * TP:来自TakePhotoActivity
     * TMF：来自TakeMeterFragment
     */
    //private String src;//跳转的来源
    private String taskId;
    private MediaPlayer mediaPlayer;
    private AssetFileDescriptor afd;
    public static String USERID = "userId";
    private Dialog progressDialog_anaylysis, progressDialog_pass;
    private Handler handler = new InnerHandler(this);
    private void drawData() {
        ViewGroup.LayoutParams layoutParams = pv.getLayoutParams();
        layoutParams.height = 600;
        pv.setLayoutParams(layoutParams);
        try {
            READVALUE = ImageUtil.makeRectangle(picData, numViewGroup, numTextViews, true);
        } finally {

        }
        baseTitleBar.setCenterText("数据");
        if (!resultCode .equals("0")) {
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
        unbinder = ButterKnife.bind(this);
        initData();
        initView();
        initMethod();
    }

    private void initData() {
        Uploading data = (Uploading) getIntent().getSerializableExtra(Uploading.UPLOADNAME);
        picRelativePath = data.getImageAddr();
        name = data.getCustomName();
        addr = data.getAddress();
        CUSTOMER_ID = data.getUserId();
        WATER_CUSTOMERID = data.getWaterCustomerId();
        taskOperateDate = data.getTaskOperateData();
        taskId = data.getTaskId();
        NumArea = data.getNumArea();
        id = data.getId();
        picAbPath = Constants.SAVEPIC + picRelativePath;
        READPHOTE = picRelativePath;
//获取声音资源
        try {
            afd = getAssets().openFd("alarm.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initMethod() {
        //showBitmap = ImageUtil.getSmallBitmap(picAbPath, 240, 320);
        showBitmap = BitmapFactory.decodeFile(picAbPath);
        if (showBitmap == null) {
            pv.setImageResource(R.drawable.photo_deleted);
            cpb.setVisibility(View.GONE);
            return;
        }
        cpb.setVisibility(View.VISIBLE);
        pv.setImageBitmap(showBitmap);
        //一进入就开始解析
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
                Log.i("LHT", "图片解析结果 " + resultMessage);
                if(resultMessage==null){
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
               /* Intent intent = new Intent(this, EstimateActivity.class);
                intent.putExtra("longId", id);
                startActivity(intent);
                finish();*/

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


    private void checking() {
        String month = new SimpleDateFormat("yyyy-MM").format(new Date());
        //上个月的水表读数
        //Log.i("LHT",CUSTOMER_ID+"," +WATER_CUSTOMERID+"" +month);
        final float[] lastWaterCode = {0};
        WebServiceUtils.getWebServiceInfo("LastMonthData", new String[]{"CUSTOMER_ID", "WATER_CUSTOMERID", "Month"}, new String[]{CUSTOMER_ID, WATER_CUSTOMERID, month}, new WebServiceUtils.CallBack() {
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
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    float dataItemValue = Float.parseFloat(jsonObject.getString("DataItemValue"));
                    lastWaterCode[0] = dataItemValue;
                    float thisCode = Float.parseFloat(READVALUE);
                    //用水差
                    float v = thisCode - lastWaterCode[0];
                    if (0 < v && v < 100) {
                        UpLoadCBData();
                    } else {
                        cpb.setProgress(-1);
                        if (v >= 100) {
                            Log.i("LHT", "大于100");
                            plauAlarm();
                            createAlarmDialog("用水量超过100吨");
                        } else if(v==0) {
                            Log.i("LHT", "用水量为0");
                            //plauAlarm();
                            createAlarmDialog("本月无用水量");
                        }else {
                            //用水差为负值
                            createAlarmDialog("用水异常");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return result;
            }
        });


    }

    /**
     * 上传数据
     */
    private void UpLoadCBData() {
        ImgCode = ImageUtil.picPathToStr(picAbPath);
        paramValues = new String[]{CUSTOMER_ID, WATER_CUSTOMERID, "", READVALUE, READPHOTE, ImgCode, taskId, taskOperateDate};

      /*  Log.i("LHT","--------------------------------------------");
        Log.i("LHT","CUSTOMER_ID "+CUSTOMER_ID);
        Log.i("LHT","WATER_CUSTOMERID "+WATER_CUSTOMERID);
        Log.i("LHT","READVALUE "+READVALUE);
        Log.i("LHT","READPHOTE "+READPHOTE);
        Log.i("LHT","ImgCode "+ImgCode);
        Log.i("LHT","taskId "+taskId);
        Log.i("LHT","taskOperateDate "+taskOperateDate);*/

        WebServiceUtils.getWebServiceInfo("UpLoadCBData", paramsName, paramValues, new WebServiceUtils.CallBack() {
            @Override
            public String result(String result) {
               /* for (String paramValue : paramValues) {
                    Log.i("LHT",paramValue);
                    Log.i("LHT","-------------------");
                }*/
                Log.i("LHT", "上传结果。。 " + result);
                if (result == null) {
                    handler.sendEmptyMessage(4);
                    return result;
                }
                if (CONNECT_FAIL.equals(result)) {
                    handler.sendEmptyMessage(5);
                    return result;
                }
                progressDialog_pass.dismiss();
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
                updateDatabase();
                break;
            case "该用户本月抄表数据已上传":
                info = "该用户本月抄表数据已上传";
                resultDraw = R.drawable.failure_bg;
                color = "#E2635C";
                updateDatabase();
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
     * 上传成功，删除本地数据库中的对应数据
     */
    private void updateDatabase() {
        if (id != -1) {
            DownloadTaskBeanRESPONSEXMLBean data = new DownloadTaskBeanRESPONSEXMLBean();
            int delete = DataSupport.delete(DownloadTaskBeanRESPONSEXMLBean.class, id);

        }
    }

    /**
     * 信息确认提示框
     */
    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.view_dialog_analysis, null);
        btn_no_dialog = ((Button) view.findViewById(R.id.btn_no_dialog));
        btn_ok_dialog = ((Button) view.findViewById(R.id.btn_ok_dialog));
        String[] split = picData.getValue().split(",");
        String readValue = split[0];
        ((TextView) view.findViewById(R.id.tv_num_dialog)).setText(readValue);
        ((TextView) view.findViewById(R.id.tv_name_dialog)).setText(name);
        ((TextView) view.findViewById(R.id.tv_addr_dialog)).setText(addr);
       // mViewGroupD = (com.northmeter.wartermeterdemo.view.numViewGroup) view.findViewById(R.id.numViewGroup_dialog);
       // ImageUtil.makeRectangle(picData, mViewGroupD, numTextViews, false);
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
    private void againPhoto() {
        Intent intent = new Intent(this, TakePhotoActivity_HP.class);
        intent.putExtra(Constants.TAKEPHOTOSRC, "ARA");
        intent.putExtra(USERID, CUSTOMER_ID);
        intent.putExtra("_id",id);
        //startActivityForResult(intent, Constants.PHOTOGRAPH_ANALYSIS);
        startActivity(intent);
        finish();
        cpb.setProgress(0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.PHOTOGRAPH_ANALYSIS && resultCode == Activity.RESULT_OK) {
            String path = intent.getStringExtra("path");
            Log.i("LHT", "返回path =" + path);
            this.picAbPath = Constants.SAVEPIC + path;
            cpb.setEnabled(true);
            numViewGroup.setVisibility(View.INVISIBLE);
            //重拍产生的图片信息，覆盖数据库以前的图片信息
            if (id != -1) {
                DownloadTaskBeanRESPONSEXMLBean data = new DownloadTaskBeanRESPONSEXMLBean();
                data.setIMAGEADDRESS(path);
                data.setTaskOperateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                data.update(id);
                Intent intent1 = new Intent();
                intent.putExtra("updateResult", true);
                setResult(RESULT_OK, intent1);
            }
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
                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
                    break;
                case 1:
                    progressDialog_anaylysis.dismiss();
                    //设置显示解析数据
                    drawData();
                    String pos = picData.getPos();
                    showBitmap=ImageUtil.setMbitmap(showBitmap,pos);
                    pv.setImageBitmap(showBitmap);
                    break;
                case 2:
                    cpb.setProgress(0);
                    dialog.dismiss();
                    //UpLoadCBData();
                    progressDialog_pass.show();
                    //checking();
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
                    cpb.setProgress(100);
                    progressDialog_pass.dismiss();
                    break;
                case 5:
                    ToastUtil.showShort(MyApplication.getContext(), "访问服务器异常，请检查网络");
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
