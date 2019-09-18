package com.fenda.homepage.http;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

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
}
