package com.fenda.call.service;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.call.utils.ImConnectUtil;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtils;

import io.rong.imkit.RongIM;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 音视频组件通信接口
 */
@Route(path = RouterPath.Call.CALL_SERVICE)
public class CallService implements ICallProvider {
    @Override
    public void init(Context context) {

    }

    @Override
    public void login(String rongCloudToken) {
        LogUtils.i("CallService login:" + rongCloudToken);
        ImConnectUtil.connectIm(rongCloudToken);
    }
}
