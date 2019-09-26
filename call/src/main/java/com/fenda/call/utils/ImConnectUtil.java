package com.fenda.call.utils;

import android.content.Context;
import android.widget.Toast;

import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.NetUtil;

import io.rong.callkit.util.SPUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 融云IM连接工具类
 */
public class ImConnectUtil {
    /**
     * 连接IM
     */
    public static void connectIm(String rongCloudToken) {
        RongIM.connect(rongCloudToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtils.i("onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                LogUtils.i("onSuccess");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtils.i("onError");
            }
        });
    }

    /**
     * Im是否连接成功
     *
     * @param context
     * @return
     */
    public static boolean isConectIm(Context context) {
        if (!NetUtil.isNetworkAvailable(context)) {
            Toast.makeText(context, context.getResources().getString(io.rong.callkit.R.string.rc_voip_call_network_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
            Toast.makeText(context, "连接IM失败,正在重新连接,请稍后重试", Toast.LENGTH_SHORT).show();
            String rongCloudToken = (String) SPUtils.get(BaseApplication.getInstance(), Constant.Settings.RONGYUNCLOUDTOKEN, "");
            connectIm(rongCloudToken);
            return false;
        }
        return true;
    }
}
