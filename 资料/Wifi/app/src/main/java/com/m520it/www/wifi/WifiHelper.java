package com.m520it.www.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by xmg on 2017/3/6.
 */

public class WifiHelper {

    private final WifiManager mWifiManager;
    private final WifiInfo mWifiInfo;

    public WifiHelper(Context context) {
        mWifiManager =(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * 开始wifi
     */
    public  void startWifi(){
        if(!mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(true);
        }
    }


    /**
     * 关闭wifi
     */
    public void stopWifi(){
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }

    }

    /**
     * 断开当前连接的wifi
     */
    public void disConnectWifi(){
        mWifiManager.disableNetwork(mWifiInfo.getNetworkId());
        mWifiManager.disconnect();
    }


    /**
     * 添加一个wifi, 允许上网
     * @param wifiName
     * @param wifiPassword
     */
    public void addNetWordWPA(String wifiName, String wifiPassword) {

        WifiConfiguration config=CreateWifiInfo(wifiName,wifiPassword,3);
        int netId = mWifiManager.addNetwork(config);
        mWifiManager.enableNetwork(netId,true);
        
    }


    /**
     * 搜索wifi
     */
    public List<ScanResult> startScanWIfi() {
        mWifiManager.startScan();
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        return  scanResults;
    }


    /**创建一个wifi配置信息*/
    private WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type)
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

    public void startScan() {

    }

}
