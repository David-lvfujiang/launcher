package com.fenda.gallery.http;

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
