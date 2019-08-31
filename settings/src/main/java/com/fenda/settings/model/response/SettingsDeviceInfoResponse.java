package com.fenda.settings.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:04
 */
public class SettingsDeviceInfoResponse implements Parcelable {

    private String id;
    private String deviceId;
    private String name;
    private String icon;
    private String dimensionalCode;
    private String rongcloud_token;
    private boolean bindStatus;


    public SettingsDeviceInfoResponse(String id, String dimensionalCode, String deviceId, String name, String icon, String rongcloud_token, boolean bindStatus) {
        this.id = id;
        this.dimensionalCode = dimensionalCode;
        this.deviceId = deviceId;
        this.name = name;
        this.icon = icon;
        this.rongcloud_token = rongcloud_token;
        this.bindStatus = bindStatus;
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
        return rongcloud_token;
    }

    public void setRongcloud_token(String rongcloud_token) {
        this.rongcloud_token = rongcloud_token;
    }
    public String getVcode() {
        return dimensionalCode;
    }

    public void setVcode(String dimensionalCode) {
        this.dimensionalCode = dimensionalCode;
    }

    public Boolean getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(Boolean bindStatus) {
        this.bindStatus = bindStatus;
    }


    @Override
    public String toString() {
        return "SettingsDeviceInfoResponse{" +
                "id=" + id +
                "dimensionalCode=" + dimensionalCode + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", rongcloud_token='" + rongcloud_token + '\'' +
                ", bindStatus='" + bindStatus + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.deviceId);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.rongcloud_token);
    }

    protected SettingsDeviceInfoResponse(Parcel in) {
        this.id = in.readString();
        this.deviceId = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.rongcloud_token = in.readString();
    }

    public static final Parcelable.Creator<SettingsDeviceInfoResponse> CREATOR = new Parcelable.Creator<SettingsDeviceInfoResponse>() {
        @Override
        public SettingsDeviceInfoResponse createFromParcel(Parcel source) {
            return new SettingsDeviceInfoResponse(source);
        }

        @Override
        public SettingsDeviceInfoResponse[] newArray(int size) {
            return new SettingsDeviceInfoResponse[size];
        }
    };
}
