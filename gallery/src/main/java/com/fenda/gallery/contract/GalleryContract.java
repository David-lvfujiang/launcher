package com.fenda.gallery.contract;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.http.UploadPhotoRequest;

import io.reactivex.Observable;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:53
 * @Description 家庭相册Contract
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


    interface Model extends BaseModel {
        Observable<BaseResponse<FamilyPhotoResponse>> getFamilyGallery(FamilyPhotoRequest loginRequest);

        Observable<BaseResponse> deleteFamilyPhoto(String ids);

        Observable<BaseResponse> uploadFamilyPhoto(UploadPhotoRequest request);
    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void getFamilyPhoto(FamilyPhotoRequest request);

        public abstract  void deleteFamilyPhoto(String ids);

        public abstract void uploadPhoto(UploadPhotoRequest request);

    }


}
