package com.fenda.test.model;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.mvp.BaseModel;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.test.api.Constant;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.LoginResult;
import com.fenda.test.api.MyService;
import com.fenda.test.api.RegisterRequest;
import com.fenda.test.contract.TestContract;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:24
 * @Description
 */
public class TestModel  implements TestContract.Model {


    @Override
    public Observable<BaseResponse<LoginResult>> register(LoginRequest request) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(MyService.class)
                .register(request)
                .compose(RxSchedulers.<BaseResponse<LoginResult>>io_main());

    }

    @Override
    public Observable<BaseResponse> searchUser(String userPhone) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(MyService.class)
                .seachUserInfoByMobile(userPhone)
                .compose(RxSchedulers.<BaseResponse>io_main());
    }
}
