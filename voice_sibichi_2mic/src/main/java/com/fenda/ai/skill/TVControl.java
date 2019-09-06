package com.fenda.ai.skill;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.tvctrl.TVCtrl;
import com.aispeech.dui.plugin.tvctrl.TVCtrlPlugin;
import com.fenda.common.util.AppTaskUtil;
import com.fenda.common.util.LogUtil;

/**
 * Created by chuck.liuzhaopeng on 2019/6/24.
 */

public class TVControl extends TVCtrl {
    private static final String TAG = "TVControl";
    private static final String QIYIMOBILE_PKG = "com.qiyi.video.speaker";
    private static final String MUSIC_PKG = "com.tencent.qqmusiccar";
    private static final String LAUNCHER = "com.fenda.lucher";
    public static boolean isInVideo = false;

    @Override
    public int openPage(String page) {
//        LogUtil.d(TAG, "openPage: " + page);
        if (AppTaskUtil.getAppTopPackage().equals(QIYIMOBILE_PKG)) {
            IQiyiPlugin.get().getVideoApi().open();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ("登录".equals(page)) {
                IQiyiPlugin.get().getVideoApi().login();
            } else if ("登出".equals(page)) {
                IQiyiPlugin.get().getVideoApi().logout();
            } else if ("购买会员".equals(page)) {
                IQiyiPlugin.get().getVideoApi().buyVip();
            } else if ("轮播台".equals(page)) {
                IQiyiPlugin.get().getVideoApi().openRadio();
            } else {
                try {
                    DDS.getInstance().getAgent().getTTSEngine().speak("为您找到以下资源",1);
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                IQiyiPlugin.get().getVideoApi().channel(page);
            }
            isInVideo = true;
            return TVCtrlPlugin.ERR_OK;
        } else {
            return TVCtrlPlugin.ERR_NOT_SUPPORT;
        }
    }


    @Override
    public int openMode(String s) {
        LogUtil.e("openMode = "+s);
        return super.openMode(s);

    }

    @Override
    public int openTV() {
        LogUtil.e("openTV");
        return super.openTV();
    }

    @Override
    public int select(String num, String row, String rank, String page) {
//        LogUtil.d(TAG, "select: " + num + "," + page);
        if (AppTaskUtil.getAppTopPackage().equals(QIYIMOBILE_PKG)) {
            if (!TextUtils.isEmpty(num)) {
                IQiyiPlugin.get().getVideoApi().playIndex(Integer.valueOf(num));//首页的第几个
            } else if (!TextUtils.isEmpty(page)) {
                if (page.contains("+")) {
                    IQiyiPlugin.get().getVideoApi().nextPage();//首页下一页
                } else if (page.contains("-")) {
                    IQiyiPlugin.get().getVideoApi().prevPage();//首页上一页
                }
            }
            isInVideo = true;
            return TVCtrlPlugin.ERR_OK;
        } else if (AppTaskUtil.getAppTopPackage().equals(MUSIC_PKG)){
            MusicPlugin.get().getMusicApi().resume();

        }else if (AppTaskUtil.getAppTopPackage().equals(LAUNCHER) ){
            if (page.contains("-")){
//                startService("back","back","", Constant.AIDL.LAUNCHER);
            }

        }
        return TVCtrlPlugin.ERR_NOT_SUPPORT;
    }


    private void startService(String widgetName,String type,String text,String appType){
        Intent mIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("appName",appType);
        bundle.putString("widgetName",widgetName);
        bundle.putString("type",type);
        bundle.putString("text",text);
        mIntent.putExtras(bundle);
//        mIntent.setClass(FDApplication.getContext(), DispatchService.class);
//        FDApplication.getContext().startService(mIntent);
    }
}
