package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface IHomePageProvider extends IProvider {

    /**
     * 语音返回首页天气内容
     * @param
     */
    void homePageFromVoiceControl(String todayWeatherTemp, String todayWeatherName);
}
