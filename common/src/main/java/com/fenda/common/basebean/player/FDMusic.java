package com.fenda.common.basebean.player;

import android.os.Parcel;
import android.os.Parcelable;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/29 11:21
  * @Description
  *
  */
public class FDMusic implements Parcelable {
    // id title singer data time image
    /**
     * 音乐id
     */
    private int musicId;
    /**
     * 音乐标题
     */
    private String musicTitle;
    /**
     * 音乐路径
     */
    private String musicUri;
    /**
     *  长度
     */
    private int musicLength;
    /**
     * icon
     */
    private String musicImage;
    /**
     * 艺术家
     */
    private String musicArtist;
    /**
     * 新闻时间
     */
    private String musicTime;
    /**
     * 内容
     */
    private String content;

    private int contentType;

    public FDMusic(int musicId, String musicTitle, String musicUri, int musicLength, String musicImage, String musicArtist, String musicTime) {
        this.musicId = musicId;
        this.musicTitle = musicTitle;
        this.musicUri = musicUri;
        this.musicLength = musicLength;
        this.musicImage = musicImage;
        this.musicArtist = musicArtist;
        this.musicTime = musicTime;
    }

    public FDMusic(String title, String uri, String iconUrl, String artist){

        musicId = 0;
        musicTitle = title;
        musicUri = uri;
        musicImage = iconUrl;
        musicArtist = artist;
        musicLength = 0;
    }

    public FDMusic(String title, String uri, String artist){

        musicId = 0;
        musicTitle = title;
        musicUri = uri;
        musicImage = "";
        musicArtist = artist;
        musicLength = 0;
    }

    public FDMusic() {
    }

    public FDMusic(Parcel source) {
        musicId = source.readInt();
        musicTitle = source.readString();
        musicUri = source.readString();
        musicLength = source.readInt();
        musicImage = source.readString();
        musicArtist = source.readString();
        musicTime = source.readString();
        contentType = source.readInt();
        content = source.readString();

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    public int getMusicLength() {
        return musicLength;
    }

    public void setMusicLength(int musicLength) {
        this.musicLength = musicLength;
    }

    public String getMusicImage() {
        return musicImage;
    }

    public void setMusicImage(String musicImage) {
        this.musicImage = musicImage;
    }

    public String getMusicArtist() {
        return musicArtist;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public String getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(String musicTime) {
        this.musicTime = musicTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(musicId);
        dest.writeString(musicTitle);
        dest.writeString(musicUri);
        dest.writeInt(musicLength);
        dest.writeString(musicImage);
        dest.writeString(musicArtist);
        dest.writeString(musicTime);
        dest.writeInt(contentType);
        dest.writeString(content);
    }

    public static final Creator<FDMusic> CREATOR = new Creator<FDMusic>() {

        @Override
        public FDMusic createFromParcel(Parcel source) {

            return new FDMusic(source);
        }

        @Override
        public FDMusic[] newArray(int size) {
            return new FDMusic[size];
        }
    };

    @Override
    public String toString() {
        return "FDMusic{" +
                "musicId=" + musicId +
                ", musicTitle='" + musicTitle + '\'' +
                ", musicUri='" + musicUri + '\'' +
                ", musicLength=" + musicLength +
                ", musicImage='" + musicImage + '\'' +
                ", musicArtist='" + musicArtist + '\'' +


                ", musicTime='" + musicTime + '\'' +
                ", contentType=" + contentType +

                '}';
    }
}
