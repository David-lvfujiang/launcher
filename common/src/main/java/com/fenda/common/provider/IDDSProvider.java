package com.fenda.common.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/7 14:18
 */
public interface IDDSProvider extends IProvider {
    void initAuth(String authCode, String codeVerifier, String cliendId, String userId);
}
