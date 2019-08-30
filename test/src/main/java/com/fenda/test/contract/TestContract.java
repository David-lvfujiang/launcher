package com.fenda.test.contract;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.LoginResult;
import com.fenda.test.api.RegisterRequest;

import io.reactivex.Observable;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:18
 * @Description
 */
public interface TestContract {

    interface View extends BaseView{

        void registerDevice(BaseResponse<LoginResult> response);


    }



    interface Model extends BaseModel {
        Observable<BaseResponse<LoginResult>> register(LoginRequest loginRequest);


    }

    abstract class Presenter extends BasePresenter<View,Model> {
        public abstract void register(LoginRequest loginRequest);

    }




}
