package com.fenda.gallery.model;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.Constant;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.http.GalleryApiService;
import com.fenda.gallery.http.UploadPhotoRequest;
import com.fenda.protocol.http.RetrofitHelper;

import io.reactivex.Observable;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 11:00
 * @Description 家庭相册Model
 */
public class GalleryModel implements GalleryContract.Model {


    @Override
    public Observable<BaseResponse<FamilyPhotoResponse>> getFamilyGallery(FamilyPhotoRequest request) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(GalleryApiService.class).getFamilyGallery(request.getCurrentPage(), request.getPageSize()).compose(RxSchedulers.<BaseResponse<FamilyPhotoResponse>>io_main());
    }

    @Override
    public Observable<BaseResponse> deleteFamilyPhoto(String ids) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(GalleryApiService.class).deleteFamilyPhoto(ids).compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> uploadFamilyPhoto(UploadPhotoRequest request) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(GalleryApiService.class).uploadFamilyPhoto(request.getRequestBody()).compose(RxSchedulers.<BaseResponse>io_main());
    }
}
