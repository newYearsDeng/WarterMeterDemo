package com.northmeter.wartermeterdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.northmeter.wartermeterdemo.utils.UpdateManager;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PopularActivity extends AutoLayoutActivity {
    public static PopularActivity instance = null;
    @BindView(R.id.ib_takePhoto_popular)
    ImageButton ibTakePhotoPopular;
    @BindView(R.id.ib_search_popular)
    ImageButton ibSearchPopular;
    @BindView(R.id.ib_search_setting)
    View contentView;
    @BindView(R.id.rl_popular_bg)
    RelativeLayout rlPopularBackground;
    //标记是否退出
    private static boolean isExit = false;
    private Handler handler = new MyHandler(this);
    private  UpdateManager manager;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_popular);
        unbinder = ButterKnife.bind(this);
        instance = this;

        //减少图片占用内存
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = getResources().openRawResource(+R.drawable.homepage_bg);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        rlPopularBackground.setBackgroundDrawable(bd);
        SharedPreferencesUtils.setParam(MyApplication.getContext(), "isOnLine", true);


        //设置标签
        Set<String> set = new LinkedHashSet<>();
        String id = (String) SharedPreferencesUtils.getParam(
                MyApplication.getContext(), Constants.SP_GET_CUSTOMER_ID, "");
        if ("0000000000000001".equals(id)) {
            set.add("a");
        } else if ("0000000000000002".equals(id)) {
            set.add("b");
        } else if ("0000000000000003".equals(id)) {
            set.add("c");
        }
//        JPushInterface.setTags(MyApplication.getContext(), set, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                LoggerUtil.d(i + "----" + s + "----" + set);
//            }
//        });



        manager = new UpdateManager(PopularActivity.this,this);
        // 检查软件更新
        manager.connectServer();
    }

    @OnClick({R.id.ib_takePhoto_popular, R.id.ib_search_popular, R.id.ib_search_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_takePhoto_popular://抄表
                Intent intent = new Intent(this, TakePhoteActivity_Pop.class);
                intent.putExtra(Constants.TAKEPHOTOSRC,"PA");
                startActivity(intent);
                break;
            case R.id.ib_search_popular:
                startActivity(new Intent(this, SearchMessageActivity.class));
               // overridePendingTransition(R.anim.scale_translate,
               //         R.anim.my_alpha_action);
                break;
            case R.id.ib_search_setting:
                Intent intent1 = new Intent(this, SettingActivity.class);
                intent1.putExtra("fromSrc","PA");
                startActivity(intent1);
               // overridePendingTransition(R.anim.scale_translate,
                //        R.anim.my_alpha_action);
                break;
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
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (handler != null) {
            handler = null;
        }
    }

    private static class MyHandler extends Handler {
        WeakReference<PopularActivity> popularActivity;

        MyHandler(PopularActivity popularActivity1) {
            popularActivity = new WeakReference<PopularActivity>(popularActivity1);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    }
}
