package com.northmeter.wartermeterdemo.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.adapter.TaskBuildUserTakePhotoAapter;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.bean.DownloadTaskBeanRESPONSEXMLBean;
import com.northmeter.wartermeterdemo.presenter.TaskBuildUserPresenter;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.ImageUtil;
import com.northmeter.wartermeterdemo.utils.LoggerUtil;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.OnItemClickListener;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.view.CannotSlideRecyclerview;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * @author zz
 * @time 2016/5/11 15:39
 * @des 立户任务
 */
public class TaskBuildUserActivity extends AutoLayoutActivity implements ITaskBuildUserActivity {

    private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
    private static final int PHOTO_WITH_AGAIN_CAMERA = 38;// 重新拍摄照片
    private static final int REQUESTCODE = 8;

    @BindView(R.id.tv_task_build_user_curstomer_id)
    TextView mTvCurstomerId;
    @BindView(R.id.et_task_builder_user_customer_phone)
    EditText mEtCustomerPhone;
    @BindView(R.id.et_task_builder_user_customer_name)
    EditText mEtCustomerName;
    @BindView(R.id.et_task_builder_user_customer_address)
    EditText mEtCustomerAddress;
    @BindView(R.id.et_task_builder_user_meter_number)
    EditText mEtMeterNumber;
    @BindView(R.id.btn_task_build_user_upload)
    CircularProgressButton mBtnUpload;
    @BindView(R.id.tv_title_bar_right_text)
    TextView mRightSave;
    @BindView(R.id.rv_build_user_photo)
    RecyclerView mRvPhoto;
    @BindView(R.id.ibtn_build_user_take_photo)
    ImageButton mIbtnTakePhoto;
    private String mTaskID;

