package com.fenda.homepage.model;

import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.homepage.R;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.http.MainApiService;
import com.fenda.homepage.model.request.AgreeUserAddRequest;
import com.fenda.protocol.http.RetrofitHelper;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/4 11:02
 * @Description
 */
public class MainModel implements MainContract.Model {
    @Override
    public Observable<BaseResponse<List<UserInfoBean>>> getFamilyContacts() {
        return RetrofitHelper.getInstance(BaseApplication.getInstance().getString(R.string.BASE_SERVER_URL))
                .getServer(MainApiService.class)
                .getFamilyContacts()
                .compose(RxSchedulers.<BaseResponse<List<UserInfoBean>>>io_main());
    }


    @Override
    public Observable<BaseResponse> agreeUserAddDevice(AgreeUserAddRequest agreeUserAddRequest, boolean confirmType) {
        return RetrofitHelper.getInstance(BaseApplication.getInstance().getString(R.string.BASE_SERVER_URL))
                .getServer(MainApiService.class)
                .confirmDeviceBind(agreeUserAddRequest, confirmType)
                .compose(RxSchedulers.<BaseResponse>io_main());
    }

}
