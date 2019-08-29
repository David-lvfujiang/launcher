package com.fenda.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.util.TUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public abstract class BaseMvpFragment<T extends BasePresenter,E extends BaseModel> extends BaseFragment {

    protected T mPresenter;
    protected E mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        mModel = TUtil.getT(this, 1);
        if (mPresenter != null) {
            mPresenter.mContext = getActivity();
            mPresenter.attachView(this,mModel);
            mPresenter.injectLifecycle((RxAppCompatActivity)getActivity());
        }
        super.onCreate(savedInstanceState);

    }

    protected abstract void initPresenter();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }
}
