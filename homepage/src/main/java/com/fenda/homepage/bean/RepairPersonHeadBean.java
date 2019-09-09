package com.fenda.homepage.bean;

public class RepairPersonHeadBean {
    private String deviceId;
    private String userId;
    private String icon;
    private String userName;

    public RepairPersonHeadBean() {
    }

    public RepairPersonHeadBean(String deviceId, String userId, String icon, String userName) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.icon = icon;
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RepairPersonHeadBean{" +
                "deviceId='" + deviceId + '\'' +
                ", userId='" + userId + '\'' +
                ", icon='" + icon + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
