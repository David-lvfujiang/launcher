package com.fenda.settings.model.response;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 14:59
 */
public class SettingsRegisterDeviceResponse {
    private String id;
    private String deviceId;
    private String name;
    private String icon;
    private String dimensionalCode;
    private String rongcloudToken;


    public SettingsRegisterDeviceResponse(String id, String dimensionalCode, String deviceId, String name, String icon, String rongcloudToken) {
        this.id = id;
        this.dimensionalCode = dimensionalCode;
        this.deviceId = deviceId;
        this.name = name;
        this.icon = icon;
        this.rongcloudToken = rongcloudToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRongcloud_token() {
        return rongcloudToken;
    }

    public void setRongcloud_token(String rongcloud_token) {
        this.rongcloudToken = rongcloud_token;
    }
    public String getVcode() {
        return dimensionalCode;
    }

    public void setVcode(String dimensionalCode) {
        this.dimensionalCode = dimensionalCode;
    }

    @Override
    public String toString() {
        return "SettingsQueryDeviceInfoResponse{" +
                "id=" + id +
                "dimensionalCode=" + dimensionalCode + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", rongcloud_token='" + rongcloudToken + '\'' +
                '}';
    }

}
