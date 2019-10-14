package com.fenda.homepage.http;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.homepage.model.request.AgreeUserAddRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/4 11:08
 * @Description
 */
public interface MainApiService {
    /**
     * 获取联系人列表
     * @return
     */
    @GET("south-device/manage/getLinkUserInfoList")
    Observable<BaseResponse<List<UserInfoBean>>> getFamilyContacts();

    //设备端同意添加联系人邀请
    @POST("south-device/manage/confirmDeviceBind")
    Observable<BaseResponse> confirmDeviceBind(@Body AgreeUserAddRequest request, @Query("confirmType") boolean confirmType);

}
