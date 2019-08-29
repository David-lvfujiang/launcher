package com.fenda.common.constant;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/29 11:31
 * @Description
 */
public interface Constant {


    interface Player{
        /**
         * 笑话
         */
        int JOKE = 1;
        /**
         * 资讯
         */
        int NEW_CONSULT =  2;
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
        int FM      = 5;

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



}
