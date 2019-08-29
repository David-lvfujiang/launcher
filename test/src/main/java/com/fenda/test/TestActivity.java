package com.fenda.test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.test.api.LoginRequest;
import com.fenda.test.api.RegisterRequest;
import com.fenda.test.contract.TestContract;
import com.fenda.test.model.TestModel;
import com.fenda.test.presenter.TestPresenter;


/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/28 9:51
 * @Description
 */
public class TestActivity extends BaseMvpActivity <TestPresenter, TestModel> implements TestContract.View {

    private Button btStart;
    private Button btTest;
    private boolean hasRecordPermission;


    @Override
    protected void initPresenter() {
        mPresenter = new TestPresenter();


    }

    @Override
    public int onBindLayout() {
        return R.layout.test_activity_test;
    }

    @Override
    public void initView() {
        btStart = findViewById(R.id.bt_start);
        btTest  = findViewById(R.id.bt_test);

        hasRecordPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;

        if (!hasRecordPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WAKE_LOCK,Manifest.permission.READ_PHONE_STATE}, 1);
        }

    }

    @Override
    public void initData() {
        if (hasRecordPermission){
            LoginRequest request = new LoginRequest();
            request.setMobile("15989349055");
            request.setPassword("123456");
            mPresenter.register(request);
        }


    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void registerDevice(BaseResponse response) {
        LogUtil.i(response.getData().toString());


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (1 == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtils.show("对话需要录音机权限");
            }
            LoginRequest request = new LoginRequest();
            request.setMobile("15989349055");
            request.setPassword("123456");
            mPresenter.register(request);
        }
    }
}
