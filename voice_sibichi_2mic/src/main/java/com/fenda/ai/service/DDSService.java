package com.fenda.ai.service;


import android.annotation.TargetApi;
import android.app.LauncherActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSConfig;
import com.aispeech.dui.dds.agent.DMCallback;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.BuildConfig;
import com.fenda.ai.R;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.author.MyDDSAuthListener;
import com.fenda.ai.author.MyDDSInitListener;
import com.fenda.ai.json.Jsonparse;
import com.fenda.ai.modle.MediaModel;
import com.fenda.ai.observer.DuiCommandObserver;
import com.fenda.ai.observer.DuiMessageObserver;
import com.fenda.ai.observer.DuiNativeApiObserver;
import com.fenda.ai.observer.DuiUpdateObserver;
import com.fenda.ai.provider.RequestService;
import com.fenda.ai.skill.Util;
import com.fenda.common.BaseApplication;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.ICalendarProvider;
import com.fenda.common.provider.IEncyclopediaProvider;
import com.fenda.common.provider.IRecommendProvider;
import com.fenda.common.provider.IRemindProvider;
import com.fenda.common.provider.IWeatherProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.service.AccessibilityMonitorService;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.SystemPropertiesProxyUtil;
import com.fenda.protocol.util.DeviceIdUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.view.SpeechView;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.netty.util.internal.StringUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


/**
 * 参见Android SDK集成文档: https://www.dui.ai/docs/operation/#/ct_common_Andriod_SDK
 */
@Route(path = RouterPath.VOICE.DDSService)
public class DDSService extends Service implements DuiUpdateObserver.UpdateCallback, DuiMessageObserver.MessageCallback {
    public static final String TAG = "DDSService";

    /**
     * 授权次数,用来记录自动授权
     */
    private int mAuthCount;

    private boolean isInit = false;
    /**
     * 初始化监听广播
     */
    private DDSService.MyReceiver mInitReceiver;

    /**
     * 消息监听器
     */
    private DuiMessageObserver mMessageObserver;
    /**
     * dds更新监听器
     */
    private DuiUpdateObserver mUpdateObserver;
    /**
     * 命令监听器
     */
    private DuiCommandObserver mCommandObserver;
    private DuiNativeApiObserver mNativeObserver;
    private boolean isFirstVar = true;
    private boolean hasvar = false;

    private SpeechView speechView;
    private long time;
    private MyDDSInitListener listener;
    private IEncyclopediaProvider provider;
    private IWeatherProvider weatherProvider;
    private ICalendarProvider calendarProvider;

    public DDSService() {
    }

    @Override
    public void onCreate() {
        LogUtil.e("onCreate========1");
        if (!AccessibilityMonitorService.isSettingOpen(AccessibilityMonitorService.class, getApplicationContext())) {
            AccessibilityMonitorService.jumpToSetting(this);
        }
        //初始化广播和语音弹窗
        initReceiverAndSpeechView();
//        setForeground();
        super.onCreate();
    }

