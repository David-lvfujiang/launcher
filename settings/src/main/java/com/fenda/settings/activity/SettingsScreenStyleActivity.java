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
 * Date:   2019/9/11 17:30
 */
public class SettingsScreenStyleActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsScreenStyleActivity";

    private ImageView ivBack;
    private RadioGroup rgRadioGroup;
    private RadioButton rbPhoto;
    private RadioButton rbTimeNum;
    private RadioButton rbTimeSimulate;

    private  RadioButton rbStyleText;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_screen_style_layout;
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.screen_style_back_iv);
        rgRadioGroup = findViewById(R.id.style_radio_group);
        rbPhoto = findViewById(R.id.rb_photo);
        rbTimeNum = findViewById(R.id.rb_num);
        rbTimeSimulate = findViewById(R.id.rb_simulate);
    }

    @Override
    public void initData() {
        String mIntentSeclectSytle;
        Intent mIntent = getIntent();
        mIntentSeclectSytle = mIntent.getStringExtra("SECLECT_STYLE_RADIOBTN");
        LogUtil.d(TAG,  "mSeclectTime = " + mIntentSeclectSytle);

        if(getString(R.string.settings_screen_style_photo).equals(mIntentSeclectSytle)){
            rbPhoto.setChecked(true);
        } else if(getString(R.string.settings_screen_style_time_num).equals(mIntentSeclectSytle)){
            rbTimeNum.setChecked(true);
        } else if(getString(R.string.settings_screen_style_time_simulate).equals(mIntentSeclectSytle)){
            rbTimeSimulate.setChecked(true);
        }

    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsScreenStyleActivity.this, SettingsScreenActivity.class);
                startActivity(mIntent);
                finish();
            }
        });

        rgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectRadioButton();
            }
        });

    }

    private void selectRadioButton() {
        rbStyleText = SettingsScreenStyleActivity.this.findViewById(rgRadioGroup.getCheckedRadioButtonId());
        LogUtil.d(TAG, "RadioButton text = " + rbStyleText.getText());
        SPUtils.put(getApplicationContext(), Constant.Settings.SCREEN_STYLE, rbStyleText.getText());
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
