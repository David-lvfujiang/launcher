package com.fenda.settings.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.MyListView;
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

    private ImageView ivBack;
    private TextView tvDisDeviceName;
    private MyListView lvDisSetListItem;
    private LinearLayout llDeviceCenter;

    private String mConnectedWifiSSID;
    private SimpleAdapter mSimpleAdapter;
    private ArrayList<HashMap<String,String>> mArrayListData;
    private WifiManager mWifiManager;

    private String mBtName;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_main_layout;
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.set_back_iv);
        tvDisDeviceName = findViewById(R.id.set_first_info_name);
        lvDisSetListItem = findViewById(R.id.set_items_iv);
        llDeviceCenter = findViewById(R.id.set_first_info_layout);
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mWifiReceiver, filter);
        IntentFilter btIntentFilter = new IntentFilter();
        btIntentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        btIntentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mBtReceiver, btIntentFilter);
    }

    @Override
    public void initData() {
        String[] listItemName = new String[] {getString(R.string.settings_set_names_list_wifi), getString(R.string.settings_set_names_list_bluetooth), getString(R.string.settings_set_names_list_light), getString(R.string.settings_set_names_list_volume), getString(R.string.settings_set_names_list_deviceinfo), getString(R.string.settings_set_names_list_about), getString(R.string.settings_set_names_list_endlink)};
        String[] listItemStatus = new String[]{getString(R.string.settings_set_status_wifi_noconnect) ,"", "", "", "", "", ""};

        mArrayListData = new ArrayList<>();
        for (int i = 0; i < listItemName.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("name", listItemName[i]);
            map.put("state", listItemStatus[i]);
            mArrayListData.add(map);
        }
        mSimpleAdapter = new SimpleAdapter(this, mArrayListData, R.layout.settings_main_items_layout, new String[]{"name", "state"}, new int[]{R.id.set_items, R.id.set_items_status});
        lvDisSetListItem.setAdapter(mSimpleAdapter);

        String deviceName = (String) SPUtils.get(BaseApplication.getInstance(), Constant.Settings.DEVICE_NAME, "");
        tvDisDeviceName.setText(deviceName);
    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llDeviceCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SetDeviceCenterIntent = new Intent(SettingsActivity.this, SettingsDeviceCenterActivity.class);
                startActivity(SetDeviceCenterIntent);
                finish();
            }
        });
        lvDisSetListItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    Intent lightIntent = new Intent(SettingsActivity.this, SettingsVolumeActivity.class);
                    startActivity(lightIntent);
                } else if(("Andlink").equals(setClickedListName)){
                    Intent andlinkIntent = new Intent(SettingsActivity.this, SettingsAndLinkQRCodeActivity.class);
                    startActivity(andlinkIntent);
                } else {
                    ToastUtils.show("开发中...");
                }
            }
        });
    }

    private BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                BluetoothDevice mConnectionBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
                LogUtil.d(TAG, "BT CONNECT staute " + mConnectionBluetoothDevice.getName() + state);
                if (BluetoothAdapter.STATE_DISCONNECTED == state) {
                    LogUtil.d(TAG, "蓝牙断开了");
                    SPUtils.remove(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME);
                    LogUtil.d(TAG, "STATE_DISCONNECTED getName = " + mConnectionBluetoothDevice.getName() + ", STATE_DISCONNECTED getAddress = " + mConnectionBluetoothDevice.getAddress());
                } else if (BluetoothAdapter.STATE_CONNECTED == state) {
                    LogUtil.d(TAG, "蓝牙连上了");
                    mBtName = mConnectionBluetoothDevice.getName();
                    SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, mBtName);
                    LogUtil.d(TAG, "STATE_CONNECTED getName = " + mConnectionBluetoothDevice.getName() + ", STATE_CONNECTED getAddress = " + mConnectionBluetoothDevice.getAddress());
                }
            }
        }
    };

    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver () {
        HashMap<String,String> params = new HashMap<>();

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                Log.i(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) { //wifi没连接上
                    mArrayListData.remove(0);
                    params.put("name","WIFI");
                    params.put("state","未连接");
                    mArrayListData.add(0,params);
                    mSimpleAdapter.notifyDataSetChanged();
                    Log.i(TAG, "wifi没连接上");
                } else if (NetworkInfo.State.CONNECTED == info.getState()) { //wifi连接上了
                    Log.i(TAG, "wifi连接上了");
                    mArrayListData.remove(0);
                    mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    mConnectedWifiSSID = mWifiManager.getConnectionInfo().getSSID();
                    mConnectedWifiSSID = mConnectedWifiSSID.substring(1,mConnectedWifiSSID.length()-1);
                    LogUtil.d(TAG,  "back wifi name = " + mConnectedWifiSSID);
                    params.put("name","WIFI");
                    params.put("state","已连接("+mConnectedWifiSSID+")");
                    mArrayListData.add(0,params);
                    mSimpleAdapter.notifyDataSetChanged();
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    mArrayListData.remove(0);
                    params.put("name","WIFI");
                    params.put("state","正在连接");
                    mArrayListData.add(0,params);
                    mSimpleAdapter.notifyDataSetChanged();
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
        unregisterReceiver(mWifiReceiver);
        unregisterReceiver(mBtReceiver);
        super.onDestroy();
    }
}
