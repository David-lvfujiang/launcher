package com.fenda.gallery.contract;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BaseView;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.http.UploadPhotoRequest;

import io.reactivex.Observable;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:18
 * @Description
 */
public interface GalleryContract {

    interface View extends BaseView {

        void getFamilyPhotoSuccess(BaseResponse<FamilyPhotoResponse> response);

        void deleteFamilyPhotoSuccess(BaseResponse response);

        void uploadPhotoSuccess(BaseResponse request);

        void getFamilyPhotoFailure(BaseResponse<FamilyPhotoResponse> response);

        void deleteFamilyPhotoFailure(BaseResponse response);

        void uploadPhotoFailure(BaseResponse response);


    }


    interface Model {
        Observable<BaseResponse<FamilyPhotoResponse>> getFamilyGallery(FamilyPhotoRequest loginRequest);

        Observable<BaseResponse> deleteFamilyPhoto(String ids);

        Observable<BaseResponse> uploadFamilyPhoto(UploadPhotoRequest request);
    }

    interface Presenter {
        void getFamilyPhoto(FamilyPhotoRequest request);

        void deleteFamilyPhoto(String ids);

        void uploadPhoto(UploadPhotoRequest request);

    }


}
