package com.fenda.ai.modle;

import com.fenda.ai.contant.SibichiContant;
import com.fenda.ai.contract.SibichiContract;
import com.fenda.ai.http.SibichiApiService;
import com.fenda.ai.modle.request.SibichiPutDeviceNameRequest;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.protocol.http.RetrofitHelper;

import io.reactivex.Observable;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/5 16:10
 */
public class SibichiModel implements SibichiContract.Model{
    @Override
    public Observable<BaseResponse> sendMsgToApp(SibichiPutDeviceNameRequest sibichiPutDeviceNameRequest) {
        return RetrofitHelper.getInstance(SibichiContant.TEST_BASE_URL).getServer(SibichiApiService.class)
                .sendDCAMessageToApp(sibichiPutDeviceNameRequest)
                .compose(RxSchedulers.<BaseResponse>io_main());
    }
}
