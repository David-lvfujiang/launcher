package com.fenda.andlink;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.chinamobile.smartgateway.andsdk.device.serviceimpl.AndSdkImpl;
import com.fenda.common.base.BaseMvpActivity;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/19 15:13
 */
public class Start extends BaseMvpActivity {
    private Button btnStart;
    private Button btnBack;
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.start_layout;
    }

    @Override
    public void initView() {
        btnStart = findViewById(R.id.start_btn);
        btnBack = findViewById(R.id.main_back);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent andlinkIntent = new Intent(Start.this, AndlinkMainActivity.class);
                startActivity(andlinkIntent);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    protected void onDestroy() {
        AndSdkImpl.getInstance().stop(this);
        super.onDestroy();
    }
}
