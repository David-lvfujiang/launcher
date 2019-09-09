package com.fenda.common.constant;

import android.net.Uri;

import com.fenda.common.BaseApplication;
import com.fenda.common.R;
import com.fenda.common.db.ContentProviderManager;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/29 11:31
 * @Description
 */
public interface Constant {
    /**
     * 播放器组件
     */
    interface Player {
        /**
         * 笑话
         */
        int JOKE = 1;
        /**
         * 资讯
         */
        int NEW_CONSULT = 2;
        /**
         * 曲艺、故事
         */
        int CROSS_TALLK = 3;
        /**
         * 诗词
         */
        int POETRY = 4;
        /**
         * 广播
         */
        int FM = 5;

        /**
         * 列表循环
         */
        int LIST_CYCLE_PLAY = 0;
        /**
         * 单曲循环
         */
        int SINGLE_CYCLE_PLAY = 1;
        /**
         * 随机播放
         */
        int RANDOM_PLAY = 2;

        String keyBroadcastSelectItem = "keyBroadcastSelectItem";
        String keyDataMusicList = "keyDataMusicList";
        String keyCurrentPlayIndex = "keyCurrentPlayIndex";
        String keyContentType = "contentType";
        String keyBroadcastMusicList = "keyBroadcastMusicList";
        String keyDataMusicKey = "keyDataMusicKey";
        String closeVoiceBroadcast = "closeVoiceBroadcast";

        String VOICE_PLAY = "voice_play";
        String VOICE_PAUSE = "voice_pause";
        String VOICE_STOP = "voice_stop";
        String VOICE_REPLAY = "voice_replay";
        String VOICE_PREV = "voice_prev";
        String VOICE_NEXT = "voice_next";
        String VOICE_LOOP_LIST_PLAY = "voice_loop_list_play";
        String VOICE_LOOP_SINGLE_PLAY = "voice_loop_sing_play";
        String CLOSE_MUSIC_ACTIVITY = "close_music_activity";
        String VOICE_RANDOM_PLAY = "voice_random_play";
        String VOICE_FORWARD = "voice_forward";
        String VOICE_BACKWARD = "voice_backward";

    }


    /**
     * 提醒组件
     */
    interface Remind {
        String ALARM_LIST = "alarmList";
        String ALARM_TYPE = "alarmType";
        String CLASS_NAME = "AlarmActivity";
        String CREATE_REMIND = "createRemind";
        String DELETE_REMIND = "deleteRemind";
        String QUERY_REMIND = "queryRemind";
        String ALARM_REMIND = "alarmRemind";

        String ACTION_CLOSE_ALARM = "com.fenda.lucher.ACTION_CLOSE_ALARM";
        String ACTION_CLOSE_REMIND = "com.fenda.lucher.ACTION_CLOSE_REMIND";

        int DELETE_ALARM = 2;
        int CLOSE_ALARM = 4;
    }

    /**
     * 音视频组件
     */
    interface Call {
        String TOKEN = "token";
    }

    /**
     * 设置组件
     */
    interface Settings {
        String USER_ID = "userId";
        String DEVICE_ID = "deviceId";
        String DEVICE_NAME = "device_name";
        String DEVICE_ICON = "device_icon";
        String VCODE = "dimensional_code";
        String RONGYUNCLOUDTOKEN = "rongyunCloud_Token";
        String DEVICE_STATUS = "device_status";
//        boolean DEVICE_STATUS = "false";
    }

    interface common {
        Uri URI = Uri.parse(ContentProviderManager.BASE_URI + "/user");
    }

}
