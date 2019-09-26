package com.fenda.settings.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.settings.R;
import com.fenda.settings.bean.SettingsBluetoothDeviceBean;
import com.fenda.settings.bean.SettingsWifiBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/6 11:46
 */
public class SettingsWifiAdapter extends BaseAdapter {
    private Context context;
    private List<SettingsWifiBean> listDevices = new ArrayList<>();

    private SettingsWifiBean mSettingsWifiBean;
    public int level;



    public SettingsWifiAdapter(Context context, Set<SettingsWifiBean> setDevices) {
        this.context = context;
        if (setDevices != null) {
            for (SettingsWifiBean wifiDevice : setDevices) {
                listDevices.add(wifiDevice);
            }
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        final ScanResult scanResult = mSettingsWifiBean.getResult();

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.settings_wifi_listitem_layout, viewGroup, false);
            viewHolder.wifiSsid= view.findViewById(R.id.wifi_ssid);
            viewHolder.wifiLevel= view.findViewById(R.id.wifi_level);
            viewHolder.tvStatus = view.findViewById(R.id.tv_status);
            viewHolder.connectWifiIcon = view.findViewById(R.id.connected_wifi_status_icon);
            viewHolder.connectWifiLoading = view.findViewById(R.id.wifi_list_item_connecting);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SettingsWifiBean wifiDevice = listDevices.get(i);
        if (wifiDevice != null) {
            viewHolder.wifiSsid.setText(scanResult.SSID);
            level = WifiManager.calculateSignalLevel(scanResult.level,5);
            if(scanResult.capabilities.contains("WEP")||scanResult.capabilities.contains("PSK") || scanResult.capabilities.contains("EAP")){
                viewHolder.wifiLevel.setImageResource(R.drawable.settings_wifi_signal_lock);
            }else{
                viewHolder.wifiLevel.setImageResource(R.drawable.settings_wifi_signal_open);
            }
            viewHolder.wifiLevel.setImageLevel(level);

        }
        return view;
    }

    static class ViewHolder {
        public TextView wifiSsid;
        public ImageView wifiLevel;
        public ImageView connectWifiIcon;
        public ImageView connectWifiLoading;
        public TextView tvStatus;
    }
}
