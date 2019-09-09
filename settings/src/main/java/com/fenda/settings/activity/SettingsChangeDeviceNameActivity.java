package com.fenda.settings.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.request.SettingsUpdateDeviceNameRequest;
import com.fenda.settings.presenter.SettingsPresenter;

import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:34
 */
@Route(path = RouterPath.SETTINGS.SettingsChangeDeviceNameActivity)
public class SettingsChangeDeviceNameActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsChangeDeviceNameActivity";

    private TextView tvSureBtn;
    private TextView tvCancelBtn;
    private EditText etName;
    private String mChangedName;

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_change_device_name_layout;
    }

    @Override
    public void initView() {
        tvCancelBtn = findViewById(R.id.change_device_name_cancel);
        tvSureBtn = findViewById(R.id.change_device_name_sure);
        etName = findViewById(R.id.edit_device_name);

        String OriginalName = (String) SPUtils.get(getApplicationContext(), Constant.Settings.DEVICE_NAME ,"");
        etName.setText(OriginalName);
        final int maxTextCount = 10;
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etName.removeTextChangedListener(this);//**** 注意的地方
                if (s.length() >= maxTextCount) {
                    etName.setText(s.toString().substring(0, maxTextCount));
                    etName.setSelection(maxTextCount);

                    Toast toast = Toast.makeText(getApplicationContext(),getString(R.string.settings_edittext_num_limit_name),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                etName.addTextChangedListener(this);//****  注意的地方
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        tvCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeNameIntent = new Intent(SettingsChangeDeviceNameActivity.this, SettingsDeviceCenterActivity.class);
                startActivity(changeNameIntent);
                finish();
            }
        });
        tvSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChangedName = etName.getText().toString();
                SettingsUpdateDeviceNameRequest request= new SettingsUpdateDeviceNameRequest();
                request.setDeviceId(SettingsContant.SETTINGS_SERIAL_NUM);
                request.setName(mChangedName);
                mPresenter.updateDeviceName(request);
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {
    }

    @Override
    public void updateDeviceNameSuccess(BaseResponse response) {
        ToastUtils.show("修改成功");
        SPUtils.put(getApplicationContext(), Constant.Settings.DEVICE_NAME, mChangedName);
        Intent changeNameIntent = new Intent(SettingsChangeDeviceNameActivity.this, SettingsDeviceCenterActivity.class);
        startActivity(changeNameIntent);
        finish();
    }

    @Override
    public void queryDeviceInfoSuccess(BaseResponse response) {

    }

    @Override
    public void unbindDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void registerDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void changeNickNameSuccess(BaseResponse response) {

    }

    @Override
    public void deleteLinkmanFromDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void getContactsListSuccess(BaseResponse<List<UserInfoBean>> response) {

    }
//
//    @Override
//    public void getContactsListSuccess(BaseResponse response) {
//
//    }

    @Override
    public void haveRegisterDevice(BaseResponse response) {

    }

    @Override
    public void updateDeviceNameFailure(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoFailure(BaseResponse response) {

    }

    @Override
    public void unbindDeviceFailure(BaseResponse response) {

    }

    @Override
    public void registerDeviceFailure(BaseResponse response) {

    }

    @Override
    public void changeNickNameFailure(BaseResponse response) {

    }

    @Override
    public void deleteLinkmanFromDeviceFailure(BaseResponse response) {

    }

    @Override
    public void getContactsListFailure(BaseResponse<List<UserInfoBean>> response) {

    }

//    @Override
//    public void getContactsListFailure(BaseResponse response) {
//
//    }
}
