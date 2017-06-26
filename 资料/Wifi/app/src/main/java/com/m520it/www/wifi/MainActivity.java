package com.m520it.www.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.m520it.www.wifi.R.id.ed_name;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_start)
    Button mBtnStart;
    @Bind(R.id.btn_stop)
    Button mBtnStop;
    @Bind(ed_name)
    EditText mEdName;
    @Bind(R.id.ed_password)
    EditText mEdPassword;
    @Bind(R.id.btn_connect)
    Button mBtnConnect;
    @Bind(R.id.btn_unconnect)
    Button mBtnUnconnect;
    @Bind(R.id.btn_search)
    Button mBtnSearch;
    @Bind(R.id.ls_wifi)
    ListView mLsWifi;
    @Bind(R.id.btn_text_net)
    Button mBtnTextNet;
    @Bind(R.id.img_logo)
    ImageView mImgLogo;
    private WifiHelper wifiHelper;
    private WifiStateReceiver wifiReceiver;
    private WifiAdapter wifiAdapter;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         wifiHelper = new WifiHelper(this);

        /**注册wifi状态改变的监听器*/
        wifiReceiver=new WifiStateReceiver(this);
        IntentFilter filter=new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(wifiReceiver,filter);

        mLsWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<ScanResult> dates = wifiAdapter.getDates();
                ScanResult scanResult = dates.get(position);
                showInputDailog("连接wifi",scanResult.SSID);
            }
        });

    }

    /**
     * 踏出一个对话框
     * @param tip  提示信息
     * @param ssid wifi名称
     */
    public void showInputDailog(String tip, final String ssid) {
        //1.准备好一个布局
        View view = View.inflate(this, R.layout.input_dailog, null);
        TextView tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        final EditText ed_ssid = (EditText) view.findViewById(R.id.ed_ssid);
        final EditText ed_password = (EditText) view.findViewById(R.id.ed_password);
        ed_ssid.setText(ssid);
        /***/
        ed_ssid.setSelection(ssid.length());
        tv_tip.setText(tip);

        //2.创建对话框
        alertDialog = new AlertDialog.Builder(this)
                //添加一个布局
                .setView(view)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**连接wifi*/
                        String password = ed_password.getText().toString().trim();
                        if(TextUtils.isEmpty(password)){
                           Toast.makeText(MainActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            wifiHelper.addNetWordWPA(ssid, password);
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        alertDialog.cancel();
                    }
                })
                .create();

        //3.显示对话框
        alertDialog.show();

    }


    @OnClick({R.id.btn_start, R.id.btn_stop, R.id.btn_connect, R.id.btn_unconnect, R.id.btn_search, R.id.btn_text_net})
    public void onClick(View view) {
        switch (view.getId()) {
            /**打开wifi*/
            case R.id.btn_start:
                wifiHelper.startWifi();
                break;
            /***
             * 关闭wifi
             */
            case R.id.btn_stop:
                wifiHelper.stopWifi();
                break;
            /**
             * 添加一个wifi并连接该wifi
             */
            case R.id.btn_connect:
                String wifiName = mEdName.getText().toString().trim();
                String wifiPassword = mEdPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(wifiName)&& !TextUtils.isEmpty(wifiPassword)){
                    wifiHelper.addNetWordWPA(wifiName,wifiPassword);
                }else{
                    Toast.makeText(MainActivity.this,"wifi名和密码不能为空",Toast.LENGTH_SHORT).show();
                }

                break;
            /**
             * 断开当前连接的wifi
             */
            case R.id.btn_unconnect:
                wifiHelper.disConnectWifi();
                break;
            /**
             * 搜索附近wifi
             */
            case R.id.btn_search:
                List<ScanResult> scanResults = wifiHelper.startScanWIfi();
                if(wifiAdapter==null) {
                    wifiAdapter = new WifiAdapter(this, scanResults);
                    mLsWifi.setAdapter(wifiAdapter);
                }else{
                    wifiAdapter.notifyDataSetChanged();
                }
                break;
            /**
             * 测试网络
             */
            case R.id.btn_text_net:
                ImageUtils.disPlayImageNoCache(this, "http://avatar.csdn.net/A/2/3/1_u012987546.jpg", mImgLogo, new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unregisterReceiver(wifiReceiver);
    }


    /**
     *  Wifi开关,信号,状态改变监听
     */
    public class WifiStateReceiver extends BroadcastReceiver {
        private static final String TAG = "WifiStateReceiver";
        Context context;

        public WifiStateReceiver(Context context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {



            } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

                /**网络状态改变*/
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    Toast.makeText(MainActivity.this,"wifi连接成功",Toast.LENGTH_SHORT).show();
                } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                    Toast.makeText(MainActivity.this,"wifi连接失败",Toast.LENGTH_SHORT).show();
                }

            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                /**Wifi状态改变*/
//                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
//                if (wifistate == WifiManager.WIFI_STATE_ENABLED) {/**wifi可用*/
//
//                    Toast.makeText(MainActivity.this,"wifi打开成功",Toast.LENGTH_SHORT).show();
//
//                } else if (wifistate == WifiManager.WIFI_STATE_DISABLED) {/**wifi不可用*/
//
//                    Toast.makeText(MainActivity.this,"wifi关闭成功",Toast.LENGTH_SHORT).show();
//                }
            }
        }

    }

}
