package com.fenda.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.R;
import com.fenda.common.mvp.BaseView;
import com.fenda.common.util.NetUtil;
import com.fenda.common.view.LoadingInitView;
import com.fenda.common.view.LoadingTransView;
import com.fenda.common.view.NetErrorView;
import com.fenda.common.view.NoDataView;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:32
 * @Description This is BaseActivity
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BaseView {


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_root);
        mContext = this;
        initCommonView();
        ARouter.getInstance().inject(this);
        initView();
        initListener();
        initData();
        EventBus.getDefault().register(this);


    }


    protected void initCommonView() {
        mViewContent = findViewById(R.id.view_stub_content);
        mViewContent = findViewById(R.id.view_stub_content);
        mViewInitLoading = findViewById(R.id.view_stub_init_loading);
        mViewTransLoading = findViewById(R.id.view_stub_trans_loading);
        mViewError = findViewById(R.id.view_stub_error);
        mViewNoData = findViewById(R.id.view_stub_nodata);

        mViewContent.setLayoutResource(onBindLayout());
        mViewContent.inflate();
    }


    public abstract int onBindLayout();

    @Override
    public abstract void initView();

    @Override
    public abstract void initData();

    @Override
    public void initListener() {
    }
    @Override
    public void showInitLoadView() {
        showInitLoadView(true);
    }
    @Override
    public void hideInitLoadView() {
        showInitLoadView(false);
    }

    @Override
    public void showTransLoadingView() {
        showTransLoadingView(true);
    }

    @Override
    public void hideTransLoadingView() {
        showTransLoadingView(false);
    }
    @Override
    public void showNoDataView() {
        showNoDataView(true);
    }
    @Override
    public void showNoDataView(int resid) {
        showNoDataView(true, resid);
    }
    @Override
    public void hideNoDataView() {
        showNoDataView(false);
    }

    @Override
    public void hideNetWorkErrView() {
        showNetWorkErrView(false);
    }
    @Override
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
    public <T> void onEvent(EventMessage<T> event) {
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
