package com.fenda.gallery.presenter;


import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.util.LogUtil;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.UploadPhotoRequest;
import com.fenda.gallery.model.GalleryModel;

import io.reactivex.functions.Consumer;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:26
 * @Description
 */
public class GalleryPresenter extends BasePresenter<GalleryContract.View, GalleryModel> implements GalleryContract.Presenter {


    @Override
    public void getFamilyPhoto(final FamilyPhotoRequest request) {
        mModel.getFamilyGallery(request).subscribe(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                if (response.getCode() == 200) {
                    mView.getFamilyPhotoSuccess(response);
                } else {
                    mView.getFamilyPhotoFailure(response);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.e(throwable.getMessage());
            }
        });

    }

    @Override
    public void deleteFamilyPhoto(String ids) {

    }

    @Override
    public void uploadPhoto(UploadPhotoRequest request) {

    }
}
