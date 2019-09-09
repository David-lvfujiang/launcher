package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IWeatherProvider extends IProvider {

    /**
     * 语音返回天气界面天气内容
     * @param weatherContent
     */
    void weatherFromVoiceControl(String weatherContent);

    /**
     * 语音返回首页天气内容
     * @param todayWeatherContent
     */
    void weatherFromVoiceControlToMainPage(String todayWeatherContent);
}
