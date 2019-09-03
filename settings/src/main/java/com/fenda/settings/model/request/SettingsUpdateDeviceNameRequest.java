package com.fenda.settings.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 9:38
 */
public class SettingsUpdateDeviceNameRequest {
    private String deviceId;
    private String name;

    public SettingsUpdateDeviceNameRequest() {
    }

    public SettingsUpdateDeviceNameRequest(String deviceId, String name) {
        this.deviceId = deviceId;
        this.name = name;
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
}
