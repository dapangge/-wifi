package com.a520it.wifidemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.a520it.wifidemo.util.WiFiHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button mBtnStart;
    private Button mBtnStop;
    private EditText mEdName;
    private EditText mEdPassword;
    private Button mBtnConnect;
    private Button mBtnUnconnect;
    private Button mBtnSearch;
    private ListView mLsWifi;
    private Button mBtnTextNet;
    private ImageView mImgLogo;
    private WiFiHelper mWiFiHelper;
    private WifiStateReceiver wifiReceiver;
    private WifiAdapter mWifiAdapter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mWiFiHelper = new WiFiHelper(this);
        registWifiReceiver();

    }

    private void registWifiReceiver() {
        wifiReceiver=new WifiStateReceiver(this);
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wifiReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistWifiReceiver();
    }

    private void unregistWifiReceiver() {
        unregisterReceiver(wifiReceiver);
    }

    private void initView() {
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        mEdName = (EditText) findViewById(R.id.ed_name);
        mEdPassword = (EditText) findViewById(R.id.ed_password);
        mBtnConnect = (Button) findViewById(R.id.btn_connect);
        mBtnUnconnect = (Button) findViewById(R.id.btn_unconnect);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mLsWifi = (ListView) findViewById(R.id.ls_wifi);
        mLsWifi.setOnItemClickListener(this);
        mBtnTextNet = (Button) findViewById(R.id.btn_text_net);
        mImgLogo = (ImageView) findViewById(R.id.img_logo);

        mBtnStart.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
        mBtnConnect.setOnClickListener(this);
        mBtnUnconnect.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        mBtnTextNet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mWiFiHelper.startWifi();
                break;
            case R.id.btn_stop:
                mWiFiHelper.stopWifi();
                break;
            case R.id.btn_connect:
                String ssid = mEdName.getText().toString();
                String pwd = mEdPassword.getText().toString();
                mWiFiHelper.connectWifi(ssid,pwd);
                break;
            case R.id.btn_unconnect:
                mWiFiHelper.disconnectWifi();
                break;
            case R.id.btn_search:
                List<ScanResult> scanResult = mWiFiHelper.search();
                mWifiAdapter = new WifiAdapter(this, scanResult);
                mLsWifi.setAdapter(mWifiAdapter);
                break;
            case R.id.btn_text_net:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult bean = mWifiAdapter.getItem(position);
        showInputDailog("请添加Wifi",bean.SSID);
    }

    public void showInputDailog(String tip, final String ssid) {
        //准备好一个布局
        View view = View.inflate(this, R.layout.input_dailog, null);
        TextView tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        final EditText ed_ssid = (EditText) view.findViewById(R.id.ed_ssid);
        final EditText ed_password = (EditText) view.findViewById(R.id.ed_password);
        ed_ssid.setText(ssid);
        ed_ssid.setSelection(ssid.length());
        tv_tip.setText(tip);

        alertDialog = new AlertDialog.Builder(this)
                //添加一个布局
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //开始连接
                        String password=ed_password.getText().toString().trim();
                        if(!TextUtils.isEmpty(password)){
                            /**连接这个wifi*/
                            mWiFiHelper.connectWifi(ssid,password);
                        }else{

                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.cancel();
                    }
                }).create();

        alertDialog.show();
    }


}
