package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ICallProvider extends IProvider {
    void login(String rongCloundToken);
    void call(int callType, final String callNumber);
}
