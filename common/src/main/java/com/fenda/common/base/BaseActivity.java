package com.fenda.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.R;
import com.fenda.common.baseapp.AppManager;
import com.fenda.common.mvp.BaseView;
import com.fenda.common.util.NetUtil;
import com.fenda.common.view.LoadingInitView;
import com.fenda.common.view.LoadingTransView;
import com.fenda.common.view.NetErrorView;
import com.fenda.common.view.NoDataView;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.CompositeDisposable;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:32
 * @Description This is BaseActivity
 */
public abstract class BaseActivity extends RxAppCompatActivity{


    protected Context mContext;

    protected NetErrorView mNetErrorView;
    protected NoDataView mNoDataView;
    protected LoadingInitView mLoadingInitView;
    protected LoadingTransView mLoadingTransView;
    private ViewStub mViewContent;
    private ViewStub mViewInitLoading;
    private ViewStub mViewTransLoading;
    private ViewStub mViewNoData;
    private ViewStub mViewError;
    private boolean isConfigChange = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.common_activity_root);
        mContext = this;
        initCommonView();
        ARouter.getInstance().inject(this);
        initView();
        initData();
        initListener();
        EventBus.getDefault().register(this);
        AppManager.getAppManager().addActivity(this);
    }

    public void initStatusBar() {
    }


    protected void initCommonView() {
        mViewContent = findViewById(R.id.view_stub_content);
        mViewInitLoading = findViewById(R.id.view_stub_init_loading);
        mViewTransLoading = findViewById(R.id.view_stub_trans_loading);
        mViewError = findViewById(R.id.view_stub_error);
        mViewNoData = findViewById(R.id.view_stub_nodata);

        mViewContent.setLayoutResource(onBindLayout());
        mViewContent.inflate();
    }


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }


    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }



    public abstract int onBindLayout();

    public abstract void initView();

    public abstract void initData();

    public void initListener() {
    }



    public void showInitLoadView() {
        showInitLoadView(true);
    }
    public void hideInitLoadView() {
        showInitLoadView(false);
    }

    public void showTransLoadingView() {
        showTransLoadingView(true);
    }

    public void hideTransLoadingView() {
        showTransLoadingView(false);
    }

    public void showNoDataView() {
        showNoDataView(true);
    }
    public void showNoDataView(int resid) {
        showNoDataView(true, resid);
    }
    public void hideNoDataView() {
        showNoDataView(false);
    }

    public void hideNetWorkErrView() {
        showNetWorkErrView(false);
    }

    public void showNetWorkErrView() {
        showNetWorkErrView(true);
    }





    private void showInitLoadView(boolean show) {
        if (mLoadingInitView == null) {
            View view = mViewInitLoading.inflate();
            mLoadingInitView = view.findViewById(R.id.view_init_loading);
        }
        mLoadingInitView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingInitView.loading(show);
    }


    private void showNetWorkErrView(boolean show) {
        if (mNetErrorView == null) {
            View view = mViewError.inflate();
            mNetErrorView = view.findViewById(R.id.view_net_error);
            mNetErrorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetUtil.checkNetToast()) {
                        return;
                    }
                    hideNetWorkErrView();
                    initData();
                }
            });
        }
        mNetErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    private void showNoDataView(boolean show) {
        if (mNoDataView == null) {
            View view = mViewNoData.inflate();
            mNoDataView = view.findViewById(R.id.view_no_data);
        }
        mNoDataView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showNoDataView(boolean show, int resid) {
        showNoDataView(show);
        if (show) {
            mNoDataView.setNoDataView(resid);
        }
    }

    private void showTransLoadingView(boolean show) {
        if (mLoadingTransView == null) {
            View view = mViewTransLoading.inflate();
            mLoadingTransView = view.findViewById(R.id.view_trans_loading);
        }
        mLoadingTransView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingTransView.loading(show);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void onEvent(EventMessage<BaseTcpMessage> event) {
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横竖切换
        isConfigChange=true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (!isConfigChange){
            AppManager.getAppManager().finishActivity(this);
        }
    }
}
