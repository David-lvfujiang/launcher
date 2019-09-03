package com.fenda.settings.presenter;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxResourceObserver;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.request.SettingChangeContractNickNameRequest;
import com.fenda.settings.model.request.SettingDeleteLinkmanRequest;
import com.fenda.settings.model.request.SettingsAgreeUserAddRequest;
import com.fenda.settings.model.request.SettingsRegisterDeviceRequest;
import com.fenda.settings.model.request.SettingsUpdateDeviceNameRequest;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 9:49
 */
public class SettingsPresenter extends SettingsContract.Presenter {
    @Override
    public void updateDeviceName(SettingsUpdateDeviceNameRequest updateDeviceNameRequestRequest) {
        mRxManage.add(mModel.updateDeviceName(updateDeviceNameRequestRequest).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.updateDeviceNameSuccess(response);
                } else {
                    mView.updateDeviceNameFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void queryDeviceInfo() {
        mRxManage.add(mModel.queryDeviceInfo().subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.queryDeviceInfoSuccess(response);
                } else {
                    mView.queryDeviceInfoFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void unbindDevice() {
        mRxManage.add(mModel.unbindDevice().subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.unbindDeviceSuccess(response);
                } else {
                    mView.unbindDeviceFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void registerDevice(SettingsRegisterDeviceRequest deviceRegisterRequest) {
        mRxManage.add(mModel.registerDevice(deviceRegisterRequest).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200 && response.getMessage().equals("操作成功")) {
                    mView.registerDeviceSuccess(response);
                } else if(response.getCode() == 200 && response.getMessage().equals("设备已经注册")){
                    mView.haveRegisterDevice(response);
                } else {
                    mView.registerDeviceFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void agreeUserAddDevice(SettingsAgreeUserAddRequest agreeUserAddRequest) {
        mRxManage.add(mModel.agreeUserAddDevice(agreeUserAddRequest).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {

                } else {

                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void changeContractNickName(SettingChangeContractNickNameRequest changeContractNickNameRequest) {
        mRxManage.add(mModel.changeContractNickName(changeContractNickNameRequest).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.changeNickNameSuccess(response);
                } else {
                    mView.changeNickNameFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void deleteLinkmanFromDevice(SettingDeleteLinkmanRequest deleteLinkmanRequest) {
        mRxManage.add(mModel.deleteLinkmanFromDevice(deleteLinkmanRequest).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.deleteLinkmanFromDeviceSuccess(response);
                } else {
                    mView.deleteLinkmanFromDeviceFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

    @Override
    public void getContactsList() {
        mRxManage.add(mModel.getContactsList().subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.getContactsListSuccess(response);
                } else {
                    mView.getContactsListFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }

}
