package com.fenda.ai.provider;

import android.content.Context;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.SkillIntent;
import com.aispeech.dui.dds.agent.VocabIntent;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.remind.Event;
import com.aispeech.dui.plugin.remind.RemindPlugin;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.skill.Util;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/3 19:28
 * @Description
 */
@Route(path = RouterPath.VOICE.REQUEST_PROVIDER)
public class RequestService implements IVoiceRequestProvider {
    @Override
    public void init(Context context) {

    }


    /**
     * 请求天气接口
     */
    @Override
    public void requestWeather(){
        try {
            SkillIntent skillIntent = new SkillIntent(VoiceConstant.SIBICHI.ID,
                    VoiceConstant.SIBICHI.TASK, VoiceConstant.SIBICHI.INTENTION,
                    new JSONObject().put(VoiceConstant.SIBICHI.TALK_KEY, VoiceConstant.SIBICHI.TALK_VALUE).toString());
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
        try {
            LogUtil.d( "FD-------openVoice ");
            DDS.getInstance().getAgent().getWakeupEngine().enableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeVoice() {

        try {
            LogUtil.d( "FD-------closeVoice ");
            DDS.getInstance().getAgent().getWakeupEngine().disableWakeup();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
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


}
