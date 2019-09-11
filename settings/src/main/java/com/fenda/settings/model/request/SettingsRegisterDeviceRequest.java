package com.fenda.settings.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 14:59
 */
public class SettingsRegisterDeviceRequest {
    private String deviceId;
    private String name;
    private String clientVersion;
    private String macAddr;

    public SettingsRegisterDeviceRequest(String deviceId, String name, String clientVersion, String macAddr) {
        this.deviceId = deviceId;
        this.name = name;
        this.clientVersion = clientVersion;
        this.macAddr = macAddr;
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
        return clientVersion;
    }

    public void setClientVersion(String client_version) {
        this.clientVersion = client_version;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String mac_addr) {
        this.macAddr = mac_addr;
    }

    @Override
    public String toString() {
        return "SettingsRegisterDeviceRequest{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", client_version=" + clientVersion +
                ", mac_addr='" + macAddr + '\'' +
                '}';
    }
}
