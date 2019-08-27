package com.fenda.common;

import android.app.Application;

/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:29
 * @Description This is BaseApplication
 */
public class BaseApplication extends Application {


    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    public static Application getInstance(){
        return instance;
    }







}
