package com.fenda.homepage.presenter;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.baserx.RxResourceObserver;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.homepage.contract.MainContract;

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
}
