package com.fenda.common.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import com.fenda.common.util.LogUtil;
import com.fenda.protocol.tcp.bus.EventBusUtils;

/**
 * 网络状态监听类
 * @author mirrer.wangzhonglin
 * @Date 2019/12/5 9:55
 * @Description
 */
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {


    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        LogUtil.e("NetworkCallbackImpl  == 网络连接上了");
        //网络以连接
        EventBusUtils.post(new com.fenda.common.network.Network(true));

    }


    @Override
    public void onLost(Network network) {
        super.onLost(network);
        LogUtil.e("NetworkCallbackImpl  == 网络关闭了");
        //网络以断开
        EventBusUtils.post(new com.fenda.common.network.Network(false));
    }


    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if(networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)){
            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                //wifi网络以连接
                LogUtil.e("NetworkCallbackImpl  == 是wifi连接");

            }else {
                //移动网络以连接
                LogUtil.e("NetworkCallbackImpl  == 是移动网络连接");

            }
        }
    }
}
