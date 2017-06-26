package com.a520it.wifidemo.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lean on 2017/6/25.
 */

public class WiFiHelper {

    private final WifiManager mWifiManager;

    public WiFiHelper(Context c) {
        mWifiManager= (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
    }

    public void startWifi(){
        if (!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }

    public void stopWifi(){
        if (mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }

    public void connectWifi(String ssid,String pwd){
        WifiConfiguration configuration=createWifiInfo(ssid,pwd,3);
        int netId = mWifiManager.addNetwork(configuration);
        boolean success=mWifiManager.enableNetwork(netId,true);
        Log.i("520it",success+"");
    }

    /**创建一个wifi配置信息*/
    private WifiConfiguration createWifiInfo(String SSID, String Password, int Type)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        /**连接不需要密码的wifi*/
        if(Type == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "\"\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        /**连接wep格式加密wifi*/
        if(Type == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+Password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        /**连接WPA格式加密wifi（就是我们平时使用的加密方法）*/
        if(Type == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    private WifiConfiguration IsExsits(String str){
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs){
            if (existingConfig.SSID.equals(str.trim())){
                return existingConfig;
            }
        }
        return null;
    }


    public void disconnectWifi(){
        int netId=mWifiManager.getConnectionInfo().getNetworkId();
        mWifiManager.disableNetwork(netId);
    }


    public List<ScanResult> search() {
        if (mWifiManager.isWifiEnabled()){
            Log.i("520it",mWifiManager.getScanResults()+"  ...");
            return mWifiManager.getScanResults();
        }
        return new ArrayList<>();
    }
}
