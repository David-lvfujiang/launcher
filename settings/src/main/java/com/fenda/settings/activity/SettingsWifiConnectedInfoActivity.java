package com.fenda.settings.activity;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;
import com.fenda.settings.utils.SettingsWifiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 18:24
 */
@Route(path = RouterPath.SETTINGS.SettingsWifiConnectedInfoActivity)
public class SettingsWifiConnectedInfoActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsWifiConnectedInfoActivity";

    private ListView lvConnectedWifiConfig;
    private TextView tvWifiName;
    private ImageView ivBack;

    private SettingsWifiUtil  mSettingsWifiUtil;
    private WifiManager mWifiManager;
    private String mConnectedSsid;
    private int mWifiSafeFlag;
    private String mWifiSafe;
    private String mWifiIp;
    private String mWifiSpeed1;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_wifi_connected_info_layout;
    }

    @Override
    public void initView() {
        String mWifiSpeedUnit;
        int mWifiSpeed;

        lvConnectedWifiConfig = findViewById(R.id.wifi_config_listview);
        tvWifiName = findViewById(R.id.wifi_config_name_tv);
        ivBack = findViewById(R.id.wifi_config_back_iv);
        mSettingsWifiUtil = new SettingsWifiUtil(getApplicationContext());

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);



        WifiInfo info = mWifiManager.getConnectionInfo();

        mWifiIp = getIpAddress();
        mWifiSpeed = info.getLinkSpeed();
        // 链接速度单位
        mWifiSpeedUnit = WifiInfo.LINK_SPEED_UNITS;
        mWifiSpeed1 = mWifiSpeed + mWifiSpeedUnit;

        // 得到配置好的网络连接
        List<WifiConfiguration> wifiConfigList = mWifiManager.getConfiguredNetworks();

        for (WifiConfiguration wifiConfiguration : wifiConfigList) {
            //配置过的SSID
            String configSsid = wifiConfiguration.SSID;
            configSsid = configSsid.replace("\"", "");

            //当前连接SSID
            String currentSSid =info.getSSID();
            currentSSid = currentSSid.replace("\"", "");
            LogUtil.d(TAG, "currentSSid = " + currentSSid);

            //比较networkId，防止配置网络保存相同的SSID
            if (currentSSid.equals(configSsid)&&info.getNetworkId()==wifiConfiguration.networkId) {
                LogUtil.d(TAG, "当前网络安全性：" + getSecurity(wifiConfiguration));
                mWifiSafeFlag = getSecurity(wifiConfiguration);
            }
        }
    }

    @Override
    public void initData() {
        SimpleAdapter mSimpleAdapter;
        ArrayList<HashMap<String,String>> mListitem;

        Intent intent = getIntent();
        mConnectedSsid = intent.getStringExtra("CONNECTED_MESSAGE");
        tvWifiName.setText(mConnectedSsid);

        String[] listName = new String[] {getString(R.string.settings_wifi_connected_cancel_save), getString(R.string.settings_wifi_connected_IP),
                getString(R.string.settings_wifi_connected_safe), getString(R.string.settings_wifi_connected_signal)};
        String[] list1;


        if(mWifiSafeFlag == 0){
            mWifiSafe = "无";
        } else if(mWifiSafeFlag == 2){
            mWifiSafe = "WPA/WPA2 PSK";
        }

        list1 = new String[]{"" , mWifiIp, mWifiSafe, mWifiSpeed1};
        mListitem = new ArrayList<>();

        for (int i = 0; i < listName.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", listName[i]);
            map.put("state", list1[i]);
            mListitem.add(map);
        }

        mSimpleAdapter = new SimpleAdapter(this, mListitem, R.layout.settings_wifi_connected_info_items_layout,
                new String[]{"name", "state"}, new int[]{R.id.connected_wifi_config_listtv, R.id.connected_wifi_config_status});

        lvConnectedWifiConfig.setAdapter(mSimpleAdapter);
    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsWifiConnectedInfoActivity.this, SettingsWifiActivity.class));
                finish();
            }
        });

        lvConnectedWifiConfig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                String setClickedListName = map.get("name").toString();
                LogUtil.d(TAG, "取消网络保存connectedSSID = " + mConnectedSsid);
                List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
                LogUtil.d(TAG, "wifiConfigurationList = " + wifiConfigurationList);
                int netId1;

                netId1 = mSettingsWifiUtil.getNetworkId(mConnectedSsid);
                LogUtil.d(TAG, "取消网络保存id = " + netId1);

                if(("忽略此网络").equals(setClickedListName)) {
                    mSettingsWifiUtil.removeWifi(netId1);
                    mWifiManager.saveConfiguration();
                    Intent connectIntent = new Intent(SettingsWifiConnectedInfoActivity.this, SettingsWifiActivity.class);
                    startActivity(connectIntent);
                    finish();
                }
            }
        });
    }

    static final int SECURITY_NONE = 0;
    static final int SECURITY_WEP = 1;
    static final int SECURITY_PSK = 2;
    static final int SECURITY_EAP = 3;

    static int getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? SECURITY_WEP : SECURITY_NONE;
    }

    public String getIpAddress() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        int[] ipAddr = new int[4];
        ipAddr[0] = ipAddress & 0xFF;
        ipAddr[1] = (ipAddress >> 8) & 0xFF;
        ipAddr[2] = (ipAddress >> 16) & 0xFF;
        ipAddr[3] = (ipAddress >> 24) & 0xFF;
        return new StringBuilder().append(ipAddr[0]).append(".").append(ipAddr[1]).append(".").append(ipAddr[2])
                .append(".").append(ipAddr[3]).toString();
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
