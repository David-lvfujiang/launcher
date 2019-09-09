package com.fenda.protocol.tcp.netty;


import com.fenda.protocol.tcp.bean.BaseTcpMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       TCPReadHandler.java</p>
 * <p>@PackageName:     com.freddy.im.netty</p>
 * <b>
 * <p>@Description:     消息接收处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/07 21:40</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class TCPReadHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public TCPReadHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("TCPReadHandler channelInactive() == " + System.currentTimeMillis());
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }

        // 触发重连
        if (channel != null && !channel.isActive()) {
            imsClient.resetConnect(false);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.err.println("TCPReadHandler exceptionCaught()");
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }

        // 触发重连
        if (channel != null && !channel.isActive()) {
            imsClient.resetConnect(false);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BaseTcpMessage message = (BaseTcpMessage) msg;
        if (message == null || message.getHead() == null) {
            return;
        }

        System.out.println("收到消息，message=" + message);
        // 收到消息后，立马给服务端回一条消息接收状态报告
        BaseTcpMessage receivedReportMsg = imsClient.getClientReceiveReplyMsg(message.getHead().getMsgId());
        if (receivedReportMsg != null) {
            imsClient.sendMsg(receivedReportMsg);
            System.out.println("发送回复消息，message=" + receivedReportMsg);
        }

        // 接收消息，由消息转发器转发到应用层
        imsClient.getMsgDispatcher().receivedMsg(message);
    }
    /**
     * 构建客户端消息接收状态报告
     * @param msgId
     * @return
     */
//    private MessageProtobuf.Msg buildReceivedReportMsg(String msgId) {
//        if (StringUtil.isNullOrEmpty(msgId)) {
//            return null;
//        }
//
//        MessageProtobuf.Msg.Builder builder = MessageProtobuf.Msg.newBuilder();
//        MessageProtobuf.Head.Builder headBuilder = MessageProtobuf.Head.newBuilder();
//        headBuilder.setMsgId(UUID.randomUUID().toString());
//        headBuilder.setMsgType(imsClient.getClientReceivedReportMsgType());
//        headBuilder.setTimestamp(System.currentTimeMillis());
//        JSONObject jsonObj = new JSONObject();
//        jsonObj.put("msgId", msgId);
//        headBuilder.setExtend(jsonObj.toString());
//        builder.setHead(headBuilder.build());
//
//        return builder.build();
//    }
}
