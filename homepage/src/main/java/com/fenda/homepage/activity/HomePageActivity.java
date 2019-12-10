package com.fenda.homepage.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
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
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.baseapp.AppManager;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.bean.LeaveMessageBean;
import com.fenda.common.bean.MessageBean;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.bean.WeatherWithHomeBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.IAppLeaveMessageProvider;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.ISettingsProvider;
import com.fenda.common.provider.IVoiceInitProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.provider.IWeatherProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.DbUtil;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.NetUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.MyNestedScrollView;
import com.fenda.homepage.Adapter.GridAdapter;
import com.fenda.homepage.Adapter.MainAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.Util.HomeUtil;
import com.fenda.homepage.bean.ApplyBean;
import com.fenda.homepage.bean.RepairPersonHeadBean;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.data.AllApplyData;
import com.fenda.homepage.model.MainModel;
import com.fenda.homepage.observer.MyContentObserver;
import com.fenda.homepage.presenter.MainPresenter;
import com.fenda.homepage.receiver.ScreenOffAdminReceiver;
import com.fenda.homepage.view.LauncherRecycleView;
import com.fenda.homepage.view.MyRelativeLayout;
import com.fenda.homepage.view.PullView;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Route(path = RouterPath.HomePage.HOMEPAGE_MAIN)
public class HomePageActivity extends BaseMvpActivity<MainPresenter, MainModel> implements MainContract.View, View.OnClickListener {
    private final static String TAG = "HomePageActivity";

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private String mBtName;
    private String mGetBindMultiIntent;
    private String mGetBindEventIntent;

    @Autowired
    IVoiceInitProvider initProvider;
    @Autowired
    ICallProvider mICallProvider;
    IVoiceRequestProvider initVoiceProvider;
    IWeatherProvider mIWeatherProvider;
    IAppLeaveMessageProvider leaveMessageProvider;

    private PowerManager mPowerManager;
    private DevicePolicyManager mPolicyManager;
    private PowerManager.WakeLock mWakeLock;
    private ComponentName mAdminReceiver;

    private ContentProviderManager manager;
    private boolean isWeather;



    @Autowired
    ISettingsProvider mISettingsProvider;


    private String mNewUserName;


    private LinearLayout linLayout;
    private HomeFragment homeFragment;
    private FragmentTransaction transaction;

    private StartWifiConfigureFragment configureFragment;
    private FragmentManager fragmentManager;


    @Override
    public int onBindLayout() {
        if (!isTaskRoot()){
            finish();
            return 0;
        }
        LogUtil.e("进入了Oncreate");
        return R.layout.homepage_home_activity;
    }



    @Override
    public void initView() {
        fragmentManager = getSupportFragmentManager();
        linLayout = findViewById(R.id.rela_pre);


        if (!NetUtil.checkNet()){

            showWifiConfigureFragment();
        }else {
            if (!AppUtils.isRegisterDevice(this)  || !AppUtils.isBindedDevice(this)){

                showWifiConfigureFragment();
            }else {
                showHomePageView();

                mISettingsProvider.queryDeviceInfo(this,false);
            }
        }



    }


    /**
     * 显示首页界面
     */
    private void showHomePageView(){

        transaction = fragmentManager.beginTransaction();
        hideWifiConfigureFragment();
        if (homeFragment == null){
            homeFragment = new HomeFragment();

        }
        if (!homeFragment.isAdded()){
            transaction.add(R.id.rela_pre,homeFragment);
        }
        transaction.show(homeFragment);
        transaction.commitAllowingStateLoss();
        if (initProvider != null){
            initProvider.PlayWelcomeTTS();
        }
        if (homeFragment != null){
            homeFragment.returnDefault();
        }
        finishAllActivity();
        initSuccess();


    }

    /**
     * 隐藏首页界面
     */
    private void hideHomePageView(){
        if (homeFragment != null && transaction != null){
            transaction.hide(homeFragment);
        }
    }

    /**
     * 显示wifi配网页面
     */
    private void showWifiConfigureFragment(){

        transaction = fragmentManager.beginTransaction();
        hideHomePageView();
        if (configureFragment == null){
            configureFragment = new StartWifiConfigureFragment();

        }

        if (!configureFragment.isAdded()){
            transaction.add(R.id.rela_pre,configureFragment);
        }
        transaction.show(configureFragment);
        transaction.commitAllowingStateLoss();



    }

