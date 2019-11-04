package com.fenda.homepage.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/10/12 18:00
 */
public class AgreeUserAddRequest {
    private String deviceId;
    private String messageContent;
    private String messageType;
    private String sendUserId;
    private String id;

    public AgreeUserAddRequest(String deviceId, String messageContent, String messageType, String sendUserId, String id) {
        this.deviceId = deviceId;
        this.messageContent = messageContent;
        this.messageType = messageType;
        this.sendUserId = sendUserId;
        this.id = id;
    }

    public AgreeUserAddRequest() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AgreeUserAddRequest{" +
                "deviceId='" + deviceId + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageType=" + messageType +
                ", sendUserId='" + sendUserId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}