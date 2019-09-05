package com.fenda.encyclopedia.model;

import com.fenda.common.mvp.BaseModel;

import java.io.Serializable;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/3
 * @describe: 股票实体
 */
public class EncyclopediaShares implements Serializable, BaseModel {
    private String name;
    private String date;//时间
    private String high;//上征指数
    private String change;//上涨指数
    private String percentage;//涨幅百分比

    public EncyclopediaShares(String name, String date, String high, String change, String percentage) {
        this.name = name;
        this.date = date;
        this.high = high;
        this.change = change;
        this.percentage = percentage;
    }

    public void setName(String name) { this.name = name; }
    public void setDate(String date) {
        this.date = date;
    }

    public void setHigh(String high) {
        this.high = high;
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
        return high;
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
                ", high='" + high + '\'' +
                ", change='" + change + '\'' +
                ", percentage='" + percentage + '\'' +
                '}';
    }
}
