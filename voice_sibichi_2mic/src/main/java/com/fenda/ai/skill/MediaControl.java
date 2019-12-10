package com.fenda.ai.skill;



import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.aispeech.dui.plugin.mediactrl.MediaCtrl;
import com.aispeech.dui.plugin.mediactrl.MediaCtrlPlugin;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.fenda.ai.modle.ControlModel;

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

            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int stop() {
        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().exit();
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().exit();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_STOP);
        }

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

            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int orderPlay(boolean b) {

        if (Util.isQQMusicPlay()){
            return MusicPlugin.get().getMusicApi().orderPlay();
        }else if (Util.isLauncherMusic()){
            return controlModel.controlPlayer(MEDIA_ORDER_PLAY);
        }


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

        return MediaCtrlPlugin.ERR_NOT_SUPPORT;
    }

    @Override
    public int definition(String s) {
        if (Util.isQQMusicPlay()){
            return MediaCtrlPlugin.ERR_NOT_SUPPORT;
        }else if (Util.isQIYIPlay()){
            return IQiyiPlugin.get().getVideoApi().definition(s);
        }


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

                return controlModel.forward(relativeTime,absoluteTime);
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
                return controlModel.backward(relativeTime,absoluteTime);

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


