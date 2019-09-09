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

    public SettingsWifiBean() {
    }

    public SettingsWifiBean(ScanResult result, int status) {
        this.result = result;
        this.status = status;
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
