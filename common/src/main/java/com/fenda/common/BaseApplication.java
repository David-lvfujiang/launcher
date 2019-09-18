package com.fenda.common;

import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.protocol.AppApplicaiton;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.HashMap;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:29
 * @Description This is BaseApplication
 */
public class BaseApplication extends AppApplicaiton {


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
    /**
     * 闹钟响铃
     */
    private boolean isRemindRing;
    /**
     * 语音是否初始化成功
     */
    private boolean isVoiceInit;
    /**
     * 是否主动请求天气(不播报语音)
     */
    private boolean isRequestWeather;
    /**
     * 是否主动请求新闻(不播报语音)
     */
    private boolean isRequestNews;
    /**
     * 是否在拨打电话
     */
    private boolean isCall;

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


    public boolean isRemindRing() {
        return isRemindRing;
    }

    public void setRemindRing(boolean remindRing) {
        isRemindRing = remindRing;
    }

    public boolean isVoiceInit() {
        return isVoiceInit;
    }

    public void setVoiceInit(boolean voiceInit) {
        isVoiceInit = voiceInit;
    }


    public boolean isRequestWeather() {
        return isRequestWeather;
    }

    public void setRequestWeather(boolean requestWeather) {
        isRequestWeather = requestWeather;
    }

    public boolean isRequestNews() {
        return isRequestNews;
    }

    public void setRequestNews(boolean requestNews) {
        isRequestNews = requestNews;
    }

    public boolean isCall() {
        return isCall;
    }

    public void setCall(boolean call) {
        isCall = call;
    }
}
