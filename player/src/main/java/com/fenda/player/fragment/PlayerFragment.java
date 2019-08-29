package com.fenda.player.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;


import com.fenda.common.constant.Constant;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.view.CircleImageView;
import com.fenda.player.R;
import com.fenda.player.bean.PlayerMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class PlayerFragment extends Fragment {
    private View mView;
    private CircleImageView imgMusicPlay;
    private TextView mMusicNameTv;
    private ObjectAnimator objectAnimator;
    private TextView mMusicAuthorTv;
    private long mCurrentPlayTime;


    public static PlayerFragment getInstance(PlayerMessage message){
        PlayerFragment fragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("player",message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.player_fragment_player,null);
        initView();
        initData();
        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void initView(){
        mMusicNameTv    = mView.findViewById(R.id.tv_music_name);
        mMusicAuthorTv  = mView.findViewById(R.id.tv_music_author);
        imgMusicPlay    = mView.findViewById(R.id.iv_music_playingbg);
    }

    private void initData(){
        imgMusicPlay.setBorderWidth(5);
        imgMusicPlay.setBorderColor(ContextCompat.getColor(getActivity(),R.color.player_secondary_text));
        initAnimation();
        startAnimation();
        Bundle bundle = getArguments();
        PlayerMessage message = (PlayerMessage) bundle.getSerializable("player");
        setMusicData(message);

//        mMusicAuthorTv.setText(tMusic.getMusicArtist());

    }

    private void setMusicData(PlayerMessage playerMessage) {
        if (playerMessage.getContentType() == Constant.Player.JOKE){
            ImageUtil.loadDefaultcImg(imgMusicPlay,playerMessage.getMusicUrl(),R.mipmap.player_joke_pic_default);
        }else if (playerMessage.getContentType() == Constant.Player.CROSS_TALLK){
            ImageUtil.loadDefaultcImg(imgMusicPlay,playerMessage.getMusicUrl(),R.mipmap.player_cover_pic_default);
        }else if (playerMessage.getContentType() == Constant.Player.POETRY){
            ImageUtil.loadDefaultcImg(imgMusicPlay,playerMessage.getMusicUrl(),R.mipmap.player_poetry_pic_default);
        }else if (playerMessage.getContentType() == Constant.Player.FM){
            ImageUtil.loadDefaultcImg(imgMusicPlay,playerMessage.getMusicUrl(),R.mipmap.player_fm_pic_default);
        }
        String title = playerMessage.getMusicTitle();
        if (title.contains("《")){
            title = title.replace("《","");
        }
        if (title.contains("》")){
            title = title.replace("》","");
        }
        mMusicNameTv.setText(title);
        mMusicAuthorTv.setText(playerMessage.getMusicName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(PlayerMessage message){
        if (message.getMsgType() == 0){
            setMusicData(message);
            if (objectAnimator != null && !objectAnimator.isRunning()){
                startAnimation();
            }
        }else if (message.getMsgType() == 1){
            cancelAnimation();
        }else if (message.getMsgType() == 2){
            if (objectAnimator != null && !objectAnimator.isRunning()){
                startAnimation();
            }
        }


    }

    public void startAnimation(){
        if (objectAnimator != null){
            objectAnimator.start();
            objectAnimator.setCurrentPlayTime(mCurrentPlayTime);
        }
    }


    private void initAnimation(){
        if (objectAnimator == null){
            objectAnimator = ObjectAnimator.ofFloat(imgMusicPlay,"rotation",0.0f,360f);
            objectAnimator.setDuration(20000);
            //循环模式
            objectAnimator.setRepeatMode(ValueAnimator.RESTART);
            //无线循环
            objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            objectAnimator.setInterpolator(new LinearInterpolator());
        }

    }

    public void  cancelAnimation(){
        if (objectAnimator != null){
            mCurrentPlayTime = objectAnimator.getCurrentPlayTime();
            objectAnimator.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
        cancelAnimation();
    }
}
