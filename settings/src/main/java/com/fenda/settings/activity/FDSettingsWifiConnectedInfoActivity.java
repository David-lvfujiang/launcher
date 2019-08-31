package com.fenda.settings.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 18:24
 */
@Route(path = RouterPath.SETTINGS.FDSettingsWifiConnectedInfoActivity)
public class FDSettingsWifiConnectedInfoActivity extends BaseMvpActivity {
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return 0;
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
}
