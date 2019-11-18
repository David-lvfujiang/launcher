package com.fenda.common.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/10/30 13:41
 * @Description 通话记录实体
 */
@Entity
public class CallRecoderBean {
    @Id(autoincrement = true)
    private Long _id;
    private int count;
    private String name;
    private String phone;
    private String icon;
    private String userId;
    // 呼叫时间
    private long callTime;
    // 通话时间
    private long communicateTime;
    //通话类型(0 音频 1 视频)
    private int callType;
    //通话状态(0 未接 1 已接)
    private int callStatus;
    //呼叫模式(0 呼入 1呼出)
    private int callMode;
    @Generated(hash = 1696580608)
    public CallRecoderBean(Long _id, int count, String name, String phone,
            String icon, String userId, long callTime, long communicateTime,
            int callType, int callStatus, int callMode) {
        this._id = _id;
        this.count = count;
        this.name = name;
        this.phone = phone;
        this.icon = icon;
        this.userId = userId;
        this.callTime = callTime;
        this.communicateTime = communicateTime;
        this.callType = callType;
        this.callStatus = callStatus;
        this.callMode = callMode;
    }
    @Generated(hash = 1715563267)
    public CallRecoderBean() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public int getCount() {
        return this.count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getIcon() {
        return this.icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getCallTime() {
        return this.callTime;
    }
    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }
    public long getCommunicateTime() {
        return this.communicateTime;
    }
    public void setCommunicateTime(long communicateTime) {
        this.communicateTime = communicateTime;
    }
    public int getCallType() {
        return this.callType;
    }
    public void setCallType(int callType) {
        this.callType = callType;
    }
    public int getCallStatus() {
        return this.callStatus;
    }
    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }
    public int getCallMode() {
        return this.callMode;
    }
    public void setCallMode(int callMode) {
        this.callMode = callMode;
    }
 
}
