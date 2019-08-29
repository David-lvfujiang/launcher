package com.fenda.test.contract;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BaseView;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.RegisterRequest;

import io.reactivex.Observable;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:18
 * @Description
 */
public interface TestContract {

    interface View extends BaseView{

        void registerDevice(BaseResponse response);



    }



    interface Model{
        Observable<BaseResponse> register(LoginRequest loginRequest);


    }

    interface Presenter{
        void register(LoginRequest loginRequest);

    }




}
