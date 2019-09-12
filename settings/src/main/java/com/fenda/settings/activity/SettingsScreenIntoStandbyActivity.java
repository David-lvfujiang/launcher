package com.fenda.settings.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/11 17:28
 */
public class SettingsScreenIntoStandbyActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsScreenIntoStandbyActivity";

    private ImageView ivBack;
    private RadioGroup radioGroup;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_screen_standy_time_layout;
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.screen_time_back_iv);
        radioGroup = findViewById(R.id.radio_group);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                LogUtil.d(TAG, "radioGroup = "+ radioGroup + " , i = " + i );
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
