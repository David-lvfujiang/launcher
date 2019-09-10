package com.fenda.settings.activity;

import android.content.Intent;
import android.net.wifi.WifiConfiguration;
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
import com.fenda.common.util.ToastUtils;
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
    private String mConnectedSSID;
    private ArrayList<HashMap<String,String>> mListitem;
    private SimpleAdapter mSimpleAdapter;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_wifi_connected_info_layout;
    }

    @Override
    public void initView() {
        lvConnectedWifiConfig = findViewById(R.id.wifi_config_listview);
        tvWifiName = findViewById(R.id.wifi_config_name_tv);
        ivBack = findViewById(R.id.wifi_config_back_iv);
        mSettingsWifiUtil = new SettingsWifiUtil(getApplicationContext());
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        mConnectedSSID = intent.getStringExtra("CONNECTED_MESSAGE");
        tvWifiName.setText(mConnectedSSID);

        String[] ListName = new String[] {getString(R.string.settings_wifi_connected_cancel_save), getString(R.string.settings_wifi_connected_other)};
        String[] List1 = new String[]{"" ,"" , ""};

        mListitem = new ArrayList<>();

        for (int i = 0; i < ListName.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", ListName[i]);
            map.put("state", List1[i]);
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
                mWifiManager = (WifiManager) getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);
                LogUtil.d(TAG, "取消网络保存connectedSSID = " + mConnectedSSID);
                List<WifiConfiguration> wifiConfigurationList = mWifiManager.getConfiguredNetworks();
                LogUtil.d(TAG, "wifiConfigurationList = " + wifiConfigurationList);
                int netId1 = 0;

                netId1 = mSettingsWifiUtil.getNetworkId(mConnectedSSID);
                LogUtil.d(TAG, "取消网络保存id = " + netId1);

                if(("取消保存网络").equals(setClickedListName)) {
                    mSettingsWifiUtil.removeWifi(netId1);
                    mWifiManager.saveConfiguration();
                    Intent connectIntent = new Intent(SettingsWifiConnectedInfoActivity.this, SettingsWifiActivity.class);
                    startActivity(connectIntent);
                    finish();
                } else if(("网络配置").equals(setClickedListName)) {
                    ToastUtils.show("网络配置");
                }
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
