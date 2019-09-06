package com.fenda.encyclopedia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fenda.common.mvp.BaseModel;

import java.io.Serializable;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/3
 * @describe: 股票实体
 */
public class EncyclopediaSharesBean implements  Parcelable {
    private String name;
    private String date;//时间
    private String current;//上征指数
    private String change;//上涨指数
    private String percentage;//涨幅百分比

    public EncyclopediaSharesBean(String name, String date, String high, String change, String percentage) {
        this.name = name;
        this.date = date;
        this.current = high;
        this.change = change;
        this.percentage = percentage;
    }

    public void setName(String name) { this.name = name; }
    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(String high) {
        this.current = high;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return current;
    }

    public String getChange() {
        return change;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "EncyclopediaShares{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", high='" + current + '\'' +
                ", change='" + change + '\'' +
                ", percentage='" + percentage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.date);
        dest.writeString(this.current);
        dest.writeString(this.change);
        dest.writeString(this.percentage);
    }

    protected EncyclopediaSharesBean(Parcel in) {
        this.name = in.readString();
        this.date = in.readString();
        this.current = in.readString();
        this.change = in.readString();
        this.percentage = in.readString();
    }

    public static final Parcelable.Creator<EncyclopediaSharesBean> CREATOR = new Parcelable.Creator<EncyclopediaSharesBean>() {
        @Override
        public EncyclopediaSharesBean createFromParcel(Parcel source) {
            return new EncyclopediaSharesBean(source);
        }

        @Override
        public EncyclopediaSharesBean[] newArray(int size) {
            return new EncyclopediaSharesBean[size];
        }
    };
}