    /**
     * 初始化语音弹窗和广播
     */
    private void initReceiverAndSpeechView() {
        // 添加一个初始成功的广播监听器
        IntentFilter filter = new IntentFilter();
        filter.addAction(VoiceConstant.ACTION_INIT_COMPLETE);
        mInitReceiver = new MyReceiver();
        registerReceiver(mInitReceiver, filter);

        // 注册一个广播,接收service中发送的dds初始状态广播
        IntentFilter intentFilter = new IntentFilter();
        // WIFI成功的广播
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // 认证成功的广播
        intentFilter.addAction(VoiceConstant.ACTION_AUTH_SUCCESS);
        // 认证失败的广播
        intentFilter.addAction(VoiceConstant.ACTION_AUTH_FAILED);
        registerReceiver(authReceiver, intentFilter);

        IntentFilter smartFilter = new IntentFilter();
        // 认证成功的广播
        smartFilter.addAction(VoiceConstant.ACTION_MIC_ENABLE);
        // 认证成功的广播
        smartFilter.addAction(VoiceConstant.ACTION_MIC_ABLE);
        //关闭闹钟
        smartFilter.addAction(VoiceConstant.ACTION_CLOSE_ALARM);
        //关闭提醒
        smartFilter.addAction(VoiceConstant.ACTION_CLOSE_REMIND);
        //关闭QQ音乐
        smartFilter.addAction(VoiceConstant.ACTION_CLOSE_QQMUSIC);
        //关闭语音弹窗
        smartFilter.addAction(VoiceConstant.ACTION_CLOSE_VIEW);
        registerReceiver(smartReceiver, smartFilter);

        speechView = new SpeechView(BaseApplication.getInstance());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void setForeground() {
        Intent intent = new Intent(DDSService.this, LauncherActivity.class);
        PendingIntent pi = PendingIntent.getActivity(DDSService.this, 0, intent, 0);

        Notification notification = Util.pupNotification(DDSService.this, pi, "FenDa");
        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return Service.START_STICKY;
    }

    /**
     * 初始化dds组件
     */
    private void init() {
        //在调试时可以打开sdk调试日志，在发布版本时，请关闭 setDebugMode(5)
        DDS.getInstance().setDebugMode(5);
        listener = new MyDDSInitListener(getApplicationContext());
        DDS.getInstance().init(getApplicationContext(), createConfig(), listener, new MyDDSAuthListener(getApplicationContext()));
        //初始化授权
        init_auth();

    }

    private void showToast(final String text) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init_auth() {
        new Thread() {
            @Override
            public void run() {
                checkDDSReady();
            }
        }.start();

    }


    /**
     * 执行自动授权
     */
    private void doAutoAuth() {
        // 自动执行授权5次,如果5次授权失败之后,给用户弹提示框
        if (mAuthCount < 5) {
            try {
                DDS.getInstance().doAuth();
                mAuthCount++;
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        } else {
            showToast("授权失败!");
        }
    }


    /**
     * 检查dds是否初始成功
     */
    public void checkDDSReady() {
        while (true) {
            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                    DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
                try {
                    if (DDS.getInstance().isAuthSuccess()) {
                        //PlayWelcomeTTS();
                        LogUtil.i("FD------auth ok 1");
                        sendInitSuccessEventBus();
                        showToast("授权成功!");
                        break;
                    } else {
                        // 自动授权
                        doAutoAuth();
                    }
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                break;
            } else {
                AILog.w(TAG, "waiting  init complete finish...");
            }
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 认证广播
     */
    private BroadcastReceiver authReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                            doauth_when_net_ok();
//                            LogUtil.i("TAG",  "FD------连上");
                        }
                    } else {
//                        LogUtil.i("TAG",  "FD-------断开");
                    }
                }
            } else if (TextUtils.equals(intent.getAction(), VoiceConstant.ACTION_AUTH_SUCCESS)) {
                LogUtil.i("FD------auth ok 2");
                sendInitSuccessEventBus();
                PlayWelcomeTTS();
                showToast("授权成功!");

            } else if (TextUtils.equals(intent.getAction(), VoiceConstant.ACTION_AUTH_FAILED)) {
                doAutoAuth();
            }
        }
    };


    private void sendInitSuccessEventBus() {

        LogUtil.e("sendInitSuccessEventBus  == SUCCESS");
        EventMessage message = new EventMessage();
        message.setCode(Constant.Common.INIT_VOICE_SUCCESS);
        message.setData(new BaseTcpMessage());
        EventBusUtils.post(message);

        DDS.getInstance().getAgent().setDMCallback(new DMCallback() {
            @Override
            public JSONObject onDMResult(JSONObject jsonObject) {
                try {
                    i(TAG,"onDMResult =====  "+jsonObject.toString());
                    JSONObject dmJson = jsonObject.optJSONObject("dm");
                    String intentName = dmJson.optString("intentName");
                    if (BaseApplication.getBaseInstance().isCall()){
                        String input = dmJson.optString("input");
                        if (!"挂断电话".equals(intentName) && !"接听".equals(intentName) && !"静音".equals(input) && !"取消静音".equals(input)) {
                            dmJson.put("nlg", "");
                            dmJson.put("shouldEndSession", false);
                            jsonObject.put("ignore", true);
                        }
                    }else if ("查询天气".equals(intentName)  ){
                        JSONObject widgetObject = dmJson.optJSONObject("widget");

                        if (BaseApplication.getBaseInstance().isRequestWeather()){
                            if (weatherProvider == null) {
                                weatherProvider = ARouter.getInstance().navigation(IWeatherProvider.class);
                            }
                            if (weatherProvider != null) {
                                weatherProvider.weatherFromVoiceControlToMainPage(widgetObject.toString());
                            }
                            BaseApplication.getBaseInstance().setRequestWeather(false);
                            dmJson.put("nlg","");
                            dmJson.put("shouldEndSession",false);
                            jsonObject.put("ignore", true);
                        }else {
                            dmJson.put("shouldEndSession",true);
                        }

                    }else if ("播报新闻".equals(intentName) && BaseApplication.getBaseInstance().isRequestNews()){
                        JSONObject widgetJson  = dmJson.optJSONObject("widget");
                        IRecommendProvider recommendProvider = ARouter.getInstance().navigation(IRecommendProvider.class);
                        if (recommendProvider != null){
                            recommendProvider.requestRecommend(widgetJson);
                        }
                        dmJson.put("nlg","");
                        dmJson.put("shouldEndSession",false);
                        jsonObject.put("ignore", true);
                        BaseApplication.getBaseInstance().setRequestNews(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                i(TAG,"onDMResult ===== end ======  "+jsonObject.toString());
                return jsonObject;
            }
        });
    }


    public static void i(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

    private void doauth_when_net_ok() {
        if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
            try {
                if (!DDS.getInstance().isAuthSuccess()) {
                    mAuthCount = 0;
                    doAutoAuth();
                }
            } catch (DDSNotInitCompleteException e) {
                e.printStackTrace();
            }
        }
    }

    private void PlayWelcomeTTS() {
//        LogUtil.i("TAG",  "FD------play welcome TTS---");
        String[] wakeupWords = new String[0];
        String minorWakeupWord = null;
        try {
            wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
            minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        String hiStr = "";
        if (wakeupWords != null && minorWakeupWord != null) {
            hiStr = this.getString(R.string.voice_hi_str2, wakeupWords[0], minorWakeupWord);
        } else if (wakeupWords != null && wakeupWords.length == 2) {
            hiStr = this.getString(R.string.voice_hi_str2, wakeupWords[0], wakeupWords[1]);
        } else if (wakeupWords != null && wakeupWords.length > 0) {
            hiStr = this.getString(R.string.voice_hi_str, wakeupWords[0]);
        }
        try {
//            LogUtil.i("TAG",  "FD------play welcome TTS");
            DDS.getInstance().getAgent().getTTSEngine().speak(hiStr, 1);
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 认证广播
     */
    private BroadcastReceiver smartReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (VoiceConstant.ACTION_MIC_ENABLE.equals(intent.getAction())) {
                try {
//                    LogUtil.d(TAG, "FD-------com.fenda.smartcall.ACTION_MIC_ENABLE ");
                    DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            } else if (VoiceConstant.ACTION_MIC_ABLE.equals(intent.getAction())) {
                try {
//                    LogUtil.d(TAG, "FD-------com.fenda.smartcall.ACTION_MIC_ABLE ");
                    DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            } else if (VoiceConstant.ACTION_CLOSE_ALARM.equals(intent.getAction())) {
                if (listener != null) {
                    listener.closeAlarm();
                }
            } else if (VoiceConstant.ACTION_CLOSE_REMIND.equals(intent.getAction())) {
                if (listener != null) {
                    listener.closeRemind();
                }
            } else if (VoiceConstant.ACTION_CLOSE_QQMUSIC.equals(intent.getAction())) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Util.isTaskQQmusic(BaseApplication.getInstance())) {
                    MusicPlugin.get().getMusicApi().exit();
                }
            } else if (VoiceConstant.ACTION_CLOSE_VIEW.equals(intent.getAction())){
                if (speechView != null){
                    speechView.closeView();
                }
            }
        }
    };

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getAction();
            if (VoiceConstant.ACTION_INIT_COMPLETE.equals(name)) {
//                LogUtil.v(TAG, "FD-------init succes");
                enableWakeup();
            }
        }
    }

    /**
     * 打开唤醒，调用后才能语音唤醒
     */
    private void enableWakeup() {
        try {
            if (!isInit) {
                // 消息监听器
                mMessageObserver = new DuiMessageObserver();
                // dds更新监听器
                mUpdateObserver = new DuiUpdateObserver();
                // 命令监听器
                mCommandObserver = new DuiCommandObserver();
                mNativeObserver = new DuiNativeApiObserver();
                mNativeObserver.regist();
                mMessageObserver.regist(this);

//                mUpdateObserver.regist(this);

                mCommandObserver.regist();
                isInit = true;
            }
            if (AppUtils.isBindedDevice(getApplicationContext())) {
                DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
            } else {
                DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
            }
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();

        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mInitReceiver);
        if (mMessageObserver != null) {
            mMessageObserver.unregist();
        }
//        if (mUpdateObserver != null) {
//            mUpdateObserver.unregist();
//        }
        if (mCommandObserver != null) {
            mCommandObserver.unregist();
        }
        unregisterReceiver(authReceiver);
        unregisterReceiver(smartReceiver);
        Intent localIntent = new Intent();
        //销毁时重新启动Service
        localIntent.setClass(this, DDSService.class);
        this.startService(localIntent);
        // 在退出app时将dds组件注销
        DDS.getInstance().release();

    }

    /**
     * 创建dds配置信息
     *
     * @return
     */
    private DDSConfig createConfig() {
        DDSConfig config = new DDSConfig();
        // 产品ID -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_ID, "278584846");
        // 用户ID -- 必填
        config.addConfig(DDSConfig.K_USER_ID, "lavon.liyuanfang@fenda.com");
        // 产品的发布分支 -- 必填
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");
        //授权方式, 支持思必驰账号授权和profile文件授权 -- 必填
//        config.addConfig(DDSConfig.K_AUTH_TYPE, AuthType.PROFILE);
        // Product Key -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "4da9491ecef256aa1d586b9a293eef5c");
        // Product Secre -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "4446809239e306bbbef7a7051f125156");


        // 产品授权秘钥，服务端生成，用于产品授权 -- 必填
//        if (BuildConfig.LOG_DEBUG){
//            //包名com.fenda.ai
//            config.addConfig(DDSConfig.K_API_KEY, "9e7baf5eae8f9e7baf5eae8f5d5284f0");
//        }else {
//            //包名com.fenda.launcher
//            config.addConfig(DDSConfig.K_API_KEY, "ccfaebdea7f5ccfaebdea7f55d6e0baf");
//        }
        if (BuildConfig.LAUNCHER) {
            config.addConfig(DDSConfig.K_API_KEY, "ccfaebdea7f5ccfaebdea7f55d6e0baf");
        } else {
            config.addConfig(DDSConfig.K_API_KEY, "9e7baf5eae8f9e7baf5eae8f5d5284f0");
        }
//        config.addConfig(DDSConfig.K_API_KEY, "9e7baf5eae8f9e7baf5eae8f5d5284f0");


//        config.addConfig(DDSConfig.K_WAKEUP_DEBUG,"true");
//        // 资源更新配置项
        // 预置在指定目录下的DUI内核资源包名, 避免在线下载内核消耗流量, 推荐使用
        config.addConfig(DDSConfig.K_DUICORE_ZIP, "duicore.zip");
        // 预置在指定目录下的DUI产品配置资源包名, 避免在线下载产品配置消耗流量, 推荐使用
        config.addConfig(DDSConfig.K_CUSTOM_ZIP, "product.zip");
        //设置为false可以关闭dui内核的热更新功能，可以配合内置dui内核资源使用
        config.addConfig(DDSConfig.K_USE_UPDATE_DUICORE, "false");
        // 是否使用内置的资源更新通知栏
        config.addConfig(DDSConfig.K_USE_UPDATE_NOTIFICATION, "false");

        config.addConfig(DDSConfig.K_AEC_MODE, "external");
//        config.addConfig(DDSConfig.K_AUDIO_FOCUS_MODE, "external"); //TTS
        // 用于唤醒音频调试, 开启后在 "/sdcard/Android/data/包名/cache" 目录下会生成唤醒音频
//        config.addConfig(DDSConfig.K_TTS_DEBUG, "true");
        String num = SystemPropertiesProxyUtil.getString(this,"ro.mic.num");
        if ("2".equals(num)){
            config.addConfig(DDSConfig.K_MIC_TYPE, "5");
        }else {
            config.addConfig(DDSConfig.K_MIC_TYPE, "2");
        }
//        config.addConfig(DDSConfig.K_MIC_TYPE, "5");
        String androidId = DeviceIdUtil.getDeviceId(this);
        LogUtil.i("ANDROID_ID = " + androidId);
        //填入唯一的deviceId -- 选填
        config.addConfig(DDSConfig.K_DEVICE_ID, androidId);

        //过滤 ‘嗯’ ‘哦’等语气词
        config.addConfig(DDSConfig.K_VAD_DISABLE_SIGNAL,"true");
        // 麦克风阵列配置项
        //config.addConfig(DDSConfig.K_MIC_TYPE, "2"); // 设置硬件采集模组的类型 0：无。默认值。 1：单麦回消 2：线性四麦 3：环形六麦 4：车载双麦 5：家具双麦


       /* config.addConfig(DDSConfig.K_PRODUCT_ID, "278581328"); // 产品ID
        config.addConfig(DDSConfig.K_USER_ID, "lavon.liyuanfang@fenda.com");  // 用户ID
        config.addConfig(DDSConfig.K_ALIAS_KEY, "test");   // 产品的发布分支
        config.addConfig(DDSConfig.K_AUTH_TYPE, AuthType.PROFILE); //授权方式, 支持思必驰账号授权和profile文件授权
        config.addConfig(DDSConfig.K_API_KEY, "73f12a5157b973f12a5157b95cce8e6d");  // 产品授权秘钥，服务端生成，用于产品授权
        config.addConfig(DDSConfig.K_PRODUCT_KEY, "439bd1275f761e015d0986cd027ec497");// Product Key -- 必填
        config.addConfig(DDSConfig.K_PRODUCT_SECRET, "789986f188b5ef722a46e7ce6dc2a7a2");// Product Secre -- 必填
        config.addConfig(DDSConfig.K_DUICORE_ZIP, "duicore.zip"); // 预置在指定目录下的DUI内核资源包名, 避免在线下载内核消耗流量, 推荐使用
        config.addConfig(DDSConfig.K_CUSTOM_ZIP, "product.zip"); // 预置在指定目录下的DUI产品配置资源包名, 避免在线下载产品配置消耗流量, 推荐使用
        config.addConfig(DDSConfig.K_USE_UPDATE_DUICORE, "false"); //设置为false可以关闭dui内核的热更新功能，可以配合内置dui内核资源使用
        config.addConfig(DDSConfig.K_USE_UPDATE_NOTIFICATION, "false"); // 是否使用内置的资源更新通知栏
        config.addConfig(DDSConfig.K_DEVICE_ID,  "000003002");*/
//        LogUtil.i(TAG, "config->" + config.toString());

        return config;
    }

    @Override
    public void onMessage() {

    }

    /**
     * DuiMessageObserver中当前状态的回调
     */
    @Override
    public void onState(String message, final String state) {
        LogUtil.v(TAG, "FD-----" + message + "||||||" + state);
        switch (message) {
            case VoiceConstant.SIBICHI.SYS_DIALOG_START:
                //唤醒时点亮屏幕
                EventBusUtils.post(Constant.Common.SCREEN_ON);
                if (BaseApplication.getBaseInstance().isRemindRing()) {
                    IRemindProvider provider = (IRemindProvider) ARouter.getInstance().build(RouterPath.REMIND.ALARM_SERVICE).navigation();
                    provider.closeAlarm(null);
                }
                break;
            case VoiceConstant.SIBICHI.SYS_DIALOG_END:
                speechView.closeView();
                break;
            // case "sys.dialog.state":
            //    handleMesagestate(state);
            //   break;
            case VoiceConstant.SIBICHI.CONTEXT_INPUT_TEXT:
                String text = Jsonparse.parseInputText(state);
                speechView.showView(text);
                break;
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_MEDIA:
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_WEB:
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_LIST:
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_CONTENT:
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_CUSTOM:
                HandleMessage(message, state);
                break;
            case VoiceConstant.SIBICHI.CONTEXT_OUTPUT_TEXT:
//                String txt = "";

                break;
            case VoiceConstant.SIBICHI.ASR_SPEECH_RESULT:
//                LogUtil.e("SIBICHI_TEST","识别到结果 : " + state);
                time = System.currentTimeMillis();
                break;
            case VoiceConstant.SIBICHI.DM_OUTPUT:
                JSONObject jo = null;
                try {
                    jo = new JSONObject(state);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String task = jo.optString("skill");
//                String task = dm.optString("task");
                if ("日历".equals(task)) {

                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            emitter.onNext("");
                        }
                    }).compose(RxSchedulers.<String>io_main())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    if (calendarProvider == null) {
                                        calendarProvider = ARouter.getInstance().navigation(ICalendarProvider.class);
                                    }
                                    calendarProvider.processCalendarMsg(state);
                                }
                            });
                } else if (task.equals("股票")) {
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            emitter.onNext("");
                        }
                    }).compose(RxSchedulers.<String>io_main())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    if (provider == null) {
                                        provider = ARouter.getInstance().navigation(IEncyclopediaProvider.class);
                                    }
                                    provider.processSharesMsg(state);
                                }
                            });
                } else if (task.equals("闲聊") || task.equals("百科")) {
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            emitter.onNext("");
                        }
                    }).compose(RxSchedulers.<String>io_main())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    if (provider == null) {
                                        provider = ARouter.getInstance().navigation(IEncyclopediaProvider.class);
                                    }
                                    provider.processQuestionTextMsg(state);
                                }
                            });
                }else if ( task.equals("成语")){
                    if (provider == null) {
                        provider = ARouter.getInstance().navigation(IEncyclopediaProvider.class);
                    }
                    provider.processIdiomTextMsg(state);
                }else if (task.equals("星座运势")){
                    Observable.create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                            emitter.onNext("");
                        }
                    }).compose(RxSchedulers.<String>io_main())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String s) throws Exception {
                                    if (provider == null) {
                                        provider = ARouter.getInstance().navigation(IEncyclopediaProvider.class);
                                    }
                                    provider.processConstellationTextMsg(state);
                                }
                            });
                }
                break;
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_TEXT:

                break;
            default:


        }
    }

    private void HandleMessage(String message, final String data) {
        LogUtil.v(TAG, "HandleMessage-----" + message + "||||||" + data);

        switch (message) {

            case VoiceConstant.SIBICHI.CONTEXT_INPUT_TEXT:
                try {
                    JSONObject jo = new JSONObject(data);
                    if (jo.has("var")) {
                        String var = jo.optString("var", "");
                        if (isFirstVar) {
                            isFirstVar = false;
                            hasvar = true;
                        } else {

                        }
                    }
                    if (jo.has("text")) {
                        if (hasvar) {

                            hasvar = false;
                            isFirstVar = true;
                        }
                        String text = jo.optString("text", "");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_CONTENT:
                try {
                    JSONObject jo = new JSONObject(data);
                    String title = jo.optString("title", "");
                    String subTitle = jo.optString("subTitle", "");
                    String imgUrl = jo.optString("imageUrl", "");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_LIST:
                try {
                    JSONObject jo = new JSONObject(data);
                    String intentName = jo.optString("intentName");
                    if ("关闭提醒".equals(intentName) || "删除提醒".equals(intentName)) {
                        String topClass = Util.isTopTaskClass(BaseApplication.getInstance());
                        LogUtil.e("topClass = " + topClass);
                        if (listener != null && !topClass.equals("com.fenda.remind.AlarmListActivity")) {
                            listener.queryRemind(data);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_WEB:
                try {
                    JSONObject jo = new JSONObject(data);
                    String url = jo.optString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            //media widget 1
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_MEDIA:
                mediaHandler(data);
                break;
            //custom widget 1 收到自定义控件消息
            case VoiceConstant.SIBICHI.CONTEXT_WIDGET_CUSTOM:
//                if (AccessibilityMonitorService.getTopPackageName().equals(QIYIMOBILE_PKG))
                LogUtil.e("CONTEXT_WIDGET_CUSTOM  == > " + data);

                try {
                    JSONObject object = new JSONObject(data);
                    String widgetName = object.getString("widgetName");
                    if ("weather".equals(widgetName)) {
                        //天气
                        Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                                emitter.onNext("");
                            }
                        }).compose(RxSchedulers.<String>io_main())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String s) throws Exception {


                                        if (weatherProvider == null) {
                                            weatherProvider = ARouter.getInstance().navigation(IWeatherProvider.class);
                                        }
                                            if (weatherProvider != null) {
                                                weatherProvider.weatherFromVoiceControl(data);
                                            }
                                    }
                                });
                    } else if ("stock".equals(widgetName)) {
                        //股票


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
            default:
        }
    }

    private MediaModel mediaModel;

    public void mediaHandler(String data) {
        JSONObject object = new JSONObject();
        try {
            if (data != null) {
                object = new JSONObject(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String widgetName = object.optString("widgetName", "");
        if (TextUtils.isEmpty(widgetName)) {
            throw new RuntimeException("widget name must not be empty");
        }
        String intentName = object.optString("intentName");
        if (!"播放影视".equals(intentName)) {
            if (intentName.length() > 2) {
                intentName = intentName.substring(2);
            }
            if (mediaModel == null) {
                mediaModel = new MediaModel();
            }
            mediaModel.handleMusicMediaWidget(object, intentName);
        }


//        if ("music".equals(widgetName)){
//            //笑话 电台 曲艺  故事 新闻 电台
//            String intentName = object.optString("intentName");
//            if (intentName.length() > 2){
//                intentName = intentName.substring(2);
//            }
//
//
//
//
////            DispatchManager.startService(widgetName,intentName,data, VoiceConstant.AIDL.LAUNCHER);
//        }else {
//            String intentName = object.optString("intentName");
//            if (intentName.length() > 2){
//                intentName = intentName.substring(2);
//            }
//            DispatchManager.startService(widgetName,intentName,data, VoiceConstant.AIDL.LAUNCHER);

//        }
//        if (FDApplication.packageNameMap.get(VoiceConstant.qqMusic) != null){
//            MusicPlugin.get().getMusicApi().pause();
//        }

    }

    @Override
    public void onUpdate(int type, String result) {
//        LogUtil.d(TAG, "onUpdate type: " + type +  "result:" + result);

    }
}
