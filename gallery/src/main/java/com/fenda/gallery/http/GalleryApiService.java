package com.fenda.gallery.http;

import com.fenda.common.base.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 18:54
 * @Description
 */
public interface GalleryApiService {
    //查询家庭相册
    @GET("south-device/manage/selectPhotos")
    Observable<BaseResponse<FamilyPhotoResponse>> getFamilyGallery(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize);

    //删除家庭相册图片
    @DELETE("south-device/manage/delete")
    Observable<BaseResponse> deleteFamilyPhoto(@Query("ids") String ids);

    //上传图片
    @Multipart
    @POST("south-device/manage/fileUpload")
    Observable<BaseResponse> uploadFamilyPhoto(@Part List<MultipartBody.Part> body);

}
