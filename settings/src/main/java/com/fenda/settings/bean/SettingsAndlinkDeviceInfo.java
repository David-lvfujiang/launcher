package com.fenda.settings.bean;

import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/19 10:45
 */
public class SettingsAndlinkDeviceInfo {
    public String deviceMac;
    public String deviceType;
    public String productToken;
    public String andlinkToken;
    public String firmwareVersion;
    public String autoAp;
    public String softApMode;
    public String softwareVersion;
    public DeviceExtInfo deviceExtInfo;



    public static class DeviceExtInfo {

        public String cmei;
        public String authMode;
        public String manuDate;
        public String OS;
        public List<ChipModel> chips;
        public String netCheckMode;

    }

    public static class ChipModel{
        public String type;
        public String factory;
        public String model;
    }
}
