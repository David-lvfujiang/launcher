package com.fenda.homepage.activity;

import android.app.ActivityOptions;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseFragment;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.bean.LeaveMessageBean;
import com.fenda.common.bean.WeatherWithHomeBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.provider.IWeatherProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.MyNestedScrollView;
import com.fenda.homepage.Adapter.GridAdapter;
import com.fenda.homepage.Adapter.MainAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.Util.HomeUtil;
import com.fenda.homepage.bean.ApplyBean;
import com.fenda.homepage.data.AllApplyData;
import com.fenda.homepage.receiver.ScreenOffAdminReceiver;
import com.fenda.homepage.view.LauncherRecycleView;
import com.fenda.homepage.view.MyRelativeLayout;
import com.fenda.homepage.view.PullView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/24 17:32
 * @Description
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener{


    private static final String TAG = "HomeFragment";
    private TextClock mHeaderTimeTv;
    private LauncherRecycleView mTipInfoRv;
    private ImageView mHeaderWeatherIv;
    private TextView mHeaderWeatherTv;
    private ImageView mAiTipIv;
    private TextView mAiTipTitleTv;
    private TextView mAiTipMicTv;
    private MyRelativeLayout relaPre;


    private List<ApplyBean> mApplyList;
    private RecyclerView mSubmenuListRv;
    private GridAdapter mGridAdapter;
    private ImageView submenuDropLeft;
    private ImageView submenuDropRight;
    private ContentProviderManager manager;
    private LinearLayout nestScroll;
    //    private LinearLayout ll_submenu_back;
    private ImageView imgGIF;
    private TextView mMsgTv;

    private MyNestedScrollView scrollView;
    private PullView pullView;

    private IWeatherProvider mIWeatherProvider;
    private IVoiceRequestProvider initVoiceProvider;

    private static final int CHANGE_Msg = 1;



    private List<FDMusic> newsRecommend;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            LogUtil.e("进入了Oncreate的接收到了handler信息");
            initSubmenuView();
            ImageUtil.loadGIFImage(R.mipmap.cm_pull,imgGIF,R.mipmap.cm_pull);
        }
    };
    private int showPageIndex;
    private int number;
    private int current;
    private boolean openNewsFlag;
    private int LAUNCHER_STATUS = Constant.Common.HOME_PAGE;

    private ScheduledThreadPoolExecutor executorService;


    Runnable cycleRollRunabler = new Runnable() {
        @Override
        public void run() {
            Log.e("fd", "cycleRollRunabler timeout " + showPageIndex);

            if (showPageIndex + 1 >= HomeUtil.PAGE_NUM_MAX) {
                mTipInfoRv.scrollToPosition(0);
                showPageIndex = 0;
                current++;
                mAiTipIv.setVisibility(View.VISIBLE);
                mAiTipMicTv.setText(R.string.cm_main_page_title_0);
                mAiTipTitleTv.setText(R.string.cm_main_page_describe_0);

            } else {
                mTipInfoRv.smoothScrollToPosition(showPageIndex + 1);
            }
        }
    };


    private Handler mHandlerMsg = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGE_Msg:
                    //完成主界面更新,拿到数据
                    int data = (int) msg.obj;
                    mMsgTv.setVisibility(View.VISIBLE);
                    if (data<100){
                        mMsgTv.setText(""+data);
                    }else {
                        mMsgTv.setText("99+");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int onBindLayout() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void initView() {

        BaseApplication.getBaseInstance().setRequestWeather(false);
        mHeaderTimeTv = mRootView.findViewById(R.id.tv_header_time);
        mTipInfoRv = mRootView.findViewById(R.id.rv_Tipinfo);
        mHeaderWeatherIv = mRootView.findViewById(R.id.iv_header_weather);
        mHeaderWeatherTv = mRootView.findViewById(R.id.tv_header_temp);
        relaPre         = mRootView.findViewById(R.id.rela_pre);

        mAiTipIv = mRootView.findViewById(R.id.iv_main_tip_icon);
        mAiTipTitleTv = mRootView.findViewById(R.id.tv_main_item_content);
        mAiTipMicTv = mRootView.findViewById(R.id.tv_ai_tiptext);
        imgGIF = mRootView.findViewById(R.id.img_gif);
        mMsgTv = mRootView.findViewById(R.id.tv_home_nsg_dot_red);

        mRootView.findViewById(R.id.tv_main_phone).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_main_cmcc).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_main_qqmusic).setOnClickListener(this);
        mRootView.findViewById(R.id.tv_main_iqiyi).setOnClickListener(this);
        mRootView.findViewById(R.id.iv_header_love).setOnClickListener(this);
        //
        mHeaderWeatherTv.setOnClickListener(this);
        mHeaderWeatherIv.setOnClickListener(this);
        initRecycleView();
        initSleepView();


    }


    private void initSleepView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = mHandler.obtainMessage();
                mHandler.sendMessage(msg);
                LogUtil.e("进入了Oncreate的发送了handler信息");
            }
        }).start();
    }




    /**
     * 二级菜单
     */
    public void initSubmenuView() {
        ViewStub stub = mRootView.findViewById(R.id.stub);
        View submenuView = stub.inflate();
        mSubmenuListRv = submenuView.findViewById(R.id.rv_submenu_list);
        submenuDropLeft = submenuView.findViewById(R.id.iv_submenu_drop_left);
        submenuDropRight = submenuView.findViewById(R.id.iv_submenu_drop_right);
        nestScroll = submenuView.findViewById(R.id.nest_scroll);
        scrollView = submenuView.findViewById(R.id.scrollView);
        pullView = submenuView.findViewById(R.id.pullView);
        relaPre.setSkillAndLauncher(this,mTipInfoRv,nestScroll,scrollView,pullView);
        initAdapter();
    }



    private void initRecycleView() {


        showPageIndex = 0;

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTipInfoRv.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mTipInfoRv);

        MainAdapter tMainAdapter = new MainAdapter(getActivity());
        mTipInfoRv.setAdapter(tMainAdapter);

        mTipInfoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
