package com.fenda.common.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fenda.common.activity.SettingsScreenDigClockActivity;


/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/25 18:49
 */
public class ScreenSaverManager {
    private static final String TAG = "ScreenSaverManager";

    private static Context mContext;
    private static long lastTime = 0;//最后一次点击的时间

    public static final int PLAY_TIME = 1 * 60 * 1000;//一分钟没有触发就播放视频

    public static final int SEND_TIME = 3 * 1000;//每3秒钟发送一次信息

    public static void init(Context context) {
        mContext = context;
    }

    public static void startMonitor() {
        handler.sendEmptyMessageDelayed(0, SEND_TIME);//每3秒钟发送一次信息
    }

    /**
     * 设置当前时间
     */
    public static void eliminateEvent() {
        lastTime = System.currentTimeMillis();
        Log.e(TAG, "lastTime = " + lastTime);
    }

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            long idleTime = System.currentTimeMillis() - lastTime;//闲置时间：当前的时间减去最后一次触发时间
            Log.i(TAG,"now = "+System.currentTimeMillis());
            Log.i(TAG, " idleTime = " + idleTime);
            if (idleTime >= PLAY_TIME) {
                goPlayActivity(); //跳转到视频播放页面
            } else {
                handler.sendEmptyMessageDelayed(0, SEND_TIME);
            }
        }
    };

    private static void goPlayActivity() {
        Intent intent = new Intent(mContext, SettingsScreenDigClockActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
