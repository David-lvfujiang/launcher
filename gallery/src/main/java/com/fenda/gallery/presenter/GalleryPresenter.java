package com.fenda.gallery.presenter;


import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxResourceObserver;
import com.fenda.common.util.ToastUtils;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.http.UploadPhotoRequest;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 11:00
 * @Description 家庭相册Presenter
 */
public class GalleryPresenter extends GalleryContract.Presenter {


    @Override
    public void getFamilyPhoto(final FamilyPhotoRequest request) {
        mRxManage.add(mModel.getFamilyGallery(request).subscribeWith(new RxResourceObserver<BaseResponse<FamilyPhotoResponse>>(mView) {
            @Override
            protected void _onNext(BaseResponse<FamilyPhotoResponse> response) {
                if (response.getCode() == 200) {
                    mView.getFamilyPhotoSuccess(response);
                } else {
                    mView.getFamilyPhotoFailure(response);
                }
            }

            @Override
            protected void _onError(String message) {
            }
        }));
    }

    @Override
    public void deleteFamilyPhoto(String ids) {
        mRxManage.add(mModel.deleteFamilyPhoto(ids).subscribeWith(new RxResourceObserver<BaseResponse>(mView) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.deleteFamilyPhotoSuccess(response);
                } else {
                    mView.deleteFamilyPhotoFailure(response);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void uploadPhoto(UploadPhotoRequest request) {
        mRxManage.add(mModel.uploadFamilyPhoto(request).subscribeWith(new RxResourceObserver<BaseResponse>(mView) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.uploadPhotoSuccess(response);
                } else {
                    mView.uploadPhotoSuccess(response);
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
