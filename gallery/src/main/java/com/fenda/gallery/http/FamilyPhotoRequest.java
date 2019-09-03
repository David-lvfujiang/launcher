package com.fenda.gallery.http;
/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:58
 * @Description 相册列表请求实体
 */
public class FamilyPhotoRequest {

    private int currentPage;
    private int pageSize;


    public FamilyPhotoRequest() {
    }

    public FamilyPhotoRequest(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
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
}
