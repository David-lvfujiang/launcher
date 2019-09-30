package com.fenda.settings.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
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
    private RadioButton rbTimeThirty;
    private RadioButton rbTimeTwenty;
    private RadioButton rbTimeTen;
    private RadioButton rbTimeNever;

    private  RadioButton rbText;

    private String mIntentSeclectTime;

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
        rbTimeThirty = findViewById(R.id.rb_30);
        rbTimeTwenty = findViewById(R.id.rb_20);
        rbTimeTen= findViewById(R.id.rb_10);
        rbTimeNever = findViewById(R.id.rb_never);
    }

    @Override
    public void initData() {
        Intent mIntent = getIntent();
        mIntentSeclectTime = mIntent.getStringExtra("SECLECT_TIME_RADIOBTN");
        LogUtil.d(TAG,  "mSeclectTime = " + mIntentSeclectTime);

        if(getString(R.string.settings_standby_3_1).equals(mIntentSeclectTime)){
            rbTimeThirty.setChecked(true);

        } else if(getString(R.string.settings_standby_2).equals(mIntentSeclectTime)){
            rbTimeTwenty.setChecked(true);
        } else if(getString(R.string.settings_standby_1).equals(mIntentSeclectTime)){
            rbTimeTen.setChecked(true);
        } else if(getString(R.string.settings_standby_never).equals(mIntentSeclectTime)){
            rbTimeNever.setChecked(true);
        }
    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsScreenIntoStandbyActivity.this, SettingsScreenActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectRadioButton();
            }
        });
    }

    private void selectRadioButton() {
        rbText = SettingsScreenIntoStandbyActivity.this.findViewById(radioGroup.getCheckedRadioButtonId());
        LogUtil.d(TAG, "RadioButton text = " + rbText.getText());
        SPUtils.put(getApplicationContext(), Constant.Settings.SCREEN_TIME, rbText.getText());
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
