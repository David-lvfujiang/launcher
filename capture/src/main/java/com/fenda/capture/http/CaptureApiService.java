package com.fenda.capture.http;

import com.fenda.capture.request.CaptureMessageRequest;
import com.fenda.common.base.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/31
 * @Describe:
 */
public interface CaptureApiService {
    //设备注册
    @POST("south-device/manage/DeviceFunctionCollect")
    @FormUrlEncoded
    Observable<BaseResponse> commitUserAction(@Field("functionType") String functionType,@Field("location") String location,@Field("usageMode") String usageMode);
}
