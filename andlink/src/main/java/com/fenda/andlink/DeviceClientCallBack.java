/*
 * Project: AndlinkSDK
 * 
 * File Created at 2018年10月29日
 * 
 * Copyright 2016 CMCC Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * ZYHY Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license.
 */
package com.fenda.andlink;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.smartgateway.andsdk.device.service.CallBack;
import com.chinamobile.smartgateway.andsdk.util.StringHelper;

/**
 * 厂商应实现的回调，用于处理下发的控制指令或错误信息
 * @Type ClinetCallBack.java
 * @Desc 
 * @author 
 * @date 2018年10月29日 下午1:48:21
 * @version 
 */
public class DeviceClientCallBack implements CallBack {

    @Override
    public void messageArrived(String arg0, String arg2) { //请根据文档处理相应的下发指令！此处处理仅供参考，厂商需二次开发！


        Log.e("qob", "messageArrived arg0 " + arg0 + " arg2 " + arg2);

        if (StringHelper.isEmpty(arg0)) {
            return;
        }

        JSONObject jsonObject = JSONObject.parseObject(arg0);

        if (null == jsonObject) {
            return;
        }

        int respCode = jsonObject.getIntValue("respCode");
        String respCont = jsonObject.getString("respCont");

        Message message = new Message();
        Bundle bundle = new Bundle();
        if (!StringHelper.isEmpty(respCont)) {
            bundle.putString("msg", respCont);
        } else {
            bundle.putString("msg", arg2 + "_" + arg0);
        }

//        message.setData(bundle);
//        message.what = respCode;
//        MainActivity.myHandler.sendMessage(message);

    }
}
