package com.fenda.ai.author;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.aispeech.ailog.AILog;
import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.DDSInitListener;
import com.aispeech.dui.dds.agent.tts.TTSEngine;
import com.aispeech.dui.dds.agent.tts.bean.CustomAudioBean;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.mediactrl.MediaCtrlPlugin;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.remind.Event;
import com.aispeech.dui.plugin.remind.RemindPlugin;
import com.aispeech.dui.plugin.setting.SettingPlugin;
import com.aispeech.dui.plugin.tvctrl.TVCtrlPlugin;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.skill.MediaControl;
import com.fenda.ai.skill.SystemControl;
import com.fenda.ai.skill.TVControl;
import com.fenda.ai.skill.Util;
import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IRemindProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * dds初始状态监听器,监听init是否成功
 * @author WangZL
 * @Date
 */
public class MyDDSInitListener implements DDSInitListener {
    private static final String TAG = "MyDDSInitListener";
    private String[] searchMusicName = {"网络歌曲","热门歌曲","流行歌曲","人气歌曲","KTV热歌","抖音热门歌曲"};


    private Context mContext;
    private CountDownTimer timer;
    private CountDownTimer remindTimer;

    private IRemindProvider provider;
    public MyDDSInitListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onInitComplete(boolean isFull) {
//        LogUtil.d(TAG, "onInitComplete isFull:"  +  isFull);
        if (isFull) {
            // 发送一个init成功的广播
            mContext.sendBroadcast(new Intent(VoiceConstant.ACTION_INIT_COMPLETE));
            //初始化唤醒语音文字
            initSpeakText();
            //初始化音乐和视频
            initMedia();
            //初始化控制技能
            initControl();
            //初始化提醒技能
            initRemind();


            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("");
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            initRemind();
                        }
                    });



        }

    }

    private void initRemind() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (provider == null){
                            provider = (IRemindProvider) ARouter.getInstance().build(RouterPath.REMIND.ALARM_SERVICE).navigation();
                        }

                    }
                });


        //提醒技能
        RemindPlugin.init(mContext);
        RemindPlugin.get().setOnRemindFinishListener(new RemindPlugin.OnRemindFinishListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onFinish(final Event event) {

                String json = new Gson().toJson(event);
                if (provider != null){
                    provider.alarmRing(json);
                }
                try {
                    if (TextUtils.isEmpty(event.getEvent())) {
                        DDS.getInstance().getAgent().getTTSEngine().speak("现在是,"+event.getPeriod()+" ,"+event.getTime().substring(0,event.getTime().length()-2),
                                1, "1101", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
                    } else {
                        DDS.getInstance().getAgent().getTTSEngine().speak("现在是,"+event.getPeriod()+" ,"+event.getTime().substring(0,event.getTime().length()-3)+"。主人你该" + event.getEvent() + "了",
                                1, "1101", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
                    }

                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                AILog.d(TAG, "提醒事件时间到了 "+event.toString());
            }
        });

        RemindPlugin.get().setOnTimerStateChangeListener(new RemindPlugin.OnTimerStateChangeListener() {
            @Override
            public void onTick(long l) {
                AILog.d(TAG, "倒计时 onTick :" + l);
            }
            @Override
            public void onFinish() {
                AILog.d(TAG, "倒计时 onFinish ");
                try {
                    DDS.getInstance().getAgent().getTTSEngine().speak("倒计时结束", 1102);
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            }
        });



        RemindPlugin.get().setOnRemindChangeListener(new RemindPlugin.OnRemindChangeListener() {
            @Override
            public void onCreateRemind(List<Event> list) {
                Gson gson = new Gson();
                String json = gson.toJson(list);
                LogUtil.i("RemindPlugin onCreateRemind = "+gson.toJson(list));
                if (provider != null){
                    provider.createAlarm(json);
                }

            }

            @Override
            public void onRemoveRemind(List<Event> list) {
                LogUtil.i("RemindPlugin onRemoveRemind = "+list.toString());
                Gson gson = new Gson();
                String json = gson.toJson(list);
                if (provider != null){
                    provider.deleteAlarmComplete(json);
                }
            }

            @Override
            public void onQueryRemind(List<Event> list) {
                Gson gson = new Gson();
                String json = gson.toJson(list);
                LogUtil.i("RemindPlugin onQueryRemind = "+list.toString());
                if (provider != null){
                    provider.queryAlarm(json);
                }

            }
        });


    }

    /**
     * 开始提醒
     * @param event
     */
    private void startRemind(final Event event) {
        remindTimer = new CountDownTimer(60000,10000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    DDS.getInstance().getAgent().getTTSEngine().speak("现在是,"+event.getPeriod()+" ,"+event.getTime().substring(0,event.getTime().length()-3)+"。主人你该" + event.getEvent() + "了",
                            1, "1101", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFinish() {
                remindTimer.cancel();

            }
        };
        remindTimer.start();
    }

    /**
     * 开始闹铃
     * @param event
     * @throws DDSNotInitCompleteException
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void startAlarm(Event event) throws DDSNotInitCompleteException {
        DDS.getInstance().getAgent().getTTSEngine().speak("现在是,"+event.getPeriod()+" ,"+event.getTime().substring(0,event.getTime().length()-2),
                1, "1101", AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ringtone = RingtoneManager.getRingtone(mContext,notify);
        if (ringtone != null && !ringtone.isPlaying()){
            ringtone.setLooping(true);
            ringtone.play();
        }

        timer = new CountDownTimer(300000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                timer.cancel();
                ringtone.stop();
            }
        };
        timer.start();
    }

    public boolean isCountDownTime(){
        if (timer != null || remindTimer != null){
            return true;
        }
        return false;
    }


    public void closeAlarm(){
        if (timer != null){
            timer.onFinish();
            timer = null;
        }
        closeRemind();
    }

    public void closeRemind(){
        if (remindTimer != null){
            remindTimer.onFinish();
            remindTimer = null;
        }
    }


    public void queryRemind(){

        RemindPlugin.get().queryRemindEvent(new RemindPlugin.QueryCallback() {
            @Override
            public void onSuccess(List<Event> list) {
                LogUtil.e("关闭提醒 onSuccess = "+list.toString());

                if (list != null){
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    if (provider != null){
                        provider.queryAlarm(json);
                    }

                    LogUtil.e(json);
                }
            }
            @Override
            public void onError(int i, String s) {
                LogUtil.e("onError ==> "+s);
            }
        });
    }


    private void initControl() {
        //播控技能
        MediaCtrlPlugin.init(mContext);
        //电视控制技能
        TVCtrlPlugin.init();
        //中控技能
        SettingPlugin.init(mContext);

        //中控相关回调
        SettingPlugin.get().setSystemCtrl(new SystemControl(mContext));

        MediaCtrlPlugin.get().setContent(true);
        //播控相关回调
        MediaCtrlPlugin.get().setMediaCtrl(new MediaControl());


        //电视控制相关回调
        TVCtrlPlugin.get().setTVCtrl(new TVControl());
    }

    private void initMedia() {
        //音乐技能（QQ音乐）
        MusicPlugin.init(mContext,MusicPlugin.TYPE_QQCAR);
        MusicPlugin.get().setListener(new MusicPlugin.c() {
            //播放回调
            @Override
            public void a() {
                //网络歌曲、热门歌曲、流行歌曲、人气歌曲、KTV热歌、抖音热门歌曲
                if (!Util.isTopTaskPackage(BaseApplication.getInstance()).equals(VoiceConstant.MUSIC_PKG)){
                    if (Util.isTaskQQmusic(BaseApplication.getInstance())){
                        Util.moveQQmusicTask(BaseApplication.getInstance());
                        MusicPlugin.get().getMusicApi().resume();
                    }else {
                        Random random = new Random();
                        int index = random.nextInt(searchMusicName.length-1);
                        MusicPlugin.get().getMusicApi().searchAndPlay(searchMusicName[index]);
                    }
                }



            }
        });
        //影视技能（爱奇艺）
        IQiyiPlugin.init(mContext);
    }

    private void initSpeakText() {
        CustomAudioBean bean = new CustomAudioBean();
        List<CustomAudioBean> beans = new ArrayList<>();
        //替换的tts播放文字
        bean.setName("在");
        //替换的音频
        bean.setPath(VoiceConstant.sVoiceDir+"/zai.pcm");
        beans.add(bean);

        CustomAudioBean gsbean = new CustomAudioBean();
        gsbean.setName("干啥");
        gsbean.setPath(VoiceConstant.sVoiceDir+"/gansha.pcm");
        beans.add(gsbean);

        CustomAudioBean llbean = new CustomAudioBean();
        llbean.setName("来了");
        llbean.setPath(VoiceConstant.sVoiceDir+"/laile.pcm");
        beans.add(llbean);

        CustomAudioBean nsbean = new CustomAudioBean();
        nsbean.setName("你说");
        nsbean.setPath(VoiceConstant.sVoiceDir+"/nishuo.pcm");
        beans.add(nsbean);

        CustomAudioBean ssbean = new CustomAudioBean();
        ssbean.setName("啥事");
        ssbean.setPath(VoiceConstant.sVoiceDir+"/shashi.pcm");
        beans.add(ssbean);

        try {
            DDS.getInstance().getAgent().getTTSEngine().setCustomAudio(beans);
            DDS.getInstance().getAgent().getTTSEngine().setMode(TTSEngine.LOCAL);
            DDS.getInstance().getAgent().getTTSEngine().setStyle("short");
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int what, final String msg) {
//        LogUtil.e(TAG, "Init onError: " + what + ", error: " + msg);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
