package com.fenda.ai.skill;

import android.content.Context;
import android.content.Intent;

import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.setting.SettingPlugin;
import com.aispeech.dui.plugin.setting.SystemCtrl;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.provider.ICalendarProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppTaskUtil;
import com.fenda.common.util.LogUtil;

import org.androidannotations.annotations.App;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * Created by chuck.liuzhaopeng on 2019/6/24.
 */

public class SystemControl extends SystemCtrl {
    private static final String TAG = "SystemControl";
    private static final String QIYIMOBILE_PKG = "com.qiyi.video.speaker";
    private IVoiceRequestProvider provider;

    public SystemControl(Context context) {
        super(context);
    }

    @Override
    public int refresh() {//刷新首页内容
        if (AppTaskUtil.getAppTopPackage().equals(QIYIMOBILE_PKG)) {
            return IQiyiPlugin.get().getVideoApi().refresh();
        }
        return SettingPlugin.ERR_OK;
    }

    @Override
    public int boot(String s, String s1, String s2, String s3) {
//        LogUtil.d(TAG, "boot s " + s + " s1 "+ s1 + "  s2 " + s2 + " s3: " +s3);
        return super.boot(s, s1, s2, s3);
    }


    @Override
    public int openApp(String s) {
        return super.openApp(s);
    }

    @Override
    public int shutdown(String s, String s1, String s2, String s3) {
//        DispatchManager.startService(Constant.AIDL.SCREEN_OFF, Constant.AIDL.SCREEN_OFF,"",Constant.AIDL.LAUNCHER);
        return SettingPlugin.ERR_OK;
    }

    @Override
    public int goBack() {//爱奇艺内返回
//        if (AccessibilityMonitorService.getTopPackageName().equals(QIYIMOBILE_PKG)) {
//            return IQiyiPlugin.get().getVideoApi().back();
//        }else {
//
//
//        }
        //返回
        Intent mIntent = new Intent();
        mIntent.setAction("back_activity");
        BaseApplication.getInstance().sendBroadcast(mIntent);
        return SettingPlugin.ERR_OK;
    }


    @Override
    public int screenOff() {
//        DispatchManager.startService(Constant.AIDL.SCREEN_OFF, Constant.AIDL.SCREEN_OFF,"",Constant.AIDL.LAUNCHER);

        return SettingPlugin.ERR_OK;
    }

    @Override
    public int screenOn() {
//        DispatchManager.startService(Constant.AIDL.SCREEN_ON, Constant.AIDL.SCREEN_ON,"",Constant.AIDL.LAUNCHER);
        return SettingPlugin.ERR_OK;
    }

    @Override
    public int openSettings(Settings settings) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("");
            }
        }).compose(RxSchedulers.<String>io_main())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();

                    }
                });
        return SettingPlugin.ERR_OK;
    }



    @Override
    public int goHome() {
        if (Util.isQIYIPlay()){
            IQiyiPlugin.get().getVideoApi().exit();
        }
        return super.goHome();
    }
}
