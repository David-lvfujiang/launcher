package com.fenda.settings.contract;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;
import com.fenda.settings.model.request.SettingChangeContractNickNameRequest;
import com.fenda.settings.model.request.SettingDeleteLinkmanRequest;
import com.fenda.settings.model.request.SettingsAgreeUserAddRequest;
import com.fenda.settings.model.request.SettingsRegisterDeviceRequest;
import com.fenda.settings.model.request.SettingsUpdateDeviceNameRequest;
import com.fenda.settings.model.response.SettingsGetContractListResponse;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;

import io.reactivex.Observable;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 9:31
 */
public interface SettingsContract {

    interface View extends BaseView {

        void updateDeviceNameSuccess(BaseResponse response);
        void queryDeviceInfoSuccess(BaseResponse<SettingsQueryDeviceInfoResponse> response);
        void unbindDeviceSuccess(BaseResponse response);
        void registerDeviceSuccess(BaseResponse<SettingsRegisterDeviceResponse> response);
        void changeNickNameSuccess(BaseResponse response);
        void deleteLinkmanFromDeviceSuccess(BaseResponse response);
        void getContactsListSuccess(BaseResponse<SettingsGetContractListResponse> response);

        void haveRegisterDevice(BaseResponse<SettingsRegisterDeviceResponse> response);

        void updateDeviceNameFailure(BaseResponse response);
        void queryDeviceInfoFailure(BaseResponse<SettingsQueryDeviceInfoResponse> response);
        void unbindDeviceFailure(BaseResponse response);
        void registerDeviceFailure(BaseResponse<SettingsRegisterDeviceResponse> response);
        void changeNickNameFailure(BaseResponse response);
        void deleteLinkmanFromDeviceFailure(BaseResponse response);
        void getContactsListFailure(BaseResponse<SettingsGetContractListResponse> response);
    }


     interface Model extends BaseModel {
        Observable<BaseResponse> updateDeviceName(SettingsUpdateDeviceNameRequest updateDeviceNameRequestRequest);
        Observable<BaseResponse> queryDeviceInfo();
        Observable<BaseResponse> unbindDevice();
        Observable<BaseResponse> registerDevice(SettingsRegisterDeviceRequest deviceRegisterRequest);
        Observable<BaseResponse> agreeUserAddDevice(SettingsAgreeUserAddRequest agreeUserAddRequest);
        Observable<BaseResponse> changeContractNickName(SettingChangeContractNickNameRequest changeContractNickNameRequest);
        Observable<BaseResponse> deleteLinkmanFromDevice(SettingDeleteLinkmanRequest deleteLinkmanRequest);
        Observable<BaseResponse> getContactsList();

    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void updateDeviceName(SettingsUpdateDeviceNameRequest updateDeviceNameRequestRequest);
        public abstract  void queryDeviceInfo();
        public abstract void unbindDevice();
        public abstract void registerDevice(SettingsRegisterDeviceRequest deviceRegisterRequest);
        public abstract void agreeUserAddDevice(SettingsAgreeUserAddRequest agreeUserAddRequest);
        public abstract void changeContractNickName(SettingChangeContractNickNameRequest changeContractNickNameRequest);
        public abstract void deleteLinkmanFromDevice(SettingDeleteLinkmanRequest deleteLinkmanRequest);
        public abstract void getContactsList();

    }
}
