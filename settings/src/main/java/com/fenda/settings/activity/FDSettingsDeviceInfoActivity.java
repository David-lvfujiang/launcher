package com.fenda.settings.activity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;
import com.fenda.settings.utils.SettingsWifiUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.getProperty;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 10:05
 */
@Route(path = RouterPath.SETTINGS.FDSettingsDeviceInfoActivity)
public class FDSettingsDeviceInfoActivity extends BaseMvpActivity {
    private static final String TAG = "FDSettingsDeviceInfoActivity";

    protected SettingsWifiUtil mWifiAdmin;

    ImageView deviceInfoBack;
    ListView deviceInfoListview;
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_device_info_layout;
    }

    @Override
    public void initView() {
        mWifiAdmin = new SettingsWifiUtil(FDSettingsDeviceInfoActivity.this);
        deviceInfoBack = findViewById(R.id.device_info_back_iv);
        deviceInfoListview = findViewById(R.id.device_info_listview);

        deviceInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(FDSettingsDeviceInfoActivity.this, FDSettingsActivity.class);
                startActivity(Intent);
                finish();
            }
        });

        String[] deviceInfoNamesDis = new String[] {getString(R.string.settings_device_info_sn), getString(R.string.settings_device_info_ip),
                getString(R.string.settings_device_info_mac), getString(R.string.settings_device_info_launcher_version),
                getString(R.string.settings_device_info_system_version), getString(R.string.settings_device_info_software_update)};
        String[] deviceInfoStatusDis = new String[]{"FD-R03-001", "", "", "", "", ""};

        String deviceIPString = getIpAddress();
        String deviceMac = mWifiAdmin.getMacAddress();
        String systemVer = getProperty("ro.product.version", "unknown");

        String versionCode = AppUtils.getVersionCode(this) + "";
        String versionName= AppUtils.getVerName(this);

        LogUtil.d(TAG, "apk versionCode = " + versionCode);
        LogUtil.d(TAG, "apk versionName = " + versionName);
        LogUtil.d(TAG, "device ip = " + deviceIPString);
        LogUtil.d(TAG, "device system version = " + systemVer);

        List<Map<String, Object>> listitem = new ArrayList<>();

        for (int i = 0; i < deviceInfoNamesDis.length; i++) {
            Map<String, Object> map = new HashMap<>();
            if(i == 1) {
                if(SettingsWifiUtil.isWifiEnabled(this)) {
                    LogUtil.d(TAG, "wifi status true ");
                    map.put("name", deviceInfoNamesDis[i]);
                    map.put("state", deviceIPString);
                    listitem.add(map);
                } else {
                    map.put("name", deviceInfoNamesDis[i]);
                    map.put("state",  deviceInfoStatusDis[i]);
                    listitem.add(map);
                    LogUtil.d(TAG, "wifi status false ");
                }
            } else if(i == 2) {
                map.put("name", deviceInfoNamesDis[i]);
                map.put("state", deviceMac);
                listitem.add(map);
            } else if (i == 3) {
                map.put("name", deviceInfoNamesDis[i]);
                map.put("state", versionName);
                listitem.add(map);
            } else if (i == 4) {
                map.put("name", deviceInfoNamesDis[i]);
                map.put("state", systemVer);
                listitem.add(map);
            } else {
                map.put("name", deviceInfoNamesDis[i]);
                map.put("state", deviceInfoStatusDis[i]);
                listitem.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listitem, R.layout.settings_device_info_items_layout,
                new String[]{"name", "state"}, new int[]{R.id.deviceinfo_items, R.id.device_info_items_info});
        //第一个参数是上下文对象，第二个是listitem， 第三个是指定每个列表项的布局文件，第四个是指定Map对象中定义的两个键（这里通过字符串数组来指定），第五个是用于指定在布局文件中定义的id（也是用数组来指定）

        deviceInfoListview.setAdapter(adapter);
        deviceInfoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                String setClickedListName = map.get("name").toString();
                LogUtil.d(TAG, "select name is =" + setClickedListName);

                if(setClickedListName.equals(getString(R.string.settings_device_info_software_update)))
                {
                    ToastUtils.show(getString(R.string.settings_device_info_no_new_version_tips));
//                    Intent updateIntent = new Intent(DeviceInfoActivity.this, DeviceInfoSoftewareUpdateActivity.class);
//                    startActivity(updateIntent);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void showErrorTip(String msg) {

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

    //获取系统属性
    public static  String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String)(get.invoke(c, key, "unknown" ));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return value;
        }
    }
    //设置系统属性
    public static void setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
