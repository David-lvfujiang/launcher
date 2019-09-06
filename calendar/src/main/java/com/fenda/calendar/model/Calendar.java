package com.fenda.calendar.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fenda.common.mvp.BaseModel;

import java.io.Serializable;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 日历实体
 */
public class Calendar implements Parcelable {
    private String weekday;
    private String year;
    private String month;
    private String day;
    private String nlmonth;
    private String nlday;

    public Calendar(String weekday, String year, String month, String day, String nlmonth, String nlday) {
        this.weekday = weekday;
        this.year = year;
        this.month = month;
        this.day = day;
        this.nlmonth = nlmonth;
        this.nlday = nlday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setNlmonth(String nlmonth) {
        this.nlmonth = nlmonth;
    }

    public void setNlday(String nlday) {
        this.nlday = nlday;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getNlmonth() {
        return nlmonth;
    }

    public String getNlday() {
        return nlday;
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "weekday='" + weekday + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", nlmonth='" + nlmonth + '\'' +
                ", nlday='" + nlday + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.weekday);
        dest.writeString(this.year);
        dest.writeString(this.month);
        dest.writeString(this.day);
        dest.writeString(this.nlmonth);
        dest.writeString(this.nlday);
    }

    protected Calendar(Parcel in) {
        this.weekday = in.readString();
        this.year = in.readString();
        this.month = in.readString();
        this.day = in.readString();
        this.nlmonth = in.readString();
        this.nlday = in.readString();
    }

    public static final Creator<Calendar> CREATOR = new Creator<Calendar>() {
        @Override
        public Calendar createFromParcel(Parcel source) {
            return new Calendar(source);
        }

        @Override
        public Calendar[] newArray(int size) {
            return new Calendar[size];
        }
    };
}
