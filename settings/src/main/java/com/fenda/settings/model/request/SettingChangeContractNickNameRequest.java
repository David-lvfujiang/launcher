package com.fenda.settings.model.request;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 15:12
 */
public class SettingChangeContractNickNameRequest {
    private String userId;
    private String nickName;

    public SettingChangeContractNickNameRequest() {
    }

    public SettingChangeContractNickNameRequest(String userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