    /**
     * 隐藏wifi配网页面
     */
    private void hideWifiConfigureFragment(){
        if (configureFragment != null && transaction != null){
            transaction.hide(configureFragment);
            configureFragment = null;
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null){
            boolean HOME_PAGE = intent.getBooleanExtra("HOME_PAGE",false);
            LogUtil.e("HOME_PAGE = "+HOME_PAGE);
            if (homeFragment != null){
                if (HOME_PAGE){
                    homeFragment.returnDefault();
                }
                finishAllActivity();
            }
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initData() {

        mAdminReceiver = new ComponentName(mContext, ScreenOffAdminReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mPolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//        if (!mPolicyManager.isAdminActive(mAdminReceiver)) {
//            openDeviceManager();
//        }


        LogUtil.d(TAG, "首页初始化语音");
        if (initProvider != null) {
            initProvider.init(this);
            initProvider.initVoice();
        }

        if (mIWeatherProvider == null){
            mIWeatherProvider = ARouter.getInstance().navigation(IWeatherProvider.class);
        }
//
//        LogUtil.e("进入了Oncreate的initData");
//
//        if (mICallProvider != null) {
//            mICallProvider.initSdk();
//            Log.e("TAG", "初始化");
//            leaveMessageProvider = (IAppLeaveMessageProvider) ARouter.getInstance().build(RouterPath.Leavemessage.LEAVEMESSAGE_SERVICE).navigation();
//            if (leaveMessageProvider != null){
//                leaveMessageProvider.initRongIMlistener();
//            }
//        }
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
    protected void initPresenter () {
        mPresenter.setVM(this, mModel);
    }



    private void initSuccess() {
        if (initVoiceProvider == null) {
            initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
        }
        if (initVoiceProvider != null) {
            initVoiceProvider.openVoice();
        }
        if (manager == null) {
            manager = ContentProviderManager.getInstance(this, Uri.parse(ContentProviderManager.BASE_URI + "/user"));
            getContentResolver().registerContentObserver(Uri.parse(ContentProviderManager.BASE_URI), true, new MyContentObserver(new Handler(), manager));
        }

        //避免重复调用
        if (initVoiceProvider != null ) {
                initVoiceProvider.requestWeather();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //暂停5S执行，不然无法获取新闻
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        initVoiceProvider.requestNews(10);

                    }
                }).start();
        }
    }

    private void finishAllActivity () {
        Stack<Activity> activities = AppManager.getAppManager().getActivityStack();
        for (int i = 0, size = activities.size(); i < size; i++) {
            Activity mActivity = activities.get(i);
            if (null != mActivity && mActivity != this) {
                mActivity.finish();
            }
        }
        AppManager.getAppManager().clearActivityStack();
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void isNetWodrkConnect () {
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
    protected void onResume () {
        super.onResume();
//        isExitHome = false;
        Intent intent = getIntent();
        if (intent != null) {
            mGetBindMultiIntent = intent.getStringExtra("BIND_INTENT");
            mGetBindEventIntent = intent.getStringExtra("BIND_EVENT_INTENT");

        }
//        LogUtil.e("进入了onResume");
//        if (initVoiceProvider == null) {
//            initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
//        }
//
//        if (initVoiceProvider != null && !BaseApplication.getBaseInstance().isRequestWeather()) {
//            initVoiceProvider.openVoice();
//        }
//
//        startCycleRollRunnable();
//        mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);
    }

    @Override
    protected void onStop () {
        super.onStop();
//        mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
//        if (executorService != null) {
//            executorService.setRemoveOnCancelPolicy(true);
//            executorService.shutdown();
//            executorService = null;
//        }
//        if (isExitHome) {
//            returnDefault();
//        }

        LogUtil.e("Homepage onStop方法执行");
    }


        @Override
    public void onClick (View v){
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
            String saveWeahterValue = (String) SPUtils.get(getApplicationContext(), Constant.Weather.SP_NOW_WEATHER, "");
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
            if (leaveMessageProvider != null){
                leaveMessageProvider.openConversationListActivity();
            }
        }
    }

    @Override
    protected void onDestroy () {
        if (mBtReceiver != null){
            unregisterReceiver(mBtReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void getFamilyContactsSuccess(BaseResponse<List<UserInfoBean>> response) {
        ContentProviderManager.getInstance(mContext, Constant.Common.URI).insertUsers(response.getData());
        ICallProvider callService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
        ISettingsProvider settingsService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
        if (callService != null) {
            callService.syncFamilyContacts();
        }
        if (settingsService != null) {
            settingsService.syncSettingsContacts();
        }

    }

    @Override
    public void getFamilyContactsFailure(BaseResponse<List<UserInfoBean>> response) {

    }


    @Override
    public void showErrorTip(String msg) {

    }


        /**
         * 亮屏
         */
        @SuppressLint("InvalidWakeLockTag")
        public void screenOn () {
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
        public void screenOff () {
            boolean admin = mPolicyManager.isAdminActive(mAdminReceiver);
            if (admin) {
                mPolicyManager.lockNow();
            } else {
                ToastUtils.show("没有设备管理员权限");
            }
        }




    public void setDiscoverableTimeout(int timeout) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, timeout);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//==============================================================================EventBus监听开始===================================================================================

    @Override
    public void onEvent ( final EventMessage<BaseTcpMessage> message){
        //家庭解散通知
        if (message.getCode() == TCPConfig.MessageType.FAMILY_DISSOLVE) {
            LogUtil.d("家庭解散通知1 " + message);
            ContentProviderManager.getInstance(mContext, Constant.Common.URI).clear();
            DbUtil.getInstance(mContext).deleteAllCallRecoder();
            if (mICallProvider != null) {
                mICallProvider.syncFamilyContacts();
            }

            if(mISettingsProvider != null){
                mISettingsProvider.syncSettingsContacts();
            }
            AppUtils.saveBindedDevice(getApplicationContext(), false);
//            ActivityManager am = (ActivityManager)getSystemService(Con
//            text.ACTIVITY_SERVICE);
//            String activityName = am.getRunningTasks(1).get(0).topActivity.getClassName();
//
            LogUtil.e("applyId 家庭解散通知1 栈顶activityName = ");

            mBluetoothAdapter.disable();
            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsBindDeviceActivity).navigation();

        } else if (TCPConfig.MessageType.DCA_MSG == message.getCode()) {
            LogUtil.d(TAG, "收到DCA消息 = " + message.getData());
            BaseTcpMessage tcpMsg = message.getData();
            String dataMsg = tcpMsg.getMsg();
            JSONObject jsonObject = JSONObject.parseObject(dataMsg);
            String authCode = jsonObject.getString("authCode");
            String cliendId = jsonObject.getString("cliendId");
            String codeVerifier = jsonObject.getString("codeVerifier");
            String userId = jsonObject.getString("userId");
            SPUtils.put(getApplicationContext(), Constant.HomePage.DCA_AUTNCODE, authCode);
            SPUtils.put(getApplicationContext(), Constant.HomePage.DCA_CODEVERIFIER, codeVerifier);
            SPUtils.put(getApplicationContext(), Constant.HomePage.DCA_CLIENDID, cliendId);
            SPUtils.put(getApplicationContext(), Constant.HomePage.DCA_USERID, userId);

            IVoiceInitProvider ddsService = (IVoiceInitProvider) ARouter.getInstance().build(RouterPath.VOICE.INIT_PROVIDER).navigation();
            if (ddsService != null) {
                ddsService.initAuth();
            }
        }
        //普通成员退出家庭通知
        else if (message.getCode() == TCPConfig.MessageType.USER_EXIT_FAMILY) {
            LogUtil.d("bind onReceiveEvent = " + message);
            LogUtil.d("普通成员退出家庭通知" + message.getData().getHead().getUserId());
            String messageContent = message.getData().getMsg();
            String userName = messageContent.substring(messageContent.indexOf("【") + 1, messageContent.indexOf("】"));
            LogUtil.e(userName);
            ContentProviderManager manager = ContentProviderManager.getInstance(BaseApplication.getBaseInstance(), Uri.parse(ContentProviderManager.BASE_URI + "/user"));
            List<UserInfoBean> beanList = manager.queryUser("name = ? ", new String[]{userName});
            if (beanList != null && beanList.size() > 0) {
                String userMobile = beanList.get(0).getMobile();
                LogUtil.i("phone = " + userMobile);
                IAppLeaveMessageProvider leaveMessageProvider = (IAppLeaveMessageProvider) ARouter.getInstance().build(RouterPath.Leavemessage.LEAVEMESSAGE_SERVICE).navigation();
                leaveMessageProvider.removeRongIMMessage(userMobile);
            } else {
                LogUtil.i("找不到用户");
            }
            MessageBean messageBean = GsonUtil.GsonToBean(messageContent, MessageBean.class);
            if (messageBean != null && messageBean.getMessageUserInfoDTO() != null) {
                String userId = messageBean.getMessageUserInfoDTO().getUserId();
                ContentProviderManager.getInstance(mContext, Constant.Common.URI).deleteUserByUserID(userId);
                DbUtil.getInstance(mContext).deleteCallRecoderByUserId(userId);
                if (mICallProvider != null) {
                    mICallProvider.syncFamilyContacts();
                }

                if(mISettingsProvider != null){
                    mISettingsProvider.syncSettingsContacts();
                }
            }
        } else if (message.getCode() == TCPConfig.MessageType.USER_REPAIR_HEAD ) {
            if (message != null && message.getData() != null) {
                BaseTcpMessage baseTcpMessage = (BaseTcpMessage) message.getData();
                String msg = baseTcpMessage.getMsg();
                RepairPersonHeadBean bean = GsonUtil.GsonToBean(msg, RepairPersonHeadBean.class);
                if (bean != null) {
                    ContentProviderManager.getInstance(mContext, Constant.Common.URI).updateUserHeadByUserID(bean.getIcon(), bean.getUserId());
                    DbUtil.getInstance(mContext).updateIconByUserId(bean.getIcon(), bean.getUserId());
                    if (mICallProvider != null) {
                        mICallProvider.syncFamilyContacts();
                    }

                    if(mISettingsProvider != null){
                        mISettingsProvider.syncSettingsContacts();
                    }
                }
            }
        } else if (message.getCode() == TCPConfig.MessageType.NEW_USER_ADD) { //新人加入家庭通知
            BaseTcpMessage dataMsg = message.getData();
            String msgContent = dataMsg.getMsg();
            MessageBean messageBean = GsonUtil.GsonToBean(msgContent, MessageBean.class);
            if (messageBean != null && messageBean.getMessageUserInfoDTO() != null) {
                UserInfoBean userInfoBean = messageBean.getMessageUserInfoDTO();
                ContentProviderManager.getInstance(mContext, Constant.Common.URI).insertUser(userInfoBean);
                if (mICallProvider != null) {
                    mICallProvider.syncFamilyContacts();
                }

                if(mISettingsProvider != null){
                    mISettingsProvider.syncSettingsContacts();
                }
            }
            mNewUserName = msgContent.substring(msgContent.indexOf("【") + 1, msgContent.indexOf("】"));
            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsContractsNickNameEditActivity).withString("newAddUserName", mNewUserName).navigation();
            LogUtil.d(TAG, "新人加入家庭通知" + mNewUserName);

        }
//        else if (message.getCode() == Constant.Common.INIT_VOICE_SUCCESS && AppUtils.isFirstStart(getApplicationContext())) {
//            // @todo  勿删 语音初始化成功后会回调这里,在语音成功之前调用会导致应用崩溃
//            LogUtil.e("===== INIT_VOICE_SUCCESS =====");
//
//            initSuccess();
//        }
        else if (message.getCode() == Constant.Common.GO_HOME) {
            //回到首页时 把列表页面回到默认位置
            finishAllActivity();
//            returnDefault();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenEvent (String type){
        if (type.equals(Constant.Common.SCREEN_OFF)) {
            LogUtil.i("homepage:screenOff");
            screenOff();
        } else if (type.equals(Constant.Common.SCREEN_ON)) {
            LogUtil.i("homepage:screenOn");
            screenOn();
        }else if (type.equals(Constant.Common.BIND_SUCCESS)){
            LogUtil.e("onScreenEvent = "+type);
            showHomePageView();
            mISettingsProvider.queryDeviceInfo(this,false);
            try {
                AppManager.getAppManager().finishActivity(Class.forName("com.fenda.settings.activity.SettingsBindDeviceActivity"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkEvent(com.fenda.common.network.Network network){
        if (network.isNetwork()){
            if (configureFragment != null){
                configureFragment.networkAvailable();
            }
        } else {
            //网络断开


        }
    }

    //==============================================================================EventBus监听结束===================================================================================

}
