package com.fenda.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;
import com.fenda.settings.utils.SettingsWifiUtil;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 18:24
 */
@Route(path = RouterPath.SETTINGS.SettingsWifiInputPswActivity)
public class SettingsWifiInputPswActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsWifiInputPswActivity";

    TextView cancelConnect, sureConnect, connectName;
    EditText wifiPsw;
    String inputWifiPsw;
    String ssid1;
    protected SettingsWifiUtil mWifiAdmin;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_wifi_input_psw_layout;
    }

    @Override
    public void initView() {
        cancelConnect = findViewById(R.id.connect_wifi_cancel);
        sureConnect = findViewById(R.id.connect_wifi_sure);
        connectName = findViewById(R.id.connect_wifi_name);
        wifiPsw = findViewById(R.id.wifi_psw);

        mWifiAdmin = new SettingsWifiUtil(SettingsWifiInputPswActivity.this);
        wifiPsw.addTextChangedListener(textWatcher);

        //4). 得到intent对象
        Intent intent = getIntent();
        //5). 通过intent读取额外数据
        ssid1 = intent.getStringExtra("MESSAGE");
        connectName.setText("请连接" + ssid1);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        cancelConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsWifiInputPswActivity.this, SettingsWifiActivity.class));
                finish();
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }

    private TextWatcher textWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(wifiPsw.length() >= 8){
                sureConnect.setVisibility(View.VISIBLE);
                sureConnect.setClickable(true);
                sureConnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d(TAG, "sure on clicked");
                        inputWifiPsw = wifiPsw.getText().toString();
                        final SharedPreferences preferences=getSharedPreferences("wifi_password", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(ssid1, inputWifiPsw);
                        editor.commit();
                        mWifiAdmin.addNetwork(mWifiAdmin.CreateWifiInfo(ssid1, inputWifiPsw, 3));

                        startActivity(new Intent(SettingsWifiInputPswActivity.this, SettingsWifiActivity.class));
                        finish();
                    }
                });
            }
        }
    };
}
