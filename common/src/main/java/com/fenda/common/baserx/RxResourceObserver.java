package com.fenda.common.baserx;

import android.content.Context;


import com.fenda.common.BaseApplication;
import com.fenda.common.R;
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

    public RxResourceObserver() {
        this(BaseApplication.getInstance());
    }

    public RxResourceObserver(Context context, String msg, boolean showDialog) {
        this.mContext = context;
        this.msg = msg;
        this.showDialog=showDialog;
    }
    public RxResourceObserver(Context context) {
        this(context,false);
    }
    public RxResourceObserver(Context context, boolean showDialog) {
        this(context,null,showDialog);
    }


    @Override
    protected void onStart() {
        // TODO 在这里可以添加请求网络前的一些初始化操作,比如打开请求网络的loading
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
