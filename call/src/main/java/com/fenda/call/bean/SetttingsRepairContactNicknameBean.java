package com.fenda.call.bean;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/4 10:28
 */
public class SetttingsRepairContactNicknameBean {
    private String  deviceId;
    private String nickName;
    private String userId;

    public SetttingsRepairContactNicknameBean() {
    }

    public SetttingsRepairContactNicknameBean(String deviceId, String nickName, String userId) {
        this.deviceId = deviceId;
        this.nickName = nickName;
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SetttingsRepairContactNicknameBean{" +
                "deviceId='" + deviceId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
