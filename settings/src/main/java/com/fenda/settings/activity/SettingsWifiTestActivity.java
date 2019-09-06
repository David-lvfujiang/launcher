package com.fenda.settings.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;
import com.fenda.settings.adapter.SettingsWifiAdapter;
import com.fenda.settings.bean.SettingsWifiBean;
import com.fenda.settings.utils.SettingsWifiUtil;

import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.WifiManager.EXTRA_SUPPLICANT_ERROR;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/6 9:57
 */
@Route(path = RouterPath.SETTINGS.SettingsWifiTestActivity)
public class SettingsWifiTestActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsWifiTestActivity";

    private Switch wifiSwitch;
    private RecyclerView rvWifiList;
    private TextView tvBack;
    private ImageView ivScanWifiGif;
    private TextView tvScanWifiTv;

    private AnimationDrawable mAnimationDrawable;
    protected SettingsWifiUtil mSettingsWifiUtil;
    private SettingsWifiAdapter mSettingsWifiAdapter;
    private List<SettingsWifiBean> mScanWifiListBean;
    private MyWifiAdapter mMyWifiAdapter;
    private WifiManager mWifiManager;

    private int status  = 0;
    private int mWifiSingalLevel;
    private String mItemClickSSID;


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
        mSettingsWifiUtil = new SettingsWifiUtil(this);
        mSettingsWifiAdapter = new SettingsWifiAdapter(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

//        registerReceiver(mWifiReceiver, filter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(100);
                finish();
            }
        });

    }

    @Override
    public void showErrorTip(String msg) {
    }

//    private class mWifiReceiver extends BroadcastReceiver{
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) { //当扫描结果后，进行刷新列表
//                refreshLocalWifiListData();
//            } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {//wifi连接网络状态变化
//                refreshLocalWifiListData();
//                NetworkInfo.DetailedState state = ((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
//                if (null != listener) {
//                    listener.onNetWorkStateChanged(state, mSSID);
//                }
//            } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {//wifi状态变化
//                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
//                if (null != listener) {
//                    listener.onWiFiStateChanged(wifiState);
//                }
//            } else if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
//                SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
//                int error = intent.getIntExtra(EXTRA_SUPPLICANT_ERROR, 0);
//                if (null != listener) {
//                    if (error == WifiManager.ERROR_AUTHENTICATING) { //这里可以获取到监听连接wifi密码错误的时候进行回调
//                        listener.onWifiPasswordFault();
//                    }
//                }
//            }
//        }
//    }

    public void refreshLocalWifiListData() {
        /*//逻辑说明：
        1.从扫描结果中将已经连接的wifi添加到wifi列表中
        2.从所有WiFilist中将已经添加过的已经连接的WiFi移除
        3.将剩余的WiFi添加到WiFilist中
                实现了已经连接的WiFi处于wifi列表的第一位
        */
        mSettingsWifiUtil.startScan(getApplicationContext());
        mScanWifiListBean.clear();
        LogUtil.d(TAG, "mWifiList = " + mScanWifiListBean.size());
        mScanWifiListBean.addAll(mSettingsWifiUtil.getWifiList(0, null));
        LogUtil.d(TAG, "mWifiList status 0 = " + mScanWifiListBean.size());

        tvScanWifiTv.setVisibility(View.GONE);
        ivScanWifiGif.setVisibility(View.GONE);
        ivScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
        mAnimationDrawable = (AnimationDrawable) ivScanWifiGif.getDrawable();
        mAnimationDrawable.stop();
        rvWifiList.setAdapter(mMyWifiAdapter);
        mMyWifiAdapter.notifyDataSetChanged();
    }

    public void onNetWorkStateChanged(NetworkInfo.DetailedState state) {
        if (state == NetworkInfo.DetailedState.SCANNING) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +正在扫描");
//            mainView.showWifiConnectInfo("正在扫描");
        } else if (state == NetworkInfo.DetailedState.CONNECTING) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +正在连接");
//            mainView.showWifiConnectInfo("正在连接");

        } else if (state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +获取IP地址");
//            mainView.showWifiConnectInfo("获取IP地址");

        } else if (state == NetworkInfo.DetailedState.CONNECTED) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +建立连接");
