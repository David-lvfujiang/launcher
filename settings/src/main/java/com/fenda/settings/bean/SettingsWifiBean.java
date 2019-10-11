package com.fenda.settings.bean;

import android.net.wifi.ScanResult;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 17:15
 */
public class SettingsWifiBean {
    private ScanResult result;
    private int status;
    private String name;
    private String itemStatus;


    public SettingsWifiBean() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String name, String itemStatus) {
        this.name = name;
        this.itemStatus = itemStatus;
    }


    public ScanResult getResult() {
        return result;
    }

    public void setResult(ScanResult result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
