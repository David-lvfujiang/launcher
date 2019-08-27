package com.fenda.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  15:32
 * @Description This is BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
