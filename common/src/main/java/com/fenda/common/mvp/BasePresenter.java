package com.fenda.common.mvp;

import android.content.Context;

import com.fenda.common.baserx.RxManager;
import com.trello.rxlifecycle2.LifecycleProvider;


/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:18
  * @Description
  *
  */
public abstract class BasePresenter<V,M extends BaseModel> {
    public Context mContext;
    public M mModel;
    public V mView;
    public RxManager mRxManage = new RxManager();

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }
    public void onStart(){
    };
    public void onDestroy() {
        mRxManage.clear();
    }




}
