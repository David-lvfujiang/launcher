package com.fenda.test.api;

import com.fenda.common.base.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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


    //查询用户
    @GET("linkman/seachUserInfoByMobile")
    Observable<BaseResponse> seachUserInfoByMobile(@Query("mobile") String mobilePhone);

}
