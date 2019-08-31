package com.fenda.launcher.config;

import com.fenda.launcher.LauncherApplication;
import com.fenda.launcher.R;

public class Constant {
    public static String TEST_BASE_URL = LauncherApplication.getInstance().getResources().getString(R.string.BASE_SERVER_URL);
    public static final String TCP_IP = LauncherApplication.getInstance().getResources().getString(R.string.TCP_IP);
    public static final int TCP_PORT = 11211;
    public static final String SERIAL_NUM = android.os.Build.SERIAL + LauncherApplication.getInstance().getResources().getString(R.string.DEVICE_SN);;
}

