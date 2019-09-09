package com.fenda.protocol.tcp;


import com.fenda.protocol.tcp.bean.BaseTcpMessage;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       IMessageProcessor.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息处理器接口</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:11</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public interface IMessageProcessor {

    void receiveMsg(BaseTcpMessage message);
    void sendMsg(BaseTcpMessage message);
}
