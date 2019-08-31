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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
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
@Route(path = RouterPath.SETTINGS.FDSettingsWifiActivity)
public class FDSettingsWifiActivity extends BaseMvpActivity implements View.OnClickListener {
    private static final String TAG = "FDSettingsWifiActivity";

    Switch wifiSwitch;
    private RecyclerView mlistView;
    protected SettingsWifiUtil mWifiAdmin;
    private List<SettingsWifiBean> mWifiList;
    public int level;
    protected String ssid;
    private TextView setWifiBack;
    private String wifiSSID;
    private MyAdapter mAadapter;
    private int status  = 0;

    private final static int  OPEN_WIFI = 1;
    private final static int  CLOSE_WIFI = 0;

    WifiManager wifiManager;
    String connectedSSID;
    private ImageView mScanWifiGif;
    private TextView ScanWifiTv;
    private AnimationDrawable animationDrawable;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_wifi_layout;
    }

    @Override
    public void initView() {
        ScanWifiTv = findViewById(R.id.scan_wifi_tv);
        mScanWifiGif = findViewById(R.id.scan_wifi_iv);
        mlistView= findViewById(R.id.set_wifi_listview);
        wifiSwitch = findViewById(R.id.wifi_switch);
        setWifiBack = findViewById(R.id.set_wifi_back_iv);


        mlistView.setLayoutManager(new LinearLayoutManager(this));
        mlistView.setItemAnimator(new DefaultItemAnimator());
        mWifiList = new ArrayList<>();
        mAadapter = new MyAdapter(getLayoutInflater(), mWifiList);
        mlistView.setAdapter(mAadapter);
        mWifiAdmin = new SettingsWifiUtil(FDSettingsWifiActivity.this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        setWifiBack.setOnClickListener(this);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        connectedSSID = wifiInfo.getSSID().replace("\"", "");

        if (!wifiManager.isWifiEnabled()) {
            wifiSwitch.setChecked(false);
        } else {
            wifiSwitch.setChecked(true);
        }

        IntentFilter filter = new IntentFilter();
        //监听wifi是开关变化的状态
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //监听wifiwifi连接状态广播
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        //监听wifi列表变化（开启一个热点或者关闭一个热点）
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        registerReceiver(mwifiReceiver, filter);
    }

    @Override
    public void initData() {
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    LogUtil.d(TAG, "wifi switch is on ");
                    mWifiAdmin.openWifi(getApplicationContext());
                    ScanWifiTv.setVisibility(View.VISIBLE);
                    mScanWifiGif.setVisibility(View.VISIBLE);
                    mScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    animationDrawable = (AnimationDrawable) mScanWifiGif.getDrawable();
                    animationDrawable.start();
                    mHandler.post(runnable);
                } else{
                    LogUtil.d(TAG, "wifi switch is off ");
                    mWifiAdmin.closeWifi(FDSettingsWifiActivity.this);
                    ScanWifiTv.setVisibility(View.GONE);
                    mScanWifiGif.setVisibility(View.GONE);
                    mScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    animationDrawable = (AnimationDrawable) mScanWifiGif.getDrawable();
                    animationDrawable.stop();

                    mWifiList.clear();
                    mlistView.setAdapter(null);
                    mHandler.removeCallbacks(runnable);
                }
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LogUtil.d("Runnable");

            if (!wifiManager.isWifiEnabled()) {
                LogUtil.d(TAG, "wifi is unable ~");
            } else {
                mWifiAdmin.startScan(getApplicationContext());
                mWifiList.clear();
                LogUtil.d(TAG, "mWifiList = " + mWifiList.size());
                mWifiList.addAll(mWifiAdmin.getWifiList(2, connectedSSID));
                LogUtil.d(TAG, "mWifiList 2 = " + mWifiList.size());

                if(mWifiList.size() > 0) {
                    ScanWifiTv.setVisibility(View.GONE);
                    mScanWifiGif.setVisibility(View.GONE);
                    mScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    animationDrawable = (AnimationDrawable) mScanWifiGif.getDrawable();
                    animationDrawable.stop();
                    mlistView.setAdapter(mAadapter);
                    mAadapter.notifyDataSetChanged();
                } else {
                    mlistView.setAdapter(null);
                    ScanWifiTv.setVisibility(View.VISIBLE);
                    mScanWifiGif.setVisibility(View.VISIBLE);
                    mScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                    animationDrawable = (AnimationDrawable) mScanWifiGif.getDrawable();
                    animationDrawable.start();
                }
            }
            mHandler.postDelayed(runnable,8000); // 执行后延迟1000毫秒再次执行，count已++
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.d("  Msg " + msg.what);
            switch (msg.what) {
                case OPEN_WIFI :
                    //在这里可以进行UI操作
                    //对msg.obj进行String强制转换

                    break;
                case CLOSE_WIFI:
                    mWifiAdmin.closeWifi(FDSettingsWifiActivity.this);
                    mWifiList.clear();
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.set_wifi_back_iv:
                setResult(100);
                finish();
                break;
        }
    }

    @Override
    public void showErrorTip(String msg) {

    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater inflater;
        List<SettingsWifiBean> list;

        public MyAdapter(LayoutInflater inflater, List<SettingsWifiBean> list) {
            this.inflater = inflater;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.settings_wifi_listitem_layout, parent,false);
            return new ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ViewHolder mHolder = (ViewHolder) holder;
            SettingsWifiBean bean = list.get(position);
            final ScanResult scanResult = bean.getResult();
            final int status = bean.getStatus();

            mHolder.wifi_ssid.setText(scanResult.SSID);
            level=WifiManager.calculateSignalLevel(scanResult.level,5);
            if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                mHolder.wifi_level.setImageResource(R.drawable.settings_wifi_signal_lock);
            }else{
                mHolder.wifi_level.setImageResource(R.drawable.settings_wifi_signal_open);
            }

            if (status == 1){  //wifi正在连接
                LogUtil.d(TAG, "wifi 正在连接 connecting ");

                mHolder.connectWifiLoading.setVisibility(View.VISIBLE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                animationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                animationDrawable.start();
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            } else if (status == 2){  //wifi连接上了
                LogUtil.d(TAG, "wifi 连接上了 connect sucess ");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.tvStatus.setText(getString(R.string.settings_wifi_connected_status));
                mHolder.connectWifiIcon.setVisibility(View.VISIBLE);

                mHolder.connectWifiLoading.setVisibility(View.GONE);
                mHolder.connectWifiLoading.setImageResource(R.drawable.settings_wifi_connecting_gif);
                animationDrawable = (AnimationDrawable) mHolder.connectWifiLoading.getDrawable();
                animationDrawable.stop();

                //wifiSwitch.setClickable(true);
            } else if(status ==3) {
                mHolder.tvStatus.setText(getString(R.string.settings_wifi_psw_error));
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.connectWifiIcon.setVisibility(View.GONE);
                LogUtil.d(TAG, "wifi psw error ");
            } else if(status == 4){
                mHolder.tvStatus.setText("wifi未连接");
                mHolder.tvStatus.setVisibility(View.VISIBLE);
                mHolder.connectWifiIcon.setVisibility(View.GONE);
                LogUtil.d(TAG, "wifi not connect ");
            } else {
                mHolder.tvStatus.setVisibility(View.GONE);
                mHolder.connectWifiIcon.setVisibility(View.GONE);
            }
            mHolder.wifi_level.setImageLevel(level);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WifiConfiguration config = new WifiConfiguration();
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String connectedSSID = wifiInfo.getSSID().replace("\"", "");

                    ssid = mWifiList.get(position).getResult().SSID;

                    LogUtil.d(TAG, "connected wifi ssid = " + connectedSSID);
                    LogUtil.d(TAG, "clicked wifi ssid = " + ssid);

                    List<WifiConfiguration> wifiConfigurationList = mWifiAdmin.getConfiguration();
                    LogUtil.d(TAG, "wifiConfigurationList = " + wifiConfigurationList);

                    if(connectedSSID.equals(ssid)) {// startActivity(new Intent(FDSettingsWifiActivity.this, FDWifiConnectConfigActivity.class));
                        Intent connectedIntent = new Intent(FDSettingsWifiActivity.this, FDSettingsWifiConnectedInfoActivity.class);
                        //2). 通过intent携带额外数据
                        connectedIntent.putExtra("CONNECTED_MESSAGE", ssid);
                        //3). 启动Activity
                        startActivity(connectedIntent);
                        finish();
                    } else {
                        //WiFi是否已经保存
                        LogUtil.d(TAG,  "save wifi flag = " + mWifiAdmin.isWifiSave(ssid));
                        if(mWifiAdmin.isWifiSave(ssid)){
                            //点击连接
                            int ssidID = mWifiAdmin.getNetworkId(ssid);
                            LogUtil.d(TAG,  "save wifi id clicked = " + ssidID);
//                            mWifiAdmin.connectConfiguration(ssidID);
                            wifiManager.enableNetwork(ssidID, true);
                        } else {
                            if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                                //密码连接
                                Intent connectIntent = new Intent(FDSettingsWifiActivity.this, FDSettingsWifiInputPswActivity.class);
                                connectIntent.putExtra("MESSAGE", ssid);
                                startActivity(connectIntent);
                                finish();
                            } else{
                                //无密码直接连接
                                LogUtil.d(TAG, "no psw wifi connect");
                                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView wifi_ssid;
            public ImageView wifi_level;
            public ImageView connectWifiIcon;
            public ImageView connectWifiLoading;
            public TextView tvStatus;

            public ViewHolder(View itemView) {
                super(itemView);
                wifi_ssid= itemView.findViewById(R.id.wifi_ssid);
                wifi_level= itemView.findViewById(R.id.wifi_level);
                tvStatus = itemView.findViewById(R.id.tv_status);
                connectWifiIcon = itemView.findViewById(R.id.connected_wifi_status_icon);
                connectWifiLoading = itemView.findViewById(R.id.wifi_list_item_connecting);

            }
        }
    }

    //监听wifi状态变化
    private BroadcastReceiver mwifiReceiver = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch (state) {
                    /**
                     * WIFI_STATE_DISABLED    WLAN已经关闭
                     * WIFI_STATE_DISABLING   WLAN正在关闭
                     * WIFI_STATE_ENABLED     WLAN已经打开
                     * WIFI_STATE_ENABLING    WLAN正在打开
                     * WIFI_STATE_UNKNOWN     未知
                     */
                    case WifiManager.WIFI_STATE_DISABLED: {
                        Log.i(TAG, "已经关闭");
                        status = 0;
                        mWifiList.clear();
                        mlistView.setAdapter(null);
//                        mAadapter.notifyDataSetChanged();
                        mHandler.removeCallbacks(runnable);
                        SPUtils.remove(FDSettingsWifiActivity.this, "WifiName");
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
                }
            } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                Log.i(TAG, "--NetworkInfo--" + info.toString());
                if (NetworkInfo.State.DISCONNECTED == info.getState()) {//wifi没连接上
                    Log.i(TAG, "wifi没连接上");
                    status = 4;
                } else if (NetworkInfo.State.CONNECTED == info.getState()) {//wifi连接上了
                    status = 2;
                    Log.i(TAG, "wifi连接上了 status = " + status);
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiSSID = wifiManager.getConnectionInfo().getSSID();
                    wifiSSID = wifiSSID.substring(1, wifiSSID.length() - 1);
                    SPUtils.put(FDSettingsWifiActivity.this, "WifiName", wifiSSID);
                    wifiSwitch.setChecked(true);
                } else if (NetworkInfo.State.CONNECTING == info.getState()) {//正在连接
                    status = 1;
                    Log.i(TAG, "wifi正在连接 status = " + status);

                    String extra = info.getExtraInfo();
                    Log.i(TAG, "extra = " + extra);
                    extra = extra.substring(1, extra.length() - 1);
                    mWifiAdmin.startScan(FDSettingsWifiActivity.this);
                    mWifiList.clear();
                    mWifiList.addAll(mWifiAdmin.getWifiList(status, extra));
                    mAadapter.notifyDataSetChanged();
                }
            } else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(intent.getAction())) {
                Log.i(TAG, "网络列表变化了");
                if (!wifiManager.isWifiEnabled()) {

                } else {
                    mWifiAdmin.startScan(getApplicationContext());
                    mWifiList.clear();
                    LogUtil.d(TAG, "mWifiList = " + mWifiList.size());
                    if(status == 2){
                        mWifiList.addAll(mWifiAdmin.getWifiList(2, connectedSSID));
                        LogUtil.d(TAG, "mWifiList status 2 = " + mWifiList.size());
                    } else {
                        mWifiList.addAll(mWifiAdmin.getWifiList(0, null));
                        LogUtil.d(TAG, "mWifiList status 0 = " + mWifiList.size());
                    }

                    if(mWifiList.size() > 0) {
                        ScanWifiTv.setVisibility(View.GONE);
                        mScanWifiGif.setVisibility(View.GONE);
                        mScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                        animationDrawable = (AnimationDrawable) mScanWifiGif.getDrawable();
                        animationDrawable.stop();
                        mlistView.setAdapter(mAadapter);
                        mAadapter.notifyDataSetChanged();
                    } else {
                        mlistView.setAdapter(null);
                        ScanWifiTv.setVisibility(View.VISIBLE);
                        mScanWifiGif.setVisibility(View.VISIBLE);
                        mScanWifiGif.setImageResource(R.drawable.settings_network_loading_gif);
                        animationDrawable = (AnimationDrawable) mScanWifiGif.getDrawable();
                        animationDrawable.start();
                    }
                }
            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int linkWifiResult = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 123);
                if (linkWifiResult == WifiManager.ERROR_AUTHENTICATING) {
                    status = 3;
                    Log.e(TAG, "wifi密码错误");
                    WifiManager wifiManager = (WifiManager) context
                            .getSystemService(Context.WIFI_SERVICE);
                    wifiSSID = wifiManager.getConnectionInfo()
                            .getSSID();
                    wifiSSID = wifiSSID.substring(1, wifiSSID.length() - 1);
                    mWifiAdmin.startScan(FDSettingsWifiActivity.this);
                    mWifiList.clear();
                    mWifiList.addAll(mWifiAdmin.getWifiList(status, wifiSSID));
                    mAadapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mwifiReceiver);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
