package com.a520it.wifidemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * Created by lean on 2017/6/25.
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
//            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
//                Toast.makeText(context, "wifi连接成功", Toast.LENGTH_SHORT).show();
//            } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
//                Toast.makeText(context, "wifi连接失败", Toast.LENGTH_SHORT).show();
//            }
        } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            /**Wifi状态改变*/
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_ENABLED) {/**wifi可用*/
                Toast.makeText(context, "wifi打开成功", Toast.LENGTH_SHORT).show();
            } else if (wifistate == WifiManager.WIFI_STATE_DISABLED) {/**wifi不可用*/
                Toast.makeText(context, "wifi关闭成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
