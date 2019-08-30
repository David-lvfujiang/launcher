package com.fenda.test.api;

import com.fenda.common.base.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 18:54
 * @Description
 */
public interface MyService {
    /**注册
     * @return
     */
    @POST("user/login")
    Observable<BaseResponse<LoginResult>> register(@Body LoginRequest loginRequest);

}