//                    mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int review_position = layoutManager.findFirstVisibleItemPosition();
                    Log.e("fd", "onScrollStateChanged review_position " + review_position + " showPageIndex " + showPageIndex);

//                    mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);

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
                        if (newsRecommend != null) {
                            number = newsRecommend.size();
                            if (current < number) {
                                mAiTipTitleTv.setText("新闻资讯｜" + newsRecommend.get(current).getMusicTitle());
                            } else {
                                current = 0;
//                                if (initVoiceProvider == null) {
//                                    initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
//                                }
//                                if (initVoiceProvider != null){
//                                    initVoiceProvider.requestNews(20);
//                                }
                            }
                        } else {
                            mAiTipTitleTv.setText(R.string.cm_main_page_describe_1);
                        }
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


    private void initAdapter () {
        //这里的第二个参数4代表的是网格的列数
        mSubmenuListRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mSubmenuListRv.setNestedScrollingEnabled(false);
        mApplyList = new ArrayList<>();
        mApplyList = AllApplyData.dataList(mApplyList);

        mGridAdapter = new GridAdapter(mApplyList);
        mSubmenuListRv.setAdapter(mGridAdapter);
        mGridAdapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String applyId) {
                LogUtil.e("applyId = " + applyId);
                Intent intent = new Intent(getActivity(), PromptActivity.class);
                if (applyId.equals(com.fenda.homepage.data.Constant.SETTINGS)) {
//                    ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//                    String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();

                    LogUtil.e("applyId 栈顶activityName = ");

                    ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity)
                            .with(ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle())
                            .navigation();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CALCULATOR)) {
                    ARouter.getInstance().build(RouterPath.Calculator.CALCULATOR_ACTIVITY).with(ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle()).navigation();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.WEATHER)) {
                    String saveWeahterValue = (String) SPUtils.get(getActivity(), Constant.Weather.SP_NOW_WEATHER, "");
                    if (saveWeahterValue != null && saveWeahterValue.length() > 1 && mIWeatherProvider != null) {
                        mIWeatherProvider.weatherFromVoiceControl(saveWeahterValue);
                    } else {
                        ARouter.getInstance().build(RouterPath.Weather.WEATHER_MAIN).with(ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle()).navigation();
                    }
                    if (initVoiceProvider != null){
                        initVoiceProvider.nowWeather();
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CALENDAR)) {
                    ARouter.getInstance().build(RouterPath.Calendar.Perpetual_CALENDAR_ACTIVITY).with(ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle()).navigation();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.PHOTO)) {
                    ARouter.getInstance().build(RouterPath.Gallery.GALLERY_CATOGORY).with(ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle()).navigation();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.TIME)) {
                    ToastUtils.show("闹钟");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.FM)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CAMERA)) {
                    PackageManager packageManager = getActivity().getPackageManager();
                    Intent packageIntent = packageManager.getLaunchIntentForPackage("com.android.camera2");
                    startActivity(packageIntent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.PLAY)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.QQ_MUSIC)) {
                    if (initVoiceProvider != null) {
                        initVoiceProvider.openQQMusic();
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.IQIYI)) {
                    if (initVoiceProvider != null) {
                        initVoiceProvider.openAqiyi();
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.NEWS)) {
                    if (initVoiceProvider != null) {
                        openNewsFlag = true;
                        initVoiceProvider.requestNews(20);
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CROSS_TALK)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CHILDREN)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.POETRY)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.JOKE)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.IDIOM)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.ENCYCLOPEDIA)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.GUOXUE)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.HOLIDAYS)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.REMIND)) {
                    if (initVoiceProvider != null) {
                        initVoiceProvider.requestAlarm();
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.STORY)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.TRANSLATION)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.STOCK)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.UNITS)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.RELATIVE)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CONSTELLATION)) {
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CMCC)) {
                    ToastUtils.show("10086");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.GDYD)) {
                    ToastUtils.show("广东移动营业厅");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_MUSIC)) {
                    ToastUtils.show("咪咕音乐");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_AIKAN)) {
                    ToastUtils.show("咪咕爱看");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_ZHIBO)) {
                    ToastUtils.show("咪咕直播");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_LINGXI)) {
                    ToastUtils.show("咪咕灵犀");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_QUANQUAN)) {
                    ToastUtils.show("咪咕圈圈");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_SHANPAO)) {
                    ToastUtils.show("咪咕擅跑");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_VIDEO)) {
                    ToastUtils.show("咪咕视频");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_MOVIE)) {
                    ToastUtils.show("咪咕影院");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_READ)) {
                    ToastUtils.show("咪咕阅读");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.MIGU_CITIC)) {
                    ToastUtils.show("咪咕中信书店");
                }
            }
        });
    }

    public int getLauncherState(){
        return LAUNCHER_STATUS;
    }

    public void setLauncherState(int state){
        this.LAUNCHER_STATUS = state;
    }

    public void onPullOnclick(View v) {
        ToastUtils.show("努力开发中，敬请期待。。。");
    }



    public void returnDefault () {
        if (nestScroll != null) {
            nestScroll.setTranslationY(BaseApplication.getBaseInstance().getScreenHeight());
            setLauncherState(Constant.Common.HOME_PAGE);
        }
    }


    public void startCycleRollRunnable() {
        if (executorService == null) {
            executorService = new ScheduledThreadPoolExecutor(1);
            executorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(cycleRollRunabler);

                }
            }, HomeUtil.PAGE_SHOW_TIME, HomeUtil.PAGE_SHOW_TIME, TimeUnit.MILLISECONDS);
        }
    }

    public void stopCycleRollRunnable () {
        if (executorService != null) {
            executorService.setRemoveOnCancelPolicy(true);
            executorService.shutdown();
            executorService = null;
        }
    }




    @Override
    public void onResume () {
        super.onResume();
        LogUtil.e("进入了onResume");
        if (initVoiceProvider == null) {
            initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
        }

        if (initVoiceProvider != null && !BaseApplication.getBaseInstance().isRequestWeather()) {
            initVoiceProvider.openVoice();
        }

        startCycleRollRunnable();
//        mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);
    }


    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        int resId = v.getId();
        if (resId == R.id.tv_main_phone) {
            //通讯录
            ARouter.getInstance().build(RouterPath.Call.MAIN_ACTIVITY).navigation();
        } else if (resId == R.id.tv_main_cmcc) {
            String apksUrl = "http://www.sfth.cn/";
            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsLoadWebviewActivity)
                    .withString("APK_URL", apksUrl)
                    .navigation();

            //通讯录
            //            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
        } else if (resId == R.id.iv_header_weather || resId == R.id.tv_header_temp) {
            String saveWeahterValue = (String) SPUtils.get(getContext(), Constant.Weather.SP_NOW_WEATHER, "");
            if (saveWeahterValue != null && saveWeahterValue.length() > 1 && mIWeatherProvider != null) {
                mIWeatherProvider.weatherFromVoiceControl(saveWeahterValue);
            } else {
                ARouter.getInstance().build(RouterPath.Weather.WEATHER_MAIN).navigation();
            }
            if (initVoiceProvider != null){
                initVoiceProvider.nowWeather();
            }

        } else if (resId == R.id.tv_main_qqmusic) {
            if (initVoiceProvider != null) {
                initVoiceProvider.openQQMusic();
            }
        } else if (resId == R.id.tv_main_iqiyi) {
            if (initVoiceProvider != null) {
                initVoiceProvider.openAqiyi();
            }
        } else if (resId == R.id.iv_header_love) {
//            if (leaveMessageProvider != null){
//                leaveMessageProvider.openConversationListActivity();
//            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHomePageFromVoiceControl (WeatherWithHomeBean weatherWithHomeBean){
        Log.e(TAG, "homePageFromVoiceControl " + weatherWithHomeBean.getWeatherTempNum() + " " + weatherWithHomeBean.getWeatherIconId());
        mHeaderWeatherTv.setText(weatherWithHomeBean.getWeatherTempNum());
        mHeaderWeatherIv.setImageDrawable(getResources().getDrawable(weatherWithHomeBean.getWeatherIconId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendEvent (MusicPlayBean bean){
        newsRecommend = bean.getFdMusics();
        List<FDMusic> newsListData;
        newsListData = bean.getFdMusics();
        if (newsListData != null && openNewsFlag) {
            openNewsFlag = false;
            ARouter.getInstance().build(RouterPath.NEWS.NEWS_ACTIVITY)
                    .withObject("newsListData", newsListData)
                    .navigation();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgEvent (LeaveMessageBean msgBean){
        final int msgNum = msgBean.getLeaveMessageNumber();
        if (msgNum == 0) {
            mMsgTv.setVisibility(View.INVISIBLE);
            LogUtils.e("onMsgEvent: " + msgNum);
        } else {
            LogUtils.e("onMsgEvent: " + msgNum);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //新建一个Message对象，存储需要发送的消息
                    Message message = new Message();
                    message.what = CHANGE_Msg;
                    message.obj = msgNum;
                    //然后将消息发送出去
                    mHandlerMsg.sendMessage(message);
                }
            }).start();
        }
    }
}
