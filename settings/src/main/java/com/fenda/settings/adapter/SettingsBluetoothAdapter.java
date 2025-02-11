package com.fenda.settings.adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fenda.settings.R;
import com.fenda.settings.bean.SettingsBluetoothDeviceBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 10:34
 */
public class SettingsBluetoothAdapter extends BaseAdapter {
    private Context context;
    private List<SettingsBluetoothDeviceBean> listDevices = new ArrayList<>();

    public SettingsBluetoothAdapter(Context context, Set<SettingsBluetoothDeviceBean> setDevices) {
        this.context = context;
        if (setDevices != null) {
            for (SettingsBluetoothDeviceBean blueDevice : setDevices) {
                listDevices.add(blueDevice);
            }
        }
    }

    public Set<SettingsBluetoothDeviceBean> getSetDevices() {
        Set<SettingsBluetoothDeviceBean> setDevices = new HashSet<>();
        if (listDevices != null) {
            for (SettingsBluetoothDeviceBean blueDevice : listDevices) {
                setDevices.add(blueDevice);
            }
        }
        return setDevices;
    }

    public List<SettingsBluetoothDeviceBean> getListDevices() {
        return listDevices;
    }

    public void setSetDevices(Set<SettingsBluetoothDeviceBean> setDevices) {
        listDevices.clear();
        for (SettingsBluetoothDeviceBean blueDevice : setDevices) {
            listDevices.add(blueDevice);
        }
    }

    @Override
    public int getCount() {
        return listDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.settings_bt_listitem_layout, viewGroup, false);
            viewHolder.blueName =  convertView.findViewById(R.id.tv_blueName);
            viewHolder.blueState = convertView.findViewById(R.id.tv_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SettingsBluetoothDeviceBean blueDevice = listDevices.get(i);
        if (blueDevice != null) {
            viewHolder.blueName.setText(blueDevice.getName());
            viewHolder.blueState.setText(blueDevice.getStatus());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView blueName;
        TextView blueState;
    }

    public void closeDiscoverableTimeout() {
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

    public void setDiscoverableTimeout(int timeout) {
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
