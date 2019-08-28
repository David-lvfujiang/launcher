package com.fenda.protocol.tcp.factory;

import android.content.Context;

import com.fenda.protocol.tcp.interf.IMSClientInterface;
import com.fenda.protocol.tcp.netty.NettyTcpClient;

public class ClientFactory {

    public static IMSClientInterface getIMSClient(Context context) {
        return NettyTcpClient.getInstance(context);
    }
}
