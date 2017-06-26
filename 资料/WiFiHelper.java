package xmg.com.wifi.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Description :
 * Author : liujun
 * Email  : liujin2son@163.com
 * Date   : 2017/3/6 0006
 */

public class WiFiHelper {

    private final WifiManager mWifiManager;
    private final WifiInfo mWifiInfo;

    public WiFiHelper(Context context) {
         mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
         mWifiInfo = mWifiManager.getConnectionInfo();
    }

    /**
     * 开始wifif
     */
    public void startWifi(){
        if(!mWifiManager.isWifiEnabled()){
            //这里会报错记住要添加权限： <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭wifi
     */
    public void stopWifi(){
        if(mWifiManager.isWifiEnabled()){
            //这里会报错记住要添加权限： <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 添加并连接一个网络
     * @param ssid wifi名
     * @param password wifi密码
     */
    public void addNetworkWPA(String ssid,String password){
        WifiConfiguration config=CreateWifiInfo(ssid,password,3);
        int netId = mWifiManager.addNetwork(config);
        mWifiManager.enableNetwork(netId,true);
    }

    /**
     * 断开某个wifi的连接
     */
    public void disConnectWifi(){
        mWifiManager.disableNetwork(getWifiNetId());
        mWifiManager.disconnect();
    }

    /**
     * 开始扫描wifi 并返回结果
     * @return
     */
    public List<ScanResult> startScanWifi() {
        mWifiManager.startScan();
        List<ScanResult> scanResults = mWifiManager.getScanResults();
        return scanResults;
    }


    /**
     * 获取当前网络的id
     * @return
     */
    public int getWifiNetId(){
        return  mWifiInfo.getNetworkId();
    }

    public int getWifiState(){
        return  mWifiManager.getWifiState();
    }

    /**
     * 创建一个wifi配置信息
     * @param SSID  wifi 名
     * @param Password wifi 密码
     * @param Type 类型：1,2,3
     * @return
     */
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


}
