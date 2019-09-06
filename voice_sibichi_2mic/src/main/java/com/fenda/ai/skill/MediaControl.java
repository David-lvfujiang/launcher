package com.fenda.ai.skill;


import android.util.Log;

import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.mediactrl.MediaCtrl;
import com.aispeech.dui.plugin.mediactrl.MediaCtrlPlugin;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.modle.ControlModel;
import com.fenda.common.BaseApplication;

import java.util.HashMap;

import static com.fenda.ai.VoiceConstant.*;


/**
 * Created by chuck.liuzhaopeng on 2019/6/24.
 */

public class MediaControl extends MediaCtrl {
    private static final String TAG = "MediaControl";

    public static HashMap<String,Integer> mediaControlMap = new HashMap<>();
    private ControlModel controlModel;


    public MediaControl() {
        controlModel = new ControlModel();
    }

    @Override
    public int play() {
        //如果前台应用是QQ音乐
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().resume();
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().resume();
        }else if (Util.isLauncherMusic()){
           return controlModel.controlPlayer(MEDIA_PLAY);

        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PAUSE || mediaControlMap.get(value) == PLAY){
//                    mediaControlMap.put(MUSIC_PKG,PLAY);
//                    Log.i(TAG," mediaControlMap ==> Put = "+PLAY);
////                    MusicPlugin.get().getMusicApi().searchAndPlay()
//                    return MusicPlugin.get().getMusicApi().resume();
//                }
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                mediaControlMap.put(QIYIMOBILE_PKG,PLAY);
//                if (mediaControlMap.get(value) == PAUSE){
//                    return IQiyiPlugin.get().getVideoApi().resume();
//
//                }
//            }else if (Util.isLauncherMusic(value)){
//                if (mediaControlMap.get(value) == PAUSE || mediaControlMap.get(value) == PLAY){
//                    mediaControlMap.put(LAUNCHER,PLAY);
//                    DispatchManager.startService("play","","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }
            return IQiyiPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int pause() {
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().pause();

        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().pause();
        }else if (Util.isLauncherMusic()){
            //自己创建的音乐播放器
            return controlModel.controlPlayer(MEDIA_PAUSE);

        }

//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                mediaControlMap.put(MUSIC_PKG,PAUSE);
//                Log.i(TAG," mediaControlMap ==> Put = "+PAUSE);
//                return MusicPlugin.get().getMusicApi().pause();
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                 mediaControlMap.put(QIYIMOBILE_PKG,PAUSE);
//                 return IQiyiPlugin.get().getVideoApi().pause();
//            }else if (Util.isLauncherMusic(value)){
//                if (mediaControlMap.get(value) == PLAY){
//                    mediaControlMap.put(LAUNCHER,PAUSE);
////                    DispatchManager.startService("pause","","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int stop() {
        if (Util.isQQMusicPlay()){
            BaseApplication.QQMUSIC.remove(MUSIC_PKG);
            return MusicPlugin.get().getMusicApi().exit();
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().exit();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_STOP);
        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    Log.e(TAG,"mediaControlMap ==> remove MUSIC_PKG  "+entry.getKey()+" - "+STOP);
//                    mediaControlMap.put(MUSIC_PKG,STOP);
//                    FDApplication.packageNameMap.remove(entry.getKey());
//                    return MusicPlugin.get().getMusicApi().exit();
//                }
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    mediaControlMap.put(QIYIMOBILE_PKG,STOP);
//                    FDApplication.packageNameMap.remove(entry.getKey());
//                    return IQiyiPlugin.get().getVideoApi().exit();
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause ){
//                    mediaControlMap.put(LAUNCHER,STOP);
//                    FDApplication.packageNameMap.remove(entry.getKey());
//                    DispatchManager.startService("stop","","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int rePlay() {
        if (Util.isQQMusicPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().replay();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_REPLAY);
        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return IQiyiPlugin.get().getVideoApi().replay();
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause ){
//                    mediaControlMap.put(LAUNCHER,PLAY);
//                    DispatchManager.startService("rePlay","","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }
        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int prev() {
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().prev();
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().prevEpisode();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_PREV);
        }
//        Intent intent = new Intent();
//        intent.setAction("moveTaskToFront");
//        FDApplication.getContext().getApplicationContext().sendBroadcast(intent);
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    mediaControlMap.put(MUSIC_PKG,PLAY);
//                    return MusicPlugin.get().getMusicApi().prev();
//                }
//            }else if (Util.isQIYIPlay(value)){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return IQiyiPlugin.get().getVideoApi().prevEpisode();
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause){
//                    mediaControlMap.put(LAUNCHER,PLAY);
//                    DispatchManager.startService("prev","","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }



    @Override
    public int next() {
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().next();
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().nextEpisode();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_NEXT);
        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    mediaControlMap.put(MUSIC_PKG,PLAY);
//                    return MusicPlugin.get().getMusicApi().next();
//                }
//            }else if (Util.isQIYIPlay(value)){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return IQiyiPlugin.get().getVideoApi().nextEpisode();
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause ){
//                    mediaControlMap.put(LAUNCHER,PLAY);
//                    DispatchManager.startService("next","","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }

            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int orderPlay(boolean b) {

        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().orderPlay();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_ORDER_PLAY);
        }

//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MusicPlugin.get().getMusicApi().orderPlay();
//                }
//            }else if (Util.isQIYIPlay(value)){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int loopListPlay(boolean b) {
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().orderPlay();
        }else if (Util.isQIYIPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_LOOP_LIST_PLAY);
        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MusicPlugin.get().getMusicApi().orderPlay();
