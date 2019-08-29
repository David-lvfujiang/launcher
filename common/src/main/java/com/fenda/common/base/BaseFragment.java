package com.fenda.common.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.R;
import com.fenda.common.mvp.BaseView;
import com.fenda.common.util.NetUtil;
import com.fenda.common.view.LoadingInitView;
import com.fenda.common.view.LoadingTransView;
import com.fenda.common.view.NetErrorView;
import com.fenda.common.view.NoDataView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:32
 * @Description This is BaseFragment
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private View mFragmentView;

    protected NetErrorView mNetErrorView;
    protected NoDataView mNoDataView;
    protected LoadingInitView mLoadingInitView;
    protected LoadingTransView mLoadingTransView;
    private ViewStub mViewContent;
    private ViewStub mViewInitLoading;
    private ViewStub mViewTransLoading;
    private ViewStub mViewNoData;
    private ViewStub mViewError;

    private boolean isViewCreated = false;
    private boolean isViewVisable = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.common_fragment_root,container,false);
        initCommonView();
        initView();
        initListener();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        //如果启用了懒加载就进行懒加载，否则就进行预加载
        if (enableLazyData()) {
            lazyLoad();
        } else {
            initData();
        }
    }

    protected void initCommonView() {
        mViewContent = mFragmentView.findViewById(R.id.view_stub_content);
        mViewContent = mFragmentView.findViewById(R.id.view_stub_content);
        mViewInitLoading = mFragmentView.findViewById(R.id.view_stub_init_loading);
        mViewTransLoading = mFragmentView.findViewById(R.id.view_stub_trans_loading);
        mViewError = mFragmentView.findViewById(R.id.view_stub_error);
        mViewNoData = mFragmentView.findViewById(R.id.view_stub_nodata);

        mViewContent.setLayoutResource(onBindLayout());
        mViewContent.inflate();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isViewVisable = isVisibleToUser;
        //如果启用了懒加载就进行懒加载，
        if (enableLazyData() && isViewVisable) {
            lazyLoad();
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isViewVisable) {
            initData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isViewVisable = false;
        }
    }
    //默认不启用懒加载
    public boolean enableLazyData() {
        return false;
    }

    public abstract int onBindLayout();

    public abstract void initView();

    public abstract void initData();

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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public <T> void onEvent(EventMessage<T> event) {
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
