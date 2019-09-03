package com.fenda.common.bean;

public class UserInfoBean {

    private int id;
    private String userId;
    private String userName;
    private String mobile;
    private int userType;
    private String icon;
    private String email;
    private String registerTime;
    private String updateTime;

    public UserInfoBean() {
    }

    public UserInfoBean(int id, String userId, String userName, String mobile, int userType, String icon, String email, String registerTime, String updateTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.mobile = mobile;
        this.userType = userType;
        this.icon = icon;
        this.email = email;
        this.registerTime = registerTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userType=" + userType +
                ", icon='" + icon + '\'' +
                ", email='" + email + '\'' +
                ", registerTime='" + registerTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
