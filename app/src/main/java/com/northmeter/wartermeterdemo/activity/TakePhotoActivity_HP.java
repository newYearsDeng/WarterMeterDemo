package com.northmeter.wartermeterdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.bean.Uploading;
import com.northmeter.wartermeterdemo.utils.CarmeraUtils;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.GetPhoneInfo;
import com.northmeter.wartermeterdemo.utils.ImageUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.view.CameraPreview;
import com.northmeter.wartermeterdemo.view.FocusView;
import com.northmeter.wartermeterdemo.view.ReferenceLine;
import com.zhy.autolayout.AutoLayoutActivity;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TakePhotoActivity_HP extends AutoLayoutActivity implements CameraPreview.OnCameraStatusListener, CameraPreview.OnCameraPictureSizeListener {
    @BindView(R.id.cameraPreview)
    CameraPreview mCameraPreview;
    @BindView(R.id.view_focus)
    FocusView viewFocus;
    @BindView(R.id.referenceLine)
    ReferenceLine referenceLine;
    @BindView(R.id.ib_close)
    ImageButton ibClose;
    @BindView(R.id.ib_photo)
    ImageButton ibPhoto;
    @BindView(R.id.ib_light)
    ImageButton ibLight;
    @BindView(R.id.rl_photo_controller)
    RelativeLayout rl_photo_controller;
    @BindView(R.id.ib_save)
    ImageButton ib_save;
    @BindView(R.id.layout_afterPhoto)
    RelativeLayout afterPhotoLayout;
    @BindView(R.id.take_photo_layout)
    RelativeLayout mTakePhotoLayout;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.rl_afterPhoto_controller)
    RelativeLayout rl_afterPhoto;
    @BindView(R.id.tv_analysis)
    TextView tvAnalysis;
    @BindView(R.id.tv_estimate)
    TextView tv_estimate;
    private int CAMERA_CODE = 2;
    private String imagePath;
    private Bitmap showBitmap;
    private boolean isClose;
    private String src;
    private Intent intent;
    private String userId;
    private List<DownloadTaskBeanRESPONSEXMLBean> mData;
    private boolean showSoftKeys;
    private int navigationBarHeight;
    //屏幕的宽高
    private float screenHeight, screenWidth;
    private float left, right, top;
    //矩形框的范围
    private String numArea;
    private long id;
    private boolean isSave;
    private Unbinder unbinder;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo__hp);
        unbinder = ButterKnife.bind(this);
        initSetting();
        initView();
        initData();
    }

    private void initData() {
        left = (float) (0.3 * screenWidth / 2);
        top = (float) (screenHeight / 2 - 50 - 0.29 * screenWidth);
        right = (float) (screenWidth - left);
    }

    private void initSetting() {
//透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    CAMERA_CODE);
        }
    }

    private void initView() {
        intent = getIntent();
        //调转的来源
        src = intent.getStringExtra(Constants.TAKEPHOTOSRC);
        id = intent.getLongExtra("_id", -1);
        userId = intent.getStringExtra(AnalysisResultActivity_HP.USERID);
        mCameraPreview.setFocusView(viewFocus);
        mCameraPreview.setOnCameraStatusListener(this);
        mCameraPreview.setOnCameraGetPictureSizeListner(this);

        //获取屏幕的宽高
        screenWidth = CarmeraUtils.getScreenWH(getApplicationContext()).widthPixels;
        screenHeight = CarmeraUtils.getScreenWH(getApplicationContext()).heightPixels;
        showSoftKeys = GetPhoneInfo.hasSoftKeys(getWindowManager());
        navigationBarHeight = GetPhoneInfo.getNavigationBarHeight(this);
        if (showSoftKeys) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_photo_controller.getLayoutParams();
            layoutParams.bottomMargin = navigationBarHeight;
            rl_photo_controller.setLayoutParams(layoutParams);
            // TODO: 2017/1/13  
            //screenHeight = screenHeight + navigationBarHeight;
        }

        tv_estimate.setVisibility(View.VISIBLE);




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == CAMERA_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) && (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                finish();
            }
        } else {
            ToastUtil.showShort(this, "没有打开权限");
            finish();
        }
    }

    public void takePhoto() {
        if (mCameraPreview != null) {
            try {
                mCameraPreview.takePicture();
                mCameraPreview.updateCameraParameters();
            } catch (Exception e) {
                ToastUtil.showShort(this, "摄像头打开失败！");
            }
        }
    }


    public void cancel() {
        showTakePhotoLayout();
        if (!showBitmap.isRecycled()) {
            showBitmap.recycle();
            showBitmap = null;
        }
    }

    public void confirm() {
        isSave = savePic();
        if (isSave) {
            if (TextUtils.isEmpty(src)) {
                return;
            }
            ToastUtil.showShort(MyApplication.getContext(), "图片已保存。。");

              if (src.equals("TMF")) {
                DownloadTaskBeanRESPONSEXMLBean data = setLBean(new DownloadTaskBeanRESPONSEXMLBean());
                int update = data.update(id);
            }

            this.finish();

        } else {
            ToastUtil.showShort(MyApplication.getContext(), "保存失败。。");
        }

    }

    @NonNull
    private DownloadTaskBeanRESPONSEXMLBean setLBean(DownloadTaskBeanRESPONSEXMLBean data) {
        //是否完成拍照
        data.setAnalysis(Constants.ALREADY_FINISH);
        //设置图片地址
        data.setIMAGEADDRESS(imagePath);

        //已完成拍照，未上传
        data.setIsFinish(Constants.ALREADY_FINISH);
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //操作任务时间
        data.setTaskOperateDate(format);
        //图片数字区域
        data.setNumArea(numArea);
        return data;
    }

    private boolean savePic() {
        if (showBitmap.isRecycled()) {
            return false;
        }

        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        imagePath = ImageUtil.createImageName(userId);
        final Bitmap savePic = ImageUtil.setMbitmap(showBitmap);
         //保存图片后，修改图片的地址
        DownloadTaskBeanRESPONSEXMLBean data =new DownloadTaskBeanRESPONSEXMLBean();
        data.setIMAGEADDRESS(imagePath);
        data.setTaskOperateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        data.update(id);

        return ImageUtil.saveImage(savePic, Constants.SAVEPIC, imagePath);
    }

    @Override
    public void onCameraStopped(final byte[] data) {
        ThreadUtils.runInWorkThread(new Runnable() {
            @Override
            public void run() {
                //final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                final  Bitmap bitmap=ImageUtil.byteToBitmap(data);
                showBitmap = ImageUtil.zoomImage(bitmap, 800);
                //旋转图片
                if (showBitmap.getHeight() < showBitmap.getWidth())
                    showBitmap = ImageUtil.rotate(showBitmap, 90);
                ThreadUtils.runInUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCropperLayout();
                    }
                });

            }
        });
    }

    private void showTakePhotoLayout() {
        mTakePhotoLayout.setVisibility(View.VISIBLE);
        afterPhotoLayout.setVisibility(View.GONE);
        mCameraPreview.start();
    }

    public void showCropperLayout() {
        mTakePhotoLayout.setVisibility(View.GONE);
        afterPhotoLayout.setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(showBitmap);
        if (showSoftKeys) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_afterPhoto.getLayoutParams();
            layoutParams.bottomMargin = navigationBarHeight;
            rl_afterPhoto.setLayoutParams(layoutParams);
        }
      /*  if (src.equals("ARA")) {
            tvAnalysis.setVisibility(View.GONE);
        }*/
    }

    public void takeLight() {
        if (mCameraPreview != null) {
            if (isClose) {
                ibLight.setBackgroundResource(R.drawable.light_on);
                mCameraPreview.turnLightOn();
            } else {
                mCameraPreview.turnLightOff();
                ibLight.setBackgroundResource(R.drawable.light_off);
            }
            isClose = !isClose;
        }
    }



    //得到图片大小的回调接口
    @Override
    public void onCameraGetSize(int mwidth, int mheight) {
        int width = 0, height = 0;
        float top1, left1, xDes, right1, bottom1, yDes;
        if (mwidth < mheight) {
            width = mwidth;
            height = mheight;
        } else {
            width = mheight;
            height = mwidth;
        }
        float scaleHeight = ((float) height) / 800;
        float nWidth = width / scaleHeight;
        float xRadio = nWidth / screenWidth;
        float yRadio = 800 / screenHeight;
        top1 = top * yRadio;
        left1 = left * xRadio;
        right1 = right * xRadio;
        xDes = right1 - left1;
        yDes = (float) (xDes / 4.8);
        bottom1 = (top1 + yDes) / yRadio;
        referenceLine.setRect((float) (top * 0.9), left, right, (float) (bottom1*0.9));
        numArea = (int)Math.floor(left1) + "," + (int)Math.floor(top1) + "," +(int)Math.ceil(xDes) + "," + (int)Math.ceil(yDes);
        //new

        //new
        /*int width = 0, height = 0;
        if (mwidth < mheight) {
            width = mwidth;
            height = mheight;
        } else {
            width = mheight;
            height = mwidth;
        }
        //拍照后的图片和屏幕的比例
        float photoRadioH = height / screenHeight;
        float photoRadioW = width / screenWidth;
        //拍照图片中的坐标位置
        float pLeft=left*photoRadioW;
        float  pRight=right*photoRadioW;
        float  pTop=top*photoRadioH;

        //1.规划图片为高800
        float scaleHeight = ((float) height) / 800;
        float nWidth = width / scaleHeight;

        //2.压缩图片中 坐标的位置
        float yLeft=pLeft/scaleHeight;
        float yRight=pRight/scaleHeight;
        float yTop=pTop/scaleHeight;

       *//* //2.压缩后图片相对于屏幕的比例
        float xRadio = screenWidth / nWidth;
        float yRadio = screenHeight / screenHeight;*//*


        //计算图片中矩形的位置
       *//* top1 = top / yRadio;
        Log.i("LHT","top1=" +top1);
        left1 = left / xRadio;
        Log.i("LHT","left1=" +left1);
        right1 = right / xRadio;
        Log.i("LHT","right1=" +right1);*//*
       // xDes = right1 - left1;
        float xDes=yRight-yLeft;
        //必须保证图片中 宽/高=4.8
        //计算图片中高
        float yDes = (float) (xDes / 4.8);
        float yBottom = yTop+yDes;
        //拍照图中的底坐标
        float pBottom = yBottom * scaleHeight;
        //屏幕中的底坐标
        float bottom=pBottom/photoRadioH;
        referenceLine.setRect(top, left, right, (float) (bottom));
        numArea = (int)Math.floor(yLeft) + "," + (int)Math.floor(yTop) + "," +(int)Math.ceil(xDes) + "," + (int)Math.ceil(yDes);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.ib_close, R.id.ib_photo, R.id.ib_light, R.id.ib_cancel, R.id.ib_save, R.id.tv_analysis, R.id.tv_estimate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_close:
                finish();
                break;
            case R.id.ib_photo:
                takePhoto();
                break;
            case R.id.ib_light:
                takeLight();
                break;
            case R.id.ib_cancel:
                cancel();
                break;
            case R.id.ib_save:
                confirm();
                break;
            case R.id.tv_analysis:
                transToAnalysis();
                break;
            case R.id.tv_estimate:
                //预估
                transToEstimate();
                break;
        }
    }

    private void transToEstimate() {
        Intent intent = new Intent(MyApplication.getContext(), EstimateActivity.class);
        intent.putExtra("longId", id);
        startActivity(intent);
        finish();


    }

    private void transToAnalysis() {

        if (!savePic()) {
            ToastUtil.showShort(this, "保存失败。。");
            return;
        }
        //已经设置过 ，从解析activity跳转回来的，
       /* if(src.equals("ARA")){
            backAnalysis();
        }*/
        DownloadTaskBeanRESPONSEXMLBean data = setLBean(DataSupport.find(DownloadTaskBeanRESPONSEXMLBean.class, id));
        data.update(id);
        Uploading upload = new Uploading();
        upload.setId(id);
        upload.setNumArea(numArea);
        upload.setImageAddr(imagePath);
        upload.setCustomName(data.getCustomerName());
        upload.setAddress(data.getAreaName());
        upload.setTaskId(data.getTaskID());
        upload.setTaskOperateData(data.getTaskOperateDate());
        upload.setWaterCustomerId(data.getMeterCode());
        upload.setUserId(data.getCustomerID());
        Intent intent = new Intent(this, AnalysisResultActivity_HP.class);
        intent.putExtra(Uploading.UPLOADNAME, upload);
        startActivity(intent);
        finish();

    }
}
