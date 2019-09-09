package com.fenda.settings.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 14:59
 */
public class SettingsRegisterDeviceRequest {
    private String deviceId;
    private String name;
    private String client_version;
    private String mac_addr;

    public SettingsRegisterDeviceRequest(String deviceId, String name, String client_version, String mac_addr) {
        this.deviceId = deviceId;
        this.name = name;
        this.client_version = client_version;
        this.mac_addr = mac_addr;
    }
    public SettingsRegisterDeviceRequest() {
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

    public String getClientVersion() {
        return client_version;
    }

    public void setClientVersion(String client_version) {
        this.client_version = client_version;
    }

    public String getMacAddr() {
        return mac_addr;
    }

    public void setMacAddr(String mac_addr) {
        this.mac_addr = mac_addr;
    }

    @Override
    public String toString() {
        return "SettingsRegisterDeviceRequest{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", client_version=" + client_version +
                ", mac_addr='" + mac_addr + '\'' +
                '}';
    }
}
