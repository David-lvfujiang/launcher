package com.fenda.ai.contract;

import com.fenda.ai.modle.request.SibichiPutDeviceNameRequest;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;

import io.reactivex.Observable;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/5 16:12
 */
public interface SibichiContract {
    interface View extends BaseView {
        void sendMsgToAppSuccess(BaseResponse response);
        void sendMsgToAppFailure(BaseResponse response);
    }

    interface Model extends BaseModel {
        Observable<BaseResponse> sendMsgToApp(SibichiPutDeviceNameRequest sibichiPutDeviceNameRequest);

    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void sendMsgToApp(SibichiPutDeviceNameRequest sibichiPutDeviceNameRequest);

    }

}
