package com.fenda.common.baserx;

import android.app.Activity;
import android.content.Context;


import com.fenda.common.BaseApplication;
import com.fenda.common.R;
import com.fenda.common.util.NetUtil;

import org.reactivestreams.Subscriber;

import io.reactivex.Observable;
import io.reactivex.Observer;


/**
 * des:订阅封装
 * Created by xsf
 * on 2016.09.10:16
 */

/********************使用例子********************/
/*_apiService.login(mobile, verifyCode)
        .//省略
        .subscribe(new RxSubscriber<User user>(mContext,false) {
@Override
public void _onNext(User user) {
        // 处理user
        }

@Override
public void _onError(String msg) {
        ToastUtil.showShort(mActivity, msg);
        });*/
public abstract class RxSubscriber<T> implements Observer<T> {

    private Context mContext;
    private String msg;
    private boolean showDialog=true;

    /**
     * 是否显示浮动dialog
     */
    public void showDialog() {
        this.showDialog= true;
    }
    public void hideDialog() {
        this.showDialog= true;
    }

    public RxSubscriber(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog=showDialog;
    }
    public RxSubscriber(Context context) {
//        this(context, BaseApplication.getAppContext().getString(R.string.loading),true);
    }
    public RxSubscriber(Context context, boolean showDialog) {
//        this(context, BaseApplication.getAppContext().getString(R.string.loading),showDialog);
    }


    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //网络
        if (!NetUtil.checkNet()) {
//            _onError(BaseApplication.getAppContext().getString(R.string.no_net));
        }
        //服务器
//        else if (e instanceof ServerException) {
//            _onError(e.getMessage());
//        }
        //其它
        else {
//            _onError(BaseApplication.getAppContext().getString(R.string.net_error));
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);

}
