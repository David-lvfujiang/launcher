package com.fenda.homepage.presenter;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxResourceObserver;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.util.LogUtil;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.model.request.AgreeUserAddRequest;

import java.util.List;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/4 11:36
 * @Description
 */
public class MainPresenter extends MainContract.Presenter {
    @Override
    public void getFamilyContacts() {
        mRxManage.add(mModel.getFamilyContacts().subscribeWith(new RxResourceObserver<BaseResponse<List<UserInfoBean>>>(mView,false) {
            @Override
            protected void _onNext(BaseResponse<List<UserInfoBean>> response) {
                if (response.getCode() == 200) {
                    mView.getFamilyContactsSuccess(response);
                } else {
                    mView.getFamilyContactsFailure(response);
                }
            }

            @Override
            protected void _onError(String message) {
            }
        }));
    }

    @Override
    public void agreeUserAddDevice(AgreeUserAddRequest agreeUserAddRequest, boolean confirmType) {
        mRxManage.add(mModel.agreeUserAddDevice(agreeUserAddRequest, confirmType).subscribeWith(new RxResourceObserver<BaseResponse>(mView,false) {
            @Override
            protected void _onNext(BaseResponse response) {
                if (response.getCode() == 200) {

                } else {

                }
            }
            @Override
            protected void _onError(String message) {
                LogUtil.e(" error" + message);
            }
        }));

    }
}
