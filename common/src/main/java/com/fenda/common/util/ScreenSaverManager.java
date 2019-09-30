package com.fenda.common.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.fenda.common.activity.SettingsScreenDigClockActivity;
import com.fenda.common.activity.SettingsScreenSimClockActivity;
import com.fenda.common.constant.Constant;

import java.util.Date;


/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/25 18:49
 */
public class ScreenSaverManager {
    private static final String TAG = "ScreenSaverManager";

    private static Context mContext;
    //最后一次点击的时间
    private static long lastTime = 0;
    //一分钟没有触发就播放视频
    public static int PLAY_TIME; // = 30 * 1000;
    //每3秒钟发送一次信息
    public static final int SEND_TIME = 3 * 1000;

    public static void init(Context context) {
        mContext = context;
    }

    /* 上一次User有动作的Time Stamp */
    private Date lastUpdateTime;
    /* 计算User有几秒没有动作的 */
    private long timePeriod;
    /* 静止超过N秒将自动进入屏保 */
    private float mHoldStillTime = 10;

    /*时间间隔*/
    private long intervalScreenSaver = 1000;
    private long intervalKeypadeSaver = 1000;


    private Handler mHandler01 = new Handler();
    private Handler mHandler02 = new Handler();


//    public static void startMonitor() {
//        //每3秒钟发送一次信息
//        handler.sendEmptyMessageDelayed(0, SEND_TIME);
//    }

    /**
     * 设置当前时间
     */
//    public static void eliminateEvent() {
//        lastTime = System.currentTimeMillis();
//        Log.e(TAG, "lastTime = " + lastTime);
//    }
//
//    private static Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            long idleTime = System.currentTimeMillis() - lastTime;//闲置时间：当前的时间减去最后一次触发时间
//            Log.i(TAG,"now = "+System.currentTimeMillis());
//            Log.i(TAG, " idleTime = " + idleTime);
//            String time01 = (String) SPUtils.get(mContext, Constant.Settings.SCREEN_TIME, "");
//            LogUtil.d(TAG, "standby time  = " + time01);
//            if("20分钟".equals(time01)){
//                PLAY_TIME = 20 * 1000;
//            } else if("10分钟".equals(time01)){
//                PLAY_TIME = 10 * 1000;
//            } else {
//                PLAY_TIME = 30 * 1000;
//            }
//            LogUtil.d(TAG, "PLAY_TIME  = " + PLAY_TIME);
//
//            if (idleTime >= PLAY_TIME) {
//                if(AppUtils.isRunScreenSaver(mContext)){
//                    /*屏保正在显示中*/
//
//                } else { //说明没有进入屏保
//                    /* 启动线程去显示屏保 */
//                    mHandler02.postAtTime(mTask02, intervalScreenSaver);
//                    /*显示屏保置为true*/
//                    AppUtils.saveRunScreenSaver(mContext, true);
//
//                }
//                goPlayActivity(); //跳转到相应屏保
//            } else {
//                /*说明静止之间没有超过规定时长*/
//                AppUtils.saveRunScreenSaver(mContext, false);
//
////                handler.sendEmptyMessageDelayed(0, SEND_TIME);
//            }
//            /*反复调用自己进行检查*/
//            handler.sendEmptyMessageDelayed(0, SEND_TIME);
//        }
//    };

//    private static void goPlayActivity() {
//        String style01 = (String) SPUtils.get(mContext, Constant.Settings.SCREEN_STYLE, "");
//        LogUtil.d(TAG, "standby style = " + style01);
//        if("模拟时钟".equals(style01)){
//            Intent intent = new Intent(mContext, SettingsScreenSimClockActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//
//        } else if ("数字时钟 ( 默认)".equals(style01)){
//            Intent intent = new Intent(mContext, SettingsScreenDigClockActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//
//        }
////        else {
////            Intent intent = new Intent(mContext, SettingsScreenDigClockActivity.class);
////            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            mContext.startActivity(intent);
////        }
//    }


    /**
     * 计时线程
     */
    private Runnable mTask01 = new Runnable() {

        @Override
        public void run() {
            Date timeNow = new Date(System.currentTimeMillis());
            /* 计算User静止不动作的时间间距 */
            /**当前的系统时间 - 上次触摸屏幕的时间 = 静止不动的时间**/
            timePeriod = (long) timeNow.getTime() - (long) lastUpdateTime.getTime();

            /*将静止时间毫秒换算成秒*/
            float timePeriodSecond = ((float) timePeriod / 1000);

            if(timePeriodSecond > mHoldStillTime){
                if(AppUtils.isRunScreenSaver(mContext)){
                    /*屏保正在显示中*/

                }else{  //说明没有进入屏保
                    /* 启动线程去显示屏保 */
                    mHandler02.postAtTime(mTask02, intervalScreenSaver);
                    /*显示屏保置为true*/
                    AppUtils.saveRunScreenSaver(mContext, true);
                }
            }else{
                /*说明静止之间没有超过规定时长*/
                AppUtils.saveRunScreenSaver(mContext, false);
            }
            /*反复调用自己进行检查*/
            mHandler01.postDelayed(mTask01, intervalKeypadeSaver);
        }
    };



    /**
     * 持续屏保显示线程
     */
    private Runnable mTask02 = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (AppUtils.isRunScreenSaver(mContext)) {  //如果屏保正在显示，就计算不断持续显示
//				hideOriginalLayout();
                showScreenSaver();
                mHandler02.postDelayed(mTask02, intervalScreenSaver);
            } else {
                mHandler02.removeCallbacks(mTask02);  //如果屏保没有显示则移除线程
            }
        }
    };

    /**
     * 显示屏保
     */
    private void showScreenSaver(){
        Log.d("danxx", "显示屏保------>");

        String style01 = (String) SPUtils.get(mContext, Constant.Settings.SCREEN_STYLE, "");
        LogUtil.d(TAG, "standby style = " + style01);
        if("模拟时钟".equals(style01)){
            Intent intent = new Intent(mContext, SettingsScreenSimClockActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        } else if ("数字时钟 ( 默认)".equals(style01)){
            Intent intent = new Intent(mContext, SettingsScreenDigClockActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

        }
//        else {
//            Intent intent = new Intent(mContext, SettingsScreenDigClockActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(intent);
//        }

    }


}
