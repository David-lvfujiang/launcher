package com.fenda.andlink;



import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chinamobile.smartgateway.andsdk.device.service.HandleSDKServerMeaasge;
import com.chinamobile.smartgateway.andsdk.device.serviceimpl.AndSdkImpl;
import com.fenda.andlink.Model.AndlinkDeviceInfo;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AndlinkMainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AndlinkMainActivity";

    private static String devMac = "D0C5D364BEE5";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.andlink_activity_main);


        findViewById(R.id.bt_init).setOnClickListener(this);
        findViewById(R.id.bt_regirst).setOnClickListener(this);
        findViewById(R.id.bt_saveKeyInfo).setOnClickListener(this);
        findViewById(R.id.bt_broadcastQlinkSuc).setOnClickListener(this);
        findViewById(R.id.bt_reset).setOnClickListener(this);
        findViewById(R.id.back_btn).setOnClickListener(this);

        HandleSDKServerMeaasge.getInstance().setCallback(new DeviceClientCallBack());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_init){

            AndlinkDeviceInfo.ChipModel tChipModel = new AndlinkDeviceInfo.ChipModel();
            tChipModel.type = "WiFi";
            tChipModel.factory = "rockchip";
            tChipModel.model = "rk3326";

            ArrayList tChips = new ArrayList();
            tChips.add(tChipModel);

            AndlinkDeviceInfo.DeviceExtInfo deviceExtInfo = new AndlinkDeviceInfo.DeviceExtInfo();
            deviceExtInfo.cmei = "";
            deviceExtInfo.authMode = "0";
            deviceExtInfo.manuDate = "2019-07";
            deviceExtInfo.OS = "Android";
            deviceExtInfo.netCheckMode = "";
            deviceExtInfo.chips = tChips;

            AndlinkDeviceInfo tDevcieInfo = new AndlinkDeviceInfo();
            tDevcieInfo.deviceMac = devMac;
            tDevcieInfo.deviceType = "500929";
            tDevcieInfo.productToken = "JUyy3SiJ3yx6hImp";
            tDevcieInfo.andlinkToken = "RMm2sEhc9v23H8cc";
            tDevcieInfo.firmwareVersion = "f1.0";
            tDevcieInfo.autoAP = "0";
            tDevcieInfo.softAPMode = "";
            tDevcieInfo.softwareVersion = "1.0.0";
            tDevcieInfo.deviceExtInfo = deviceExtInfo;

            Gson tGson = new Gson();

            AndSdkImpl.getInstance().init(getApplication(), tGson.toJson(tDevcieInfo));
        }
        else if (v.getId() == R.id.bt_regirst){
            String info = AndSdkImpl.getInstance().getDeviceInfo();
            LogUtil.d(TAG, "bt_regirst = " + info);
        }
        else if (v.getId() == R.id.bt_saveKeyInfo){
            AndSdkImpl.getInstance().saveKeyInfo(getApplication(), "RLCvVEReNwlGpCOVw-e4ZkazVYNzjlDqP5TBXRVkDZcJ3bwRDG-w2cf8tKpUKizb", "https://cgw.komect.com:443");
        }
        else if (v.getId() == R.id.bt_broadcastQlinkSuc){

            AndSdkImpl.getInstance().broadcastQlinkSuc(getApplication(), devMac, "500929");
        }
        else if (v.getId() == R.id.bt_reset){
            AndSdkImpl.getInstance().reset(0);
        } else if(v.getId() == R.id.back_btn){
            finish();
        }
    }

    @Override
    protected void onDestroy() {

//        AndSdkImpl.getInstance().stop(this); //厂商需在onDestroy方法第一句调用，做必要的清理工作

        super.onDestroy();

    }
}
