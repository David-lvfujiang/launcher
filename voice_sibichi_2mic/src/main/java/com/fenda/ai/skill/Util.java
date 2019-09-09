package com.fenda.ai.skill;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.fenda.ai.VoiceConstant;
import com.fenda.common.BaseApplication;
import com.fenda.common.service.AccessibilityMonitorService;
import com.fenda.common.R;
import com.fenda.common.util.AppTaskUtil;


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
            if (VoiceConstant.MUSIC_PKG.equals(AppTaskUtil.getAppTopPackage()) && !BaseApplication.QQMUSIC.isEmpty()){
                return true;
            }else {
                boolean isLauncher = AppTaskUtil.getAppTopPackage().equals(VoiceConstant.LAUNCHER) && (BaseApplication.getInstance().isNewsPlay() || BaseApplication.getInstance().isMusicPlay());
                if (!AppTaskUtil.getAppTopPackage().equals(VoiceConstant.QIYIMOBILE_PKG) && !isLauncher){
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
        if (AppTaskUtil.getAppTopPackage().equals(VoiceConstant.LAUNCHER) && (BaseApplication.getInstance().isMusicPlay() || BaseApplication.getInstance().isNewsPlay())){
            return true;
        }
        return false;
    }
}


