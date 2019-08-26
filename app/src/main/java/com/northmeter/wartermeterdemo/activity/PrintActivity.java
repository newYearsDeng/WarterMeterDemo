package com.northmeter.wartermeterdemo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.northmeter.wartermeterdemo.R;
import com.northmeter.wartermeterdemo.base.BaseTitleBar;
import com.northmeter.wartermeterdemo.utils.Constants;
import com.northmeter.wartermeterdemo.utils.MyApplication;
import com.northmeter.wartermeterdemo.utils.SharedPreferencesUtils;
import com.northmeter.wartermeterdemo.utils.StatusBarCompat;
import com.wpx.WPXMain;
import com.wpx.util.WPXUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PrintActivity extends AutoLayoutActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, WPXMain.SearchCallBack {
    @BindView(R.id.tv_title_bar_right_text)
    TextView tvTitleBarRightText;
    @BindView(R.id.tb_print)
    ToggleButton tbPrint;
    @BindView(R.id.tv_connect)
    TextView tvConnect;
    @BindView(R.id.tvShow_print)
    TextView tvShowPrint;
    @BindView(R.id.lv_bond_print)
    ListView lvBondPrint;
    private BluetoothAdapter btAdpter = BluetoothAdapter.getDefaultAdapter();
    private ArrayList<BluetoothDevice> bondDevices = new ArrayList<>();// 用于存放已配对蓝牙设备
    private final String[] from = new String[]{"name", "address"};
    private final int[] to = new int[]{R.id.tv_device_name,
            R.id.tv_device_address};
    private SimpleAdapter bondAdapter;
    private Unbinder unbinder;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, Color.parseColor("#01B2ED"));
        setContentView(R.layout.activity_print);
        unbinder = ButterKnife.bind(this);
        initView();
        initEvent();
        WPXMain.addSerachDeviceCallBack(this);
    }

    private void resetBondDevice() {
        final Set<BluetoothDevice> set = WPXMain.getBondedDevices();
        Log.i("LHT", "setSize " + set.size());
        loadBondDevices(set);
    }

    private void loadBondDevices(Set<BluetoothDevice> devices) {
        bondDevices.clear();
        bondDevices.addAll(devices);
        bondAdapter = getSimpleAdapter(bondDevices);
        lvBondPrint.setAdapter(bondAdapter);
    }

    private SimpleAdapter getSimpleAdapter(ArrayList<BluetoothDevice> devices) {

        final List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (final BluetoothDevice device : devices) {
            final Map<String, String> map = new HashMap<String, String>();
            map.put("name", device.getName());
            map.put("address", device.getAddress());
            data.add(map);
        }
        final SimpleAdapter adapter = new SimpleAdapter(
                this, data, R.layout.layout_device_item,
                from, to);
        return adapter;
    }


    @OnClick({R.id.tv_title_bar_right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_bar_right_text:
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                finish();
                break;
        }
    }

    private void initEvent() {
        tbPrint.setOnCheckedChangeListener(this);
        lvBondPrint.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = bondDevices.get(position);
        tvConnect.setText(device.getName());
        SharedPreferencesUtils.setParam(MyApplication.getContext(), Constants.SECLETBLUETOOTHMAC, device.getAddress());

    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!WPXMain.isBluetoothEnabled()) {
            //蓝牙关闭的情况
            Log.i("LHT", "蓝牙关闭的情况");
            openBluetooth();
        } else {
            Log.i("LHT", "2222222222222");
            //蓝牙打开的情况
            closeBluetooth();
        }

    }
    private void closeBluetooth() {
        WPXMain.closeBluetooth();
    }
    private void openBluetooth() {
        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(enableBtIntent);
    }
    private void initView() {
        BaseTitleBar baseTitleBar = new BaseTitleBar(this, getWindow().getDecorView());
        baseTitleBar.setCenterText("蓝牙设置");
        tvTitleBarRightText.setText("添加");
        String name = (String) SharedPreferencesUtils.getParam(MyApplication.getContext(), Constants.SECLETBLUETOOTHNAME, "无设备");
        tvConnect.setText(name);
        // resetBondDevice();
        if (btAdpter.isEnabled()) {
            tbPrint.setButtonDrawable(R.drawable.open);
            resetBondDevice();
        }
    }
    @Override
    public void startSearch() {
    }
    @Override
    public void searching(BluetoothDevice bluetoothDevice) {

    }
    @Override
    public void stopSearch() {

    }
    @Override
    public void onStateChange() {
        if (btAdpter.getState() == BluetoothAdapter.STATE_ON) {
            Log.i("LHT", "on");
            tbPrint.setButtonDrawable(R.drawable.open);
            resetBondDevice();
            lvBondPrint.setVisibility(View.VISIBLE);
        } else if (btAdpter.getState() == BluetoothAdapter.STATE_OFF) {
            Log.i("LHT", "off");
            tbPrint.setButtonDrawable(R.drawable.close);
            lvBondPrint.setVisibility(View.GONE);
        }
    }
}

