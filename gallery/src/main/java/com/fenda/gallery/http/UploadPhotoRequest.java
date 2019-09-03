package com.fenda.gallery.http;

import java.util.List;

import okhttp3.MultipartBody;
/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:59
 * @Description 上传相册请求实体
 */
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
