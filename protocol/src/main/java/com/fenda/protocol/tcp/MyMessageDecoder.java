package com.fenda.protocol.tcp;


import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.Head;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class MyMessageDecoder extends ReplayingDecoder<MyMessageDecoder.State> {
    private Head mHead = new Head();

    public MyMessageDecoder() {
        super(State.HEADER_MAGIC);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {


        switch (state()){
            case HEADER_MAGIC:
                int magic = buf.readInt();
                int head = checkMagic(magic);
                mHead.setHead(head);
                checkpoint(State.HEADER_USER_ID);

            case HEADER_USER_ID:
                long userId = buf.readLong();
                mHead.setUserId(userId);
                checkpoint(State.HEADER_MSG_ID);
            case HEADER_MSG_ID:
                long msgId = buf.readLong();
                mHead.setMsgId(msgId);
                checkpoint(State.HEADER_MSG_TYPE);
            case HEADER_MSG_TYPE:
                int msgType = buf.readInt();
                mHead.setMsgType(msgType);
                checkpoint(State.HEADER_MSG_LENGHT);
            case HEADER_MSG_LENGHT:
                int msgLength = buf.readInt();
                mHead.setMsgLength(msgLength);
                checkpoint(State.BODY);
            case BODY:
                byte[] body = new byte[mHead.getMsgLength()];
                buf.readBytes(body);
                BaseTcpMessage message = new BaseTcpMessage();
                message.setHead(mHead);
                String data = new String(body);
                message.setMsg(data);
                list.add(message);
                checkpoint(State.HEADER_MAGIC);
        }



    }


    enum State{
        HEADER_MAGIC,
        HEADER_USER_ID,
        HEADER_MSG_ID,
        HEADER_MSG_TYPE,
        HEADER_MSG_LENGHT,
        BODY

    }

    private int checkMagic(int magic){
        if (magic == 0){
            throw new IllegalArgumentException();
        }
        if (magic != TCPConfig.HEAD){
            throw new IllegalArgumentException();
        }
        return magic;
    }
}
