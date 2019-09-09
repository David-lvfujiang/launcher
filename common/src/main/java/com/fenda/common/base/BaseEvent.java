package com.fenda.common.base;

/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  20:26
 * @Description This is BaseEvent
 */
public class BaseEvent<T> {
    private int code;
    private T data;

    public BaseEvent(int code) {
        this.code = code;
    }

    public BaseEvent(int code, T data) {
        this.code = code;
        this.data = data;
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
        return "BaseEvent{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
