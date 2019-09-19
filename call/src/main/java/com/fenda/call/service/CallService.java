package com.fenda.call.service;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.call.utils.ImConnectUtil;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.IleaveMessageProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtils;

import io.rong.callkit.RongCallKit;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
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
    public void initSdk() {
        RongIM.init(BaseApplication.getContext());
        //调用留言模块，注册消息接收监听
        Log.e("监听", "initSdk: ");
        IleaveMessageProvider leaveMessageProvider = (IleaveMessageProvider) ARouter.getInstance().build(RouterPath.Leavemessage.LEAVEMESSAGE_PROVIDER).navigation();
        leaveMessageProvider.setRongIMMessageListener();
    }

    @Override
    public void login(String rongCloudToken) {
        LogUtils.i("CallService login:" + rongCloudToken);
        ImConnectUtil.connectIm(rongCloudToken);
    }


    public void call(int callType, String callNumber) {
        if (ImConnectUtil.isConectIm(BaseApplication.getInstance())) {
            if (callType == 1) {
                RongCallKit.startSingleCall(BaseApplication.getInstance(), callNumber, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                Log.d("TAG", "onClick: startSingleCall id" + callNumber);
            } else if (callType == 0) {
                RongCallKit.startSingleCall(BaseApplication.getInstance(), callNumber, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
            }
        }
    }

    @Override
    public void endCall() {
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        if (session != null) {
            RongCallClient.getInstance().hangUpCall(session.getCallId());
        }
    }
}
