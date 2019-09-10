package com.fenda.settings.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/10 15:30
 */
public class SettingsChangeBtNameAcivity extends BaseMvpActivity {
    private static final String TAG = "SettingsChangeBtNameAcivity";

    private TextView tvSureBtn;
    private TextView tvCancelBtn;
    private EditText etBtName;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private String mIntentBtName;
    private String mChangedBtName;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_change_bt_name_layout;
    }

    @Override
    public void initView() {
        tvCancelBtn = findViewById(R.id.change_device_bt_name_cancel);
        tvSureBtn = findViewById(R.id.change_device_bt_name_sure);
        etBtName = findViewById(R.id.edit_device_bt_name);
    }

    @Override
    public void initData() {
        Intent mIntent = getIntent();
        mIntentBtName = mIntent.getStringExtra("BT_NAME");
        LogUtil.d(TAG, "mIntentBtName = " + mIntentBtName);
        etBtName.setText(mIntentBtName);
    }

    @Override
    public void initListener() {
        tvCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsChangeBtNameAcivity.this, SettingsBluetoothActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
        tvSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChangedBtName = etBtName.getText().toString();
                if(!mBluetoothAdapter.setName(mChangedBtName)) {
                    ToastUtils.show(R.string.settings_change_bt_name_fail);
                }
//                Intent mIntent = new Intent(SettingsChangeBtNameAcivity.this, SettingsBluetoothActivity.class);
//                startActivity(mIntent);
                finish();

//                else {
//                    ToastUtils.show(R.string.settings_change_bt_name_fail);
//                }
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
