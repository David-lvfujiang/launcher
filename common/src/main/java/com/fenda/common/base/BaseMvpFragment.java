package com.fenda.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.fenda.common.baserx.RxManager;
import com.fenda.common.mvp.BaseModel;
import com.fenda.common.mvp.BasePresenter;
import com.fenda.common.mvp.BaseView;
import com.fenda.common.util.TUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/30 19:01
  * @Description
  *
  */
public abstract class BaseMvpFragment<T extends BasePresenter,E extends BaseModel> extends BaseFragment implements BaseView {

    protected T mPresenter;
    protected E mModel;
    public RxManager mRxManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mRxManager = new RxManager();
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this,1);
        if(mPresenter!=null){
            mPresenter.mContext = this.getActivity();
        }
        initPresenter();

        super.onCreate(savedInstanceState);

    }

    /**
     * 此处把Model和View与Presenter相关联
     */
    protected abstract void initPresenter();


    @Override
    public void onDestroy() {
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
