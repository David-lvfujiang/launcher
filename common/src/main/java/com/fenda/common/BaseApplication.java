package com.fenda.common;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chinamobile.smartgateway.andsdk.device.serviceimpl.AndSdkImpl;
import com.fenda.common.bean.AndlinkDeviceInfo;
import com.fenda.common.util.DensityUtil;
import com.fenda.protocol.AppApplicaiton;
import com.google.gson.Gson;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:29
 * @Description This is BaseApplication
 */
public class BaseApplication extends AppApplicaiton {


    private static BaseApplication instance;
    public static HashMap<String,Integer> QQMUSIC = new HashMap<>();
    private static String devMac = "D0C5D364BEE5";

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

    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    /**
     * 屏幕的高度
     */
    private int screenHeight;



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (!BuildConfig.LOG_DEBUG){
            ARouter.openLog();
            ARouter.openDebug();
        }



        ARouter.init(this);
        Bugly.init(this, "a6a9c0f9cc", false);
//        AndlinkDeviceInfo.ChipModel tChipModel = new AndlinkDeviceInfo.ChipModel();
//        tChipModel.type = "WiFi";
//        tChipModel.factory = "rockchip";
//        tChipModel.model = "rk3326";
//
//        ArrayList tChips = new ArrayList();
//        tChips.add(tChipModel);
//
//        AndlinkDeviceInfo.DeviceExtInfo deviceExtInfo = new AndlinkDeviceInfo.DeviceExtInfo();
//        deviceExtInfo.cmei = "864226033993999";
//        deviceExtInfo.authMode = "0";
//        deviceExtInfo.manuDate = "2019-07";
//        deviceExtInfo.OS = "Android";
//        deviceExtInfo.netCheckMode = "";
//        deviceExtInfo.chips = tChips;
//
//        AndlinkDeviceInfo tDevcieInfo = new AndlinkDeviceInfo();
//        tDevcieInfo.deviceMac = devMac;
//        tDevcieInfo.deviceType = "500929";
//        tDevcieInfo.productToken = "JUyy3SiJ3yx6hImp";
//        tDevcieInfo.andlinkToken = "RMm2sEhc9v23H8cc";
//        tDevcieInfo.firmwareVersion = "f1.0";
//        tDevcieInfo.autoAP = "0";
//        tDevcieInfo.softAPMode = "";
//        tDevcieInfo.softwareVersion = "1.0.0";
//        tDevcieInfo.deviceExtInfo = deviceExtInfo;

//        Gson tGson = new Gson();

//        AndSdkImpl.getInstance().init(this, tGson.toJson(tDevcieInfo));
//        MTSdk.init(this, "864226033993999", null, null, "M100000534");


        //日志框架初始化
//        Logger.addLogAdapter(new AndroidLogAdapter(){
//            @Override
//            public boolean isLoggable(int priority, @Nullable String tag) {
//                return BuildConfig.LOG_DEBUG;
//            }
//        });
        screenWidth  = DensityUtil.getScreenWidth(this);
        screenHeight = DensityUtil.getScreenHeight(this);

//        //待机界面
//        ScreenSaverManager.init(this);
//        ScreenSaverManager.startMonitor();
//        ScreenSaverManager.eliminateEvent();
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

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
