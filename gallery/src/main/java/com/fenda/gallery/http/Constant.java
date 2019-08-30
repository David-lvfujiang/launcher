package com.fenda.gallery.http;

import android.os.Environment;

import com.fenda.common.BaseApplication;
import com.fenda.gallery.R;

import java.io.File;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 19:33
 * @Description
 */
public class Constant {
    public static String TEST_BASE_URL = BaseApplication.getInstance().getResources().getString(R.string.BASE_SERVER_URL);
    public static final String TCP_IP = BaseApplication.getInstance().getResources().getString(R.string.TCP_IP);
    public static final int TCP_PORT = 11211;
    public static final String SERIAL_NUM = android.os.Build.SERIAL;
    public interface PHOTO{
        String DirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "imgs"+File.separator;

    }
}
