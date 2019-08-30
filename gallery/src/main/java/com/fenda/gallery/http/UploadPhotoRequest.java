package com.fenda.gallery.http;

import java.util.List;

import okhttp3.MultipartBody;

public class UploadPhotoRequest {


    private List<MultipartBody.Part> requestBody;


    public UploadPhotoRequest() {
    }

    public UploadPhotoRequest(List<MultipartBody.Part> requestBody) {
        this.requestBody = requestBody;
    }


    public List<MultipartBody.Part> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(List<MultipartBody.Part> requestBody) {
        this.requestBody = requestBody;
    }
}
