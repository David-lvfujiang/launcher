package com.fenda.calendar.model;

import com.fenda.common.mvp.BaseModel;

import java.io.Serializable;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 日历实体
 */
public class Calendar implements BaseModel, Serializable {
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
}
