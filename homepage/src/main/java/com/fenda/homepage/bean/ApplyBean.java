package com.fenda.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author matt.liaojianpeng
 * 日期时间: 2019/9/1 16:31
 * 内容描述:
 * 版本：
 * 包名：
 */
public class ApplyBean implements Parcelable {
    private String applyId;
    private String applyName;
    private String submenuTitle;
    private int applyImage;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public ApplyBean(String applyId, String applyName, int applyImage, String submenuTitle){
        this.applyId = applyId;
        this.applyName = applyName;
        this.applyImage = applyImage;
        this.submenuTitle = submenuTitle;
    }
    public ApplyBean(String applyId , String applyName, int applyImage){
        this.applyId = applyId;
        this.applyName = applyName;
        this.applyImage = applyImage;
    }
    public ApplyBean() {
    }

    public static final Creator<ApplyBean> CREATOR = new Creator<ApplyBean>() {
        @Override
        public ApplyBean createFromParcel(Parcel in) {
            return new ApplyBean(in);
        }

        @Override
        public ApplyBean[] newArray(int size) {
            return new ApplyBean[size];
        }
    };

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getSubmenuTitle() {
        return submenuTitle;
    }

    public void setSubmenuTitle(String submenuTitle) {
        this.submenuTitle = submenuTitle;
    }

    public int getApplyImage() {
        return applyImage;
    }

    public void setApplyImage(int applyImage) {
        this.applyImage = applyImage;
    }
    public ApplyBean(Parcel source) {
        applyId = source.readString();
        applyName = source.readString();
        submenuTitle = source.readString();
        applyImage = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(applyId);
        dest.writeString(applyName);
        dest.writeString(submenuTitle);
        dest.writeInt(applyImage);
    }

}
