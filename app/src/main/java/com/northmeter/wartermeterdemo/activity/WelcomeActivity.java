package com.northmeter.wartermeterdemo.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.adapter.MyViewPageAdapter;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */
public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.vP_welcome)
    ViewPager vPWelcome;
    private Unbinder unbinder;
    private MyViewPageAdapter adapter;
    private List<View> pList;
    private int pic[]=new int[]{R.drawable.welcome1,R.drawable.welcome2,R.drawable.welcome3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        unbinder = ButterKnife.bind(this);
        initView();
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    private void initView() {
        pList=new ArrayList<>();
        for (int i=0;i<pic.length;i++){
            ImageView iv=new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //减少图片占用内存
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            InputStream is = getResources().openRawResource(pic[i]);
            Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
            iv.setImageBitmap(bm);
            pList.add(iv);
        }
        adapter=new MyViewPageAdapter(pList,this);
        vPWelcome.setAdapter(adapter);
        adapter.setmyViewPagerAdapterListener(new MyViewPageAdapter.myViewPagerAapterListener() {
            @Override
            public void setActivityFinish(int i) {
                if(i==2){
                    startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
