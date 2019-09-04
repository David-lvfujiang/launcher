package com.fenda.settings.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;
import com.fenda.settings.utils.SettingsWifiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 11:31
 */
@Route(path = RouterPath.SETTINGS.SettingsActivity)
public class SettingsActivity extends BaseMvpActivity   {
    private static final String TAG = "SettingsActivity";

    private ImageView setBackIv;
    private TextView disDeviceNameTv;
    private ListView disSetListItemLv;
    private LinearLayout deviceCenterLL;

    private String wifiSSID;

    private SimpleAdapter listViewAdapter;
    private ArrayList<HashMap<String,String>> listitemData;
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_main_layout;
    }

    @Override
    public void initView() {
        setBackIv = findViewById(R.id.set_back_iv);
        disDeviceNameTv = findViewById(R.id.set_first_info_name);
        disSetListItemLv = findViewById(R.id.set_items_iv);
        deviceCenterLL = findViewById(R.id.set_first_info_layout);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mwifiReceiver, filter);
    }

    @Override
    public void initData() {
        String[] listItemName = new String[] {getString(R.string.settings_set_names_list_wifi), getString(R.string.settings_set_names_list_bluetooth), getString(R.string.settings_set_names_list_light), getString(R.string.settings_set_names_list_volume), getString(R.string.settings_set_names_list_deviceinfo), getString(R.string.settings_set_names_list_about)};
        String[] listItemStatus = new String[]{getString(R.string.settings_set_status_wifi_noconnect) ,"", "", "", "", ""};

        listitemData = new ArrayList<>();
        for (int i = 0; i < listItemName.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", listItemName[i]);
            map.put("state", listItemStatus[i]);
            listitemData.add(map);
        }
        listViewAdapter = new SimpleAdapter(this, listitemData, R.layout.settings_main_items_layout, new String[]{"name", "state"}, new int[]{R.id.set_items, R.id.set_items_status});
        disSetListItemLv.setAdapter(listViewAdapter);
    }

    @Override
    public void initListener() {
        setBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        deviceCenterLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SetDeviceCenterIntent = new Intent(SettingsActivity.this, SettingsDeviceCenterActivity.class);
                startActivity(SetDeviceCenterIntent);
                finish();
            }
        });
        disSetListItemLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                String setClickedListName = map.get("name").toString();
                if(("WIFI").equals(setClickedListName)) {
                    Intent SetWifiIntent = new Intent(SettingsActivity.this, SettingsWifiActivity.class);
                    startActivityForResult(SetWifiIntent,200);
                } else if(("蓝牙").equals(setClickedListName)) {
                    Intent SetBTIntent = new Intent(SettingsActivity.this, SettingsBluetoothActivity.class);
                    startActivity(SetBTIntent);
                } else if(("关于小乐").equals(setClickedListName)) {
                    String ApksUrl = "https://ai.fenda.com/CloudIntroduce/index.html";
                    Intent aboutIntent = new Intent(SettingsActivity.this, SettingsLoadWebviewActivity.class);
                    aboutIntent.putExtra("APK_URL", ApksUrl);
                    startActivity(aboutIntent);
                } else if(("设备信息").equals(setClickedListName)) {
                    Intent deviceInfoIntent = new Intent(SettingsActivity.this, SettingsDeviceInfoActivity.class);
                    startActivity(deviceInfoIntent);
                } else if(("屏幕与亮度").equals(setClickedListName)) {
                    Intent lightIntent = new Intent(SettingsActivity.this, SetttingsBrightnessActivity.class);
                    startActivity(lightIntent);
                } else if(("音量").equals(setClickedListName)) {
                    Intent lightIntent = new Intent(SettingsActivity.this, SettingsBindDeviceActivity.class);
                    startActivity(lightIntent);
                } else {
                    ToastUtils.show("开发中...");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200){
            listitemData.remove(0);
            HashMap<String,String> params = new HashMap<>();
            String wifiName = (String) SPUtils.get(this,"WifiName","");
            params.put("name","WIFI");
            if (!TextUtils.isEmpty(wifiName) && SettingsWifiUtil.isWifiEnabled(this)){
                params.put("state","已连接("+wifiName+")");
            } else{
                params.put("state",getString(R.string.settings_set_status_wifi_noconnect));
            }
            listitemData.add(0,params);
            listViewAdapter.notifyDataSetChanged();
        }
    }

    private BroadcastReceiver mwifiReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                Log.i(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) { //wifi没连接上
                    Log.i(TAG, "wifi没连接上");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) { //wifi连接上了
                    Log.i(TAG, "wifi连接上了");
                    listitemData.remove(0);
                    HashMap<String,String> params = new HashMap<>();
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiSSID = wifiManager.getConnectionInfo().getSSID();
                    wifiSSID = wifiSSID.substring(1,wifiSSID.length()-1);
                    LogUtil.d(TAG,  "back wifi name = " + wifiSSID);
                    params.put("name","WIFI");
                    params.put("state","已连接("+wifiSSID+")");

                    listitemData.add(0,params);
                    listViewAdapter.notifyDataSetChanged();

//                    deviceStatusActivity.judgeviceStatus(getApplicationContext());
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接

                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.i(TAG, "网络列表变化了");
            }
        }
    };


    @Override
    public void showErrorTip(String msg) {
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mwifiReceiver);
        super.onDestroy();
    }
}
