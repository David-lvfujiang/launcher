package com.fenda.common.activity;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.fenda.common.R;
import com.fenda.common.base.BaseMvpActivity;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/29 15:55
 */
public class SettingsScreenSimClockActivity extends BaseMvpActivity {


    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_screen_sim_clock_layout;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        finish();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        finish();
        return true;
    }
}
