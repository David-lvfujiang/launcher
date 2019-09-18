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
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextClock;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.bean.WeatherWithHomeBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.IRecommendProvider;
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
import com.fenda.homepage.Adapter.GridAdapter;
import com.fenda.homepage.Adapter.MainAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.Util.HomeUtil;
import com.fenda.homepage.bean.ApplyBean;
import com.fenda.homepage.bean.RepairPersonHeadBean;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.data.AllApplyData;
import com.fenda.homepage.data.UndevelopedApplyData;
import com.fenda.homepage.model.MainModel;
import com.fenda.homepage.presenter.MainPresenter;
import com.fenda.homepage.receiver.ScreenOffAdminReceiver;
import com.fenda.homepage.scrollview.ObservableScrollView;
import com.fenda.homepage.scrollview.ScrollViewListener;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Route(path = RouterPath.HomePage.HOMEPAGE_MAIN)
public class HomePageActivity extends BaseMvpActivity<MainPresenter, MainModel> implements MainContract.View, View.OnClickListener, View.OnTouchListener , ScrollViewListener , IRecommendProvider {
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
    private SlidingDrawer slidingDrawer;

    private List<ApplyBean> mApplyList;
    private List<ApplyBean> mUndevelopedApplyList;
    private RecyclerView mSubmenuListRv;
    private RecyclerView mSubmenuListRv2;
    private GridAdapter mGridAdapter;
    private GridAdapter mGridAdapter2;
    private ImageView submenuDropLeft;
    private ImageView submenuDropRight;


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
        mHeaderWeatherIv = findViewById(R.id.iv_header_weather);
        mHeaderWeatherTv = findViewById(R.id.tv_header_temp);

        mAiTipIv = findViewById(R.id.iv_main_tip_icon);
        mAiTipTitleTv = findViewById(R.id.tv_main_item_content);
        mAiTipMicTv = findViewById(R.id.tv_ai_tiptext);
        mPull = findViewById(R.id.iv_homepage_pull);
        ImageUtil.loadGIFImage(R.mipmap.cm_pull,mPull,R.mipmap.a123456);
        mTipInfoRv.setOnTouchListener(this);

        findViewById(R.id.iv_main_phone).setOnClickListener(this);
        findViewById(R.id.iv_main_cmcc).setOnClickListener(this);
        findViewById(R.id.iv_main_qqmusic).setOnClickListener(this);
        findViewById(R.id.iv_main_iqiyi).setOnClickListener(this);

