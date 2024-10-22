package com.fenda.settings.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.MyListView;
import com.fenda.settings.R;
import com.fenda.settings.adapter.SettingsBluetoothAdapter;
import com.fenda.settings.bean.SettingsBluetoothDeviceBean;
import com.fenda.settings.utils.SettingsBluetoothUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 10:24
 */
@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
@Route(path = RouterPath.SETTINGS.SettingsBluetoothActivity)
public class SettingsBluetoothActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsBluetoothActivity";

    private TextView tvBtName;
    private ImageView ivBack;
    private MyListView disBtInfosListView;
    private Switch btSwitch;
    private Button flashBtn;
    private LinearLayout llChangeBtName;

    //本地蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private SettingsBluetoothAdapter mSettingsBluetoothAdapter;
    private IntentFilter mIntentFilter;
    private Set<SettingsBluetoothDeviceBean> mSettingsBluetoothDeviceBean = new HashSet<>();
    private Set<BluetoothDevice> mSetBluetoothDevice;

    private String mConnectedBtName;
    private String mConnectedBtAddress;
    private String mLocalBtName;
    private BluetoothDevice mDisConnectBlueDevice;
    private String mGetConnectedBtName;
    private String mGetConnectedBtName1;

    /**
     * 蓝牙音频传输协议
     */
    private BluetoothA2dp mBluetoothA2dp;
    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGattCallback mBluetoothGattCallback;
    @Autowired
    boolean mOpenBluetooth;
    @Autowired
    boolean mVoiceControl;
    private boolean isBtOpen;

    public SettingsBluetoothActivity() {
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_bt_layout;
    }

    @Override
    public void initView() {
        llChangeBtName = findViewById(R.id.change_bt_name_layout);
        tvBtName = findViewById(R.id.set_bt_dis_bt_name);
        ivBack = findViewById(R.id.set_bt_back_iv);
        disBtInfosListView = findViewById(R.id.set_bt_listview);
        btSwitch = findViewById(R.id.bt_switch);
        flashBtn = findViewById(R.id.set_bt_flash_btn);

        if (mSettingsBluetoothAdapter == null) {
            mSettingsBluetoothAdapter = new SettingsBluetoothAdapter(SettingsBluetoothActivity.this, mSettingsBluetoothDeviceBean);
            disBtInfosListView.setAdapter(mSettingsBluetoothAdapter);
        }

        if (mBluetoothAdapter == null) {
            ToastUtils.show("本地蓝牙不可用!");
            finish();
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // **注册搜索蓝牙receiver*
        mIntentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        mIntentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mIntentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mIntentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        mIntentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mIntentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mIntentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        mIntentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        int btMode = SettingsBluetoothUtil.getBluetoothScanMode();
        registerReceiver(mBroadcastReceiver, mIntentFilter);

        LogUtil.d(TAG, "getBtMode = " + btMode);
    }

    @Override
    public void initData() {
        mLocalBtName = mBluetoothAdapter.getName();
        LogUtil.d(TAG, "mLocalBtName = " + mLocalBtName + " ;mBluetoothAdapter = " + mBluetoothAdapter);
        tvBtName.setText("本机蓝牙名称：" + mLocalBtName);

        isBtOpen = mBluetoothAdapter.isEnabled();
        mGetConnectedBtName1 = (String) SPUtils.get(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, "");

        //判断本机蓝牙是否打开
        if (isBtOpen) {
            btSwitch.setChecked(true);
            flashBtn.setVisibility(View.VISIBLE);
            if(mGetConnectedBtName1 != ""){
                LogUtil.d(TAG, "isBtOpen mGetConnectedBtName1 = " + mGetConnectedBtName1);
                SettingsBluetoothUtil.closeDiscoverableTimeout();
            }
            getBondedDevices();
            mBluetoothAdapter.startDiscovery();
        } else {
            flashBtn.setVisibility(View.GONE);
            btSwitch.setChecked(false);
        }

        mBluetoothGattCallback = new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    LogUtil.d("连接蓝牙服务成功");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    LogUtil.d("断开蓝牙服务");
                }
            }
        };
        if (mOpenBluetooth && mVoiceControl && !isBtOpen) {
            btSwitch.setChecked(true);
        } else if (!mOpenBluetooth && mVoiceControl && isBtOpen){
            btSwitch.setChecked(false);
        }
    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsBluetoothActivity.this, SettingsActivity.class);
                startActivity(mIntent);

                finish();
            }
        });
        llChangeBtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsBluetoothActivity.this, SettingsChangeBtNameAcivity.class);
                mIntent.putExtra("BT_NAME", mLocalBtName);
                startActivity(mIntent);
