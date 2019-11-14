package com.fenda.ai;

import android.os.Environment;

import java.io.File;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/2 8:59
 * @Description
 */
public class VoiceConstant {

    public static String sVoiceDir= Environment.getExternalStorageDirectory() + File.separator + "voice";

    public static final String MUSIC_PKG = "com.tencent.qqmusiccar";
    public static final String QIYIMOBILE_PKG = "com.qiyi.video.speaker";
    public static final String LAUNCHER = "com.fenda.launcher";
    public static final String MUSIC_CLASS_NAME = "com.fenda.player.MusicActivity";
    public static final String NEWS_CLASS_NAME = "com.fenda.lucher.news.NewsActivity";
    public static final String ACTION_INIT_COMPLETE = "ddsdemo.action.init_complete";
    public static final String ACTION_AUTH_SUCCESS = "ddsdemo.action.auth_success";
    public static final String ACTION_AUTH_FAILED = "ddsdemo.action.auth_failed";
    public static final String ACTION_MIC_ENABLE = "com.fenda.smartcall.ACTION_MIC_ENABLE";
    public static final String ACTION_MIC_ABLE = "com.fenda.smartcall.ACTION_MIC_ABLE";
    public static final String SMART_CALL_CLASSNAME = "io.rong.callkit.SingleCallActivity";
    public static final String ACTION_CLOSE_ALARM = "com.fenda.lucher.ACTION_CLOSE_ALARM";
    public static final String ACTION_CLOSE_REMIND = "com.fenda.lucher.ACTION_CLOSE_REMIND";
    public static final String ACTION_CLOSE_QQMUSIC = "com.fenda.lucher.ACTION_CLOSE_QQMUSIC";
    public static final String ACTION_CLOSE_VIEW    = "com.fenda.launcher.ACTION_CLOSE_VIEW";


    public static final int PLAY = 0;
    public static final int PAUSE = 1;
    public static final int STOP = 2;

    public static final int MEDIA_PLAY = 0;
    public static final int MEDIA_PAUSE = 1;
    public static final int MEDIA_STOP = 2;
    public static final int MEDIA_REPLAY = 3;
    public static final int MEDIA_PREV = 4;
    public static final int MEDIA_NEXT = 5;
    public static final int MEDIA_ORDER_PLAY = 6;
    public static final int MEDIA_LOOP_LIST_PLAY = 7;
    public static final int MEDIA_LOOP_SINGLE_PLAY = 8;
    public static final int MEDIA_RANDOM_PLAY = 9;
    public static final int MEDIA_FAVORITE = 10;
    public static final int MEDIA_FULL_SCREEN = 11;
    public static final int MEDIA_DEFINITION = 12;
    public static final int MEDIA_FORWARD = 13;
    public static final int MEDIA_BACKWARD = 14;
    public static final int MEDIA_SET_SPEED = 15;
    public static final int MEDIA_SET_PART = 16;
    public static final int MEDIA_SET_SIZE = 17;





    public interface SIBICHI{
        String SYS_DIALOG_START = "sys.dialog.start";
        String SYS_DIALOG_END = "sys.dialog.end";
        String CONTEXT_INPUT_TEXT = "context.input.text";
        String CONTEXT_WIDGET_MEDIA = "context.widget.media";
        String CONTEXT_WIDGET_WEB = "context.widget.web";
        String CONTEXT_WIDGET_LIST = "context.widget.list";
        String CONTEXT_WIDGET_TEXT = "context.widget.text";
        String CONTEXT_WIDGET_CONTENT = "context.widget.content";
        String CONTEXT_WIDGET_CUSTOM = "context.widget.custom";
        String AVATAR_SILENCE = "avatar.silence";
        String AVATAR_LISTENING = "avatar.listening";
        String AVATAR_UNDERSTANDING = "avatar.understanding";
        String AVATAR_SPEAKING = "avatar.speaking";
        String CONTEXT_OUTPUT_TEXT = "context.output.text";
        String ASR_SPEECH_RESULT = "asr.speech.result";
        String DM_OUTPUT = "dm.output";

        String ID = "2019050700000081";
        String TASK = "天气";
        String INTENTION = "主动请求天气";
        String WEATHER_INTENT_NAME = "查询天气";
        String TALK_KEY = "";
        String TALK_VALUE = "自定义的今天天气";
        String WEATHER_TALK_VALUE = "现在的天气";
    }


}