//                }
//            }else if (Util.isQIYIPlay(value)){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause ){
//                    DispatchManager.startService(AIDL.LIST_PLAY,"","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int loopSinglePlay(boolean b) {

        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().singleLoop();
        }else if (Util.isQIYIPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_LOOP_SINGLE_PLAY);
        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MusicPlugin.get().getMusicApi().singleLoop();
//                }
//            }else if (Util.isQIYIPlay(value)){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause ){
//                    DispatchManager.startService(AIDL.SINGLE_PLAY,"","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int randomPlay(boolean b) {
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().randomPlay();
        }else if (Util.isQIYIPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_RANDOM_PLAY);
        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MusicPlugin.get().getMusicApi().randomPlay();
//                }
//            }else if (Util.isQIYIPlay(value)){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//                }
//            }else if (Util.isLauncherMusic(value)){
//                boolean isPlayOrPause = mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE;
//                if (isPlayOrPause ){
//                    DispatchManager.startService(AIDL.RANDOM_PLAY,"","", Constant.AIDL.LAUNCHER);
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int favorite(boolean b) {
        if (Util.isQQMusicPlay()){
            if (b) {
                  return MusicPlugin.get().getMusicApi().favorite();
               } else {
                    return MusicPlugin.get().getMusicApi().cancelFavorite();
               }
        }else if (Util.isQIYIPlay()){
                  if (b) {
                        return IQiyiPlugin.get().getVideoApi().favorite();
                    } else {
                        return IQiyiPlugin.get().getVideoApi().unFavorite();
                    }

        }
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    if (b) {
//                        return MusicPlugin.get().getMusicApi().favorite();
//                    } else {
//                        return MusicPlugin.get().getMusicApi().cancelFavorite();
//                    }
//                }
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    if (b) {
//                        return IQiyiPlugin.get().getVideoApi().favorite();
//                    } else {
//                        return IQiyiPlugin.get().getVideoApi().unFavorite();
//                    }
//                }
//            }
//        }
//
//
        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int fullScreen(boolean b) {
        if (Util.isQQMusicPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isQIYIPlay()){
         if (b) {
             return IQiyiPlugin.get().getVideoApi().screenPlay();
         } else {
             return IQiyiPlugin.get().getVideoApi().unScreenPlay();
             }

        }
//        Log.d(TAG, "fullScreen: " + b);
//
//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    if (b) {
//                        return IQiyiPlugin.get().getVideoApi().screenPlay();
//                    } else {
//                        return IQiyiPlugin.get().getVideoApi().unScreenPlay();
//                    }
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int definition(String s) {
        if (Util.isQQMusicPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().definition(s);
        }

//        for (Map.Entry<String, String> entry : FDApplication.packageNameMap.entrySet()) {
//            String value = entry.getValue();
//            if (Util.isQQMusicPlay(value)){
//                //QQ音乐
//                return MediaCtrlPlugin.ERR_NOT_SUPPORT;
//            }else if (Util.isQIYIPlay(value) ){
//                //爱奇艺
//                if (mediaControlMap.get(value) == PLAY || mediaControlMap.get(value) == PAUSE){
//                    return IQiyiPlugin.get().getVideoApi().definition(s);
//                }
//            }
//        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int forward(int relativeTime, int absoluteTime) {
        //快进
            if (Util.isQQMusicPlay()){
                //QQ音乐
                return MusicPlugin.get().getMusicApi().forward(relativeTime);
            }else if (Util.isQIYIPlay() ){
                //爱奇艺
                    return IQiyiPlugin.get().getVideoApi().forward(relativeTime, absoluteTime);
            }else if (Util.isLauncherMusic()){
                return controlModel.controlPlayer(MEDIA_FORWARD,absoluteTime);
            }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int backward(int relativeTime, int absoluteTime) {
        //快退
            if (Util.isQQMusicPlay()){
                //QQ音乐
                return MusicPlugin.get().getMusicApi().backward(relativeTime);
            }else if (Util.isQIYIPlay() ){
                //爱奇艺
                    return IQiyiPlugin.get().getVideoApi().backward(relativeTime, absoluteTime);
            }else if (Util.isLauncherMusic()){
                return controlModel.controlPlayer(MEDIA_BACKWARD,absoluteTime);

        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int setSpeed(String speed) {

            if (Util.isQQMusicPlay()){
                //QQ音乐
                return MediaCtrlPlugin.ERR_NOT_SUPPORT;
            }else if (Util.isQIYIPlay() ){
                //爱奇艺
                    return IQiyiPlugin.get().getVideoApi().setSpeed(speed);

        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int setPart(boolean skip, String part) {

            if (Util.isQQMusicPlay()){
                //QQ音乐
                return MediaCtrlPlugin.ERR_NOT_SUPPORT;
            }else if (Util.isQIYIPlay() ){
                //爱奇艺
                    return IQiyiPlugin.get().getVideoApi().setSkipPart(skip, part);
        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int setSize(String size) {
            if (Util.isQQMusicPlay()){
                //QQ音乐
                return MediaCtrlPlugin.ERR_NOT_SUPPORT;
            }else if (Util.isQIYIPlay() ){
                //爱奇艺
                    return IQiyiPlugin.get().getVideoApi().setSize(size);
        }

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }



}


