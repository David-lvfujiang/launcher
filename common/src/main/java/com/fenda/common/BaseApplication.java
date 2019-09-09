package com.fenda.common;

import android.app.Application;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:29
 * @Description This is BaseApplication
 */
public class BaseApplication extends Application {


    private static BaseApplication instance;
    public static HashMap<String,Integer> QQMUSIC = new HashMap<>();
    /**
     * 媒体播放状态
     *
     */
    private boolean isMusicPlay;
    /**
     * 新闻播放
     */
    private boolean isNewsPlay;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(getInstance());

        //日志框架初始化
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.LOG_DEBUG;
            }
        });



    }


    public static BaseApplication getInstance() {
        return instance;
    }


    public boolean isNewsPlay() {
        return isNewsPlay;
    }

    public void setNewsPlay(boolean newsPlay) {
        isNewsPlay = newsPlay;
    }

    public void setMusicPlay(boolean isMusicPlay){
        this.isMusicPlay = isMusicPlay;
    }

    public boolean isMusicPlay(){
        return isMusicPlay;
    }



}
