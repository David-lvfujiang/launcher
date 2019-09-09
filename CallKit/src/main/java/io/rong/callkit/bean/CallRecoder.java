package io.rong.callkit.bean;

public class CallRecoder {
    private String name;
    private String phone;
    private String icon;
    private long time;
    private int callType;

    public CallRecoder() {
    }

    public CallRecoder(String name, String phone, String icon, long time, int callType) {
        this.name = name;
        this.phone = phone;
        this.icon = icon;
        this.time = time;
        this.callType = callType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    @Override
    public String toString() {
        return "CallRecoder{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", icon='" + icon + '\'' +
                ", time='" + time + '\'' +
                ", callType=" + callType +
                '}';
    }
}
