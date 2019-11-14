package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 9:30
 * @Description
 */
public interface IVoiceInitProvider extends IProvider {

    /**初始化语音**/
    void initVoice();

//    void initAuth(String authCode, String codeVerifier, String cliendId, String userId);
    void initAuth();

}
