package com.fenda.settings.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 11:32
 */
@Route(path = RouterPath.SETTINGS.SetttingsBrightnessActivity)
public class SetttingsBrightnessActivity extends BaseMvpActivity {
    private static final String TAG = "SetttingsBrightnessActivity";

    private ImageView ivBack;
    private SeekBar brightnessBar;

    private int mIntScreenBrightness;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_brightness_layout;
    }

    @Override
    public void initView() {
        brightnessBar = findViewById(R.id.light_seekbar);
        ivBack = findViewById(R.id.light_back_iv);
        brightnessBar.setMax(255);
    }

    @Override
    public void initData() {
        screenBrightnessCheck();

    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void screenBrightnessCheck() {
        //先关闭系统的亮度自动调节
        try {
            if(android.provider.Settings.System.getInt(getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE) == android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                android.provider.Settings.System.putInt(getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                        android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        }
        catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //获取当前亮度,获取失败则返回255
        try {
            mIntScreenBrightness= Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
//                intScreenBrightness = Settings.System.getInt(getContentResolver(),
//                Settings.System.SCREEN_BRIGHTNESS,
//                255);
        //文本、进度条显示
        brightnessBar.setProgress(mIntScreenBrightness);
        brightnessBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

     //改变App当前Window亮度
    public void changeAppBrightness(int brightness)
    {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
        LogUtil.d(TAG, "changeAppBrightness brightness = " + brightness);
    }

    //设置亮度，退出app也能保持该亮度值
    public static void saveBrightness(Context context, int brightness) {

        ContentResolver resolver = context.getContentResolver();
        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        LogUtil.d(TAG, "saveBrightness brightness = " + brightness);
        resolver.notifyChange(uri, null);
    }

    @Override
    public void showErrorTip(String msg) {

    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //改变亮度
            changeAppBrightness(progress);
            saveBrightness(SetttingsBrightnessActivity.this, progress);
            LogUtil.d(TAG, "seekbar brightness = " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    //通过监听Settings.System.SCREEN_BRIGHTNESS
    private ContentObserver mBrightnessObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            //获取当前亮度,获取失败则返回255
            try {
                int currentValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                brightnessBar.setProgress(currentValue);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        this.getContentResolver().registerContentObserver(
                Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS), true, mBrightnessObserver);
        super.onResume();
    }
}
