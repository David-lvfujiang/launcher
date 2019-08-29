package com.fenda.test.presenter;


import com.fenda.common.base.BaseResponse;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.util.LogUtil;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.RegisterRequest;
import com.fenda.test.contract.TestContract;
import com.fenda.test.model.TestModel;

import io.reactivex.functions.Consumer;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:26
 * @Description
 */
public class TestPresenter extends BasePresenter<TestContract.View, TestModel> implements TestContract.Presenter {


    @Override
    public void register(LoginRequest request) {
        mModel.register(request).subscribe(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                LogUtil.d(response);
                mView.registerDevice(response);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.e(throwable.getMessage());
            }
        });
    }
}
