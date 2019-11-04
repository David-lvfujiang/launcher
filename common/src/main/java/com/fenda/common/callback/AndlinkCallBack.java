package com.fenda.common.callback;

import com.chinamobile.smartgateway.andsdk.device.service.CallBack;
import com.chinamobile.smartgateway.andsdk.device.serviceimpl.AndSdkImpl;
import com.chinamobile.smartgateway.andsdk.servicetest.ClientCallBack;
import com.fenda.common.bean.AndlinkDmInformInfo;
import com.fenda.common.util.LogUtil;
import com.fenda.protocol.util.DeviceIdUtil;
import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/10/14 15:19
 */
public class AndlinkCallBack implements CallBack {
    private static String TAG = "AndlinkCallBack";
    @Override
    public void messageArrived(String s, String s1) {
        if(s.contains("1008")){
            LogUtil.d(TAG, "开始发布设备Inform信息");
            Gson tGson2 = new Gson();

            AndlinkDmInformInfo.dataParamValues tEvevtTypeParams1 = new AndlinkDmInformInfo.dataParamValues();
            AndlinkDmInformInfo.dataParamValues tEvevtTypeParams2 = new AndlinkDmInformInfo.dataParamValues();
            tEvevtTypeParams1.paramCode = "softVersion";
            tEvevtTypeParams1.paramValue = "V2.0.3";
            tEvevtTypeParams2.paramCode = "firmware";
            tEvevtTypeParams2.paramValue = "V2.0.3";

            ArrayList tEvevtType = new ArrayList();
            tEvevtType.add(tEvevtTypeParams1);
            tEvevtType.add(tEvevtTypeParams2);

            AndlinkDmInformInfo.dataParams tDataParams = new AndlinkDmInformInfo.dataParams();
            tDataParams.params = tEvevtType;

            AndlinkDmInformInfo tAndlinkDmInformInfo = new AndlinkDmInformInfo();
            tAndlinkDmInformInfo.deviceId = DeviceIdUtil.getDeviceId();
            tAndlinkDmInformInfo.eventType = "Inform";
            tAndlinkDmInformInfo.timestamp = System.currentTimeMillis();
            tAndlinkDmInformInfo.data = tDataParams;

            LogUtil.d(TAG, "AndlinkDmInformInfo Json = " + tGson2.toJson(tAndlinkDmInformInfo));
            AndSdkImpl.getInstance().informMessage(tGson2.toJson(tAndlinkDmInformInfo));

        }
        LogUtil.d(TAG, "messageArrived s = " + s);
        LogUtil.d(TAG, "messageArrived s1 = " + s1);
    }
}
