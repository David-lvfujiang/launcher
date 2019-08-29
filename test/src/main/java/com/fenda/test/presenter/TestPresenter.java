package com.fenda.test.presenter;


import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.baserx.RxSubscriber;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.util.LogUtil;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.RegisterRequest;
import com.fenda.test.contract.TestContract;
import com.fenda.test.model.TestModel;

import org.reactivestreams.Subscription;

import io.reactivex.functions.Consumer;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:26
 * @Description
 */
public class TestPresenter extends TestContract.Presenter {


    @Override
    public void register(LoginRequest loginRequest) {
        mView.showLoading("开始加载");
        mRxManage.add(mModel.register(loginRequest).subscribe(new Consumer<BaseResponse>() {
            @Override
            public void accept(BaseResponse response) throws Exception {
                LogUtil.d(response);
                mView.registerDevice(response);
            }
        }));
    }
}
