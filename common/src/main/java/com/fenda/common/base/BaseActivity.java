package com.fenda.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.R;
import com.fenda.common.baseapp.AppManager;
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

import java.lang.ref.WeakReference;


/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:32
 * @Description This is BaseActivity
 */
public abstract class BaseActivity extends RxAppCompatActivity {


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

    private TextView tvBack;
    private LinearLayout linTitle;
    private MyHandler mHandler;
    private static final int mMessageDelay = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        mHandler = new MyHandler(this);
        boolean isFull = initStatusBar();
        if (!isFull) {
            setStatusBarFullTransparent();
        } else {
            NoTitleFullScreen();
        }
        initHomeAvtivity();
        setContentView(R.layout.common_activity_root);
        mContext = this;
        initCommonView();
        ARouter.getInstance().inject(this);
        initView();
        initListener();
        initData();
        EventBus.getDefault().register(this);
        AppManager.getAppManager().addActivity(this);
    }

    public void initHomeAvtivity(){

    }

    public boolean initStatusBar() {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            ScreenSaverManager.eliminateEvent();//设置手指离开屏幕的时间
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 一段时间后页面自动消失
     *
     * @param interval 时间间隔
     */
    protected void autoFinsh(int interval) {
        mHandler.sendEmptyMessageDelayed(mMessageDelay, interval);
    }

    static class MyHandler extends Handler {
        public WeakReference<BaseActivity> weakReference;

        public MyHandler(BaseActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case mMessageDelay:
                    BaseActivity activity = (BaseActivity) weakReference.get();
                    if (activity != null) {
                        activity.finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 全透状态栏
     */
    private void setStatusBarFullTransparent() {
        //21表示5.0
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //19表示4.4
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 初始化顶部Title
     *
     * @param title
     */
    protected void initTitle(String title) {
        if (tvBack == null) {
            tvBack = findViewById(R.id.tv_back);
            if (tvBack != null) {
                if (TextUtils.isEmpty(title)) {
                    title = "";
                }
                tvBack.setText(title);
                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finishActivity();
                    }
                });
            }
        } else {
            if (!TextUtils.isEmpty(title)) {
                tvBack.setText(title);
            }
        }
    }

    /**
     * 设置Title背景颜色
     *
     * @param color
     */
    protected void setTitleBackgroundColor(int color) {
        if (linTitle == null) {
            linTitle = findViewById(R.id.lin_title);
        }
        if (linTitle != null) {
            linTitle.setBackgroundColor(color);
        }
    }

    /**
     * 设置返回键的图片
     *
     * @param res
     */
    protected void setBackImage(int res) {
        if (tvBack != null) {
            tvBack.setCompoundDrawablesWithIntrinsicBounds(res, 0, 0, 0);
        }
    }


    /**
     * 关闭页面，如果需要在关闭页面时做特殊处理
     * 请重写这个方法
     */
    protected void finishActivity() {
        this.finish();
    }

    /**
     * 全屏显示 设置这个状态可能无法下拉状态栏
     */
    private void NoTitleFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
//        showNetWorkErrView(false);
    }

    public void showNetWorkErrView() {
//        showNetWorkErrView(true);
    }


    private void showInitLoadView(boolean show) {
        if (mLoadingInitView == null) {
            View view = mViewInitLoading.inflate();
            mLoadingInitView = view.findViewById(R.id.view_init_loading);
        }
        mLoadingInitView.setVisibility(show ? View.VISIBLE : View.GONE);
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

    public void showContentView(boolean show) {
        if (mViewContent != null) {
            mViewContent.setVisibility(show ? View.VISIBLE : View.GONE);
        }

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
    public void onEvent(EventMessage<BaseTcpMessage> event) {
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //横竖切换
        isConfigChange = true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (!isConfigChange) {
            AppManager.getAppManager().finishActivity(this);
        }
    }
}
