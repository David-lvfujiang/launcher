package com.fenda.capture.activity;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.capture.R;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;

@Route(path = RouterPath.Capture.CAPTURE_ACTIVITY)
public class CaptureActivity extends BaseActivity {
    @Override
    public int onBindLayout() {
        return R.layout.activity_capture;
    }
    @Override
    public void initView() {
    }
    @Override
    public void initData() {

    }
}
