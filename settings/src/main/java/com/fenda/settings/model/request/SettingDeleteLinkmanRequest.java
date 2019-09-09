package com.fenda.settings.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 15:17
 */
public class SettingDeleteLinkmanRequest {
    private String userId;

    public SettingDeleteLinkmanRequest() {
    }

    public SettingDeleteLinkmanRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
