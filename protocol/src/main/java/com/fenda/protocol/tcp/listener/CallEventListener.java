package com.fenda.protocol.tcp.listener;

import android.text.TextUtils;

import com.fenda.protocol.tcp.MessageProcessor;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.Head;


public class CallEventListener implements OnEventListener {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public CallEventListener(String userId) {
        this.userId = userId;
    }

    @Override
    public void dispatchMsg(BaseTcpMessage msg) {
        MessageProcessor.getInstance().receiveMsg(msg);
    }


    @Override
    public int getReconnectInterval() {
        return 0;
    }

    @Override
    public int getConnectTimeout() {
        return 0;
    }

    @Override
    public int getForegroundHeartbeatInterval() {
        return 0;
    }

    @Override
    public int getBackgroundHeartbeatInterval() {
        return 0;
    }

    @Override
    public BaseTcpMessage getHandshakeMsg() {
        return null;
    }

    /**
     * 获取心跳消息
     * @return
     */
    @Override
    public BaseTcpMessage getHeartbeatMsg() {
        if (TextUtils.isEmpty(userId)){
            userId = "0";
        }
        Head mHead = new Head();
        mHead.setHead(TCPConfig.HEAD);
        mHead.setUserId(Long.parseLong(userId));
        mHead.setMsgId(1);
        mHead.setMsgType(TCPConfig.MessageType.HEAD_REQ);
        String time = String.valueOf(System.currentTimeMillis());
        mHead.setMsgLength(time.length());
        BaseTcpMessage message = new BaseTcpMessage();
        message.setHead(mHead);
        message.setMsg(time);
        return message;
    }

    @Override
    public BaseTcpMessage getClientReceiveReplyMsg(long msgId) {
        if (TextUtils.isEmpty(userId)){
            userId = "0";
        }
        Head mHead = new Head();
        mHead.setHead(TCPConfig.HEAD);
        mHead.setUserId(Long.parseLong(userId));
        mHead.setMsgId(msgId);
        mHead.setMsgType(TCPConfig.MessageType.MESSAGE_REPLEY);
        String messageContent = "ok";
        mHead.setMsgLength(messageContent.length());
        BaseTcpMessage message = new BaseTcpMessage();
        message.setHead(mHead);
        message.setMsg(messageContent);
        return message;
    }

    @Override
    public int getServerSentReportMsgType() {
        return 0;
    }

    @Override
    public int getClientReceivedReportMsgType() {
        return 0;
    }

    @Override
    public int getResendCount() {
        return 0;
    }

    @Override
    public int getResendInterval() {
        return 0;
    }
}
