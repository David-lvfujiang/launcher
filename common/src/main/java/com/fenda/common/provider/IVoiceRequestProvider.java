package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.fenda.common.bean.UserInfoBean;

import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 8:44
 * @Description
 */
public interface IVoiceRequestProvider extends IProvider {

    /**请求天气**/
    void requestWeather();
    /**现在的天气**/
    void nowWeather();
    /**关闭音乐**/
    void cancelMusic();
    /**删除闹钟**/
    void deleteAlarm(String date);
    /**打开语音**/
    void openVoice();
    /**关闭语音**/
    void closeVoice();
    /**更新思必驰联系人**/
    void updateVocab(List<String> beanList);
    /**关闭QQ音乐**/
    void cancelQQMusic();
    /**打开QQ音乐**/
    void openQQMusic();
    /**打开爱奇艺**/
    void openAqiyi();
    /**请求新闻**/
    void requestNews(int number);


}
