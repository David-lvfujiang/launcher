package com.fenda.settings.model;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.http.SettingsApiService;
import com.fenda.settings.model.request.SettingChangeContractNickNameRequest;
import com.fenda.settings.model.request.SettingDeleteLinkmanRequest;
import com.fenda.settings.model.request.SettingsAgreeUserAddRequest;
import com.fenda.settings.model.request.SettingsRegisterDeviceRequest;
import com.fenda.settings.model.request.SettingsUpdateDeviceNameRequest;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 9:30
 */
public class SettingsModel implements SettingsContract.Model {

    @Override
    public Observable<BaseResponse> updateDeviceName(SettingsUpdateDeviceNameRequest updateDeviceNameRequestRequest) {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .updateDeviceName(updateDeviceNameRequestRequest.getDeviceId(), updateDeviceNameRequestRequest.getName())
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> queryDeviceInfo() {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .getDeviceInfo()
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> unbindDevice() {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .unBindDevice()
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> registerDevice(SettingsRegisterDeviceRequest deviceRegisterRequest) {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .deviceRegister(deviceRegisterRequest)
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> agreeUserAddDevice(SettingsAgreeUserAddRequest agreeUserAddRequest, boolean confirmType) {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .confirmDeviceBind(agreeUserAddRequest, confirmType)
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> changeContractNickName(SettingChangeContractNickNameRequest changeContractNickNameRequest) {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .changeContractNickName(changeContractNickNameRequest.getNickName(), changeContractNickNameRequest.getUserId())
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse> deleteLinkmanFromDevice(SettingDeleteLinkmanRequest deleteLinkmanRequest) {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .deleteLinkmanFromDevice(deleteLinkmanRequest.getUserId())
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

    @Override
    public Observable<BaseResponse<List<UserInfoBean>>> getContactsList() {
        return RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class)
                .getContactsList()
                .compose(RxSchedulers.<BaseResponse<List<UserInfoBean>>>io_main());
    }

}
