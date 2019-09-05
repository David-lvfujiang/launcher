package com.fenda.launcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.router.RouterPath;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/8/26 14:37
 * @Description
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void test(View view) {
        ARouter.getInstance().build(RouterPath.HomePage.HomePageActivity).navigation();
    }
}
