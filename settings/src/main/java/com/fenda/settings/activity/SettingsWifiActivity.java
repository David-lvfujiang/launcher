package com.fenda.settings.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.net.wifi.WifiConfiguration.KeyMgmt;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;
import com.fenda.settings.bean.SettingsWifiBean;
import com.fenda.settings.utils.SettingsWifiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 15:33
 */
@Route(path = RouterPath.SETTINGS.SettingsWifiActivity)
public class SettingsWifiActivity extends BaseMvpActivity{
    private static final String TAG = "SettingsWifiActivity";

    private Switch wifiSwitch;
    private RecyclerView rvWifiList;
    private TextView tvBack;
    private ImageView ivScanWifiGif;
    private TextView tvScanWifiTv;

    private WifiManager mWifiManager;
    private AnimationDrawable mAnimationDrawable;
    protected SettingsWifiUtil mSettingsWifiUtil;
    private List<SettingsWifiBean> mScanWifiListBean;
    private MyWifiAdapter mMyWifiAdapter;
    private SettingsWifiBean mSettingsWifiBean;
    private WifiInfo mWifiInfo;

    private final static int  OPEN_WIFI = 1;
    private final static int  CLOSE_WIFI = 0;

    private String mConnectedSSID;
    protected String mListItemClickedSsid;
    private int mBroadcastStatus;
    public int level;
    private String mPswSureSsid;



    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_wifi_layout;
    }

    @Override
    public void initView() {
        tvScanWifiTv = findViewById(R.id.scan_wifi_tv);
        ivScanWifiGif = findViewById(R.id.scan_wifi_iv);
        rvWifiList= findViewById(R.id.set_wifi_listview);
        wifiSwitch = findViewById(R.id.wifi_switch);
        tvBack = findViewById(R.id.set_wifi_back_iv);

        rvWifiList.setLayoutManager(new LinearLayoutManager(this));
        rvWifiList.setItemAnimator(new DefaultItemAnimator());
        mScanWifiListBean = new ArrayList<>();
        mMyWifiAdapter = new MyWifiAdapter(getLayoutInflater(), mScanWifiListBean);
        rvWifiList.setAdapter(mMyWifiAdapter);
        mSettingsWifiUtil = new SettingsWifiUtil(SettingsWifiActivity.this);
        mSettingsWifiBean = new SettingsWifiBean();

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

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
        registerReceiver(mWifiReceiver, filter);
    }

    @Override
    public void initData() {
        Intent mIntent = getIntent();
        mPswSureSsid = mIntent.getStringExtra("SURE_CONNECT_SSID");


        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    LogUtil.d(TAG, "wifi switch is on ");
                    mSettingsWifiUtil.openWifi(getApplicationContext());
                    tvScanWifiTv.setVisibility(View.VISIBLE);
                    ivScanWifiGif.setVisibility(View.VISIBLE);
                    ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
                    mAnimationDrawable.start();
                    mHandler.post(runnable);
                } else{
                    LogUtil.d(TAG, "wifi switch is off ");
                    mSettingsWifiUtil.closeWifi(SettingsWifiActivity.this);
                    tvScanWifiTv.setVisibility(View.GONE);
                    ivScanWifiGif.setVisibility(View.GONE);
                    ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
                    mAnimationDrawable.stop();

                    mScanWifiListBean.clear();
                    rvWifiList.setAdapter(null);
                    mHandler.removeCallbacks(runnable);
                }
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!mWifiManager.isWifiEnabled()) {
                LogUtil.d(TAG, "wifi is unable ~");
            } else {
                mSettingsWifiUtil.startScan(getApplicationContext());
                mScanWifiListBean.clear();
                LogUtil.d(TAG, "mWifiList = " + mScanWifiListBean.size());
                mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(1, mConnectedSSID));
                LogUtil.d(TAG, "mWifiList 2 = " + mScanWifiListBean.size());

                if(mScanWifiListBean.size() > 0) {
                    tvScanWifiTv.setVisibility(View.GONE);
                    ivScanWifiGif.setVisibility(View.GONE);
                    ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
                    mAnimationDrawable.stop();
                    rvWifiList.setAdapter(mMyWifiAdapter);
                    mMyWifiAdapter.notifyDataSetChanged();
                } else {
                    rvWifiList.setAdapter(null);
                    tvScanWifiTv.setVisibility(View.VISIBLE);
                    ivScanWifiGif.setVisibility(View.VISIBLE);
                    ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
                    mAnimationDrawable.start();
                }
            }
            mHandler.postDelayed(runnable,8000); // 执行后延迟8000毫秒再次执行
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("  Msg " + msg.what);
            switch (msg.what) {
                case OPEN_WIFI :
                    break;
                case CLOSE_WIFI:
                    mSettingsWifiUtil.closeWifi(SettingsWifiActivity.this);
                default:
                    break;
            }
        }
    };

    @Override
    public void initListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsWifiActivity.this, SettingsActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
    @Override
    public void showErrorTip(String msg) {

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
            View view = mLayoutInflater.inflate(R.layout.settings_wifi_listitem_layout, parent,false);
            return new ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder mHolder = (ViewHolder) holder;
            mSettingsWifiBean = mWifiBeanList.get(position);
            final ScanResult scanResult = mSettingsWifiBean.getResult();
            final int status = mSettingsWifiBean.getStatus();
            LogUtil.d(TAG,  "onBindViewHolder status = " + status);

            mHolder.wifiSsid.setText(scanResult.SSID);
            level=WifiManager.calculateSignalLevel(scanResult.level,5);
            if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                mHolder.wifi_level.setImageResource(R.drawable.settings_wifi_signal_lock);
            }else{
                mHolder.wifi_level.setImageResource(R.drawable.settings_wifi_signal_open);
            }
            mHolder.wifi_level.setImageLevel(level);

            if (status == 2){  //wifi正在连接
                LogUtil.d(TAG, "wifi 正在连接 connecting ");
                mHolder.connectWifiLoading.setVisibility(View.VISIBLE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                mAnimationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                mAnimationDrawable.start();
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            } else if (status == 1){  //wifi连接上了
                LogUtil.d(TAG, "wifi 连接上了 connect sucess ");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.tvStatus.setText(getString(R.string.settings_wifi_connected_status));
                mHolder.connectWifiIcon.setVisibility(View.VISIBLE);
                mHolder.connectWifiLoading.setVisibility(View.GONE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                mAnimationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                mAnimationDrawable.stop();
            } else if(status ==3) {
                LogUtil.d(TAG, "wifi psw error ");
                mHolder.tvStatus.setText(getString(R.string.settings_wifi_psw_error));
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            } else if(status == 4){
                LogUtil.d(TAG, "wifi not connect ");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
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

                    List<WifiConfiguration> wifiConfigurationList = mSettingsWifiUtil.getConfiguration();
                    LogUtil.d(TAG, "wifiConfigurationList = " + wifiConfigurationList);
                    LogUtil.d(TAG, "connected wifi ssid = " + mConnectedSSID);
                    LogUtil.d(TAG, "clicked wifi ssid = " + mListItemClickedSsid);


                    if(mConnectedSSID.equals(mListItemClickedSsid)) {
                        Intent connectedIntent = new Intent(SettingsWifiActivity.this, SettingsWifiConnectedInfoActivity.class);
                        connectedIntent.putExtra("CONNECTED_MESSAGE", mListItemClickedSsid);
                        startActivity(connectedIntent);
                        finish();
                    } else {
                        //WiFi是否已经保存
                        LogUtil.d(TAG,  "save wifi flag = " + mSettingsWifiUtil.isWifiSave(mListItemClickedSsid));
                        if(mSettingsWifiUtil.isWifiSave(mListItemClickedSsid)){
                            //点击连接
                            int ssidID = mSettingsWifiUtil.getNetworkId(mListItemClickedSsid);
                            LogUtil.d(TAG,  "save wifi id clicked = " + ssidID);
                            mWifiManager.enableNetwork(ssidID, true);
                        } else {
//                            mSettingsWifiUtil.addNetwork(mSettingsWifiUtil.createWifiInfo(mListItemClickedSsid, "TEST##tech775511", 3));

                            if(scanResult.capabilities.contains("WEP") || scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                                //密码连接
                                Intent connectIntent = new Intent(SettingsWifiActivity.this, SettingsWifiInputPswActivity.class);
                                connectIntent.putExtra("MESSAGE", mListItemClickedSsid);
                                startActivity(connectIntent);
                                finish();
                            } else{
                                //无密码直接连接
                                LogUtil.d(TAG, "no psw wifi connect");
                                mSettingsWifiUtil.addNetwork(mSettingsWifiUtil.createWifiInfo(mListItemClickedSsid, "", 1));
//                                mSettingsWifiBean.setStatus(1);
                                int ssidID2 = mSettingsWifiUtil.getNetworkId(mListItemClickedSsid);
                                LogUtil.d(TAG,  "save wifi id clicked222  = " + ssidID2);
                                mWifiManager.enableNetwork(ssidID2, true);

                                mHolder.tvStatus.setVisibility(View.VISIBLE);
                                mHolder.tvStatus.setText(getString(R.string.settings_wifi_connected_status));
                                mHolder.connectWifiIcon.setVisibility(View.VISIBLE);

//                                config.hiddenSSID = true;
//                                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//                                config.allowedKeyManagement.set(KeyMgmt.NONE);

//                                config.wepKeys[0] = "\"" + "\"";
//                                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//                                config.wepTxKeyIndex = 0;
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
            public ImageView wifi_level;
            public ImageView connectWifiIcon;
            public ImageView connectWifiLoading;
            public TextView tvStatus;

            public ViewHolder(View itemView) {
                super(itemView);
                wifiSsid= itemView.findViewById(R.id.wifi_ssid);
                wifi_level= itemView.findViewById(R.id.wifi_level);
                tvStatus = itemView.findViewById(R.id.tv_status);
                connectWifiIcon = itemView.findViewById(R.id.connected_wifi_status_icon);
                connectWifiLoading = itemView.findViewById(R.id.wifi_list_item_connecting);
            }
        }
    }

    //监听wifi状态变化
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.i(TAG, "已经关闭");
                        mSettingsWifiBean.setStatus(0);
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
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();

                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    Log.i(TAG, "wifi没连接上");
                    mSettingsWifiBean.setStatus(4);
                    mConnectedSSID = "";
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    mSettingsWifiBean.setStatus(1);
                    mWifiInfo = mWifiManager.getConnectionInfo();
                    mConnectedSSID = mWifiInfo.getSSID().replace("\"", "");
                    LogUtil.d(TAG, "BroadcastReceiver wifiSSID = "+ mConnectedSSID);
                    wifiSwitch.setChecked(true);
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    mSettingsWifiBean.setStatus(2);
                    mBroadcastStatus =2;
                    Log.i(TAG, "wifi正在连接 status = " + mSettingsWifiBean.getStatus());

                    String extra = info.getExtraInfo();
                    Log.i(TAG, "extra = " + extra);
                    extra = extra.substring(1, extra.length() - 1);
                    mSettingsWifiUtil.startScan(SettingsWifiActivity.this);
                    mScanWifiListBean.clear();
                    mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(mBroadcastStatus, extra));
                    mMyWifiAdapter.notifyDataSetChanged();
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.i(TAG, "网络列表变化了");
//                if (!mWifiManager.isWifiEnabled()) {
//
//                } else {
//                    mSettingsWifiUtil.startScan(getApplicationContext());
//                    mScanWifiListBean.clear();
//                    LogUtil.d(TAG, "mWifiList = " + mScanWifiListBean.size());
//                    if(status == 2){
//                        mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(2, connectedSSID));
//                        LogUtil.d(TAG, "mWifiList status 2 = " + mScanWifiListBean.size());
//                    } else {
//                        mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(0, null));
//                        LogUtil.d(TAG, "mWifiList status 0 = " + mScanWifiListBean.size());
//                    }
//
//                    if(mScanWifiListBean.size() > 0) {
//                        tvScanWifiTv.setVisibility(View.GONE);
//                        ivScanWifiGif.setVisibility(View.GONE);
//                        ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
//                        mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
//                        mAnimationDrawable.stop();
//                        rvWifiList.setAdapter(mMyWifiAdapter);
//                        mMyWifiAdapter.notifyDataSetChanged();
//                    } else {
//                        rvWifiList.setAdapter(null);
//                        tvScanWifiTv.setVisibility(View.VISIBLE);
//                        ivScanWifiGif.setVisibility(View.VISIBLE);
//                        ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
//                        mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
//                        mAnimationDrawable.start();
//                    }
//                }
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    Log.e(TAG, "mWifiReceiver : wifi密码错误");
                    if(mPswSureSsid != null){
                        String wifiSSID1 = mWifiManager.getConnectionInfo().getSSID();
                        String wifiSSID2 = wifiSSID1.substring(1, wifiSSID1.length() - 1);
                        if(mPswSureSsid.equals(wifiSSID2)){
                            mSettingsWifiBean.setStatus(3);
                            ToastUtils.show(wifiSSID1 + "  密码错误，请重试");
                            Log.e(TAG, "wifi密码错误 SSID = " + wifiSSID2);
                            int ssidId = mSettingsWifiUtil.getNetworkId(wifiSSID2);
                            Log.e(TAG, "wifi密码错误 SSID ID = " + ssidId);
                            mSettingsWifiUtil.removeWifi(ssidId);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mWifiReceiver);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
