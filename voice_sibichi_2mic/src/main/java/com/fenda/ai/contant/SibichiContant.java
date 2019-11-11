package com.fenda.ai.contant;


import com.fenda.ai.R;
import com.fenda.common.BaseApplication;
import com.fenda.protocol.util.DeviceIdUtil;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/5 16:17
 */
public class SibichiContant {
    public static String TEST_BASE_URL = BaseApplication.getInstance().getResources().getString(R.string.BASE_SERVER_URL);
    public static final String TCP_IP = BaseApplication.getInstance().getResources().getString(R.string.TCP_IP);
    public static final int TCP_PORT = 11211;
    public static final String SETTINGS_SERIAL_NUM = DeviceIdUtil.getDeviceId();


}
