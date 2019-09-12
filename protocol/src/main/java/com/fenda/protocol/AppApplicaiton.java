package com.fenda.protocol;

import android.app.Application;
import android.content.Context;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/12 16:01
 * @Description
 */
public class AppApplicaiton extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}

