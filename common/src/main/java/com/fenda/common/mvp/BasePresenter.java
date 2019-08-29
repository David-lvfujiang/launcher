package com.fenda.common.mvp;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleProvider;


/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:18
  * @Description
  *
  */
public abstract class BasePresenter<V,M extends BaseModel> {
    public Context mContext;
    public V mView;
    public M mModel;

    public void attachView(V mView,M mModel){
        this.mView = mView;
        this.mModel = mModel;
    }





    public void detachView() {
        if (mView != null){
            mView = null;
        }
    }

    public void injectLifecycle(LifecycleProvider lifecycle) {
        if (mModel != null) {
            mModel.injectLifecycle(lifecycle);
        }
    }





}
