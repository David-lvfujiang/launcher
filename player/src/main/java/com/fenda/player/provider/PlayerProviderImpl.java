package com.fenda.player.provider;

import android.app.Instrumentation;
import android.content.Context;
import android.view.KeyEvent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IPlayerProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.protocol.tcp.bus.EventBusUtils;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 16:49
 * @Description
 */
@Route(path = RouterPath.PLAYER.MUSIC_PROVIDER)
public class PlayerProviderImpl implements IPlayerProvider {
    @Override
    public void prevPage() {
        Instrumentation inst = new Instrumentation();
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
    }

    @Override
    public void play() {
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_PLAY);
        EventBusUtils.post(bean);
    }

    @Override
    public void pause() {
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_PAUSE);
        EventBusUtils.post(bean);
    }

    @Override
    public void stop() {
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_STOP);
        EventBusUtils.post(bean);
    }

    @Override
    public void rePlay() {
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_REPLAY);
        EventBusUtils.post(bean);
    }

    @Override
    public void prev() {
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_PREV);
        EventBusUtils.post(bean);
    }

    @Override
    public void next() {
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_NEXT);
        EventBusUtils.post(bean);
    }

    @Override
    public void loopListPlay() {
    //列表循环
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_LOOP_LIST_PLAY);
        EventBusUtils.post(bean);
    }

    @Override
    public void loopSinglePlay() {
    //单曲循环
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_LOOP_SINGLE_PLAY);
        EventBusUtils.post(bean);
    }

    @Override
    public void randomPlay() {
        //随机播放
        MusicPlayBean bean = new MusicPlayBean();
        bean.setAidlMsgType(Constant.Player.VOICE_RANDOM_PLAY);
        EventBusUtils.post(bean);
    }

    @Override
    public void forward(int relativeTime) {
    //快进
        MusicPlayBean bean = new MusicPlayBean();
        bean.setRelativeTime(relativeTime);
        bean.setAidlMsgType(Constant.Player.VOICE_FORWARD);
        EventBusUtils.post(bean);
    }

    @Override
    public void backward(int relativeTime) {
        //快退

        MusicPlayBean bean = new MusicPlayBean();
        bean.setRelativeTime(relativeTime);
        bean.setAidlMsgType(Constant.Player.VOICE_BACKWARD);
        EventBusUtils.post(bean);
    }

    @Override
    public void init(Context context) {

    }
}
