package com.fenda.homepage.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseFragment;
import com.fenda.common.base.BaseMvpFragment;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.SettingsWifiBean;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.ISettingsProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.NetUtil;
import com.fenda.common.util.SettingsCheckWifiLoginTask;
import com.fenda.common.util.SettingsWifiUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.homepage.R;
import com.fenda.homepage.contract.MainContract;
import com.fenda.homepage.model.MainModel;
import com.fenda.homepage.presenter.MainPresenter;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/12/2 15:03
 * @Description
 */
public class StartWifiConfigureFragment extends BaseMvpFragment<MainPresenter, MainModel> implements MainContract.View, View.OnClickListener {

    private static final String TAG = "StartWifiConfigureActivity";

    public static final int DISABLE_EXPAND = 0x00010000;
    public static final int DISABLE_NONE = 0x00000000;

    private Switch wifiSwitch;
    private RecyclerView rvWifiList;
    private ImageView tvBack;
    private ImageView ivScanWifiGif;
    private TextView tvScanWifiTv;
    private ImageView ivImageView;

    private WifiManager mWifiManager;
    private AnimationDrawable mAnimationDrawable;
    private AnimationDrawable mAnimationDrawable1;
    protected SettingsWifiUtil mSettingsWifiUtil;
    private List<SettingsWifiBean> mScanWifiListBean;
    private MyWifiAdapter mMyWifiAdapter;
    private SettingsWifiBean mSettingsWifiBean;

    private final static int  OPEN_WIFI = 1;
    private final static int  CLOSE_WIFI = 0;

    private String mConnectedSsid;
    protected String mListItemClickedSsid;
    public int level;
    private String mPswSureSsid;
    private ProgressDialog progressDialog;

    private ISettingsProvider settingService;

