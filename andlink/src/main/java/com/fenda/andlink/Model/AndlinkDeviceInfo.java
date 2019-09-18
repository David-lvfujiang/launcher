package com.fenda.andlink.Model;

import java.util.List;

public class AndlinkDeviceInfo {
    public String deviceMac;
    public String deviceType;
    public String productToken;
    public String andlinkToken;
    public String firmwareVersion;
    public String autoAP;
    public String softAPMode;
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