        mHeaderWeatherTv.setOnClickListener(this);
        mHeaderWeatherIv.setOnClickListener(this);
        //二级菜单
        initSubmenuView();
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
            if (initVoiceProvider != null){
                initVoiceProvider.requestWeather();
                initVoiceProvider.openVoice();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void isNetWodrkConnect() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeInfo = connectivityManager.getActiveNetwork();

        if (activeInfo == null) {    //|| !activeInfo. || !activeInfo.isAvailable) {
            //  tv.text = "网络不可用—NetworkCallback"
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
        } else if (resId == R.id.iv_submenu_drop_left||resId ==R.id.iv_submenu_drop_right) {
            slidingDrawer.animateClose();
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
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            startY = event.getY();

        } else if (eventAction == MotionEvent.ACTION_UP) {
            endY = event.getY();
            if ((endY - startY) < -80) {
                slidingDrawer.animateOpen();
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
    public void onEvent(String type) {
        if (type.equals(Constant.Common.SCREEN_OFF)) {
            screenOff();
        } else if (type.equals(Constant.Common.SCREEN_ON)) {
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


    /**
     * 二级菜单
     */
    public void initSubmenuView() {
        mSubmenuListRv = findViewById(R.id.rv_submenu_list);
        mSubmenuListRv2 = findViewById(R.id.rv_submenu_list2);
        submenuDropLeft = findViewById(R.id.iv_submenu_drop_left);
        submenuDropRight = findViewById(R.id.iv_submenu_drop_right);
        slidingDrawer = findViewById(R.id.slidingdrawer);
        submenuDropLeft.setOnClickListener(this);
        submenuDropRight.setOnClickListener(this);
        ((ObservableScrollView)findViewById(R.id.sv_submenu)).setScrollViewListener(this);
        (findViewById(R.id.ll_submenu_back)).setOnTouchListener(new View.OnTouchListener() {
            float startY = 0;
            float endY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();

                if(eventAction==MotionEvent.ACTION_DOWN){
                    startY = event.getY();

                } else if (eventAction==MotionEvent.ACTION_UP){
                    endY = event.getY();
                    if ((endY-startY)>50) {
                        slidingDrawer.animateClose();
                    }
                }
                return true;
            }
        });
        mApplyList = new ArrayList<>();
        mApplyList = AllApplyData.dataList(mApplyList);
        mUndevelopedApplyList = new ArrayList<>();
        mUndevelopedApplyList = UndevelopedApplyData.dataList(mUndevelopedApplyList);
        //这里的第二个参数4代表的是网格的列数
        mSubmenuListRv.setLayoutManager(new GridLayoutManager(mContext, 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSubmenuListRv2.setLayoutManager(new GridLayoutManager(mContext, 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {

            @Override
            public void onDrawerClosed() {
                mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);
            }
        });
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

            @Override
            public void onDrawerOpened() {
                mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
            }
        });

        mGridAdapter = new GridAdapter(mApplyList);
        mGridAdapter2 = new GridAdapter(mUndevelopedApplyList);
        mSubmenuListRv.setAdapter(mGridAdapter);
        mSubmenuListRv2.setAdapter(mGridAdapter2);
        mGridAdapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( View view, String applyId) {
                Intent intent = new Intent(HomePageActivity.this,PromptActivity.class);
                if(applyId.equals(com.fenda.homepage.data.Constant.SETTINGS)){
                    ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CALCULATOR)){
                    ToastUtils.show("计算器");
                }
                else if (applyId.equals(com.fenda.homepage.data.Constant.WEATHER)) {
                    //                    ToastUtils.show("天气");
                    String saveWeahterValue = (String) SPUtils.get(getApplicationContext(), com.fenda.common.constant.Constant.Weather.SP_NOW_WEATHER, "");
                    if (saveWeahterValue != null && saveWeahterValue.length() > 1){
                        mIWeatherProvider.weatherFromVoiceControl(saveWeahterValue);
                    }
                    else {
                        ARouter.getInstance().build(RouterPath.Weather.WEATHER_MAIN).navigation();
                    }
                    initVoiceProvider.nowWeather();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CALENDAR)) {
                    //                    ToastUtils.show("日历");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.PHOTO)) {
                    //                    ToastUtils.show("相册");
                    ARouter.getInstance().build(RouterPath.Gallery.GALLERY_CATOGORY).navigation();
                } else if (applyId.equals(com.fenda.homepage.data.Constant.TIME)) {
                    ToastUtils.show("时钟");
                } else if (applyId.equals(com.fenda.homepage.data.Constant.FM)) {
                    //                    ToastUtils.show("收音机");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CAMERA)) {
                    //                    ToastUtils.show("相机");
                    PackageManager packageManager = getPackageManager();
                    Intent packageIntent = packageManager.getLaunchIntentForPackage("com.android.camera2");
                    startActivity(packageIntent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.PLAY)) {
                    //                    ToastUtils.show("播放器");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.QQ_MUSIC)) {
                    //                    ToastUtils.show("QQ音乐");
                    if (initVoiceProvider != null){
                        initVoiceProvider.openQQMusic();
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.IQIYI)) {
                    //                    ToastUtils.show("爱奇艺");
                    if (initVoiceProvider != null){
                        initVoiceProvider.openAqiyi();
                    }
                } else if (applyId.equals(com.fenda.homepage.data.Constant.NEWS)) {
                    //                    ToastUtils.show("新闻");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CROSS_TALK)) {
                    //                    ToastUtils.show("相声");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CHILDREN)) {
                    //                    ToastUtils.show("儿歌");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.POETRY)) {
                    //                    ToastUtils.show("诗词");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.JOKE)) {
                    //                    ToastUtils.show("笑话");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.IDIOM)) {
                    //                    ToastUtils.show("成语");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.ENCYCLOPEDIA)) {
                    //                    ToastUtils.show("百科");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.GUOXUE)) {
                    //                    ToastUtils.show("国学");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.HOLIDAYS)) {
                    //                    ToastUtils.show("节假日查询");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.REMIND)) {
                    //                    ToastUtils.show("提醒");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.STORY)) {
                    //                    ToastUtils.show("故事");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.TRANSLATION)) {
                    //                    ToastUtils.show("翻译");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.STOCK)) {
                    //                    ToastUtils.show("股票");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.UNITS)) {
                    //                    ToastUtils.show("单位换算");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.RELATIVE)) {
                    //                    ToastUtils.show("亲戚关系计算");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(com.fenda.homepage.data.Constant.CONSTELLATION)) {
                    //                    ToastUtils.show("星座运势");
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
        mGridAdapter2.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String applyId) {
                ToastUtils.show("努力开发中，敬请期待。。。");
            }
        });

    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        //        Log.v("ashgdfaskdfh","y="+y);
        //        Log.v("ashgdfaskdfh","oldx="+oldy);
        Animation rotateAnimation = new RotateAnimation(y/125,oldy/125,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDetachWallpaper(true);
        submenuDropLeft.startAnimation(rotateAnimation);
        Animation rotateAnimation2 = new RotateAnimation(-y/125,-oldy/125,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation2.setFillAfter(true);
        rotateAnimation2.setDuration(0);
        rotateAnimation2.setRepeatCount(0);
        rotateAnimation2.setInterpolator(new LinearInterpolator());
        rotateAnimation2.setDetachWallpaper(true);
        submenuDropRight.startAnimation(rotateAnimation2);
    }

    @Override
    public void requestRecommend(JSONObject dataRecommend) {
        //首页推荐内容数据处理
    }

    @Override
    public void init(Context context) {

    }
}