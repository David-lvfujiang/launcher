package com.fenda.settings.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.bean.SettingsWifiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 17:17
 */
public class SettingsWifiUtil {
    private static final String TAG = "SettingsWifiUtil";

    public static final int CLOSING_WIFI = 0;
    public static final int CLOSE_WIFI_FLAG = 1;
    public static final int OPEN_WIFI_FLAG = 2;
    public static final int OPENED_WIFI = 3;
    public static final int TYPE_NO_PASSWD = 0x11;
    public static final int TYPE_WEP = 0x12;
    public static final int TYPE_WPA = 0x13;

    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;



    // 构造器
    public SettingsWifiUtil(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    //枚举
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    // 打开WIFI
    @SuppressLint("WrongConstant")
    public void openWifi(Context context) {
        LogUtil.d(TAG, "start open wifi !");
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
            LogUtil.d(TAG, "start open wifi 2222!");
        } else if (mWifiManager.getWifiState() == OPEN_WIFI_FLAG) {
            Toast.makeText(context,"亲，Wifi正在开启，不用再开了", Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(context,"亲，Wifi已经开启,不用再开了", Toast.LENGTH_SHORT).show();
        }
    }

    // 关闭wifi
    @SuppressLint("WrongConstant")
    public void closeWifi(Context context) {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
//        else if(mWifiManager.getWifiState() == CLOSE_WIFI_FlAG){
//            Toast.makeText(context,"亲，Wifi已经关闭，不用再关了", Toast.LENGTH_SHORT).show();
//        }else if (mWifiManager.getWifiState() == 0) {
//            Toast.makeText(context,"亲，Wifi正在关闭，不用再关了", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(context,"请重新关闭", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 打开wifi
     */
    public void openWifi1() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭wifi
     */
    public void closeWifi1() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * Function:判断扫描结果是否连接上<br>
     * @param result
     * @return<br>
     */
    public boolean isConnect(ScanResult result) {
        if (result == null) {
            return false;
        }
        mWifiInfo = mWifiManager.getConnectionInfo();
        String g2 = "\"" + result.SSID + "\"";
        if (mWifiInfo.getSSID() != null && mWifiInfo.getSSID().endsWith(g2) && mWifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return true;
        }
        return false;
    }

    public boolean connectSpecificAP(ScanResult scan) {
        List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
        boolean networkInSupplicant = false;
        boolean connectResult = false;
        // 重新连接指定AP
        mWifiManager.disconnect();
        for (WifiConfiguration w : list) {
            // 将指定AP 名字转化
            // String str = convertToQuotedString(info.ssid);
            if (w.BSSID != null && w.BSSID.equals(scan.BSSID)) {
                connectResult = mWifiManager.enableNetwork(w.networkId, true);
                // mWifiManager.saveConfiguration();
                networkInSupplicant = true;
                break;
            }
        }
        if (!networkInSupplicant) {
            WifiConfiguration config = CreateWifiInfo(scan, "");
            connectResult = addNetwork11(config);
        }

        return connectResult;
    }

    /**
     * 添加到网络
     *
     * @param wcg
     */
    public boolean addNetwork11(WifiConfiguration wcg) {
        if (wcg == null) {
            return false;
        }
        // receiverDhcp = new ReceiverDhcp(ctx, mWifiManager, this,
        // wlanHandler);
        // ctx.registerReceiver(receiverDhcp, new
        // IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        mWifiManager.saveConfiguration();
        System.out.println(b);
        return b;
    }


    // 然后是一个实际应用方法，只验证过没有密码的情况：
    public WifiConfiguration CreateWifiInfo(ScanResult scan, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.status = WifiConfiguration.Status.ENABLED;

        if (scan.capabilities.contains("WEP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);

            config.SSID = "\"" + scan.SSID + "\"";

            config.wepTxKeyIndex = 0;
            config.wepKeys[0] = Password;
            // config.preSharedKey = "\"" + SHARED_KEY + "\"";
        } else if (scan.capabilities.contains("PSK")) {
            //
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else if (scan.capabilities.contains("EAP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            config.SSID = "\"" + scan.SSID + "\"";
            // config.BSSID = info.mac;
            config.preSharedKey = null;
            //
        }

        return config;
    }


    /**
     * 检查当前wifi状态
     *
     * @return
     */
    public int checkState1() {
        return mWifiManager.getWifiState();
    }


    // 检查当前WIFI状态
    @SuppressLint("WrongConstant")
    public void checkState(Context context) {
        if (mWifiManager.getWifiState() == CLOSING_WIFI) {
            ToastUtils.show("Wifi正在关闭");
        } else if (mWifiManager.getWifiState() == CLOSE_WIFI_FLAG) {
            ToastUtils.show("Wifi已经关闭");
        } else if (mWifiManager.getWifiState() == OPEN_WIFI_FLAG) {
            ToastUtils.show("Wifi正在开启");
        } else if (mWifiManager.getWifiState() == OPENED_WIFI) {
            ToastUtils.show("Wifi已经开启");
        } else {
            ToastUtils.show("没有获取到WiFi状态");
        }
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 判断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock()
    {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    // 得到配置好的网络
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    // 指定配置好的网络进行连接
    public void connectConfiguration(int index) {
        // 索引大于配置好的网络索引返回
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }

    /**
     * 得到Wifi配置好的信息
     */
    public void getConfigurationInfo() {
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();// 得到配置好的网络信息
        for (int i = 0; i < mWifiConfiguration.size(); i++) {
            Log.i("getConfiguration", mWifiConfiguration.get(i).SSID);
            Log.i("getConfiguration",
                    String.valueOf(mWifiConfiguration.get(i).networkId));
        }
    }
    /**
     * 得到配置好的网络
     *
     * @return
     */
    public List<WifiConfiguration> getWiFiConfiguration() {
        return mWifiConfiguration;
    }


    /**
     * 判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
     *
     * @param mSSID
     * @return
     */
    public int isConfiguration(String mSSID) {
        Log.i("IsConfiguration", String.valueOf(mWifiConfiguration.size()));
        for (int i = 0; i < mWifiConfiguration.size(); i++) {
            Log.i(mWifiConfiguration.get(i).SSID,
                    String.valueOf(mWifiConfiguration.get(i).networkId));
            if (mWifiConfiguration.get(i).SSID.equals(mSSID)) {// 地址相同
                return mWifiConfiguration.get(i).networkId;
            }
        }
        return -1;
    }



    /**
     * 添加指定WIFI的配置信息,原列表不存在此SSID
     *
     * @param wifiList
     * @param ssid
     * @param pwd
     * @return
     */
    public int addWifiConfig(List<ScanResult> wifiList, String ssid, String pwd) {
        int wifiId = -1;
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult wifi = wifiList.get(i);
            if (wifi.SSID.equals(ssid)) {
                Log.i("AddWifiConfig", "equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                // \"转义字符，代表"
                wifiCong.SSID = "\"" + wifi.SSID + "\"";
                wifiCong.preSharedKey = "\"" + pwd + "\"";// WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);// 将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                if (wifiId != -1) {
                    return wifiId;
                }
            }
        }
        return wifiId;
    }

    public void startScan1() {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }


    @SuppressLint("WrongConstant")
    public void startScan(Context context) {
        Boolean ss = mWifiManager.startScan();
        Log.d(TAG, "wifi scan boolean =" + ss);
        //得到扫描结果
        List<ScanResult> results = mWifiManager.getScanResults();
        Log.d(TAG, "wifi scan =" + results.size());
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();

        if (results.size() == 0) {
            Log.d(TAG, "wifi scan results null");
            if(mWifiManager.getWifiState()==OPENED_WIFI) {
                // Toast.makeText(context,"当前区域没有无线网络",Toast.LENGTH_SHORT).show();
            } else if(mWifiManager.getWifiState()==OPEN_WIFI_FLAG) {
                ToastUtils.show("wifi正在开启，请稍后扫描");
            } else {
                Log.d(TAG,"WiFi没有开启");
                Toast.makeText(context,"WiFi没有开启", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "wifi scan have results");
            mWifiList = new ArrayList();
            for(ScanResult result : results) {
                if (result.SSID == null || result.SSID.length() == 0 || result.capabilities.contains("[IBSS]")) {
                    continue;
                }
                boolean found = false;
                for(ScanResult item:mWifiList) {
                    if(item.SSID.equals(result.SSID)&&item.capabilities.equals(result.capabilities)){
                        found = true;
                        break;
                    }
                }
                if(!found){
                    mWifiList.add(result);
                }
            }
        }
    }

    /**
     * 得到网络列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    // 得到网络列表
    public List<SettingsWifiBean> getWifiList(int status, String ssid) {
        List<SettingsWifiBean> beans = new ArrayList<>();
        if (mWifiList == null) {
            return beans;
        }
        for (ScanResult scanResult : mWifiList) {
            SettingsWifiBean bean = new SettingsWifiBean();
            if (scanResult.SSID.equals(ssid)){
                bean.setStatus(status);
                bean.setResult(scanResult);
                beans.add(0,bean);
            } else {
                bean.setStatus(0);
                bean.setResult(scanResult);
                beans.add(bean);
            }
        }
        return beans;
    }

    // 查看扫描结果
    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++) {
            stringBuilder
                    .append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            stringBuilder.append((mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }
        return stringBuilder;
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBssid() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIpAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }


    // 得到连接的ID
    public int getNetworkId(String mSsid) {
        int connectId = 0;
        List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
//        LogUtil.d(TAG, "wifiConfigurationList = " + wifiConfigurationList);

        if (wifiConfigurationList != null && wifiConfigurationList.size() != 0) {
            for (int i = 0; i < wifiConfigurationList.size(); i++) {
                WifiConfiguration wifiConfiguration = wifiConfigurationList.get(i);
                String mSSID  = wifiConfiguration.SSID.replace("\"", "");
                LogUtil.d(TAG, "ssid = " + mSSID + "+" + mSsid);
                // wifiSSID就是SSID
                if (mSSID != null && mSSID.equals(mSsid)) {
                    connectId = wifiConfiguration.networkId;
                }
            }
        }
        return connectId;
    }

    /**
     * 得到连接的ID
     *
     * @return
     */
    public int getNetWordId2() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }


    // 得到WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 添加一个网络并连接
    public void addNetwork(WifiConfiguration wcg) {
        int wcgId = mWifiManager.addNetwork(wcg);
        boolean b =  mWifiManager.enableNetwork(wcgId, true);
        System.out.println("a--" + wcgId);
        System.out.println("b--" + b);
    }

    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
    public void removeWifi(int netId) {
        disconnectWifi(netId);
        mWifiManager.removeNetwork(netId);
    }

    /**
     * 判断指定的wifi是否保存
     *
     * @param mSSID
     * @return
     */
    public boolean isWifiSave(String mSSID) {
        if (mWifiConfiguration != null) {
            for (WifiConfiguration existingConfig : mWifiConfiguration) {
                if (existingConfig.SSID.equals("\"" + mSSID + "\"")) {
                    return true;
                }
            }
        }
        return false;
    }

    //创建wifi热点的。
    public WifiConfiguration createWifiInfo(String mSSID, String password, int mType) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + mSSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(mSSID);
        if(tempConfig != null) {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if(mType == 1) //WIFICIPHER_NOPASS
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(mType == 2) //WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0]= "\""+password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(mType == 3) //WIFICIPHER_WPA
        {
            config.preSharedKey = "\""+password+"\"";
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

    private WifiConfiguration isExsits(String mSSID)
    {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+mSSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }

    public static boolean isWifiEnabled(Context myContext) {
        if (myContext == null) {
            throw new NullPointerException("Global context is null");
        }
        WifiManager wifiMgr = (WifiManager) myContext.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) myContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 连接指定Id的WIFI
     *
     * @param wifiId
     * @return
     */
    public boolean connectWifi(int wifiId) {
        for (int i = 0; i < mWifiConfiguration.size(); i++) {
            WifiConfiguration wifi = mWifiConfiguration.get(i);
            if (wifi.networkId == wifiId) {
                // 激活该Id，建立连接
                while (!(mWifiManager.enableNetwork(wifiId, true))) {
                    // status:0--已经连接，1--不可连接，2--可以连接
                    Log.i("ConnectWifi", String.valueOf(mWifiConfiguration.get(wifiId).status));
                }
                return true;
            }
        }
        return false;
    }
    /**
     * 指定配置好的网络进行连接
     *
     * @param index
     */
    public void connetionConfiguration(int index) {
        if (index > mWifiConfiguration.size()) {
            return;
        }
        // 连接配置好指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

}
