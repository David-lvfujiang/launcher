package com.fenda.settings.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:06
 */
public class SettingsAgreeUserAddRequest {
    private String deviceId;
    private String messageContent;
    private String messageType;
    private String sendUserId;
    private String id;
    private boolean confirmType;

    public SettingsAgreeUserAddRequest(String deviceId, String messageContent, String messageType, String sendUserId, String id, boolean confirmType) {
        this.deviceId = deviceId;
        this.messageContent = messageContent;
        this.messageType = messageType;
        this.sendUserId = sendUserId;
        this.id = id;
        this.confirmType = confirmType;
    }

    public SettingsAgreeUserAddRequest() {
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

    public boolean getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(boolean confirmType) {
        this.confirmType = confirmType;
    }

    @Override
    public String toString() {
        return "SettingsAgreeUserAddRequest{" +
                "deviceId='" + deviceId + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageType=" + messageType +
                ", sendUserId='" + sendUserId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
