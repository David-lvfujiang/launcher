package com.fenda.homepage.contract;

import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/4 10:55
 * @Description 主页Contract
 */
public interface MainContract {
    interface View extends BaseView {
        void getFamilyContactsSuccess(BaseResponse<List<UserInfoBean>> response);

        void getFamilyContactsFailure(BaseResponse<List<UserInfoBean>> response);

    }

    interface Model extends BaseModel {
        Observable<BaseResponse<List<UserInfoBean>>> getFamilyContacts();
    }

    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void getFamilyContacts();

    }
}