    private TaskBuildUserPresenter mBuildUserPresenter;
    private String mCustomerID;
    private ArrayList<String> mPhotosPath;
    private int mClickPosition = -1;
    private TaskBuildUserTakePhotoAapter mTakePhotoAapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_task_build_user);
        unbinder = ButterKnife.bind(this);
        // StatusBarCompat.compat(this);
        initData();
    }

    private void initData() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(TaskBuildUserActivity.this, getWindow().getDecorView());
        baseTitleBar.setCenterText("立户任务");
        mBtnUpload.setIndeterminateProgressMode(true);

        mTaskID = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_ID);
        mCustomerID = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_CUSTOMER_ID);
        String customerAddress = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_ADDRESS);
        String customerName = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_NAME);
        String meterCode = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_METER_CODE);
        String phoneNumber = getIntent().getStringExtra(Constants.PUT_EXTRA_TASK_PHONE_NUMBER);
        mTvCurstomerId.setText(mCustomerID);
        mEtCustomerPhone.setText(phoneNumber);
        mEtCustomerName.setText(customerName);
        mEtCustomerAddress.setText(customerAddress);
        mEtMeterNumber.setText(meterCode);

        //创建集合保存图片路径
        mPhotosPath = new ArrayList<>();

        //初始化presenter
        mBuildUserPresenter = new TaskBuildUserPresenter(this, mTaskID);
        mBuildUserPresenter.searchDBPicture(mTaskID);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mTakePhotoAapter != null) {
            mTakePhotoAapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.tv_title_bar_right_text, R.id.btn_task_build_user_upload, R.id.ibtn_build_user_take_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_bar_right_text:
                //保存到数据库
                saveMessage();
                break;
            case R.id.btn_task_build_user_upload:
                //调用上传
                mBuildUserPresenter.uploadTask();
                break;
            case R.id.ibtn_build_user_take_photo:
                //检测申请权限成功才开始拍照
                checkCamera();

                break;
        }
    }

    /**
     * 检查相机权限
     */
    private void checkCamera() {
        //检查权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                new AlertDialog.Builder(TaskBuildUserActivity.this)
                        .setMessage("需要开启 照相和存储 权限才能使用此功能")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            } else {

                //申请权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUESTCODE);
            }
        } else {
            //已经拥有权限进行拍照
            takePhoto();
        }
    }

    /**
     * 开始拍照
     */
    private void takePhoto() {
        if (mPhotosPath.size() >= 4) {
            ToastUtil.showShort(MyApplication.getContext(), "最多只能拍四张照片");
            return;
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PHOTO_WITH_CAMERA);
        }
    }

    /**
     * 获取图片路径 响应startActivityForResult
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {  //返回成功
            switch (requestCode) {
                case PHOTO_WITH_CAMERA: {
                    //拍照获取图片
                    String status = Environment.getExternalStorageState();
                    //判断是否有SD卡
                    if (status.equals(Environment.MEDIA_MOUNTED)) {

                        String photoFile = processPicture();
//                        Uri uri = Uri.fromFile(new File(photoFile));
                        //集合添加uri地址
                        mPhotosPath.add(photoFile);

                        //设置recyclerview
                        setRecyclerview();

                    } else {
                        Toast.makeText(MyApplication.getContext(), "没有找到SD卡", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
                case PHOTO_WITH_AGAIN_CAMERA:
                    //重新拍照
                    //对图片进行处理
                    String photoFile = processPicture();
                    if (mClickPosition != -1) {
                        mPhotosPath.set(mClickPosition, photoFile);
                    }

                    break;
            }
        }
    }

    /**
     * 图片处理
     * @return
     */
    @NonNull
    private String processPicture() {
        //缩放图片的比例
        Bitmap smallBitmap = ImageUtil.
                getSmallBitmap(Environment.getExternalStorageDirectory() + "/image.jpg", 720, 1280);
        //在图片上写上时间
        Bitmap bitmap = ImageUtil.setMbitmap(smallBitmap);
        //保存图片
        String imageName = ImageUtil.createImageName(mCustomerID);
        ImageUtil.saveImageQuality(bitmap, Constants.SAVEPIC, imageName, 70);
        //图片路径转成uri(fresco要用uri路径，picasso用String路径就可以了)
        return Constants.SAVEPIC + imageName;
    }

    /**
     * 设置recyclerview
     */
    private void setRecyclerview() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, mPhotosPath.size());
        mRvPhoto.setLayoutManager(gridLayoutManager);
        mTakePhotoAapter = new TaskBuildUserTakePhotoAapter(this, mPhotosPath);
        mRvPhoto.setAdapter(mTakePhotoAapter);

        //条目点击事件
        mTakePhotoAapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                mClickPosition = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(TaskBuildUserActivity.this);
                String photoPath = mPhotosPath.get(position);
                ImageView imageView = new ImageView(MyApplication.getContext());

                //在alertDialog上显示图片
                Picasso.with(MyApplication.getContext())
                        .load(new File(photoPath))
                        .into(imageView);

                builder.setView(imageView);

                builder.setPositiveButton("重拍", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //重新拍照片
                        againTakePhoto();
                    }
                }).setNegativeButton("取消", null);

                final AlertDialog dialog = builder.show();
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击图片隐藏dialog
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    /**
     * 重新拍照片
     */
    private void againTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_WITH_AGAIN_CAMERA);
    }


    /**
     * 保存信息到数据库
     */
    private void saveMessage() {
        Editable customerPhone = mEtCustomerPhone.getText();
        Editable customerName = mEtCustomerName.getText();
        Editable customerAddress = mEtCustomerAddress.getText();
        Editable meterNumber = mEtMeterNumber.getText();

        if (customerPhone.length() < 11) {
            ToastUtil.showShort(MyApplication.getContext(), "手机长度不正确");
            return;
        }

        DownloadTaskBeanRESPONSEXMLBean bean = new DownloadTaskBeanRESPONSEXMLBean();

        //修改数据并保存到数据库
        bean.setCustomerTel(customerPhone.toString());
        bean.setCustomerName(customerName.toString());
        bean.setCustomerAddress(customerAddress.toString());
        bean.setMeterCode(meterNumber.toString());
        bean.setTaskOperateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        bean.setIsFinish(Constants.ALREADY_FINISH);

        //保存图片
        String photoPaths = "";
        for (int temp = 0; temp < mPhotosPath.size(); temp++) {
            if (temp == mPhotosPath.size() - 1) {
                photoPaths += mPhotosPath.get(temp);
            } else {
                photoPaths += mPhotosPath.get(temp) + ",";
            }
        }
        bean.setIMAGEADDRESS(photoPaths);

        int updateAll = bean.updateAll("taskid = ?", mTaskID);
        if (0 == updateAll) {
            ToastUtil.showShort(MyApplication.getContext(), "保存失败");
        } else {
            ToastUtil.showShort(MyApplication.getContext(), "保存成功");
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
    public void toastMsg(String toastStr) {
        ToastUtil.showShort(MyApplication.getContext(), toastStr);
    }

    @Override
    public void showDialogProgress(int progress) {
        mBtnUpload.setProgress(progress);
    }

    @Override
    public void showDialogProgress0() {
        if (mBtnUpload.getProgress() == -1) {
            mBtnUpload.setProgress(0);
        }
    }

    @Override
    public void finishAvtivity() {
        mPhotosPath.clear();
        finish();
    }

    @Override
    public void showDBPicture(String[] split) {
        for (int i = 0; i < split.length; i++) {
            //添加数据库的照片
            mPhotosPath.add(split[i]);
        }

        setRecyclerview();
    }

}
