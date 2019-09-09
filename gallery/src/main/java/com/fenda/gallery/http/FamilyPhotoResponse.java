package com.fenda.gallery.http;

import com.fenda.gallery.bean.PhoneCameraBean;

import java.util.List;
/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:59
 * @Description 相册列表响应实体
 */
public class FamilyPhotoResponse {


    private int currentPage;
    private int pageSize;
    private int totalNum;
    private int isMore;
    private int totalPage;
    private int startIndex;
    private List<PhoneCameraBean> items;

    public FamilyPhotoResponse(int currentPage, int pageSize, int totalNum, int isMore, int totalPage, int startIndex, List<PhoneCameraBean> items) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.isMore = isMore;
        this.totalPage = totalPage;
        this.startIndex = startIndex;
        this.items = items;
    }

    public FamilyPhotoResponse() {
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getIsMore() {
        return isMore;
    }

    public void setIsMore(int isMore) {
        this.isMore = isMore;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public List<PhoneCameraBean> getItems() {
        return items;
    }

    public void setItems(List<PhoneCameraBean> items) {
        this.items = items;
    }


}
