package com.fenda.homepage.activity;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.bean.WeatherWithHomeBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.ISettingsProvider;
import com.fenda.common.provider.IVoiceInitProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.provider.IWeatherProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.homepage.Adapter.MainAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.Util.HomeUtil;
import com.fenda.homepage.bean.RepairPersonHeadBean;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.model.MainModel;
import com.fenda.homepage.observer.MyContentObserver;
import com.fenda.homepage.presenter.MainPresenter;
import com.fenda.homepage.receiver.ScreenOffAdminReceiver;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.List;

@Route(path = RouterPath.HomePage.HOMEPAGE_MAIN)
public class HomePageActivity extends BaseMvpActivity<MainPresenter, MainModel> implements MainContract.View, View.OnClickListener, View.OnTouchListener  {
    private final static String TAG = "HomePageActivity";

    TextClock mHeaderTimeTv;
    RecyclerView mTipInfoRv;
    ImageView mHeaderWeatherIv;
    TextView mHeaderWeatherTv;
    ImageView mAiTipIv;
    ImageView mPull;
    TextView mAiTipTitleTv;
    TextView mAiTipMicTv;

    private int showPageIndex;
    private Handler mCyclicRollHandler = new Handler();
    private String mBtName;
    private String mGetBindMultiIntent;
    private String mGetBindEventIntent;

    @Autowired
    IVoiceInitProvider initProvider;
    @Autowired
    ICallProvider mICallProvider;
    IVoiceRequestProvider initVoiceProvider;
    IWeatherProvider mIWeatherProvider;

    private PowerManager mPowerManager;
    private DevicePolicyManager mPolicyManager;
    private PowerManager.WakeLock mWakeLock;
    private ComponentName mAdminReceiver;
    private ContentProviderManager manager;
    private List<FDMusic> newsRecommend;
    private int current = 0;
    private int number = 0;

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
        mHeaderWeatherIv = findViewById(R.id.iv_header_weather);
        mHeaderWeatherTv = findViewById(R.id.tv_header_temp);

        mAiTipIv = findViewById(R.id.iv_main_tip_icon);
        mAiTipTitleTv = findViewById(R.id.tv_main_item_content);
        mAiTipMicTv = findViewById(R.id.tv_ai_tiptext);

        mPull = findViewById(R.id.iv_homepage_pull);
        ImageUtil.loadGIFImage(R.mipmap.cm_pull,mPull,R.mipmap.cm_pull);
        mTipInfoRv.setOnTouchListener(this);

        findViewById(R.id.iv_main_phone).setOnClickListener(this);
        findViewById(R.id.iv_main_cmcc).setOnClickListener(this);
        findViewById(R.id.iv_main_qqmusic).setOnClickListener(this);
        findViewById(R.id.iv_main_iqiyi).setOnClickListener(this);

