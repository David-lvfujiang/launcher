package com.fenda.settings.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

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
//			temp = mTouchObject.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));
//            以上取得socket方法不能正常使用， 用以下方法代替
            //Method m = blueDevice.getDevice().getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            //temp = (BluetoothSocket) m.invoke(blueDevice.getDevice(), 1);
            //怪异错误： 直接赋值给socket,对socket操作可能出现异常，  要通过中间变量temp赋值给socket
        }catch (SecurityException e) {
            e.printStackTrace();
            // } catch (NoSuchMethodException e) {
            //    e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //} catch (IllegalAccessException e) {
            //    e.printStackTrace();
            // } catch (InvocationTargetException e) {
            //    e.printStackTrace();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        blueSocket = temp;



//
//        BluetoothSocket temp = null;
//        try {
////            temp = blueDevice.getDevice().createRfcommSocketToServiceRecord(
////                    MY_UUID_SECURE);
////			temp = mTouchObject.bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(CONNECT_UUID));
////            以上取得socket方法不能正常使用， 用以下方法代替
//            Method m = blueDevice.getDevice().getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//            temp = (BluetoothSocket) m.invoke(blueDevice.getDevice(), 1);
//            //怪异错误： 直接赋值给socket,对socket操作可能出现异常，  要通过中间变量temp赋值给socket
//        }catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        blueSocket = temp;
    }
}
