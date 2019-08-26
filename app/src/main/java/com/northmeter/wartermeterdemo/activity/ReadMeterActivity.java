package com.northmeter.wartermeterdemo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.ImageUtil;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.view.CircularProgressButton;
import com.zhy.autolayout.AutoLayoutActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * created by lht on 2016/5/9
 */
public class ReadMeterActivity extends AutoLayoutActivity {

    @BindView(R.id.tv_userNum_readMeter)
    TextView tvUserNumReadMeter;
    @BindView(R.id.tv_meterNum_readMeter)
    TextView tvMeterNumReadMeter;
    @BindView(R.id.tv_userName_ReadMeter)
    TextView tvUserNameReadMeter;
    @BindView(R.id.tv_forms_readMeter)
    TextView tvFormsReadMeter;
    @BindView(R.id.tv_area_readMeter)
    TextView tvAreaReadMeter;
    @BindView(R.id.iv_takePhoto_readMeter)
    ImageButton ivTakePhotoReadMeter;
    @BindView(R.id.btn_handInput_readMeter)
    CircularProgressButton btnHandInputReadMeter;
    @BindView(R.id.iv_show_readMeter)
    ImageView iv_show;
    @BindView(R.id.relative_takePhoto_readMeter)
    RelativeLayout rl;
    @BindView(R.id.tv_title_bar_right_text)
    TextView tv_save;
    private boolean inOrAnly = true;
    private String PicAbPath;
    private Bitmap saveBitmap;
    private Dialog dialog;
    private String imgName;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_re_meter);
        unbinder = ButterKnife.bind(this);
     //   StatusBarCompat.compat(this);
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("抄表");
        tv_save.setVisibility(View.GONE);


    }

    @OnClick({R.id.iv_takePhoto_readMeter, R.id.btn_handInput_readMeter, R.id.iv_show_readMeter,R.id.tv_title_bar_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show_readMeter:
                createDialog();
                break;
            case R.id.iv_takePhoto_readMeter:
                transterPhoto();
                break;
            case R.id.btn_handInput_readMeter:
                if (!inOrAnly) {
                    Intent intent1 = new Intent(this, AnalysisResultActivity_Pop.class);
                    intent1.putExtra("path", imgName);
                   // Log.i("LHT","PicAbPath "+imgName);
                    startActivity(intent1);
                    finish();
                } else {
                    ToastUtil.showShort(this, "对不起，您没有该项的权限");
                }
                break;
            case R.id.tv_title_bar_right_text:
                ToastUtil.showShort(this, "保存");
                break;
        }
    }

    private void transterPhoto() {
        Intent intent = new Intent(this, TakePhoteActivity_Pop.class);
        intent.putExtra(Constants.TAKEPHOTOSRC, "RM");
        startActivityForResult(intent, Constants.PHOTOGRAPH);
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.view_dialog_pic, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_dialog_pic);
        iv.setImageBitmap(saveBitmap);
        dialog = new Dialog(this, R.style.dialogWindowAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(R.color.vifrification);
        setParams(attributes);
        dialog.show();
        dialog.setCancelable(true);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        view.findViewById(R.id.btn_photoAgain_dialog_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transterPhoto();
                dialog.dismiss();
            }
        });
    }

    private void saveMeter() {
        if (PicAbPath != null) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(saveBitmap!=null){
            saveBitmap.recycle();
            saveBitmap=null;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Constants.PHOTOGRAPH && resultCode == Activity.RESULT_OK) {
            imgName = intent.getStringExtra("path");
            PicAbPath = Constants.SAVEPIC + imgName;
        }
        saveBitmap = ImageUtil.getSmallBitmap(PicAbPath, 300, 400);
        doResult();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void doResult() {
        if (saveBitmap == null) {
            return;
        }
        rl.setVisibility(View.INVISIBLE);
        tv_save.setVisibility(View.VISIBLE);
        tv_save.setText("保存");
        iv_show.setVisibility(View.VISIBLE);
       iv_show.setImageBitmap(saveBitmap);
        btnHandInputReadMeter.setProgress(100);
        inOrAnly = false;
    }

    public void setParams(WindowManager.LayoutParams lay) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels;
        lay.width = dm.widthPixels;
    }


}
