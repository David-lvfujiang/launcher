package com.fenda.ai.http;

import com.fenda.ai.modle.request.SibichiPutDeviceNameRequest;
import com.fenda.common.base.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/5 16:00
 */
public interface SibichiApiService {
    //推送思必驰device Name数据到App
    @POST("apiForDCAAndroid/sendMessageDeviceToApp")
    Observable<BaseResponse> sendDCAMessageToApp(@Body SibichiPutDeviceNameRequest sibichiPutDeviceNameRequest);
}
