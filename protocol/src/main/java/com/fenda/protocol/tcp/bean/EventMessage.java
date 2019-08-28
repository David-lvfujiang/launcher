package com.fenda.protocol.tcp.bean;

public class EventMessage<T> {

    private int code;
    private T data;

    public EventMessage(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public EventMessage() {
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventMessage{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
