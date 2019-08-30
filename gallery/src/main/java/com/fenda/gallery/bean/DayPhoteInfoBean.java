package com.fenda.gallery.bean;

import java.util.ArrayList;
import java.util.List;

public class DayPhoteInfoBean {
    private String header;
    private String footer;
    private List<PhoneCameraBean> list = new ArrayList<>();

    public DayPhoteInfoBean() {
    }

    public DayPhoteInfoBean(String header, String footer, List<PhoneCameraBean> list) {
        this.header = header;
        this.footer = footer;
        this.list = list;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public List<PhoneCameraBean> getList() {
        return list;
    }

    public void setList(List<PhoneCameraBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DayPhoteInfoBean{" +
                "header='" + header + '\'' +
                ", footer='" + footer + '\'' +
                ", list=" + list +
                '}';
    }
}
