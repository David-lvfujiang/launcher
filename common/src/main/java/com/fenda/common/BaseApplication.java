package com.fenda.common;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.protocol.AppApplicaiton;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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

        ARouter.init(this);
        Bugly.init(this, "a6a9c0f9cc", false);


        //日志框架初始化
//        Logger.addLogAdapter(new AndroidLogAdapter(){
//            @Override
//            public boolean isLoggable(int priority, @Nullable String tag) {
//                return BuildConfig.LOG_DEBUG;
//            }
//        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();
    }

    public static BaseApplication getBaseInstance() {
        return instance;
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
