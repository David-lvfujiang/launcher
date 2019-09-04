package com.fenda.settings.http;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.settings.model.request.SettingsAgreeUserAddRequest;
import com.fenda.settings.model.request.SettingsRegisterDeviceRequest;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;

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
    //修改设备名称
    @FormUrlEncoded
    @PUT("south-device/manage/updateDeviceName")
    Observable<BaseResponse> updateDeviceName(@Field("deviceId") String deviceId, @Field("name") String name);

    //获取设备信息
    @GET("south-device/manage/getDeviceInfo")
    Observable<BaseResponse<SettingsQueryDeviceInfoResponse>> getDeviceInfo();

    //解散家庭
    @POST("south-device/manage/unBind")
    Observable<BaseResponse> unBindDevice ();

    //设备注册
    @POST("south-device/manage/open/deviceRegister")
    Observable<BaseResponse> deviceRegister(@Body SettingsRegisterDeviceRequest deviceRegisterRequest);

    //设备端同意添加联系人邀请
    @POST("south-device/manage/confirmDeviceBind")
    Observable<BaseResponse<SettingsQueryDeviceInfoResponse>> confirmDeviceBind(@Body SettingsAgreeUserAddRequest request);

    //修改家庭圈昵称
    @FormUrlEncoded
    @PUT("south-device/manage/updateFamilyNickName")
    Observable<BaseResponse> changeContractNickName(@Field("nickName") String nickName, @Field("userId") String userId);

    //设备端删除家庭成员
    @FormUrlEncoded
    @PUT("south-device/manage/deleteLinkmanFromDevice")
    Observable<BaseResponse> deleteLinkmanFromDevice(@Field("userId") String userId);

    //获取联系人列表
    @GET("south-device/manage/getLinkUserInfoList")
    Observable<BaseResponse<List<UserInfoBean>>> getContactsList();


}
