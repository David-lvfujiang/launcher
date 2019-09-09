package com.fenda.common.baserx;

import android.content.Context;


import com.fenda.common.BaseApplication;
import com.fenda.common.R;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.mvp.BaseView;
import com.fenda.common.util.NetUtil;

import io.reactivex.observers.ResourceObserver;


/********************使用例子********************/
/*_apiService.login(mobile, verifyCode)
        .//省略
        .subscribe(new RxResourceObserver<User user>(mContext,false) {
@Override
public void _onNext(User user) {
        // 处理user
        }

@Override
public void _onError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        });*/
/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/30 10:09
 * @Description
 *
 */
public abstract class RxResourceObserver<T> extends ResourceObserver<T> {

    private BaseView mView;
    private String msg;
    private boolean showLoading;


    public RxResourceObserver(BaseView mView) {
        this(mView,true);
    }

    public RxResourceObserver(BaseView mView, String msg, boolean showLoading) {
        this.mView =  mView;
        this.msg = msg;
        this.showLoading = showLoading;
    }
    public RxResourceObserver(BaseView mView, boolean showLoading) {
        this(mView,null,showLoading);
    }


    @Override
    protected void onStart() {
        // TODO 在这里可以添加请求网络前的一些初始化操作,比如打开请求网络的loading
        if (showLoading && mView != null){
            mView.showLoading(msg);
        }
    }

    @Override
    public void onComplete() {
        if (mView != null){
            mView.hideLoading();
            mView.hideNetError();
        }

    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (mView != null){
            mView.hideLoading();
        }
        //网络
        if (!NetUtil.checkNet()) {
            if (mView != null ){
                mView.showNetError();
            }
            _onError(BaseApplication.getInstance().getString(R.string.no_net));
        }
        //服务器
        else if (e instanceof ServerException) {
            _onError(e.getMessage());
        }
        //其它
        else {
            _onError(BaseApplication.getInstance().getString(R.string.net_error));
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

}