//            mainView.showWifiConnectInfo("建立连接");

        } else if (state == NetworkInfo.DetailedState.DISCONNECTING) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +正在断开连接");
//            mainView.showWifiConnectInfo("正在断开连接");

        } else if (state == NetworkInfo.DetailedState.DISCONNECTED) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +已断开连接");
//            mainView.showWifiConnectInfo("已断开连接");

        } else if (state == NetworkInfo.DetailedState.FAILED) {
            LogUtil.d(TAG, "onReceive onNetWorkStateChanged: +连接失败");
//            mainView.showWifiConnectInfo("连接失败");
        }
//同时可以更新view的列表
//        mainView.updateWifiListView();
    }
    private class MyWifiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        LayoutInflater mLayoutInflater;
        List<SettingsWifiBean> mWifiBeanList;

        public MyWifiAdapter(LayoutInflater mLayoutInflater, List<SettingsWifiBean> mWifiBeanList) {
            this.mLayoutInflater = mLayoutInflater;
            this.mWifiBeanList = mWifiBeanList;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View mView = mLayoutInflater.inflate(R.layout.settings_wifi_listitem_layout, viewGroup,false);
            return new MyWifiViewHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
            final MyWifiViewHolder mMyWifiViewHolder = (MyWifiViewHolder) viewHolder;
            SettingsWifiBean mSettingsWifiBean = mWifiBeanList.get(i);
            final ScanResult mScanResult = mSettingsWifiBean.getResult();
            final int status = mSettingsWifiBean.getStatus();

            mMyWifiViewHolder.tvWifiSsid.setText(mScanResult.SSID);
            mWifiSingalLevel=WifiManager.calculateSignalLevel(mScanResult.level,5);
            if(mScanResult.capabilities.contains("WEP")||mScanResult.capabilities.contains("PSK") || mScanResult.capabilities.contains("EAP")){
                mMyWifiViewHolder.ivWifiLevel.setImageResource(R.drawable.settings_wifi_signal_lock);
            }else{
                mMyWifiViewHolder.ivWifiLevel.setImageResource(R.drawable.settings_wifi_signal_open);
            }
            mMyWifiViewHolder.ivWifiLevel.setImageLevel(mWifiSingalLevel);

//            mMyWifiViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mItemClickSSID = mScanWifiListBean.get(i).getResult().SSID;
//                    int wifiItemId = mSettingsWifiAdapter.IsConfiguration("\"" + mScanWifiListBean.get(i).getResult().SSID + "\"");
//                    if(wifiItemId != -1){
//                        if (mSettingsWifiAdapter.ConnectWifi(wifiItemId)) {
//                            // 连接已保存密码的WiFi
//                            mMyWifiViewHolder.ivConnectWifiLoading.setVisibility(View.VISIBLE);
//                            mMyWifiViewHolder.ivConnectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
//                            mAnimationDrawable = (AnimationDrawable) mMyWifiViewHolder.ivConnectWifiLoading.getDrawable();
//                            mAnimationDrawable.start();
//                            mMyWifiViewHolder.ivConnectWifiIcon.setVisibility(View.GONE);
////                            new Thread(new refreshWifiThread()).start();
//                        } else {
//
//                        }
//                    }
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return mWifiBeanList.size();
        }

        private class MyWifiViewHolder extends RecyclerView.ViewHolder {
            public TextView tvWifiSsid;
            public ImageView ivWifiLevel;
            public ImageView ivConnectWifiIcon;
            public ImageView ivConnectWifiLoading;
            public TextView tvWifiStatus;

            public MyWifiViewHolder(View mView) {
                super(mView);
                tvWifiSsid= itemView.findViewById(R.id.wifi_ssid);
                ivWifiLevel= itemView.findViewById(R.id.wifi_level);
                tvWifiStatus = itemView.findViewById(R.id.tv_status);
                ivConnectWifiIcon = itemView.findViewById(R.id.connected_wifi_status_icon);
                ivConnectWifiLoading = itemView.findViewById(R.id.wifi_list_item_connecting);

            }
        }
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(mwifiReceiver);
//        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
