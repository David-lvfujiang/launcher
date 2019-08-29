package com.fenda.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.util.TUtil;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:19
  * @Description
  *
  */
public abstract class BaseMvpActivity<T extends BasePresenter,M extends BaseModel> extends BaseActivity {

    protected T mPresenter;
    protected M mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mModel = TUtil.getT(this,1);
        initPresenter();
        if (mPresenter != null) {
            mPresenter.mContext = this;
            mPresenter.attachView(this,mModel);
            mPresenter.injectLifecycle(this);
        }
        super.onCreate(savedInstanceState);
    }


    protected abstract void initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
        }
    }
}
