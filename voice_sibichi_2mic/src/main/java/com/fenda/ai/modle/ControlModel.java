package com.fenda.ai.modle;

import com.aispeech.dui.plugin.iqiyi.IQiyiPlugin;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.VoiceConstant;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IPlayerProvider;
import com.fenda.common.router.RouterPath;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 16:20
 * @Description
 */
public class ControlModel {

    private IPlayerProvider provider;

    public int controlPlayer(int type){
        return controlPlayer(type,0);
    }

    /**
     * 控制播放器
     * @param type
     * @param relativeTime
     */
    public int controlPlayer(final int type, final int relativeTime){

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("");
            }
        }).compose(RxSchedulers.<String>io_main())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (provider == null){
                            provider = ARouter.getInstance().navigation(IPlayerProvider.class);
                        }
                        judgeType(type,relativeTime);
                    }
                });


        return IQiyiPlugin.ERR_OK;

    }

    private void judgeType(int type,int relativeTime) {
        switch (type){
            case VoiceConstant.MEDIA_PLAY:
                provider.play();
                break;
            case VoiceConstant.MEDIA_PAUSE:
                provider.pause();
                break;
            case VoiceConstant.MEDIA_STOP:
                provider.stop();
                break;
            case VoiceConstant.MEDIA_REPLAY:
                provider.rePlay();
                break;
            case VoiceConstant.MEDIA_PREV:
                provider.prev();
                break;
            case VoiceConstant.MEDIA_NEXT:
                provider.next();
                break;
            case VoiceConstant.MEDIA_ORDER_PLAY:
                break;
            case VoiceConstant.MEDIA_LOOP_LIST_PLAY:
                provider.loopListPlay();
                break;
            case VoiceConstant.MEDIA_LOOP_SINGLE_PLAY:
                provider.loopSinglePlay();
                break;
            case VoiceConstant.MEDIA_RANDOM_PLAY:
                provider.randomPlay();
                break;
            case VoiceConstant.MEDIA_FAVORITE:

                break;
            case VoiceConstant.MEDIA_FULL_SCREEN:
                break;
            case VoiceConstant.MEDIA_DEFINITION:

                break;
            case VoiceConstant.MEDIA_FORWARD:
                provider.forward(relativeTime);
                break;
            case VoiceConstant.MEDIA_BACKWARD:
                provider.backward(relativeTime);
                break;
            case VoiceConstant.MEDIA_SET_SPEED:

                break;
            case VoiceConstant.MEDIA_SET_PART:

                break;
            case VoiceConstant.MEDIA_SET_SIZE:

                break;
        }
    }

}
