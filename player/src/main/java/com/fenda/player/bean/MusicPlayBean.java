package com.fenda.player.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class MusicPlayBean implements Parcelable {
    private List<FDMusic> fdMusics;
    private String msgName;
    private int msgType;
    private String aidlMsgType;
    private int relativeTime;
    private int currentItem;





    public MusicPlayBean(List<FDMusic> fdMusics, String msgName, int msgType, String aidlMsgType, int relativeTime, int currentItem) {
        this.fdMusics = fdMusics;
        this.msgName = msgName;
        this.msgType = msgType;
        this.aidlMsgType = aidlMsgType;
        this.relativeTime = relativeTime;
        this.currentItem = currentItem;
    }


    public MusicPlayBean() {
    }
    public MusicPlayBean(Parcel source){
        if (fdMusics == null){
            fdMusics = new ArrayList<>();
        }
        source.readTypedList(fdMusics,FDMusic.CREATOR);
        msgName = source.readString();
        msgType = source.readInt();
        aidlMsgType = source.readString();
        relativeTime = source.readInt();
        currentItem = source.readInt();
    }

    public List<FDMusic> getFdMusics() {
        return fdMusics;
    }

    public void setFdMusics(List<FDMusic> fdMusics) {
        this.fdMusics = fdMusics;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getAidlMsgType() {
        return aidlMsgType;
    }



    public void setAidlMsgType(String aidlMsgType) {
        this.aidlMsgType = aidlMsgType;
    }

    public int getRelativeTime() {
        return relativeTime;
    }

    public void setRelativeTime(int relativeTime) {
        this.relativeTime = relativeTime;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(fdMusics);
        dest.writeString(msgName);
        dest.writeInt(msgType);
        dest.writeString(aidlMsgType);
        dest.writeInt(relativeTime);
        dest.writeInt(currentItem);


    }

    public static final Creator<MusicPlayBean> CREATOR = new Creator<MusicPlayBean>() {
        @Override
        public MusicPlayBean createFromParcel(Parcel source) {
            return new MusicPlayBean(source);
        }

        @Override
        public MusicPlayBean[] newArray(int size) {
            return new MusicPlayBean[size];
        }
    };


    @Override
    public String toString() {
        return "MusicPlayBean{" +
                "fdMusics=" + fdMusics +
                ", msgName='" + msgName + '\'' +
                ", msgType=" + msgType +
                ", aidlMsgType='" + aidlMsgType + '\'' +
                ", relativeTime=" + relativeTime +
                '}';
    }
}
