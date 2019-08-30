package com.fenda.test.model;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.mvp.BaseModel;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.test.api.Constant;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.MyService;
import com.fenda.test.contract.TestContract;

import io.reactivex.Observable;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 10:24
 * @Description
 */
public class TestModel extends BaseModel implements TestContract.Model {


    @Override
    public Observable<BaseResponse> register(LoginRequest request) {
        return RetrofitHelper.getInstance(Constant.TEST_BASE_URL).getServer(MyService.class).register(request).compose(RxSchedulers.<BaseResponse>io_main(getLifecycle()));
    }
}
