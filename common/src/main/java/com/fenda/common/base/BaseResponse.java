package com.fenda.common.base;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/30 19:00
  * @Description
  *
  */
public class BaseResponse<T> {

    private int code;
    private String message;
    private long timestamp;
    private String requestId;
    private int handleTime;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(int handleTime) {
        this.handleTime = handleTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static class CodeBean {
    }

    public static class DataBean {
    }
}
