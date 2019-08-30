package com.fenda.gallery.model;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.mvp.BaseModel;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.Constant;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.http.GalleryApiService;
import com.fenda.gallery.http.UploadPhotoRequest;
import com.fenda.protocol.http.RetrofitHelper;

import io.reactivex.Observable;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:24
 * @Description
 */
public class GalleryModel extends BaseModel implements GalleryContract.Model {


    @Override
    public Observable<BaseResponse<FamilyPhotoResponse>> getFamilyGallery(FamilyPhotoRequest request) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(GalleryApiService.class).getFamilyGallery(request.getCurrentPage(), request.getPageSize()).compose(RxSchedulers.<BaseResponse<FamilyPhotoResponse>>io_main(getLifecycle()));
    }

    @Override
    public Observable<BaseResponse> deleteFamilyPhoto(String ids) {
        return null;
    }

    @Override
    public Observable<BaseResponse> uploadFamilyPhoto(UploadPhotoRequest request) {
        return null;
    }
}
