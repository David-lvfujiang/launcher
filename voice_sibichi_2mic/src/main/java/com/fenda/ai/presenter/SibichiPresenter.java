package com.fenda.ai.presenter;

import com.fenda.ai.contract.SibichiContract;
import com.fenda.ai.modle.request.SibichiPutDeviceNameRequest;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxResourceObserver;
import com.fenda.common.util.LogUtil;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/5 16:33
 */
public class SibichiPresenter extends SibichiContract.Presenter {
    @Override
    public void sendMsgToApp(SibichiPutDeviceNameRequest sibichiPutDeviceNameRequest) {
        mRxManage.add(mModel.sendMsgToApp(sibichiPutDeviceNameRequest).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {
                    mView.sendMsgToAppSuccess(response);
                } else {
                    mView.sendMsgToAppFailure(response);
                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));
    }
}
