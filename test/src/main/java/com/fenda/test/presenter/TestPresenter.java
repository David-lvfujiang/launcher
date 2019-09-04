package com.fenda.test.presenter;


import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxResourceObserver;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.LoginResult;
import com.fenda.test.contract.TestContract;

import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:26
 * @Description
 */
public class TestPresenter extends TestContract.Presenter {

    @Override
    public void register(LoginRequest loginRequest) {
        mRxManage.add(mModel.register(loginRequest).subscribeWith(new RxResourceObserver<BaseResponse<LoginResult>>(mView,true) {
            @Override
            protected void _onNext(BaseResponse<LoginResult> loginResultBaseResponse) {
                loginResultBaseResponse.getMessage();
                mView.registerDevice(loginResultBaseResponse);

            }

            @Override
            protected void _onError(String message) {

            }
        }));
//        mRxManage.add(mModel.register(loginRequest).subscribe(new Consumer<BaseResponse>() {
//            @Override
//            public void accept(BaseResponse response) throws Exception {
//                LogUtil.d(response);
//                mView.registerDevice(response);
//            }
//        }));
    }

    @Override
    public void searchUserByPhone(String userPhone) {

        mRxManage.add(mModel.searchUser(userPhone).subscribeWith(new RxResourceObserver<BaseResponse>(mView,true) {
            @Override
            protected void _onNext(BaseResponse loginResultBaseResponse) {
     //           loginResultBaseResponse.getMessage();
                mView.searchUserByPhone(loginResultBaseResponse);

            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }
}
