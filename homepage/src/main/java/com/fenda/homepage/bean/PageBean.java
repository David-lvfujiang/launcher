package com.fenda.homepage.bean;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/11/29 14:28
 * @Description
 */
public class PageBean {
    /**
     * 新闻标题t
     */
    private String title;
    /**
     * 新闻url
     */
    private String url;
    /**
     * 默认图片
     */
    private int defaultImg;
    /**
     * 类型  新闻  电影   音乐
     */
    private int type;


    public PageBean() {
    }

    public PageBean(String title, String url, int defaultImg, int type) {
        this.title = title;
        this.url = url;
        this.defaultImg = defaultImg;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
