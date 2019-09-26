package com.fenda.ai.skill;

import android.content.Context;
import android.content.Intent;

import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.setting.SettingPlugin;
import com.aispeech.dui.plugin.setting.SystemCtrl;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.baseapp.AppManager;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IPlayerProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppTaskUtil;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.fenda.ai.VoiceConstant.MEDIA_STOP;
import static com.fenda.ai.VoiceConstant.MUSIC_PKG;

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
        if (s.equals("相册")) {
            if (!AppManager.getAppManager().isForeground("GalleryCategoryActivity") || !AppTaskUtil.isLauncherForeground()) {
                try {
                    AppManager.getAppManager().finishActivity(Class.forName("com.fenda.gallery.activity.GalleryCategoryActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                ARouter.getInstance().build(RouterPath.Gallery.GALLERY_CATOGORY).navigation();
            }
            return SettingPlugin.ERR_OK;
        } else {
            return super.openApp(s);
        }
    }

    @Override
    public int shutdown(String s, String s1, String s2, String s3) {
//        DispatchManager.startService(Constant.AIDL.SCREEN_OFF, Constant.AIDL.SCREEN_OFF,"",Constant.AIDL.LAUNCHER);
        // 退出播放
        if (Util.isTaskQQmusic(BaseApplication.getInstance())) {
            MusicPlugin.get().getMusicApi().pause();
        }
        if (Util.isTopTaskQIYI()) {
            IQiyiPlugin.get().getVideoApi().pause();
        }
        IPlayerProvider provider = ARouter.getInstance().navigation(IPlayerProvider.class);
        if (provider != null) {
            provider.stop();
        }
        // 锁屏
        EventBusUtils.post(Constant.Common.SCREEN_OFF);
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
        // QQ音乐暂停播放才能息屏
        if (Util.isTaskQQmusic(BaseApplication.getInstance())) {
            MusicPlugin.get().getMusicApi().pause();
        }
        EventBusUtils.post(Constant.Common.SCREEN_OFF);
        return SettingPlugin.ERR_OK;
    }

    @Override
    public int screenOn() {
//        DispatchManager.startService(Constant.AIDL.SCREEN_ON, Constant.AIDL.SCREEN_ON,"",Constant.AIDL.LAUNCHER);
        EventBusUtils.post(Constant.Common.SCREEN_ON);
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
                        if (!AppManager.getAppManager().isForeground("SettingsActivity") || !AppTaskUtil.isLauncherForeground()) {
                            try {
                                AppManager.getAppManager().finishActivity(Class.forName("com.fenda.settings.activity.SettingsActivity"));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
                        }
                    }
                });
        return SettingPlugin.ERR_OK;
    }


    @Override
    public int setWIFI(Operation operation) {
        if (operation != SystemCtrl.Operation.Open) {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("");
                }
            }).compose(RxSchedulers.<String>io_main())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsWifiActivity).navigation();

                        }
                    });
        }
        return SettingPlugin.ERR_OK;
    }

    @Override
    public int setBluetooth(final Operation operation) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("");
            }
        }).compose(RxSchedulers.<String>io_main())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (operation == SystemCtrl.Operation.Open) {
                            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsBluetoothActivity).withBoolean(Constant.Common.OPEN_BLUE_TOOTH, true).withBoolean(Constant.Common.VOICE_CONTROL, true).navigation();
                        } else {
                            ARouter.getInstance().build(RouterPath.SETTINGS.SettingsBluetoothActivity).withBoolean(Constant.Common.OPEN_BLUE_TOOTH, false).withBoolean(Constant.Common.VOICE_CONTROL, true).navigation();
                        }

                    }
                });

        return SettingPlugin.ERR_OK;
    }

    @Override
    public int goHome() {
        if (Util.isQIYIPlay()) {
            IQiyiPlugin.get().getVideoApi().exit();
        }
        EventMessage<BaseTcpMessage> message = new EventMessage();
        message.setCode(Constant.Common.GO_HOME);
        message.setData(new BaseTcpMessage());
        EventBusUtils.post(message);

        return super.goHome();
    }
}