        mHeaderWeatherTv.setOnClickListener(this);
        mHeaderWeatherIv.setOnClickListener(this);
        IntentFilter btIntentFilter = new IntentFilter();
        btIntentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        btIntentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBtReceiver, btIntentFilter);

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
                        if(newsRecommend!=null){
                            number = newsRecommend.size();
                            if (current<number){
                                mAiTipTitleTv.setText("新闻资讯｜"+newsRecommend.get(current).getMusicTitle());
                            }else {
                                current=0;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initData() {
        mAdminReceiver = new ComponentName(mContext, ScreenOffAdminReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mPolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        Intent mIntent = getIntent();

        mGetBindMultiIntent = mIntent.getStringExtra("BIND_INTENT");
        mGetBindEventIntent = mIntent.getStringExtra("BIND_EVENT_INTENT");
        LogUtil.d(TAG, "mGetBindMultiIntent = " + mGetBindMultiIntent);
        LogUtil.d(TAG, "mGetBindEventIntent = " + mGetBindEventIntent);

        if (!mPolicyManager.isAdminActive(mAdminReceiver)) {
            openDeviceManager();
        }
        if (initProvider != null) {
            initProvider.init(this);
            initProvider.initVoice();
        }


        if (initVoiceProvider == null) {
            initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
        }

        if (mIWeatherProvider == null){
            mIWeatherProvider = ARouter.getInstance().navigation(IWeatherProvider.class);
        }


        if (mICallProvider != null) {
            mICallProvider.initSdk();
        }

        ISettingsProvider settingService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
        if (settingService != null) {
            settingService.deviceStatus(this);
        }
        isNetWodrkConnect();

    }

    /**
     * 打开设备管理员权限
     */
    private void openDeviceManager() {
        ComponentName componentName = new ComponentName(getPackageName(), "com.fenda.homepage.receiver.ScreenOffAdminReceiver");
        try {
            Method setActiveAdmin = mPolicyManager.getClass().getDeclaredMethod("setActiveAdmin", ComponentName.class, boolean.class);
            setActiveAdmin.setAccessible(true);
            setActiveAdmin.invoke(mPolicyManager, componentName, true);
        } catch (Exception e) {

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
            ContentProviderManager.getInstance(mContext, Constant.Common.URI).clear();
            AppUtils.saveBindedDevice(getApplicationContext(), false);
            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsBindDeviceActivity).navigation();
        }
        //普通成员退出家庭通知
        else if (message.getCode() == TCPConfig.MessageType.USER_EXIT_FAMILY) {
            LogUtil.d("bind onReceiveEvent = " + message);
            ContentProviderManager.getInstance(mContext, Constant.Common.URI).clear();
            mPresenter.getFamilyContacts();
        } else if (message.getCode() == TCPConfig.MessageType.USER_REPAIR_HEAD) {
            if (message != null && message.getData() != null) {
                BaseTcpMessage baseTcpMessage = (BaseTcpMessage) message.getData();
                String msg = baseTcpMessage.getMsg();
                RepairPersonHeadBean bean = GsonUtil.GsonToBean(msg, RepairPersonHeadBean.class);
                if (bean != null) {
                    ContentProviderManager.getInstance(mContext, Constant.Common.URI).updateUserHeadByUserID(bean.getIcon(), bean.getUserId());
                }

            }
        }else if (message.getCode() == Constant.Common.INIT_VOICE_SUCCESS){
            // @todo  勿删 语音初始化成功后会回调这里,在语音成功之前调用会导致应用崩溃
            LogUtil.e("===== INIT_VOICE_SUCCESS =====");
            if (initVoiceProvider == null){
                initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
            }
            if (initVoiceProvider != null){
                initVoiceProvider.openVoice();
            }
            if (manager == null){
                manager = ContentProviderManager.getInstance(this, Uri.parse(ContentProviderManager.BASE_URI + "/user"));
                getContentResolver().registerContentObserver(Uri.parse(ContentProviderManager.BASE_URI),true,new MyContentObserver(new Handler(),manager));
                //避免重复调用
                if (initVoiceProvider != null){
                    initVoiceProvider.requestWeather();
                    initVoiceProvider.requestNews(20);
                }

            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void isNetWodrkConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeInfo = connectivityManager.getActiveNetwork();
        LogUtil.d(TAG, "activeInfo = " + activeInfo);

        if (activeInfo == null) {//|| !activeInfo. || !activeInfo.isAvailable) {
            //  tv.text = "网络不可用—NetworkCallback"
            //noNetWorkSnackBar();
            ToastUtils.show("网络未连接，请先连接网络！");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    LogUtil.d(TAG, "wifi onAvailable: " + network);
                    if(mGetBindEventIntent == null){
                        if(mGetBindMultiIntent == null){
                            LogUtil.d(TAG,  "null 正常进入主界面");
                            ISettingsProvider settingService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
                            if (settingService != null) {
                                LogUtil.d(TAG, "init device status");
                                settingService.deviceStatus(getApplicationContext());
                            }
                            // 清除本地联系人数据时重新请求网络数据并保存到本地数据库
                            if (ContentProviderManager.getInstance(mContext, Constant.Common.URI).isEmpty()) {
                                mPresenter.getFamilyContacts();
                            }
                        } else{
                            LogUtil.d(TAG,  "特殊方式进入主界面");
                        }
                    } else{
                        LogUtil.d(TAG,  "绑定成功进入主界面");
                        ISettingsProvider settingService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
                        if (settingService != null) {
                            LogUtil.d(TAG, "init device status");
                            settingService.deviceStatus(getApplicationContext());
                        }
                        // 清除本地联系人数据时重新请求网络数据并保存到本地数据库
                        if (ContentProviderManager.getInstance(mContext, Constant.Common.URI).isEmpty()) {
                            mPresenter.getFamilyContacts();
                        }
                    }
                }

                @Override
                public void onLosing(Network network, int maxMsToLive) {
                    super.onLosing(network, maxMsToLive);
                    LogUtil.d(TAG, "onLosing: " + network);
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    LogUtil.d(TAG, "onLost: " + network);
                    // ToastUtils.show(getString(R.string.network_not_connect));
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    LogUtil.d(TAG, "onUnavailable: ");
                }

                @Override
                public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    LogUtil.d(TAG, "onCapabilitiesChanged: " + network);
                }

                @Override
                public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties);
                    LogUtil.d(TAG, "onLinkPropertiesChanged: " + network);
                }
            });
        }
    }

    private BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                BluetoothDevice mConnectionBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
                LogUtil.d(TAG, "BT CONNECT staute " + mConnectionBluetoothDevice.getName() + state);
                if (BluetoothAdapter.STATE_DISCONNECTED == state) {
                    LogUtil.d(TAG, "蓝牙断开了");
                    SPUtils.remove(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME);
                    LogUtil.d(TAG, "STATE_DISCONNECTED getName = " + mConnectionBluetoothDevice.getName() + ", STATE_DISCONNECTED getAddress = " + mConnectionBluetoothDevice.getAddress());
                } else if (BluetoothAdapter.STATE_CONNECTED == state) {
                    LogUtil.d(TAG, "蓝牙连上了");
                    mBtName = mConnectionBluetoothDevice.getName();
                    SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, mBtName);
                    LogUtil.d(TAG, "STATE_CONNECTED getName = " + mConnectionBluetoothDevice.getName() + ", STATE_CONNECTED getAddress = " + mConnectionBluetoothDevice.getAddress());
                }
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        LogUtil.d(TAG, "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        LogUtil.d(TAG, "完成配对");
                        SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, device.getName());
                        SPUtils.put(getApplicationContext(), String.valueOf(Constant.Settings.BT_CONNECTED_ADDRESS), device.getAddress());
                        break;
                    case BluetoothDevice.BOND_NONE:
                        LogUtil.d(TAG, "取消配对");
                        ToastUtils.show("成功取消配对");
                        SPUtils.remove(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME);
                        SPUtils.remove(getApplicationContext(), String.valueOf(Constant.Settings.BT_CONNECTED_ADDRESS));
                        break;
                    default:
                        break;
                }
            }
        }
    };

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
        } else if (resId == R.id.iv_main_cmcc) {
            //通讯录
//            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
        } else if (resId == R.id.iv_header_weather || resId == R.id.tv_header_temp) {


            String saveWeahterValue = (String) SPUtils.get(getApplicationContext(), Constant.Weather.SP_NOW_WEATHER, "");
            if (saveWeahterValue != null && saveWeahterValue.length() > 1){
                mIWeatherProvider.weatherFromVoiceControl(saveWeahterValue);
            }
            else {
                ARouter.getInstance().build(RouterPath.Weather.WEATHER_MAIN).navigation();
            }
            initVoiceProvider.nowWeather();

        } else if (resId == R.id.iv_main_qqmusic) {
            if (initVoiceProvider != null){
                initVoiceProvider.openQQMusic();
            }
        } else if (resId == R.id.iv_main_iqiyi) {
            if (initVoiceProvider != null){
                initVoiceProvider.openAqiyi();
            }
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mBtReceiver);
        super.onDestroy();

    }

    @Override
    public void getFamilyContactsSuccess(BaseResponse<List<UserInfoBean>> response) {
        ContentProviderManager.getInstance(mContext, Constant.Common.URI).insertUsers(response.getData());
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

    float startY = 0;
    float endY = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //添加SubmenuActivity是否已经打开判断，防止多次上滑，打开多个activity
        if(!SubmenuActivity.isSubmenuActivityOpen) {
            int eventAction = event.getAction();
            if (eventAction == MotionEvent.ACTION_DOWN) {
                startY = event.getY();

            } else if (eventAction == MotionEvent.ACTION_UP) {
                endY = event.getY();
                if ((endY - startY) < -80) {
                    Intent intent = new Intent(this, SubmenuActivity.class);
                    startActivity(intent);
                    //                    overridePendingTransition(R.anim.homepage_push_up_in, R.anim.homepage_push_up_out);
                }
            }
        }
        //一定要返回false，让RecyclerView监听左右滑动
        return false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventHomePageFromVoiceControl(WeatherWithHomeBean weatherWithHomeBean) {
        Log.e(TAG, "homePageFromVoiceControl " + weatherWithHomeBean.getWeatherTempNum() + " " + weatherWithHomeBean.getWeatherIconId());
        mHeaderWeatherTv.setText(weatherWithHomeBean.getWeatherTempNum());
        mHeaderWeatherIv.setImageDrawable(getResources().getDrawable(weatherWithHomeBean.getWeatherIconId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecommendEvent(MusicPlayBean bean) {
        newsRecommend = bean.getFdMusics();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenEvent(String type) {
        if (type.equals(Constant.Common.SCREEN_OFF)) {
            LogUtil.i("homepage:screenOff");
            screenOff();
        } else if (type.equals(Constant.Common.SCREEN_ON)) {
            LogUtil.i("homepage:screenOn");
            screenOn();
        }
    }

    /**
     * 亮屏
     */
    @SuppressLint("InvalidWakeLockTag")
    public void screenOn() {
        boolean screenOn = mPowerManager.isInteractive();
        if (!screenOn) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
            mWakeLock.acquire();
            mWakeLock.release();
        }
    }

    /**
     * 熄屏
     */
    public void screenOff() {
        boolean admin = mPolicyManager.isAdminActive(mAdminReceiver);
        if (admin) {
            mPolicyManager.lockNow();
        } else {
            ToastUtils.show("没有设备管理员权限");
        }
    }
}