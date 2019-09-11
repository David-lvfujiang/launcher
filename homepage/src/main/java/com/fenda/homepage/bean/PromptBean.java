package com.fenda.homepage.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author : matt.Ljp
 * date : 2019/9/11 16:15
 * description :
 */
public class PromptBean implements Parcelable {

    private String mPromptTitle;
    private String mPrompt1;
    private String mPrompt2;
    private String mPrompt3;
    private String mPrompt4;
    private String mPrompt5;

    public String getPromptTitle() {
        return mPromptTitle;
    }

    public void setPromptTitle(String promptTitle) {
        mPromptTitle = promptTitle;
    }

    public String getPrompt1() {
        return mPrompt1;
    }

    public void setPrompt1(String prompt1) {
        mPrompt1 = prompt1;
    }

    public String getPrompt2() {
        return mPrompt2;
    }

    public void setPrompt2(String prompt2) {
        mPrompt2 = prompt2;
    }

    public String getPrompt3() {
        return mPrompt3;
    }

    public void setPrompt3(String prompt3) {
        mPrompt3 = prompt3;
    }

    public String getPrompt4() {
        return mPrompt4;
    }

    public void setPrompt4(String prompt4) {
        mPrompt4 = prompt4;
    }

    public String getPrompt5() {
        return mPrompt5;
    }

    public void setPrompt5(String prompt5) {
        mPrompt5 = prompt5;
    }
    public PromptBean(){}
    public PromptBean(String mPrompt1, String mPrompt2, String mPrompt3, String mPrompt4, String mPrompt5){
        this.mPrompt1 = mPrompt1;
        this.mPrompt2 = mPrompt2;
        this.mPrompt3 = mPrompt3;
        this.mPrompt4 = mPrompt4;
        this.mPrompt5 = mPrompt5;
    }
    public PromptBean(String mPromptTitle, String mPrompt1, String mPrompt2, String mPrompt3, String mPrompt4, String mPrompt5){
        this.mPromptTitle = mPromptTitle;
        this.mPrompt1 = mPrompt1;
        this.mPrompt2 = mPrompt2;
        this.mPrompt3 = mPrompt3;
        this.mPrompt4 = mPrompt4;
        this.mPrompt5 = mPrompt5;
    }

    public static final Creator<PromptBean> CREATOR = new Creator<PromptBean>() {
        @Override
        public PromptBean createFromParcel(Parcel in) {
            return new PromptBean(in);
        }
        @Override
        public PromptBean[] newArray(int size) {
            return new PromptBean[size];
        }
    };

    public PromptBean(Parcel source) {
        mPromptTitle = source.readString();
        mPrompt1 = source.readString();
        mPrompt2 = source.readString();
        mPrompt3 = source.readString();
        mPrompt4 = source.readString();
        mPrompt5 = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPromptTitle);
        dest.writeString(mPrompt1);
        dest.writeString(mPrompt2);
        dest.writeString(mPrompt3);
        dest.writeString(mPrompt4);
        dest.writeString(mPrompt5);
    }
}
