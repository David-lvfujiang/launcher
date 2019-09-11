package com.fenda.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fenda.settings.R;
import com.fenda.settings.bean.SettingsBluetoothDeviceBean;

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
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
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


}
