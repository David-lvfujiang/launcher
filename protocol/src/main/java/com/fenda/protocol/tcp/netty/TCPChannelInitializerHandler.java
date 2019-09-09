package com.fenda.protocol.tcp.netty;



import com.fenda.protocol.tcp.HeartbeatHandler;
import com.fenda.protocol.tcp.HeartbeatRespHandler;
import com.fenda.protocol.tcp.MyMessageDecoder;
import com.fenda.protocol.tcp.MyMessageEncoder;
import com.fenda.protocol.tcp.TCPConfig;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       TCPChannelInitializerHandler.java</p>
 * <p>@PackageName:     com.freddy.im.netty</p>
 * <b>
 * <p>@Description:     Channel初始化配置</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/05 07:11</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class TCPChannelInitializerHandler extends ChannelInitializer<Channel> {

    private NettyTcpClient imsClient;

    public TCPChannelInitializerHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        // 增加编解码支持
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyMessageDecoder());
        //添加心跳 默认3次没接收到心跳消息触发重连 客户端50s发送一次心跳
        pipeline.addLast(IdleStateHandler.class.getSimpleName(), new IdleStateHandler(
                TCPConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND * 3 , TCPConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND, 0, TimeUnit.MILLISECONDS));
        channel.pipeline().addLast(new HeartbeatHandler(imsClient));

        // 心跳消息响应处理handler
        pipeline.addLast(HeartbeatRespHandler.class.getSimpleName(), new HeartbeatRespHandler(imsClient));
        // 接收消息处理handler
        pipeline.addLast(TCPReadHandler.class.getSimpleName(), new TCPReadHandler(imsClient));
    }
}
