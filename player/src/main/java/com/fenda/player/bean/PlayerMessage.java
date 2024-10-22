package com.fenda.player.bean;

import java.io.Serializable;

/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class PlayerMessage implements Serializable {

    private String musicUrl;
    private String musicName;
    private String musicTitle;
    private int msgType;
    private boolean playAnimation;
    private int contentType;
    private String content;
    private int playtime;

    public PlayerMessage(String musicUrl, String musicName, String musicTitle, int msgType, boolean playAnimation, int contentType, String content, int playtime) {
        this.musicUrl = musicUrl;
        this.musicName = musicName;
        this.musicTitle = musicTitle;
        this.msgType = msgType;
        this.playAnimation = playAnimation;
        this.contentType = contentType;
        this.content = content;
        this.playtime = playtime;
    }

    public PlayerMessage() {
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

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public boolean isPlayAnimation() {
        return playAnimation;
    }

    public void setPlayAnimation(boolean playAnimation) {
        this.playAnimation = playAnimation;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public int getPlaytime() {
        return playtime;
    }

    @Override
    public String toString() {
        return "PlayerMessage{" +
                "musicUrl='" + musicUrl + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicTitle='" + musicTitle + '\'' +
                ", msgType=" + msgType +
                ", playAnimation=" + playAnimation +
                ", contentType=" + contentType +
                ", content='" + content + '\'' +
                ", playtime=" + playtime +
                '}';
    }
}
