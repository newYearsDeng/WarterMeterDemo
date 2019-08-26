package com.northmeter.wartermeterdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.northmeter.wartermeterdemo.utils.ToastUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * created by lht on 2016/5/9
 */
public class PayResultActivity extends AutoLayoutActivity {
    @BindView(R.id.btn_print_pay_result)
    Button btnPrintPayResult;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_pay_result);
        unbinder = ButterKnife.bind(this);

      //  StatusBarCompat.compat(this);
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.hideRightSave();
        baseTitleBar.setCenterText("抄表");
    }

    @OnClick({ R.id.btn_print_pay_result})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_print_pay_result:
                ToastUtil.showShort(this,"打印支付凭证");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
