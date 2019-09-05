package com.fenda.launcher;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aispeech.dui.plugin.setting.SystemCtrl;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.author.MyDDSAuthListener;
import com.fenda.ai.author.MyDDSInitListener;
import com.fenda.ai.provider.InitService;
import com.fenda.ai.skill.MediaControl;
import com.fenda.ai.skill.SystemControl;
import com.fenda.ai.skill.Util;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.provider.IRemindProvider;
import com.fenda.common.provider.IVoiceInitProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppTaskUtil;
import com.fenda.common.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/26 14:37
  * @Description
  *
  */

public class MainActivity extends BaseActivity {


    private String[] mPermission = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
    };

    public static final int PERMISSION_REQ = 0x123456;

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;


    private List<String> mRequestPermission = new ArrayList<String>();

    @Autowired
    IVoiceInitProvider service;

    @Autowired
    IRemindProvider provider;

    @Override
    public int onBindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        hasPermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
            }else {
                service.initVoice();
                LogUtil.i("provider"+provider.toString());
            }
        }

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasPermission()) {
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }

    }

    public void test(View view) {
//        ARouter.getInstance().build(RouterPath.Gallery.GALLERY_CATOGORY).navigation();
        String topPackage = AppTaskUtil.getAppTopPackage();
        LogUtil.i("topPackage = "+topPackage);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
        }
        service.initVoice();
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