//                finish();
            }
        });
        flashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 如果正在搜索，就先取消搜索
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                getBondedDevices();
                boolean flashDiscoveryBool = mBluetoothAdapter.startDiscovery();
                LogUtil.d(TAG, "我在搜索 flash bool = " + flashDiscoveryBool);
                mSettingsBluetoothAdapter.notifyDataSetChanged();
            }
        });

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LogUtil.d(TAG, "BT switch checked  " + isChecked);
                    mBluetoothAdapter.enable();

                    int btMode2 = SettingsBluetoothUtil.getBluetoothScanMode();
                    LogUtil.d(TAG, "getBtMode22 = " + btMode2);

//                    if(mGetConnectedBtName1 != ""){
//                        LogUtil.d(TAG, "mGetConnectedBtName1 = " + mGetConnectedBtName1);
//                        SettingsBluetoothUtil.closeDiscoverableTimeout();
//                    }

                } else {
                    LogUtil.d(TAG, "BT switch unchecked  " + isChecked);
                    ToastUtils.show("蓝牙已关闭");
                    mBluetoothAdapter.disable();
                    mSettingsBluetoothAdapter.getListDevices().clear();
                    mSettingsBluetoothAdapter.notifyDataSetChanged();
                }
            }
        });

        disBtInfosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<SettingsBluetoothDeviceBean> listDevices = mSettingsBluetoothAdapter.getListDevices();
                final SettingsBluetoothDeviceBean blueDevice = listDevices.get(i);
                String msg = null;
                // **还没有配对*
                if (blueDevice.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    msg = "是否与设备" + blueDevice.getName() + "配对并连接？";
                } else {
                    msg = "是否与设备" + blueDevice.getName() + "连接？";
                }
                if (("已连接").equals(blueDevice.getStatus())) {
                    return;
                }
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                showDailog(msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // **当前需要配对的蓝牙设备*/
                        mBluetoothDevice = blueDevice.getDevice();
                        LogUtil.d(TAG, "onItemClicked = " + blueDevice.getDevice());

                        String mConnectDeviceName = (String) SPUtils.get(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, "");
                        String mConnectDeviceAddress = (String) SPUtils.get(getApplicationContext(), Constant.Settings.BT_CONNECTED_ADDRESS, "");

                        LogUtil.d(TAG, "mConnectDeviceName = " + mConnectDeviceName);
                        LogUtil.d(TAG, "mConnectDeviceAddress = " + mConnectDeviceAddress);


                        if(!mConnectDeviceName.equals("")){
                            ToastUtils.show("请先断开当前连接，再重试！");
                            return;
//                            BluetoothDevice blue111 = getConnectedBtMac();
//                            LogUtil.d(TAG, "blue111 = " + blue111);
//                            LogUtil.d(TAG, "blue111 getAddress = " + blue111.getAddress());
//                            SettingsBluetoothUtil.unpairDevice(blue111);
                        }
                        // **还没有配对*/
                        if (blueDevice.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                            blueDevice.getDevice().createBond();
                        } else {
                            //**完成配对的,直接连接*/
                            contectDevices(blueDevice);
                        }
                    }
                });
            }
        });

       //**长按取消配对*/
        disBtInfosListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<SettingsBluetoothDeviceBean> listDevices = mSettingsBluetoothAdapter.getListDevices();
                final SettingsBluetoothDeviceBean blueDevices = listDevices.get(i);

                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                //**还没有配对*/
                if (blueDevices.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    return true;
                    //**完成配对的*/
                } else {
                    showDailog("是否取消" + blueDevices.getName() + "配对？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            LogUtil.d(TAG, "setOnItemLongClickListener = " + blueDevices);
                            LogUtil.d(TAG, "setOnItemLongClickListener = " + blueDevices.getDevice());
                            SettingsBluetoothUtil.unpairDevice(blueDevices.getDevice());
                        }
                    });
                }
                return true;
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }

    private BluetoothDevice getConnectedBtMac() {
        if (!mSettingsBluetoothDeviceBean.isEmpty()) {
            mSettingsBluetoothDeviceBean.clear();
        }
        mSettingsBluetoothAdapter.getListDevices().clear();
        mSetBluetoothDevice = mBluetoothAdapter.getBondedDevices();
        BluetoothDevice mGetConnectedBtMac = null;
        LogUtil.d(TAG, "FD---bonded device size = " + mSetBluetoothDevice.size());

        for (BluetoothDevice bluetoothDevice : mSetBluetoothDevice) {
            SettingsBluetoothDeviceBean blueDevice = new SettingsBluetoothDeviceBean();
            blueDevice.setName(bluetoothDevice.getName());
            blueDevice.setAddress(bluetoothDevice.getAddress());
            blueDevice.setDevice(bluetoothDevice);

            try {
                Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                boolean isConnected = false;
                try {
                    isConnected = (boolean) isConnectedMethod.invoke(bluetoothDevice, (Object[]) null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                if (isConnected) {
                    LogUtil.d(TAG, "isConnectedMethod  = " + bluetoothDevice.getAddress());
                    mGetConnectedBtMac = bluetoothDevice;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return mGetConnectedBtMac;
    }

    /**
     * 获取所有已经绑定的蓝牙设备并显示
     */
    private void getBondedDevices() {
        if (!mSettingsBluetoothDeviceBean.isEmpty()) {
            mSettingsBluetoothDeviceBean.clear();
        }
        mSettingsBluetoothAdapter.getListDevices().clear();
        mSetBluetoothDevice = mBluetoothAdapter.getBondedDevices();
        mGetConnectedBtName = (String) SPUtils.get(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, "");
        LogUtil.d(TAG, "FD---bonded device size = " + mSetBluetoothDevice.size());

        for (BluetoothDevice bluetoothDevice : mSetBluetoothDevice) {
            SettingsBluetoothDeviceBean blueDevice = new SettingsBluetoothDeviceBean();
            blueDevice.setName(bluetoothDevice.getName());
            blueDevice.setAddress(bluetoothDevice.getAddress());
            blueDevice.setDevice(bluetoothDevice);

            try {
                Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                boolean isConnected = false;
                try {
                    isConnected = (boolean) isConnectedMethod.invoke(bluetoothDevice, (Object[]) null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                if(isConnected){
                    LogUtil.d(TAG, "isConnectedMethod  = "+bluetoothDevice.getAddress());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            LogUtil.d(TAG, "FD---bonded name = " + bluetoothDevice.getName());
            LogUtil.d(TAG, "FD--- mGetConnectedBTName = " + mGetConnectedBtName);

            if(mGetConnectedBtName.equals("")){
                LogUtil.d(TAG, "mGetConnectedBTName is null");
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    blueDevice.setStatus("已配对");
                }
                else if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    blueDevice.setStatus("正在配对...");
                } else if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                } else {
                    blueDevice.setStatus("");
                }
            } else {
                LogUtil.d(TAG, "mGetConnectedBTName is not null");
//                SettingsBluetoothUtil.closeDiscoverableTimeout();

                if(mGetConnectedBtName.equals(bluetoothDevice.getName())){
                    blueDevice.setStatus("已连接");
                    SettingsBluetoothUtil.closeDiscoverableTimeout();
                }
            }
            mSettingsBluetoothAdapter.getListDevices().add(blueDevice);
        }

        if (mSettingsBluetoothAdapter == null) {
            mSettingsBluetoothAdapter = new SettingsBluetoothAdapter(this, mSettingsBluetoothDeviceBean);
            disBtInfosListView.setAdapter(mSettingsBluetoothAdapter);
        } else {
            //blueAdapter.setSetDevices(setDevices);
            mSettingsBluetoothAdapter.notifyDataSetChanged();
        }
    }

    private void changeBtItemStatus(String name, String addr, String status) {
        int index = 0;
        if(0 != mSettingsBluetoothAdapter.getListDevices().size()) {
            for (int i = 0; i < mSettingsBluetoothAdapter.getListDevices().size(); i++) {
                String itemName = mSettingsBluetoothAdapter.getListDevices().get(i).getName();
                String itemAddr = mSettingsBluetoothAdapter.getListDevices().get(i).getAddress();
                if (name != null) {
                    if (name.equals(itemName)) {
                        mSettingsBluetoothAdapter.getListDevices().get(i).setStatus(status);
                        index = i;
                        break;
                    }
                } else if (addr != null) {
                    if (addr.equals(itemAddr)) {
                        mSettingsBluetoothAdapter.getListDevices().get(i).setStatus(status);
                        index = i;
                        break;
                    }
                }
            }
        }
        SettingsBluetoothDeviceBean bd = mSettingsBluetoothAdapter.getListDevices().get(index);
        mSettingsBluetoothAdapter.getListDevices().remove(index);
        mSettingsBluetoothAdapter.getListDevices().add(0, bd);
        mSettingsBluetoothAdapter.notifyDataSetChanged();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //** 搜索到的蓝牙设备*/
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经配对的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    SettingsBluetoothDeviceBean blueDevice = new SettingsBluetoothDeviceBean();
                    blueDevice.setName(device.getName() == null ? device.getAddress() : device.getName());
                    blueDevice.setAddress(device.getAddress());
                    blueDevice.setDevice(device);
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        blueDevice.setStatus("已配对");
                    } else if (device.getBondState() == BluetoothDevice.BOND_BONDING) {
                        blueDevice.setStatus("正在配对...");
                    } else if (device.getBondState() == BluetoothDevice.BOND_NONE) {

                    } else {
                        blueDevice.setStatus("");
                    }
                    mSettingsBluetoothAdapter.getListDevices().add(blueDevice);
                    mSettingsBluetoothAdapter.notifyDataSetChanged();
                }
                //**当绑定的状态改变时*/
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                List<SettingsBluetoothDeviceBean> listDevices = mSettingsBluetoothAdapter.getListDevices();
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        LogUtil.d(TAG, "正在配对......");
                        String btName = (String) SPUtils.get(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, "");
                        if(btName == ""){
                            changeBtItemStatus(device.getName(), device.getAddress(), "正在配对...");
                        } else {
                            ToastUtils.show("请先断开当前连接~");
                            return;
                        }
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        LogUtil.d(TAG, "完成配对");
                        changeBtItemStatus(device.getName(), device.getAddress(), "已连接");
                        SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, device.getName());
                        SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_ADDRESS, device.getAddress());
                        break;
                    case BluetoothDevice.BOND_NONE:
                        LogUtil.d(TAG, "取消配对");
                        ToastUtils.show("成功取消配对");
                        LogUtil.d(TAG, "BOND_NONE = " + device.getName() + " +  " + device.getAddress());
                        changeBtItemStatus(device.getName(), device.getAddress(), "");
                        SPUtils.remove(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME);
                        SPUtils.remove(getApplicationContext(), Constant.Settings.BT_CONNECTED_ADDRESS);
                        break;
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                LogUtil.d(TAG, "搜索完成......");
                hideProgressDailog();
            } else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
                LogUtil.d(TAG, "ACTION_SCAN_MODE_CHANGED = " + action);
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                LogUtil.d(TAG, "BT status is changed~");
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        LogUtil.d(TAG, "TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        LogUtil.d(TAG, "STATE_ON");
                        btSwitch.setChecked(true);
                        flashBtn.setVisibility(View.VISIBLE);

                        btSwitch.setEnabled(true);
                        if(mGetConnectedBtName1 != ""){
                            LogUtil.d(TAG, "STATE_ON mGetConnectedBtName1 = " + mGetConnectedBtName1);
                            SettingsBluetoothUtil.closeDiscoverableTimeout();
                        }

                        getBondedDevices();
                        mBluetoothAdapter.startDiscovery();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        LogUtil.d(TAG, "STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        btSwitch.setChecked(false);
                        flashBtn.setVisibility(View.GONE);
                        btSwitch.setEnabled(true);
                        LogUtil.d(TAG, "STATE_OFF");
                        break;
                    default:
                        break;
                }
            } else if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                BluetoothDevice mConnectionBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
                LogUtil.d(TAG, "BT CONNECT staute = " + mConnectionBluetoothDevice.getName() + ", state =" + state);
                if (BluetoothAdapter.STATE_DISCONNECTED == state) {
                    LogUtil.d(TAG, "蓝牙断开了");
                    changeBtItemStatus(mConnectionBluetoothDevice.getName(), mConnectionBluetoothDevice.getAddress(), "");
                    SPUtils.remove(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME);
                    SPUtils.remove(getApplicationContext(), String.valueOf(Constant.Settings.BT_CONNECTED_ADDRESS));
                    LogUtil.d(TAG, "STATE_DISCONNECTED getName = " + mConnectionBluetoothDevice.getName() + ", STATE_DISCONNECTED getAddress = " + mConnectionBluetoothDevice.getAddress());
                } else if (BluetoothAdapter.STATE_CONNECTED == state) {
                    LogUtil.d(TAG, "蓝牙连上了");
                    changeBtItemStatus(mConnectionBluetoothDevice.getName(), mConnectionBluetoothDevice.getAddress(), "已连接");
                    mConnectedBtName = mConnectionBluetoothDevice.getName();
                    mConnectedBtAddress = mConnectionBluetoothDevice.getAddress();
                    SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_NAME, mConnectedBtName);
                    SPUtils.put(getApplicationContext(), Constant.Settings.BT_CONNECTED_ADDRESS, mConnectedBtAddress);
                    LogUtil.d(TAG, "STATE_CONNECTED getName = " + mConnectionBluetoothDevice.getName() + ", STATE_CONNECTED getAddress = " + mConnectionBluetoothDevice.getAddress());
                    SettingsBluetoothUtil.closeDiscoverableTimeout();
                }
            }
        }
    };

    //开始配对蓝牙设备
    private void startPariBlue(SettingsBluetoothDeviceBean blueDevice) {
        SettingsBluetoothUtil blueUtils = new SettingsBluetoothUtil(blueDevice);
        blueUtils.createBond(blueDevice.getDevice());
        dismissDailog();
    }

     //开始连接蓝牙设备
    private void contectDevices(SettingsBluetoothDeviceBean blueDevice) {
        SettingsBluetoothUtil blueUtils = new SettingsBluetoothUtil(blueDevice);
        blueUtils.doPair();
        dismissDailog();
    }
    //连接蓝牙设备（通过监听蓝牙协议的服务，在连接服务的时候使用BluetoothA2dp协议）
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceDisconnected(int profile) {

        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {

            if (profile == BluetoothProfile.HEADSET) {
                LogUtil.d(TAG, "开始连接蓝牙 HEADSET");
            } else if (profile == BluetoothProfile.A2DP) {
                LogUtil.d(TAG, "开始连接蓝牙 A2DP");
                //**ServiceListener（使用了反射技术调用连接的方法）*/
                mBluetoothA2dp = (BluetoothA2dp) proxy;
                if (mBluetoothA2dp.getConnectionState(mBluetoothDevice) != BluetoothProfile.STATE_CONNECTED) {
                    try {
                        mBluetoothA2dp.getClass()
                                .getMethod("connect", BluetoothDevice.class)
                                .invoke(mBluetoothA2dp, mBluetoothDevice);
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

    public void showDailog(String msg, DialogInterface.OnClickListener listeners) {
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(SettingsBluetoothActivity.this).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", listeners);
        alertDialog.show();
    }

    public void dismissDailog() {
        final AlertDialog alertDialog = null;
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        }
    }

    public void hideProgressDailog() {
        ProgressDialog progressDialog = null;
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}


