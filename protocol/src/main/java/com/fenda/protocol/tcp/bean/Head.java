package com.fenda.protocol.tcp.bean;

/**
 * 消息头
 */
public class Head {
    private int head;//消息头
    private long userId;//用户ID
    private long msgId;//消息ID
    private int  msgType;//消息类型
    private int msgLength;//消息长度

    public Head() {
    }

    public Head(int head, long userId, long msgId, int msgType, int msgLength) {
        this.head = head;
        this.userId = userId;
        this.msgId = msgId;
        this.msgType = msgType;
        this.msgLength = msgLength;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    @Override
    public String toString() {
        return "Head{" +
                "head=" + head +
                ", userId=" + userId +
                ", msgId=" + msgId +
                ", msgType=" + msgType +
                ", msgLength=" + msgLength +
                '}';
    }
}
