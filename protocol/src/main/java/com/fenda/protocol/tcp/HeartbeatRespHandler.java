package com.fenda.protocol.tcp;

import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.netty.NettyTcpClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       HeartbeatRespHandler.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     心跳消息响应处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/08 01:08</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class HeartbeatRespHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public HeartbeatRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BaseTcpMessage heartbeatRespMsg = (BaseTcpMessage) msg;
        if (heartbeatRespMsg == null || heartbeatRespMsg.getHead() == null) {
            return;
        }

        BaseTcpMessage heartbeatMsg = imsClient.getHeartbeatMsg();
        if (heartbeatMsg == null || heartbeatMsg.getHead() == null) {
            return;
        }

        int heartbeatMsgType = TCPConfig.MessageType.HEAD_RES;
        int type = heartbeatRespMsg.getHead().getMsgType();
        if (heartbeatMsgType == type) {
            System.out.println("收到服务端心跳响应消息，message1 = " + heartbeatRespMsg);
        } else if (TCPConfig.MessageType.SERVICE_HEAD_REQ == type){
            System.out.println("收到服务端心跳请求消息，message2 = " + heartbeatRespMsg);
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
