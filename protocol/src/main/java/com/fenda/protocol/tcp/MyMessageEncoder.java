package com.fenda.protocol.tcp;


import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.Head;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyMessageEncoder extends MessageToByteEncoder<BaseTcpMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseTcpMessage baseTcpMessage, ByteBuf buf) throws Exception {
        Head head = baseTcpMessage.getHead();
        byte[] bytes = baseTcpMessage.getMsg().getBytes();
        buf.writeInt(head.getHead())
                .writeLong(head.getUserId())
                .writeLong(head.getMsgId())
                .writeInt(head.getMsgType())
                .writeInt(head.getMsgLength())
                .writeBytes(bytes);

    }
}
