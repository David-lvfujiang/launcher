package com.fenda.ai.provider;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.DMCallback;
import com.aispeech.dui.dds.agent.SkillIntent;
import com.aispeech.dui.dds.agent.VocabIntent;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.remind.Event;
import com.aispeech.dui.plugin.remind.RemindPlugin;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.skill.Util;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.IAlarmProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/3 19:28
 * @Description
 */
@Route(path = RouterPath.VOICE.REQUEST_PROVIDER)
public class RequestService implements IVoiceRequestProvider {
    private Context mContext;
    private IAlarmProvider alarmProvider;

    @Override
    public void init(Context context) {
        mContext = context;
    }

    /**
     * 请求天气接口
     */
    @Override
    public void requestWeather(){
        try {
            BaseApplication.getBaseInstance().setRequestWeather(true);
            Thread.sleep(800);
            LogUtil.e("requestWeather ==== 请求天气 ====");
            SkillIntent skillIntent = new SkillIntent("2019042500000544",
                    VoiceConstant.SIBICHI.TASK, "查询天气",
                    new JSONObject().put("text", "现在的天气").toString());
            DDS.getInstance().getAgent().triggerIntent(skillIntent);

//            requestNews(10);
        } catch (Exception e) {
            LogUtil.e("Exception === "+e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void nowWeather() {
        try {
            SkillIntent skillIntent = new SkillIntent("2019042500000544",
                    VoiceConstant.SIBICHI.TASK, "查询天气",
                    new JSONObject().put("text", "现在的天气").toString());
            DDS.getInstance().getAgent().triggerIntent(skillIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelMusic(){

        if (Util.isQQMusicPlay()){
            MusicPlugin.get().getMusicApi().exit();
        }
        try {
            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL){
                DDS.getInstance().getAgent().stopDialog();
            }
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteAlarm(String date){
        Event event = new Gson().fromJson(date,Event.class);
//        Intent intent = new Intent(Constant.ALARM.ACTION_CLOSE_ALARM);
//        sendBroadcast(intent);
        LogUtil.e(event.toString());
        event.setOperation("删除");

        RemindPlugin.get().removeRemindEvent(event, new RemindPlugin.RemoveCallback() {
            @Override
            public void onSuccess() {
                LogUtil.e("删除成功");
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("删除失败 = "+s);
            }
        });

    }

    @Override
    public void openVoice() {
            LogUtil.d( "FD-------openVoice ");

            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                    DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
                try {
                    if (DDS.getInstance().isAuthSuccess()) {
                        DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
                    }
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            }


    }

    @Override
    public void closeVoice() {

            LogUtil.d( "FD-------closeVoice ");

            if (DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_FULL ||
                    DDS.getInstance().getInitStatus() == DDS.INIT_COMPLETE_NOT_FULL) {
                try {
                    if (DDS.getInstance().isAuthSuccess()) {
                        DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
                        cancelMusic();
                    }
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
            }

    }

    @Override
    public void updateVocab(List<String> beanList) {
        try {
            if (beanList != null && beanList.size() > 0){
                DDS.getInstance().getAgent().updateVocabs(new VocabIntent()
                        .setName("sys.联系人")
                        .setAction(VocabIntent.ACTION_CLEAR_AND_INSERT)
                        .setContents(beanList));
                LogUtil.e("添加思必驰词库===》"+beanList.toString());
            }else {
                //清空之前上传到该词库的所有词条
                DDS.getInstance().getAgent().updateVocabs(
                        new VocabIntent()
                                .setName("sys.联系人")
                                .setAction(VocabIntent.ACTION_CLEAR_ALL)
                );
                LogUtil.e("删除思必驰词库===》");
            }

        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void cancelQQMusic() {
        if (Util.isTaskQQmusic(BaseApplication.getInstance())){
            Intent intent = new Intent(VoiceConstant.ACTION_CLOSE_QQMUSIC);
            mContext.sendBroadcast(intent);
        }




    }

    @Override
    public void openQQMusic() {
        if (!Util.isTopTaskPackage(BaseApplication.getBaseInstance()).equals(VoiceConstant.MUSIC_PKG)){
            if (Util.isTaskQQmusic(BaseApplication.getBaseInstance())){
                Util.moveQQmusicTask(BaseApplication.getBaseInstance());
            }else {
                MusicPlugin.get().getMusicApi().openMyMusic();
            }
        }

    }

    @Override
    public void openAqiyi() {
        IQiyiPlugin.get().getVideoApi().open();

    }

    @Override
    public void requestNews(int number) {
        try {

            BaseApplication.getBaseInstance().setRequestNews(true);
            SkillIntent skillIntent = new SkillIntent("2019031900001180",
                    "新闻", "播报新闻",
                    new JSONObject().put("text", "播放新闻").toString());
            DDS.getInstance().getAgent().triggerIntent(skillIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestAlarm() {
        RemindPlugin.get().queryRemindEvent(new RemindPlugin.QueryCallback() {
            @Override
            public void onSuccess(List<Event> list) {
                LogUtil.e("onSuccess = "+list.toString());
                List<Event> remindList = new ArrayList<>();
                if (list != null){
                    for (Event event : list) {
                        if (TextUtils.isEmpty(event.getEvent())){
                            remindList.add(event);
                        }
                    }
                }
                //提醒
                Gson gson = new Gson();
                String json = gson.toJson(remindList);
                LogUtil.i("RemindPlugin onQueryRemind 闹钟 = "+remindList.toString());
                if (alarmProvider == null){
                    alarmProvider = ARouter.getInstance().navigation(IAlarmProvider.class);
                }
                if (alarmProvider != null){
                    alarmProvider.queryAlarm(json);
                }


            }

            @Override
            public void onError(int i, String s) {
                LogUtil.e("onError = "+s);
                List<Event> remindList = new ArrayList<>();
                //提醒
                Gson gson = new Gson();
                String json = gson.toJson(remindList);
                if (alarmProvider == null){
                    alarmProvider = ARouter.getInstance().navigation(IAlarmProvider.class);
                }
                if (alarmProvider != null){
                    alarmProvider.queryAlarm(json);
                }

            }
        });
    }


}
