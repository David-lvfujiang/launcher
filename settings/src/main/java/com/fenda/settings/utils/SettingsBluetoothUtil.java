package com.fenda.settings.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.fenda.common.util.LogUtil;
import com.fenda.settings.bean.SettingsBluetoothDeviceBean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 10:43
 */
public class SettingsBluetoothUtil {
    private static final String TAG = "SettingsBluetoothUtil";

    private final SettingsBluetoothDeviceBean blueDevice;
    private Handler mOthHandler;
    private BluetoothSocket blueSocket;       //蓝牙连接socket
    //UUID: Audio Source (0000110a-0000-1000-8000-00805f9b34fb)
    //UUID: Audio Sink (0000110b-0000-1000-8000-00805f9b34fb)
    //UUID: Headset AG (00001112-0000-1000-8000-00805f9b34fb)
    // private static final UUID MY_UUID_SECURE = UUID.fromString("0000110a-0000-1000-8000-00805f9b34fb");
    private static final UUID MY_UUID_SECURE = UUID.fromString("00001112-0000-1000-8000-00805f9b34fb");

    public SettingsBluetoothUtil(SettingsBluetoothDeviceBean blueDevice) {
        this.blueDevice = blueDevice;
    }

    /**
     * 配对指定的蓝牙设备
     * @param btDevice
     * @return
     */
    public  boolean createBond(BluetoothDevice btDevice){
        boolean result = false;
        try{
            Method m = btDevice.getClass().getDeclaredMethod("createBond",new Class[]{});
            m.setAccessible(true);
            Boolean originalResult = (Boolean) m.invoke(btDevice);
            result = originalResult.booleanValue();
        } catch(Exception ex){
        }
        return result;
    }

    /**
     * 配对
     */
    public void doPair() {
        if(null == mOthHandler){
            HandlerThread handlerThread = new HandlerThread("other_thread");
            handlerThread.start();
            mOthHandler = new Handler(handlerThread.getLooper());
        }
        mOthHandler.post(new Runnable() {
            @Override
            public void run() {
                initSocket();   //取得socket
                try {
                    if(blueSocket!=null) {
                        blueSocket.connect();
                    }
                } catch (IOException e) {
                    try {
                        blueSocket.close();
                    } catch (IOException e2) {
                    }
                }
            }
        });
    }

    /**
     * 取消蓝牙配对
     * @param device
     */
    public static void unpairDevice(BluetoothDevice device) {
        try {
            LogUtil.d(TAG, "unpairDevice = " + device.getClass());
            LogUtil.d(TAG, "unpairDevice 2 = " + device.getClass().getMethod("removeBond", (Class[]) null));
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.d("BlueUtils", e.getMessage());
        }
    }

    /**
     * 取得BluetoothSocket
     */
    public void initSocket() {
        BluetoothSocket temp = null;
        try {
            temp = blueDevice.getDevice().createInsecureRfcommSocketToServiceRecord(MY_UUID_SECURE);
        }catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        blueSocket = temp;
    }

    public static void closeDiscoverableTimeout() {
        LogUtil.d(TAG, "closeDiscoverableTimeout");
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode =BluetoothAdapter.class.getMethod("setScanMode", int.class,int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, 1);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setDiscoverableTimeout(int timeout) {
        BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode =BluetoothAdapter.class.getMethod("setScanMode", int.class,int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, timeout);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,timeout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
