package com.fenda.remind.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class AlarmBean implements Parcelable {

    private String date;
    private String repeat;
    private String repeat_asr;
    private String period;
    private String time;
    private String object;
    private String event;
    private long timestamp;
    private String operation;
    private String time_interval;
    private String vid;
    private long id;
    private long recent_tsp;
    private int type;


    public AlarmBean(String date, String repeat, String repeat_asr, String period, String time, String object, String event, long timestamp, String operation, String time_interval, String vid, long id, long recent_tsp) {
        this.date = date;
        this.repeat = repeat;
        this.repeat_asr = repeat_asr;
        this.period = period;
        this.time = time;
        this.object = object;
        this.event = event;
        this.timestamp = timestamp;
        this.operation = operation;
        this.time_interval = time_interval;
        this.vid = vid;
        this.id = id;
        this.recent_tsp = recent_tsp;
    }

    public AlarmBean() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getRepeat_asr() {
        return repeat_asr;
    }

    public void setRepeat_asr(String repeat_asr) {
        this.repeat_asr = repeat_asr;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTime_interval() {
        return time_interval;
    }

    public void setTime_interval(String time_interval) {
        this.time_interval = time_interval;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecent_tsp() {
        return recent_tsp;
    }

    public void setRecent_tsp(long recent_tsp) {
        this.recent_tsp = recent_tsp;
    }

    @Override
    public String toString() {
        return "AlarmBean{" +
                "date='" + date + '\'' +
                ", repeat='" + repeat + '\'' +
                ", repeat_asr='" + repeat_asr + '\'' +
                ", period='" + period + '\'' +
                ", time='" + time + '\'' +
                ", object='" + object + '\'' +
                ", event='" + event + '\'' +
                ", timestamp=" + timestamp +
                ", operation='" + operation + '\'' +
                ", time_interval='" + time_interval + '\'' +
                ", vid='" + vid + '\'' +
                ", id=" + id +
                ", recent_tsp=" + recent_tsp +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.repeat);
        dest.writeString(this.repeat_asr);
        dest.writeString(this.period);
        dest.writeString(this.time);
        dest.writeString(this.object);
        dest.writeString(this.event);
        dest.writeLong(this.timestamp);
        dest.writeString(this.operation);
        dest.writeString(this.time_interval);
        dest.writeString(this.vid);
        dest.writeLong(this.id);
        dest.writeLong(this.recent_tsp);
        dest.writeInt(this.type);
    }

    protected AlarmBean(Parcel in) {
        this.date = in.readString();
        this.repeat = in.readString();
        this.repeat_asr = in.readString();
        this.period = in.readString();
        this.time = in.readString();
        this.object = in.readString();
        this.event = in.readString();
        this.timestamp = in.readLong();
        this.operation = in.readString();
        this.time_interval = in.readString();
        this.vid = in.readString();
        this.id = in.readLong();
        this.recent_tsp = in.readLong();
        this.type = in.readInt();
    }

    public static final Creator<AlarmBean> CREATOR = new Creator<AlarmBean>() {
        @Override
        public AlarmBean createFromParcel(Parcel source) {
            return new AlarmBean(source);
        }

        @Override
        public AlarmBean[] newArray(int size) {
            return new AlarmBean[size];
        }
    };
}
