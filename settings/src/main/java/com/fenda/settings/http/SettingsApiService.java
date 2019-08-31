package com.fenda.settings.http;

import com.fenda.common.base.BaseResponse;
import com.fenda.settings.model.request.SettingsAgreeUserAddRequest;
import com.fenda.settings.model.request.SettingsDeviceRegisterRequest;
import com.fenda.settings.model.response.SettingsDeviceInfoResponse;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 14:55
 */
public interface SettingsApiService {
    //设备注册
    @POST("south-device/manage/open/deviceRegister")
    Observable<BaseResponse> deviceRegister(@Body SettingsDeviceRegisterRequest deviceRegisterRequest);

    //获取设备信息
    @GET("south-device/manage/getDeviceInfo")
    Observable<BaseResponse<SettingsDeviceInfoResponse>> getDeviceInfo();

    //设备端同意添加联系人邀请
    @POST("south-device/manage/confirmDeviceBind")
    Observable<BaseResponse<SettingsDeviceInfoResponse>> confirmDeviceBind(@Body SettingsAgreeUserAddRequest request);

    //修改家庭圈昵称
    @FormUrlEncoded
    @PUT("south-device/manage/updateDeviceName")
    Observable<BaseResponse> updateDeviceName(@Field("deviceId") String deviceId, @Field("name") String name);

    //获取联系人列表
    @GET("south-device/manage/getLinkUserInfoList")
    Observable<BaseResponse<List<UserInfoBean>>> getContactList();

    //修改家庭圈昵称
    @FormUrlEncoded
    @PUT("south-device/manage/updateFamilyNickName")
    Observable<BaseResponse> changeNickName(@Field("nickName") String nickName, @Field("userId") String userId);

    //解散家庭
    @FormUrlEncoded
    @POST("south-device/manage/unBind")
    Observable<BaseResponse> unBindDevice (@Field("deviceId") String deviceId);

    //设备端删除家庭成员
    @FormUrlEncoded
    @PUT("south-device/manage/deleteLinkmanFromDevice")
    Observable<BaseResponse> deleteLinkmanFromDevice(@Field("userId") String userId);
}
