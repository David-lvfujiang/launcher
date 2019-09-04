package com.fenda.homepage.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.ISettingsProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.router.RouterPath;
import com.fenda.homepage.Adapter.MainAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.Util.HomeUtil;
import com.fenda.homepage.bean.RepairPersonHeadBean;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.model.MainModel;
import com.fenda.homepage.presenter.MainPresenter;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;

import java.util.List;
@Route(path = RouterPath.HomePage.HOMEPAGE_MAIN)
public class HomePageActivity extends BaseMvpActivity<MainPresenter, MainModel> implements MainContract.View, View.OnClickListener, View.OnTouchListener {


    TextClock mHeaderTimeTv;
    RecyclerView mTipInfoRv;
    ImageView mheaderWeatherIv;
    ImageView mAiTipIv;
    TextView mAiTipTitleTv;
    TextView mAiTipMicTv;

    private int showPageIndex;
    private Handler mCyclicRollHandler = new Handler();

    Runnable cycleRollRunabler = new Runnable() {
        @Override
        public void run() {
            Log.e("fd", "cycleRollRunabler timeout " + showPageIndex);

            if (showPageIndex + 1 >= HomeUtil.PAGE_NUM_MAX) {
                mTipInfoRv.scrollToPosition(0);
                showPageIndex = 0;

                mAiTipIv.setVisibility(View.VISIBLE);
                mAiTipMicTv.setText(R.string.cm_main_page_title_0);
                mAiTipTitleTv.setText(R.string.cm_main_page_describe_0);

                mCyclicRollHandler.postDelayed(this, HomeUtil.PAGE_SHOW_TIME);
            } else {
                mTipInfoRv.smoothScrollToPosition(showPageIndex + 1);
            }
        }
    };


    @Override
    public int onBindLayout() {
        return R.layout.homepage_home_activity;
    }

    @Override
    public void initView() {
        mHeaderTimeTv = findViewById(R.id.tv_header_time);
        mTipInfoRv = findViewById(R.id.rv_Tipinfo);
        mheaderWeatherIv = findViewById(R.id.iv_header_weather);

        mAiTipIv = findViewById(R.id.iv_main_tip_icon);
        mAiTipTitleTv = findViewById(R.id.tv_main_item_content);
        mAiTipMicTv = findViewById(R.id.tv_ai_tiptext);
        findViewById(R.id.linearlayout_layout).setOnTouchListener(this);
        findViewById(R.id.iv_main_jd).setOnClickListener(this);
        findViewById(R.id.iv_main_phone).setOnClickListener(this);
        findViewById(R.id.iv_main_other).setOnClickListener(this);
        findViewById(R.id.iv_main_tools).setOnClickListener(this);
        findViewById(R.id.iv_main_study).setOnClickListener(this);
        mheaderWeatherIv.setOnClickListener(this);
        showPageIndex = 0;

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTipInfoRv.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mTipInfoRv);

        MainAdapter tMainAdapter = new MainAdapter(this);
        mTipInfoRv.setAdapter(tMainAdapter);

        mTipInfoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                    mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int review_position = layoutManager.findFirstVisibleItemPosition();
                    Log.e("fd", "onScrollStateChanged review_position " + review_position + " showPageIndex " + showPageIndex);

                    mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);

                    if (review_position == showPageIndex) {
                        return;
                    }
                    showPageIndex = review_position;

                    if (showPageIndex == 0) {
                        mAiTipIv.setVisibility(View.VISIBLE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_0);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_0);
                    } else if (showPageIndex == 1) {
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_1);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_1);

                    } else if (showPageIndex == 2) {
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_2);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_2);
                    } else if (showPageIndex == 3) {
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_3);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_3);

                    } else if (showPageIndex == 4) {
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_4);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_4);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //              int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            }
        });
    }

    @Override
    public void initData() {
        ISettingsProvider settingService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
        if (settingService != null) {
            settingService.deviceStatus(this);
        }
        // 清除本地联系人数据时重新请求网络数据并保存到本地数据库
        if (ContentProviderManager.getInstance(this, Constant.common.URI).isEmpty()) {
            mPresenter.getFamilyContacts();
        }
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void onEvent(final EventMessage<BaseTcpMessage> message) {
        //家庭解散通知
        if (message.getCode() == TCPConfig.MessageType.FAMILY_DISSOLVE) {
            LogUtil.d("家庭解散通知1 " + message);
            ContentProviderManager.getInstance(mContext, Constant.common.URI).clear();
            AppUtils.saveBindedDevice(getApplicationContext(), false);
            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsBindDeviceActivity).navigation();

        }
        //普通成员退出家庭通知
        else if (message.getCode() == TCPConfig.MessageType.USER_EXIT_FAMILY) {
            LogUtil.d("bind onReceiveEvent = " + message);
            ContentProviderManager.getInstance(mContext, Constant.common.URI).clear();
            mPresenter.getFamilyContacts();
        } else if (message.getCode() == TCPConfig.MessageType.USER_REPAIR_HEAD) {
            if (message != null && message.getData() != null) {
                BaseTcpMessage baseTcpMessage = (BaseTcpMessage) message.getData();
                String msg = baseTcpMessage.getMsg();
                RepairPersonHeadBean bean = GsonUtil.GsonToBean(msg, RepairPersonHeadBean.class);
                if (bean != null) {
                    ContentProviderManager.getInstance(mContext, Constant.common.URI).updateUserHeadByUserID(bean.getIcon(), bean.getUserId());
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
    }


    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_main_phone) {
            //通讯录
            ARouter.getInstance().build(RouterPath.Call.MAIN_ACTIVITY).navigation();
        }
    }

    @Override
    public void getFamilyContactsSuccess(BaseResponse<List<UserInfoBean>> response) {
        ContentProviderManager.getInstance(mContext, Constant.common.URI).insertUsers(response.getData());
    }

    @Override
    public void getFamilyContactsFailure(BaseResponse<List<UserInfoBean>> response) {

    }


    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //如果这个方法消费了这个这个event事件，就返回True，否则false。
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float  startY = 0,  offsetY;
        int eventAction = event.getAction();
        if(eventAction==MotionEvent.ACTION_DOWN){
            startY = event.getY();
        } else if (eventAction==MotionEvent.ACTION_UP){
            offsetY = event.getY() - startY;
            if (offsetY < -10) {
                // up
                Intent intent = new Intent(this,SubmenuActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }
}
