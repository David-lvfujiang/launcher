package com.fenda.launcher;

import com.fenda.common.BaseApplication;

import io.rong.imkit.RongIM;

public class LauncherApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        RongIM.init(this);
    }


}
