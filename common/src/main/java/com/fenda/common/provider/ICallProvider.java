package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ICallProvider extends IProvider {
    void initSdk();

    void login(String rongCloundToken);

    void call(int callType, String callNumber);

    void endCall();

    void muteCall(boolean isMute);

    void acceptCall();

}
