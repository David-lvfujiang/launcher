package com.fenda.common.service;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

import com.fenda.common.util.AppTaskUtil;
import com.fenda.common.util.LogUtil;

import java.util.HashMap;


/**
  * @author mirrer.wangzhonglin
  * @Date 2019/9/2 15:52
  * @Description
  *
  */
public class AccessibilityMonitorService extends AccessibilityService {
    private static final String TAG = "AccessService";


    private CharSequence mWindowClassName;
    private static HashMap<String,String> packageMap = new HashMap<>();
    private MyBroadcast broadcast;


    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("===========================AccessibilityMonitorService=============================");
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
                String className = event.getClassName().toString();
//                if (MUSIC_PKG.equals(mPackage) ){
////                    if (FDApplication.packageNameMap.get(qqMusic) == null){
////                        FDApplication.packageNameMap.put(qqMusic,MUSIC_PKG);
////                    }
////                    Log.i(TAG," mediaControlMap ==> Put = "+PLAY);
////                    MediaControl.mediaControlMap.put(MUSIC_PKG,PLAY);
//                }else if (QIYIMOBILE_PKG.equals(mPackage) ){
//                    if (FDApplication.packageNameMap.get(aqyVideo) == null){
//                        FDApplication.packageNameMap.put(aqyVideo,QIYIMOBILE_PKG);
//                    }
//                    if (FDApplication.packageNameMap.get(qqMusic) != null ){
//                        LogUtil.i(TAG," mediaControlMap ==> Put = MUSIC_PKG  : "+PAUSE);
//                        MediaControl.mediaControlMap.put(MUSIC_PKG,PAUSE);
//                        MusicPlugin.get().getMusicApi().pause();
//                    }
//
//                    MediaControl.mediaControlMap.put(QIYIMOBILE_PKG,PLAY);
//                }else if (LAUNCHER.equals(mPackage)){
//                    if (FDApplication.packageNameMap.get(launcher) == null){
//                        FDApplication.packageNameMap.put(launcher,LAUNCHER);
//                    }
//                    MediaControl.mediaControlMap.put(LAUNCHER,PLAY);
//                    MediaControl.mediaControlMap.put(QIYIMOBILE_PKG,STOP);
//                }else if (className.equals(Constant.SMART_CALL_CLASSNAME)){
//                    if (FDApplication.packageNameMap.get(qqMusic) != null && MediaControl.mediaControlMap.get(MUSIC_PKG) == PLAY){
//                        MediaControl.mediaControlMap.put(MUSIC_PKG,PAUSE);
//                        MusicPlugin.get().getMusicApi().pause();
//                    }
//                }

//                if (!"com.fenda.ai".equals(mPackage)){
//                    packageMap.put("key",mPackage);
//                    packageMap.put("class",className);
//                }

                String mPack = AppTaskUtil.getAppTopPackage();

                packageMap.put("key",mPack);
                packageMap.put("class",className);

                LogUtil.e("mPackage = "+mPackage +" className = "+className+" mPack = "+mPack);
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