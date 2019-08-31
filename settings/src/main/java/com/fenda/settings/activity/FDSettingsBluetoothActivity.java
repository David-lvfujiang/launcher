package com.fenda.settings.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.MyListView;
import com.fenda.settings.R;
import com.fenda.settings.adapter.SettingsBluetoothAdapter;
import com.fenda.settings.bean.SettingsBluetoothDeviceBean;
import com.fenda.settings.utils.SettingsBluetoothUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 10:24
 */
@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
@Route(path = RouterPath.SETTINGS.FDSettingsBluetoothActivity)
public class FDSettingsBluetoothActivity extends BaseMvpActivity implements View.OnClickListener {
    private static final String TAG = "FDSettingsBluetoothActivity";

    TextView disBtName;
    ImageView btBack;
    MyListView disBtInfosListView;
    Switch btSwitch;
    Button btFlashBtn;
    LinearLayout btOpening;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    //本地蓝牙适配器
    private SettingsBluetoothAdapter blueAdapter;
    private IntentFilter mFilter;
    private Set<SettingsBluetoothDeviceBean> setDevices = new HashSet<>();
    private Set<BluetoothDevice> devices;

    /**
     * 蓝牙音频传输协议
     */
    private BluetoothA2dp a2dp;
    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice currentBluetoothDevice;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_bt_layout;
    }

    @Override
    public void initView() {
        disBtName = findViewById(R.id.set_bt_dis_bt_name);
        btBack = findViewById(R.id.set_bt_back_iv);
        disBtInfosListView = findViewById(R.id.set_bt_listview);
        btSwitch = findViewById(R.id.bt_switch);
        btFlashBtn = findViewById(R.id.set_bt_flash_btn);
        btOpening = findViewById(R.id.bt_opening_layout);

        if(bluetoothAdapter == null){
            ToastUtils.show("本地蓝牙不可用!");
            finish();   //退出应用
        }
        String NameBt = bluetoothAdapter.getName();   //获取本机蓝牙名称
        disBtName.setText("本机蓝牙名称：" + NameBt);
        LogUtil.d(TAG, "bluetooth name  = " + NameBt);

        boolean isBtOpen = bluetoothAdapter.isEnabled();
        LogUtil.d(TAG, "BT status-------  " + isBtOpen);
        //判断本机蓝牙是否打开
        if (bluetoothAdapter.isEnabled()) {
            btSwitch.setChecked(true);
        } else {
            btSwitch.setChecked(false);
        }
        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    LogUtil.d(TAG, "BT switch checked  " + isChecked);
                    bluetoothAdapter.enable();
//
//                    MediaPlayer player  =   new MediaPlayer();
//
//                    String  path   =  "/sdcard/Music/test.wav";
//
//                    try {
//                        player.setDataSource(path);
//                        player.prepare();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    player.start();
                    getBondedDevices();

                    // 如果正在搜索，就先取消搜索
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    LogUtil.d(TAG, "我在搜索");
                    // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
                    boolean discoveryBool = bluetoothAdapter.startDiscovery();
                    LogUtil.d(TAG, "我在搜索 bool = " + discoveryBool);
                    disBtInfosListView.setAdapter(blueAdapter);
                }
                else
                {
                    LogUtil.d(TAG, "BT switch unchecked  " + isChecked);
                    ToastUtils.show("蓝牙已关闭");
                    bluetoothAdapter.disable();
                    disBtInfosListView.setAdapter(null);
                }
            }
        });

        btBack.setOnClickListener(this);
        btFlashBtn.setOnClickListener(this);

        disBtInfosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<SettingsBluetoothDeviceBean> listDevices = blueAdapter.getListDevices();
                final SettingsBluetoothDeviceBean blueDevice = listDevices.get(i);
                String msg = "";
                /**还没有配对*/
                if (blueDevice.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    msg = "是否与设备" + blueDevice.getName() + "配对并连接？";
                } else {
                    msg = "是否与设备" + blueDevice.getName() + "连接？";
                }
                showDailog(msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**当前需要配对的蓝牙设备*/
                        currentBluetoothDevice = blueDevice.getDevice();
                        /**还没有配对*/
                        if (blueDevice.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                            //baseActivity.showProgressDailog();
                            startPariBlue(blueDevice);
                        } else {
                            /**完成配对的,直接连接*/
                            contectBuleDevices();
                        }
                    }
                });
            }
        });

        /**长按取消配对*/
        disBtInfosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<SettingsBluetoothDeviceBean> listDevices = blueAdapter.getListDevices();
                final SettingsBluetoothDeviceBean blueDevices = listDevices.get(i);
                /**还没有配对*/
                if (blueDevices.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    return false;
                    /**完成配对的*/
                } else {
                    showDailog("是否取消" + blueDevices.getName() + "配对？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SettingsBluetoothUtil.unpairDevice(blueDevices.getDevice());
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public void initData() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /**注册搜索蓝牙receiver*/
        mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //绑定状态监听
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //搜索完成时监听
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);


        registerReceiver(mReceiver, mFilter);
        getBondedDevices();
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.set_bt_back_iv:
                finish();
                break;
            case R.id.set_bt_flash_btn:
                // 如果正在搜索，就先取消搜索
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                LogUtil.d(TAG,"我在搜索");
                boolean flashDiscoveryBool = bluetoothAdapter.startDiscovery();

                LogUtil.d(TAG, "我在搜索 flash bool = " + flashDiscoveryBool);
                blueAdapter.notifyDataSetChanged();
                break;
        }
    }
    /**
     * 获取所有已经绑定的蓝牙设备并显示
     */
    private void getBondedDevices() {
        if (!setDevices.isEmpty()) {
            setDevices.clear();
        }
        devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : devices) {
            SettingsBluetoothDeviceBean blueDevice = new SettingsBluetoothDeviceBean();
            blueDevice.setName(bluetoothDevice.getName());
            blueDevice.setAddress(bluetoothDevice.getAddress());
            blueDevice.setDevice(bluetoothDevice);
            blueDevice.setStatus("已配对");
            setDevices.add(blueDevice);
        }
        if (blueAdapter == null) {
            blueAdapter = new SettingsBluetoothAdapter(FDSettingsBluetoothActivity.this, setDevices);
            disBtInfosListView.setAdapter(blueAdapter);
        } else {
            blueAdapter.setSetDevices(setDevices);
            blueAdapter.notifyDataSetChanged();
        }
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            /** 搜索到的蓝牙设备*/
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经配对的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    SettingsBluetoothDeviceBean blueDevice = new SettingsBluetoothDeviceBean();
                    blueDevice.setName(device.getName()==null?device.getAddress():device.getName());
                    blueDevice.setAddress(device.getAddress());
                    blueDevice.setDevice(device);
                    setDevices.add(blueDevice);
                    blueAdapter.setSetDevices(setDevices);
                    blueAdapter.notifyDataSetChanged();
                    LogUtil.d(TAG, "搜索结果......" + device.getName() + device.getAddress());
                }
                /**当绑定的状态改变时*/
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        LogUtil.d(TAG, "正在配对......");
                        blueAdapter.notifyDataSetChanged();
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        LogUtil.d(TAG, "完成配对");
                        hideProgressDailog();
                        blueAdapter.notifyDataSetChanged();
                        /**开始连接*/
                        bluetoothAdapter.cancelDiscovery();
                        contectBuleDevices();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        LogUtil.d(TAG,"取消配对");
                        ToastUtils.show("成功取消配对");
                        getBondedDevices();
                        break;
                    default:
                        break;
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
                LogUtil.d(TAG, "搜索完成......");
                hideProgressDailog();
            } else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                LogUtil.d(TAG, "BT status is changed~");
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        LogUtil.d(TAG, "TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        btSwitch.setChecked(true);
                        LogUtil.d(TAG, "STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        LogUtil.d(TAG, "STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        btSwitch.setChecked(false);
                        LogUtil.d(TAG, "STATE_OFF");
                        break;
                }
            }
        }
    };

    /**
     * 开始配对蓝牙设备
     *
     * @param blueDevice
     */
    private void startPariBlue(SettingsBluetoothDeviceBean   blueDevice) {
        SettingsBluetoothUtil blueUtils = new SettingsBluetoothUtil(blueDevice);
        blueUtils.doPair();
        hideDailog();
    }

    /**
     * 开始连接蓝牙设备
     */
    private void contectBuleDevices() {
        LogUtil.d(TAG,  "开始连接蓝牙");
        /**使用A2DP协议连接设备*/
        bluetoothAdapter.getProfileProxy(FDSettingsBluetoothActivity.this, mProfileServiceListener, BluetoothProfile.A2DP);
    }

    /**
     * 连接蓝牙设备（通过监听蓝牙协议的服务，在连接服务的时候使用BluetoothA2dp协议）
     */
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceDisconnected(int profile) {

        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {

            if (profile == BluetoothProfile.HEADSET) {
                LogUtil.d(TAG,  "开始连接蓝牙 HEADSET");
//                    bh = (BluetoothHeadset) proxy;
//                    if (bh.getConnectionState(mTouchObject.bluetoothDevice) != BluetoothProfile.STATE_CONNECTED){
//                        bh.getClass()
//                                .getMethod("connect", BluetoothDevice.class)
//                                .invoke(bh, mTouchObject.bluetoothDevice);
                // }

            } else if (profile == BluetoothProfile.A2DP) {
               LogUtil.d(TAG,  "开始连接蓝牙 A2DP");
                /**ServiceListener（使用了反射技术调用连接的方法）*/
                a2dp = (BluetoothA2dp) proxy;
                if (a2dp.getConnectionState(currentBluetoothDevice) != BluetoothProfile.STATE_CONNECTED) {
                    try {
                        a2dp.getClass()
                                .getMethod("connect", BluetoothDevice.class)
                                .invoke(a2dp, currentBluetoothDevice);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    getBondedDevices();
                }
            }

        }
    };
    public void showDailog(String msg, DialogInterface.OnClickListener listeners)
    {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(FDSettingsBluetoothActivity.this).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"确认",listeners);
        alertDialog.show();
    }

    public void hideDailog(){
        final AlertDialog alertDialog = null;
        if(alertDialog!=null){
            if(alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }
    }

    public void hideProgressDailog(){
        ProgressDialog progressDialog = null;
        if(progressDialog!=null){
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


}
