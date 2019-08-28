package com.fenda.protocol.tcp.bean;

public class BaseTcpMessage {
    private Head head;
    private String msg;


    public BaseTcpMessage(Head head, String msg) {
        this.head = head;
        this.msg = msg;
    }

    public BaseTcpMessage() {
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseTcpMessage{" +
                "head=" + head +
                ", msg=" + msg +
                '}';
    }
}
