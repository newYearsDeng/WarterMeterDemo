package com.northmeter.wartermeterdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.bean.Uploading;
import com.northmeter.wartermeterdemo.utils.CarmeraUtils;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.GetPhoneInfo;
import com.northmeter.wartermeterdemo.utils.ImageUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.PictureHelper;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.ThreadUtils;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.view.CameraPreview;
import com.northmeter.wartermeterdemo.view.FocusView;
import com.northmeter.wartermeterdemo.view.ReferenceLine;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TakePhoteActivity_Pop extends AutoLayoutActivity implements CameraPreview.OnCameraStatusListener, CameraPreview.OnCameraPictureSizeListener {
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
    @BindView(R.id.cropper_layout)
    RelativeLayout cropper_layout;
    @BindView(R.id.take_photo_layout)
    RelativeLayout mTakePhotoLayout;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.rl_afterPhoto_controller)
    RelativeLayout after_controller;
    @BindView(R.id.ib_xiangce)
    ImageButton ib_xinagce;
    private int CAMERA_CODE = 2;
    //图片的相对路径
    private String imagePath;
    private Bitmap showBitmap;
    private boolean isClose;
    private String src;
    private Intent intent;
    private String userId;
    private boolean showSoftKeys;
    private int navigationBarHeight;
    //屏幕的宽高
    private float screenHeight, screenWidth;
    private float left, right, top;
    private String numArea;
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
        setContentView(R.layout.activity_take_phote);

        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        unbinder = ButterKnife.bind(this);
        initSetting();
        initView();
        initData();
    }

    private void initData() {
        //屏幕中的坐标位置
        left = (float) (0.3 * screenWidth / 2);
        top = (float) (screenHeight / 2 - 50 - 0.29 * screenWidth);
        right = (float) (screenWidth - left);
        //调转的来源
        src = intent.getStringExtra(Constants.TAKEPHOTOSRC);
    }

    private void initSetting() {
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
        mCameraPreview.setFocusView(viewFocus);
        mCameraPreview.setOnCameraStatusListener(this);
        mCameraPreview.setOnCameraGetPictureSizeListner(this);
        //rl_photo_controller = (RelativeLayout) findViewById(R.id.rl_photo_controller);
      //  ib_xinagce.setVisibility(View.VISIBLE);

        //获取屏幕的宽高
        screenWidth = CarmeraUtils.getScreenWH(getApplicationContext()).widthPixels;
        screenHeight = CarmeraUtils.getScreenWH(getApplicationContext()).heightPixels;
        showSoftKeys = GetPhoneInfo.hasSoftKeys(getWindowManager());
        navigationBarHeight = GetPhoneInfo.getNavigationBarHeight(this);
        if (showSoftKeys) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_photo_controller.getLayoutParams();
            layoutParams.bottomMargin = navigationBarHeight;
            rl_photo_controller.setLayoutParams(layoutParams);
           // screenHeight = screenHeight + navigationBarHeight;
        }

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
        if (!showBitmap.isRecycled()) {
            showBitmap.recycle();
            showBitmap = null;
        }
        showTakePhotoLayout();
    }

    public void confirm() {
        isSave = savePic();
        if (isSave) {
            if (TextUtils.isEmpty(src)) {
                return;
            }
            if (src.equals("RM") || src.equals("ARA")) {
                Intent intent = new Intent();
                intent.putExtra("path", imagePath);
                setResult(RESULT_OK, intent);
                //来自popularActivity
            } else if (src.equals("PA")) {
                Intent intent = new Intent(this, AnalysisResultActivity_Pop.class);
                Uploading uploading = new Uploading();
                uploading.setImageAddr(imagePath);
                uploading.setNumArea(numArea);
                intent.putExtra(Uploading.UPLOADNAME, uploading);
                startActivity(intent);
            }
            this.finish();

        } else {

            ToastUtil.showShort(MyApplication.getContext(), "保存失败。。");


        }
    }

    private boolean savePic() {
        if (showBitmap.isRecycled()) {
            return false;
        }

        if (TextUtils.isEmpty(userId)) {
            userId = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, "-1");
        }
        imagePath = ImageUtil.createImageName(userId);
        final Bitmap savePic = ImageUtil.setMbitmap(showBitmap);
        return ImageUtil.saveImage(savePic, Constants.SAVEPIC, imagePath);
    }

    @Override
    public void onCameraStopped(final byte[] data) {
        ThreadUtils.runInWorkThread(new Runnable() {
            @Override
            public void run() {
                //final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                // TODO: 2017/1/13  解决oom 
                Bitmap bitmap=ImageUtil.byteToBitmap(data);
                showBitmap = ImageUtil.zoomImage(bitmap, 800);
                //旋转图片
                if (showBitmap.getHeight() < showBitmap.getWidth())
                    showBitmap = ImageUtil.rotate(showBitmap, 90);

                ThreadUtils.runInUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCropperLayout(showBitmap);
                    }
                });

            }
        });
    }
    private void showTakePhotoLayout() {
        cropper_layout.setVisibility(View.GONE);
        mTakePhotoLayout.setVisibility(View.VISIBLE);
        mCameraPreview.start();
    }

    public void showCropperLayout(Bitmap showBitmap) {
        mTakePhotoLayout.setVisibility(View.GONE);
        cropper_layout.setVisibility(View.VISIBLE);
        mImageView.setImageBitmap(showBitmap);
        if (showSoftKeys) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) after_controller.getLayoutParams();
            layoutParams.bottomMargin = navigationBarHeight;
            after_controller.setLayoutParams(layoutParams);
        }

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

        //2
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
        referenceLine.setRect((float) (top * 0.9), left, right, (float) (bottom1 *0.9));
        //referenceLine.setRect((float) (top), left, right, (float) (bottom1));
         numArea = (int) Math.floor(left1) + "," + (int) Math.floor(top1) + "," + (int) Math.ceil(xDes) + "," + (int) Math.ceil(yDes);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.ib_close, R.id.ib_photo, R.id.ib_light, R.id.ib_cancel, R.id.ib_save, R.id.ib_xiangce})
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
            case R.id.ib_xiangce:
                startAlbumForResult();
                break;
        }
    }

    private void startAlbumForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 250);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 250) {
            if (data != null) {
                Uri uri = data.getData();
                String pickPath = PictureHelper.getPath(this, uri);  // 获取图片路径的方法调用
                System.out.println("图片路径 -->> " + pickPath);
                showBitmap = BitmapFactory.decodeFile(pickPath);
                showCropperLayout(showBitmap);
            }

        }
    }
}
