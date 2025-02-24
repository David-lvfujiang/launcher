package com.fenda.homepage.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SettingsWifiUtil;
import com.fenda.homepage.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/19 18:02
 */
public class HomepageWifiInputPswActivity extends BaseMvpActivity {
    private static final String TAG = "HomepageWifiInputPswActivity";

    private TextView tvCancelbtn;
    private TextView tvSurebtn;
    private EditText etPsw;

    protected SettingsWifiUtil mSettingsWifiUtil;

    private String mInputWifiPsw;
    private String mConnectSsid;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.homepage_wifi_input_psw_layout;
    }

    @Override
    public void initView() {
        TextView tvConnectName;

        tvCancelbtn = findViewById(R.id.connect_wifi_cancel);
        tvSurebtn = findViewById(R.id.connect_wifi_sure);
        tvConnectName = findViewById(R.id.connect_wifi_name);
        etPsw = findViewById(R.id.wifi_psw);

        mSettingsWifiUtil = new SettingsWifiUtil(HomepageWifiInputPswActivity.this);
        etPsw.addTextChangedListener(textWatcher);

        Intent mIntent = getIntent();
        mConnectSsid = mIntent.getStringExtra("MESSAGE");
        tvConnectName.setText("请输入密码连接 " + mConnectSsid);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        tvCancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageWifiInputPswActivity.this, StartWifiConfigureActivity.class));
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
            int mPswMinLength = 8;
            int mPswMaxLength = 64;

            if(etPsw.length() >= mPswMinLength && etPsw.length() < mPswMaxLength){
                tvSurebtn.setVisibility(View.VISIBLE);
                tvSurebtn.setClickable(true);
                tvSurebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.d(TAG, "sure on clicked");
                        mInputWifiPsw = etPsw.getText().toString();
                        final SharedPreferences preferences=getSharedPreferences("wifi_password", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(mConnectSsid, mInputWifiPsw);
                        editor.commit();
                        mSettingsWifiUtil.addNetwork(mSettingsWifiUtil.createWifiInfo(mConnectSsid, mInputWifiPsw, 3));

                        Intent mIntent = new Intent(HomepageWifiInputPswActivity.this, StartWifiConfigureActivity.class);
                        mIntent.putExtra("SURE_CONNECT_SSID", mConnectSsid);
                        startActivity(mIntent);
                        finish();
                    }
                });
            } else {
                tvSurebtn.setVisibility(View.GONE);
                tvSurebtn.setClickable(false);
            }
        }
    };

}
