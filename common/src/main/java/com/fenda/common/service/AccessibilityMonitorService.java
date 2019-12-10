package com.fenda.common.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.provider.VoicemailContract;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IPlayerProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.util.AppTaskUtil;
import com.fenda.common.util.LogUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


/**
  * @author mirrer.wangzhonglin
  * @Date 2019/9/2 15:52
  * @Description
  *
  */
public class AccessibilityMonitorService extends AccessibilityService {
    private static final String TAG = "AccessService";
    public static final String QIYIMOBILE_PKG = "com.qiyi.video.speaker";


    private CharSequence mWindowClassName;
    private static HashMap<String,String> packageMap = new HashMap<>();
    private MyBroadcast broadcast;
    private IVoiceRequestProvider provider;


    @Override
    public void onCreate() {
        super.onCreate();
        broadcast = new MyBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("back_activity");
        registerReceiver(broadcast,intentFilter);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int type = event.getEventType();
        switch (type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                mWindowClassName = event.getClassName();
                String mPackage = event.getPackageName() == null ? "" : event.getPackageName().toString();

                if (QIYIMOBILE_PKG.equals(mPackage)){
                     if (provider == null){
                         provider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
                     }
                     provider.cancelQQMusic();
                }
                packageMap.put("key",mPackage);
                if (mWindowClassName != null){
                    packageMap.put("class",mWindowClassName.toString());
                }

                break;
            default:
                break;

        }
    }

    public static String getTopPackageName() {
        return packageMap.get("key");
    }
    public static String getTopClassName() {
        return packageMap.get("class");
    }

    @Override
    public void onInterrupt() {

    }


    public  void back(){
        this.performGlobalAction(GLOBAL_ACTION_BACK);
    }

    /**
     * 检查系统设置：是否开启辅助服务
     *
     * @param service 辅助服务
     */
    public static boolean isSettingOpen(Class service, Context cxt) {
        try {
            int enable = Settings.Secure.getInt(cxt.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED, 0);
            if (enable != 1){
                return false;
            }
            String services = Settings.Secure.getString(cxt.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (!TextUtils.isEmpty(services)) {
                TextUtils.SimpleStringSplitter split = new TextUtils.SimpleStringSplitter(':');
                split.setString(services);
                // 遍历所有已开启的辅助服务名
                while (split.hasNext()) {
                    if (split.next().equalsIgnoreCase(cxt.getPackageName() + "/" + service.getName())){
                        return true;
                    }
                }
            }
        } catch (Throwable e) {//若出现异常，则说明该手机设置被厂商篡改了,需要适配
//            LogUtil.e(TAG, "isSettingOpen: " + e.getMessage());
        }
        return false;
    }

    /**
     * 跳转到系统设置：开启辅助服务
     */
    public static void jumpToSetting(final Context cxt) {
        try {
            cxt.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } catch (Throwable e) {//若出现异常，则说明该手机设置被厂商篡改了,需要适配
            try {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                cxt.startActivity(intent);
            } catch (Throwable e2) {
//                LogUtil.e(TAG, "jumpToSetting: " + e2.getMessage());
            }
        }

    }


    private class MyBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            back();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcast != null){
            unregisterReceiver(broadcast);
        }
    }
}