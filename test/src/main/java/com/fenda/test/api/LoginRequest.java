package com.fenda.test.api;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 20:01
 * @Description
 */
public class LoginRequest {


    private String mobile;
    private String password;


    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
