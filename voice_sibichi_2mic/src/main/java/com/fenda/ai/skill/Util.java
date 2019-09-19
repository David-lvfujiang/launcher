package com.fenda.ai.skill;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.fenda.ai.VoiceConstant;
import com.fenda.common.BaseApplication;
import com.fenda.common.service.AccessibilityMonitorService;
import com.fenda.common.R;
import com.fenda.common.util.AppTaskUtil;

import java.util.List;



public class Util {
    public static Notification pupNotification(Context mcontext, PendingIntent pi, String state){
        Notification notification = null;
        NotificationManager mNotificationManager;

        String id ="channel_service";
        CharSequence name = "aispeech recorder";
        int importance =NotificationManager.IMPORTANCE_HIGH;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mNotificationManager = (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
                notification = new Notification.Builder(mcontext,id)
                        .setContentTitle("奋达AI")
                        .setContentText(state)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pi)
                        .build();
            }
        }else {
            notification = new Notification.Builder(mcontext)
                    .setContentTitle("奋达AI")
                    .setContentText(state)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(mcontext.getResources(), R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .build();
        }
        return notification;
    }


    public static String getTopApp(Context mContext) {

        return AccessibilityMonitorService.getTopClassName();
//        String curAppTaskClassName = "";
//        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> appTask = am.getRunningTasks(Integer.MAX_VALUE);
//        if (appTask.size() > 0) {
//            curAppTaskClassName = appTask.get(0).topActivity.getClassName();
//        }
////        LogUtil.d(TAG, "curAppTaskPackgeName = " + curAppTaskPackgeName +
////                "  curAppTaskClassName = " + curAppTaskClassName);
////                  if(curAppTaskPackgeName.equals(DREAMING_PACKAGE_NAME) && curAppTaskClassName.indexOf(DREAMING_CLASS_NAME) != -1){
////             				//true 表示在当前界面
////                  					//DREAMING_PACKAGE_NAME 需要判断APP的包名
////                         				//DREAMING_CLASS_NAME  需要判断APP的类名
////                                          }
////    }
//        return curAppTaskClassName;
    }

    /**
     * 判断音乐播放器是否在前台,在后台时是否有其它的播放器在前台
     * @return
     */
    public static boolean isQQMusicPlay(){
            if (isTopTaskPackage(BaseApplication.getInstance()).equals(VoiceConstant.MUSIC_PKG)){
                return true;
            }else {
                boolean isLauncher = AppTaskUtil.getAppTopPackage().equals(VoiceConstant.LAUNCHER) && (BaseApplication.getBaseInstance().isNewsPlay() || BaseApplication.getBaseInstance().isMusicPlay());
                if (!AppTaskUtil.getAppTopPackage().equals(VoiceConstant.QIYIMOBILE_PKG) && !isLauncher && isTaskQQmusic(BaseApplication.getInstance())){
                    return true;
                }
            }
        return false;
    }

    /**
     * 判断爱奇艺是不是在后台
     * @return
     */
    public static boolean isQIYIPlay(){
            if (VoiceConstant.QIYIMOBILE_PKG.equals(AppTaskUtil.getAppTopPackage())){
                return true;
            }
        return false;

    }


    /**
     * 判断launcher播放器是否在前台
     * @return
     */
    public static boolean isLauncherMusic(){
        if (isTopTaskPackage(BaseApplication.getInstance()).equals(VoiceConstant.LAUNCHER) && (BaseApplication.getBaseInstance().isMusicPlay() || BaseApplication.getBaseInstance().isNewsPlay())){
            return true;
        }
        return false;
    }

    /**
     * 查看后台进程是否有QQ音乐
     * @param mContext
     * @return
     */
    public static boolean isTaskQQmusic(Context mContext){
        ActivityManager mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        Log.e("TAG","taskList = "+taskList.size());
        for (ActivityManager.RunningTaskInfo taskInfo : taskList) {
            Log.i("TAG","taskList = INFO "+taskInfo.topActivity.getPackageName());
            if (VoiceConstant.MUSIC_PKG.equals(taskInfo.topActivity.getPackageName())){
                return true;
            }
        }
        return false;
    }
    /**
     * 查看前台进程是否是QIYI
     * @return
     */
    public static boolean isTopTaskQIYI(){
        if (isTopTaskPackage(BaseApplication.getInstance()).equals(VoiceConstant.QIYIMOBILE_PKG)){
            return true;
        }
        return false;
    }
    public static String isTopTaskPackage(Context mContext){

        ActivityManager mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        Log.e("TAG","taskList = "+taskList.size());
        if (taskList != null){
            return taskList.get(0).topActivity.getPackageName();
        }
        return "";

    }
    public static String isTopTaskClass(Context mContext){

        ActivityManager mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        Log.e("TAG","taskList = "+taskList.size());
        if (taskList != null){
            return taskList.get(0).topActivity.getClassName();
        }
        return "";

    }


    public static void moveQQmusicTask(Context mContext){
        ActivityManager mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo taskInfo : taskList) {
            if (VoiceConstant.MUSIC_PKG.equals(taskInfo.topActivity.getPackageName())){
                mAm.moveTaskToFront(taskInfo.id,0);
                return;
            }
        }
    }







}


