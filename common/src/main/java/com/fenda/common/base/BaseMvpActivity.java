package com.fenda.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fenda.common.baserx.RxManager;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;
import com.fenda.common.util.TUtil;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:19
  * @Description
  *
  */
public abstract class BaseMvpActivity<T extends BasePresenter,M extends BaseModel> extends BaseActivity implements BaseView {

    protected T mPresenter;
    protected M mModel;

    public RxManager mRxManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mRxManager=new RxManager();
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this,1);
        if (mPresenter != null){
            mPresenter.mContext = this;
        }
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    /**
     * 此处把Model和View与Presenter相关联
     */
    protected abstract void initPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.onDestroy();
        }
        if (mRxManager != null){
            mRxManager.clear();
        }


    }

    @Override
    public void showLoading(String title) {
        showInitLoadView();

    }

    @Override
    public void hideLoading() {
        hideInitLoadView();

    }



    @Override
    public void showNetError() {
        showNetWorkErrView();

    }

    @Override
    public void hideNetError() {
        hideNetWorkErrView();

    }
}
