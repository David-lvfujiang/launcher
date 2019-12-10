package com.fenda.common.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/3 15:18
 */
public interface ISettingsProvider extends IProvider {
    void deviceStatus(Context context);

    void queryDeviceInfo(Context context,boolean isSendEvent);

    void syncSettingsContacts();
}