    @Override
    public int onBindLayout() {
        return R.layout.start_wifi_configure_layout;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("  Msg " + msg.what);
            switch (msg.what) {
                case OPEN_WIFI :
                    break;
                case CLOSE_WIFI:
                    mSettingsWifiUtil.closeWifi(mContext);
                default:
                    break;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {

        ivImageView = mRootView.findViewById(R.id.start_wifi_circle_progress);
        tvScanWifiTv = mRootView.findViewById(R.id.start_scan_wifi_tv);
        ivScanWifiGif = mRootView.findViewById(R.id.start_scan_wifi_iv);
        rvWifiList= mRootView.findViewById(R.id.start_set_wifi_listview);
        wifiSwitch = mRootView.findViewById(R.id.start_wifi_switch);
        tvBack = mRootView.findViewById(R.id.start_set_wifi_back_iv);

        rvWifiList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWifiList.setItemAnimator(new DefaultItemAnimator());
        mScanWifiListBean = new ArrayList<>();
        mMyWifiAdapter = new MyWifiAdapter(getLayoutInflater(), mScanWifiListBean);
        rvWifiList.setAdapter(mMyWifiAdapter);
        mSettingsWifiUtil = new SettingsWifiUtil(getContext());
        mSettingsWifiBean = new SettingsWifiBean();

        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!mWifiManager.isWifiEnabled()) {
            wifiSwitch.setChecked(false);
            mSettingsWifiBean.setStatus(0);
        } else {
            wifiSwitch.setChecked(true);
            mSettingsWifiBean.setStatus(6);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mWifiReceiver, filter);
        if (NetUtil.checkNet() && !BaseApplication.getBaseInstance().isVoiceAuth()){
            showProgressDialog();
        }

    }

    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("正在初始化中，请稍等...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!mWifiManager.isWifiEnabled()) {
                LogUtil.d(TAG, "wifi is unable ~");
            } else {
                mSettingsWifiUtil.startScan(mContext);
                mScanWifiListBean.clear();
                mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(1, mConnectedSsid));
                LogUtil.d(TAG, "mWifiList = " + mScanWifiListBean.size());
//                LogUtil.d(TAG, "bindView status === " + mSettingsWifiBean.getStatus());
                if(mScanWifiListBean.size() > 0) {
                    wifiSwitch.setVisibility(View.VISIBLE);
                    ivImageView.setVisibility(View.GONE);

                    tvScanWifiTv.setVisibility(View.GONE);
                    ivScanWifiGif.setVisibility(View.GONE);
                    ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
                    mAnimationDrawable.stop();
                    rvWifiList.setAdapter(mMyWifiAdapter);
                    mMyWifiAdapter.notifyDataSetChanged();
                } else {
                    rvWifiList.setAdapter(null);
                    wifiSwitch.setVisibility(View.GONE);
                    ivImageView.setVisibility(View.VISIBLE);
                    ivImageView.setImageResource(R.drawable.settings_wifi_connecting_gif);

                    tvScanWifiTv.setVisibility(View.VISIBLE);
                    ivScanWifiGif.setVisibility(View.VISIBLE);
                    ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
                    mAnimationDrawable1 = (AnimationDrawable) ivImageView.getDrawable();
                    mAnimationDrawable.start();
                    mAnimationDrawable1.start();
                }
            }
            // 执行后延迟8000毫秒再次执行
            mHandler.postDelayed(runnable,8000);
        }
    };



    //监听wifi状态变化
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver () {
        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            WifiInfo mWifiInfo;
            int mBroadcastStatus;

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.i(TAG, "已经关闭");
//                        mSettingsWifiBean.setStatus(0);
                        mScanWifiListBean.clear();
                        rvWifiList.setAdapter(null);
                        mHandler.removeCallbacks(runnable);
                        break;
                    }
                    case WifiManager.WIFI_STATE_DISABLING: {
                        Log.i(TAG, "正在关闭");
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLED: {
                        Log.i(TAG, "已经打开");
                        mHandler.post(runnable);
                        break;
                    }
                    case WifiManager.WIFI_STATE_ENABLING: {
                        Log.i(TAG, "正在打开");
                        break;
                    }
                    case WifiManager.WIFI_STATE_UNKNOWN: {
                        Log.i(TAG, "未知状态");
                        break;
                    }
                    default:
                        break;
                }
            } else if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                LogUtil.d(TAG, "Android.net.conn.CONNECTIVITY_CHANGE");
                Intent mIntent = new Intent();
                mIntent.setAction("Android.net.conn.CONNECTIVITY_CHANGE");
                mIntent.setPackage(mContext.getPackageName());
                mContext.sendBroadcast(mIntent);
            }
            else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {
                    Log.i(TAG, "wifi没连接上");
                    mSettingsWifiBean.setStatus(4);
                    mConnectedSsid = "";
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {
                    Log.i(TAG, "wifi连接上了");
                    mSettingsWifiBean.setStatus(1);
                    mWifiInfo = mWifiManager.getConnectionInfo();
                    mConnectedSsid = mWifiInfo.getSSID().replace("\"", "");
                    wifiSwitch.setChecked(true);

                } else if (NetworkInfo.State.CONNECTING == info.getState()) {
                    mSettingsWifiBean.setStatus(3);
                    mBroadcastStatus = 3;
                    String extra = info.getExtraInfo();
                    Log.i(TAG, "extra = " + extra);
                    extra = extra.substring(1, extra.length() - 1);
                    mSettingsWifiUtil.startScan(mContext);
                    mScanWifiListBean.clear();
                    mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(mBroadcastStatus, extra));
                    mMyWifiAdapter.notifyDataSetChanged();
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                LogUtil.d(TAG, "WifiManager.SCAN_RESULTS_AVAILABLE_ACTION");
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    Log.e(TAG, "mWifiReceiver : wifi密码错误");
                    mSettingsWifiBean.setStatus(2);
                    if(mPswSureSsid != null){
                        String wifiSsid1 = mWifiManager.getConnectionInfo().getSSID();
                        String wifiSsid2 = wifiSsid1.substring(1, wifiSsid1.length() - 1);
                        mSettingsWifiBean.setItemStatus(wifiSsid2, "错误啦");

                        if(mPswSureSsid.equals(wifiSsid2)){
                            ToastUtils.show(wifiSsid1 + "  密码错误，请重试");
                            Log.e(TAG, "wifi密码错误 SSID = " + wifiSsid2);
                            int ssidId = mSettingsWifiUtil.getNetworkId(wifiSsid2);
                            mSettingsWifiUtil.removeWifi(ssidId);
                        }
                    }
                }
            }
        }
    };

    /**
     * 网络连接成功
     */
    public void networkAvailable(){
        if (BaseApplication.getBaseInstance().isVoiceAuth()){
            if (settingService == null){
                settingService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
            }
            if (settingService != null) {
                LogUtil.d(TAG, "init device status");
                settingService.deviceStatus(mContext);
            }
        }else {
            showProgressDialog();
        }
        portalWifi();


    }





    //** wifi 认证 检测 **/
    private void portalWifi() {
        SettingsCheckWifiLoginTask.checkWifi(new SettingsCheckWifiLoginTask.ICheckWifiCallBack() {
            @SuppressLint("LongLogTag")
            @Override
            public void portalNetWork(boolean isLogin) {
                //不需要wifi认证
                if(!isLogin){
                    LogUtil.d(TAG, "不需要wifi门户验证");

                }else {
                    LogUtil.d(TAG, "需要wifi门户验证");
                    ToastUtils.show("请先进行网络认证！");
                    String apksUrl = "http://i.eastmoney.com/2188113638420790";
//                    Intent aboutIntent = new Intent(StartWifiConfigureActivity.this, SettingsLoadWebviewActivity.class);
//                    aboutIntent.putExtra("APK_URL", apksUrl);
//                    startActivity(aboutIntent);
                }
            }
        });
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onDestroy() {
        Log.e(TAG, "ondestory");

        super.onDestroy();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onStop() {
        Log.e(TAG, "onStop");
        setStatusBarDisable(DISABLE_NONE);
//        mContext.unregisterReceiver(mWifiReceiver);
        mHandler.removeCallbacksAndMessages(null);
        super.onStop();
    }

    private void dialogDismiss(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }

    }

    private void setStatusBarDisable(int disableStatus) {
        //调用statusBar的disable方法
        @SuppressLint("WrongConstant") Object service = mContext.getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName
                    ("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, disableStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void getFamilyContactsSuccess(BaseResponse<List<UserInfoBean>> response) {

    }

    @Override
    public void getFamilyContactsFailure(BaseResponse<List<UserInfoBean>> response) {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void showContent() {

    }

    @Override
    public void hideContent() {

    }

    @SuppressLint("LongLogTag")
    @Override
    public <BaseTcpMessage> void onEvent(EventMessage<BaseTcpMessage> event) {
        if (event.getCode() == Constant.Common.INIT_VOICE_SUCCESS) {
            // @todo  勿删 语音初始化成功后会回调这里,在语音成功之前调用会导致应用崩溃
            Log.e(TAG, "===== INIT_VOICE_SUCCESS =====");
            dialogDismiss();
            if (settingService == null){
                settingService = (ISettingsProvider) ARouter.getInstance().build(RouterPath.SETTINGS.SettingsService).navigation();
            }
            if (settingService != null) {
                LogUtil.d(TAG, "init device status");
                settingService.deviceStatus(mContext);
            }
        }
    }





    private class MyWifiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater mLayoutInflater;
        List<SettingsWifiBean> mWifiBeanList;

        public MyWifiAdapter(LayoutInflater mLayoutInflater, List<SettingsWifiBean> mWifiBeanList) {
            this.mLayoutInflater = mLayoutInflater;
            this.mWifiBeanList = mWifiBeanList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mLayoutInflater.inflate(R.layout.homepage_wifi_listitem_layout, parent,false);
            return new MyWifiAdapter.ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final MyWifiAdapter.ViewHolder mHolder = (MyWifiAdapter.ViewHolder) holder;
            mSettingsWifiBean = mWifiBeanList.get(position);
            final ScanResult scanResult = mSettingsWifiBean.getResult();
            final int status = mSettingsWifiBean.getStatus();
            mHolder.wifiSsid.setText(scanResult.SSID);
            level = WifiManager.calculateSignalLevel(scanResult.level,5);
            if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                mHolder.wifiLevel.setImageResource(R.drawable.settings_wifi_signal_lock);
            }else{
                mHolder.wifiLevel.setImageResource(R.drawable.settings_wifi_signal_open);
            }
            mHolder.wifiLevel.setImageLevel(level);

            if (status == 3){
                LogUtil.d(TAG, "wifi 正在连接 connecting ");
                mHolder.connectWifiLoading.setVisibility(View.VISIBLE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                mAnimationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                mAnimationDrawable.start();
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            } else if (status == 1){
                LogUtil.d(TAG, "wifi 连接上了 connect sucess ");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.tvStatus.setText("已连接");
                mHolder.connectWifiIcon.setVisibility(View.VISIBLE);
                mHolder.connectWifiLoading.setVisibility(View.GONE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                mAnimationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                mAnimationDrawable.stop();
            } else if(status == 2) {
                mHolder.tvStatus.setText("密码错误，请重试！");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.connectWifiIcon.setVisibility(View.GONE);
                mHolder.connectWifiLoading.setVisibility(View.GONE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                mAnimationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                mAnimationDrawable.stop();

            } else if(status == 4){
                LogUtil.d(TAG, "wifi not connect ");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.tvStatus.setText("没有连上");
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            } else {
                mHolder.tvStatus.setVisibility(View.GONE);
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListItemClickedSsid = mScanWifiListBean.get(position).getResult().SSID;
                    mPswSureSsid = mListItemClickedSsid;
                    WifiConfiguration config = new WifiConfiguration();

                    config.allowedAuthAlgorithms.clear();
                    config.allowedGroupCiphers.clear();
                    config.allowedKeyManagement.clear();
                    config.allowedPairwiseCiphers.clear();
                    config.allowedProtocols.clear();
                    config.SSID = "\"" + mListItemClickedSsid + "\"";
                    config.hiddenSSID=true;

//                    List<WifiConfiguration> wifiConfigurationList = mSettingsWifiUtil.getConfiguration();
                    LogUtil.d(TAG, "connected wifi ssid = " + mConnectedSsid);
                    LogUtil.d(TAG, "clicked wifi ssid = " + mListItemClickedSsid);

                    if(mListItemClickedSsid.equals(mConnectedSsid)) {
                        Intent connectedIntent = new Intent(getContext(), HomepageWifiConnectedInfoActivity.class);
                        connectedIntent.putExtra("CONNECTED_MESSAGE", mListItemClickedSsid);
                        startActivity(connectedIntent);
                    } else {
                        //WiFi是否已经保存
                        LogUtil.d(TAG,  "save wifi flag = " + mSettingsWifiUtil.isWifiSave(mListItemClickedSsid));
                        if(mSettingsWifiUtil.isWifiSave(mListItemClickedSsid)){
                            //点击连接
                            int ssidId = mSettingsWifiUtil.getNetworkId(mListItemClickedSsid);
                            LogUtil.d(TAG,  "save wifi id clicked = " + ssidId);
                            mWifiManager.enableNetwork(ssidId, true);
                        } else {
                            if(scanResult.capabilities.contains("WEP") || scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                                //密码连接
                                Intent connectIntent = new Intent(getContext(), HomepageWifiInputPswActivity.class);
                                connectIntent.putExtra("MESSAGE", mListItemClickedSsid);
                                startActivity(connectIntent);
                            } else{
                                //无密码直接连接
                                LogUtil.d(TAG, "no psw wifi connect");
                                boolean isWifi = mSettingsWifiUtil.connectSpecificAP(scanResult);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (isWifi) {
                                    LogUtil.d(TAG, "无密码wifi连接成功");
                                } else {
                                    Toast.makeText(getContext(), "连接失败！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mWifiBeanList.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView wifiSsid;
            public ImageView wifiLevel;
            public ImageView connectWifiIcon;
            public ImageView connectWifiLoading;
            public TextView tvStatus;

            public ViewHolder(View itemView) {
                super(itemView);
                wifiSsid= itemView.findViewById(R.id.start_wifi_ssid);
                wifiLevel= itemView.findViewById(R.id.start_wifi_level);
                tvStatus = itemView.findViewById(R.id.start_tv_status);
                connectWifiIcon = itemView.findViewById(R.id.start_connected_wifi_status_icon);
                connectWifiLoading = itemView.findViewById(R.id.start_wifi_list_item_connecting);
            }
        }
    }






}
