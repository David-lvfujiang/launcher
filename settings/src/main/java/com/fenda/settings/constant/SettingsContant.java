package com.fenda.settings.constant;

import com.fenda.common.BaseApplication;
import com.fenda.settings.R;


/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 15:32
 */
public class SettingsContant {
    public static String TEST_BASE_URL = BaseApplication.getInstance().getResources().getString(R.string.BASE_SERVER_URL);
    public static final String TCP_IP = BaseApplication.getInstance().getResources().getString(R.string.TCP_IP);
    public static final int TCP_PORT = 11211;
    public static final String SETTINGS_SERIAL_NUM = android.os.Build.SERIAL + BaseApplication.getInstance().getResources().getString(R.string.DEVICE_SN);;
}
