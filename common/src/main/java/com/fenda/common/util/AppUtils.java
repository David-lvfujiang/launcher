package com.fenda.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import java.util.List;


public class AppUtils {

    private static final String FILE_NAME = "fd_sp_data";
    private static final String KEY_SP_BIND_DEVICE = "key_sp_binddevice";
    private static final String KEY_SP_REGISTER_DEVICE= "key_sp_registerdeviceid";
    private static final String KEY_SP_WIFI_CHECKED= "key_sp_wifichecked";


    public static void jump2SmartCallApp(final Context context) {

        PackageManager packageManager = context.getPackageManager();
        if (checkPackInfo(context, "com.fenda.smartcall")) {
            Intent intent = packageManager.getLaunchIntentForPackage("com.fenda.smartcall");
            context.startActivity(intent);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.show("没有安装语音通话");
                }
            });
        }
    }

    /**
     * 检查包是否存在
     *
     * @param packname
     * @return
     */
    private static boolean checkPackInfo(Context context, String packname) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packname, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     *获取当前本地APK的版本
    * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext)
    {
        int versionCode = 0;
        try{
            versionCode =mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0).versionCode;
        }catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     *获取版本号名称
     * @param context
     * @return
     */
    public static String getVerName(Context context)
    {
        String verName = "";
        try{
            verName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        }catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return verName;
    }

    public static Boolean isBindedDevice(Context context){

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_SP_BIND_DEVICE, false);
    }

    public static void saveBindedDevice(Context context, boolean isBind){

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SP_BIND_DEVICE, isBind).apply();
    }

    public static Boolean isWifiChecked(Context context){

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        return sp.getBoolean(KEY_SP_WIFI_CHECKED, false);
    }

    public static void saveWifiChecked(Context context, boolean isBind){

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SP_WIFI_CHECKED, isBind).apply();
    }

    public static Boolean isRegisterDevice(Context context){

        SharedPreferences sp1 = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp1.getBoolean(KEY_SP_REGISTER_DEVICE, false);
    }

    public static void saveRegisterDevice(Context context, boolean isRegister){
        SharedPreferences sp2 = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp2.edit().putBoolean(KEY_SP_REGISTER_DEVICE, isRegister).apply();
    }


    public static String getTopAppClassName(Context mContext){
        String className = null;
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        if (taskInfos.size() > 0){
            className = taskInfos.get(0).topActivity.getClassName();
        }
        return className;

    }

    public static String getTopAppPackageName(Context mContext){
        String packageName = null;
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = manager.getRunningTasks(Integer.MAX_VALUE);
        if (taskInfos.size() > 0){
            packageName = taskInfos.get(0).topActivity.getPackageName();
        }
        return packageName;

    }

}
