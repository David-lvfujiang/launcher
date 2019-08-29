//package com.fenda.player.model;
//
//import android.content.Context;
//import android.content.Intent;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.net.wifi.WifiManager;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.PowerManager;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.fenda.common.BaseApplication;
//import com.fenda.common.constant.Constant;
//import com.fenda.common.util.ImageUtil;
//import com.fenda.common.util.LogUtil;
//import com.fenda.common.util.NetUtil;
//import com.fenda.common.util.ToastUtils;
//import com.fenda.player.R;
//import com.fenda.player.bean.FDMusic;
//import com.fenda.player.bean.PlayerMessage;
//import com.fenda.protocol.tcp.bus.EventBusUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * @author mirrer.wangzhonglin
// * @Date 2019/8/29 15:37
// * @Description
// */
//public class MediaPlayerManager {
//
//
//    private MediaPlayer mediaPlayer;
//    private WifiManager.WifiLock wifiLock;
//    private TextView tvMusicTime;
//    private TextView tvMusicRunTime;
//    private SeekBar mMusicProgressSb;
//    private ImageView imgFmPlay;
//    private ImageView mMusicPlayBt;
//    private Context mContext;
//
//    private int contentType;
//    private int playStatus;
//
//    private ImageView mMusicNextBt;
//    private ImageView mMusicPreBt;
//    private ImageView mMusicRepeatBt;
//    private ImageView imgMusicBg;
//
//
//    private int[] bgImg = {R.mipmap.player_xiangsheng_bg_01, R.mipmap.player_xiangsheng_bg_02, R.mipmap.player_xiangsheng_bg_03, R.mipmap.player_xiangsheng_bg_04, R.mipmap.player_xiangsheng_bg_05};
//    private int[] jokeImg = {R.mipmap.player_joke_bg1, R.mipmap.player_joke_bg2, R.mipmap.player_joke_bg3, R.mipmap.player_joke_bg4, R.mipmap.player_joke_bg5};
//
//
//
//    private List<Integer> musicRandom = new ArrayList<>();
//
//
//    private CountDownTimer timer;
//    private boolean isPause;
//    private int current_item;
//    private Handler mProgressHandler = new Handler();
//
//    private Runnable mProgressRunable = new Runnable() {
//        @Override
//        public void run() {
//            if (mediaPlayer != null) {
////                Log.e("qob", "当前进度 " + mediaPlayer.getCurrentPosition() + " 总进度 " + mediaPlayer.getDuration());
//                if (mediaPlayer.getDuration() == 0 || mediaPlayer.getCurrentPosition() == 0) {
//                    mMusicProgressSb.setProgress(0);
//                    if (contentType != Constant.Player.FM){
//                        mProgressHandler.postDelayed(this, 1000);
//                    }
//                    runTimeSecond = 0;
//                } else {
//                    runTimeSecond = mediaPlayer.getCurrentPosition();
////                    LogUtils.e("in - "+in +" runTimeSecond - "+ runTimeSecond+" duration - "+duration);
//                    mMusicProgressSb.setProgress(runTimeSecond);
//                    runTimeSecond = runTimeSecond/1000;
//                    if (contentType != Constant.Player.FM){
//                        mProgressHandler.postDelayed(this, 1000);
//                    }
//                    if (runTimeSecond < 60){
//                        if (runTimeSecond < 10){
//                            tvMusicRunTime.setText("00:0"+runTimeSecond);
//                        }else {
//                            tvMusicRunTime.setText("00:"+runTimeSecond);
//                        }
//                    } else {
//                        int second = runTimeSecond % 60;
//                        int minute = runTimeSecond / 60;
//                        String text = (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : String.valueOf(second));
//                        tvMusicRunTime.setText(text);
//                    }
//                }
//            }
//
//        }
//    };
//    private int runTimeSecond;
//
//
//    /**
//     * 初始化MediaPlayer
//     */
//    private void initMediaPlayer(Context mContext) {
//        if (mediaPlayer == null) {
//            mediaPlayer = new MediaPlayer();
//        }
//
////        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
////        manager.requestAudioFocus(changeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
//
//        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
//        mediaPlayer.setVolume(0.5f, 0.5f);
//
//        // 设置是否循环播放
//        mediaPlayer.setLooping(false);
//
//        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
//        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
////        // 当播放的时候一直让屏幕变亮
////        mediaPlayer.setScreenOnWhilePlaying(true);
//
//        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
//        wifiLock = ((WifiManager) mContext.getSystemService(Context.WIFI_SERVICE))
//                .createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
//        wifiLock.acquire();
//
//
//        initMediaPlayerListener();
//
//
//    }
//
//    public void setContentType(int contentType){
//        this.contentType = contentType;
//    }
//
//
//    private void initMediaPlayerListener() {
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                int time = mp.getDuration();
//                int length = time / 1000;
//                int second = length % 60;
//                int minute = length / 60;
//
//                tvMusicTime.setText("/" + minute + ":" + (second < 10 ? "0" + second : second));
//                mMusicProgressSb.setMax(time);
//                LogUtil.e("MediaPlayer -----》" + time);
//                mediaPlayer.start();
//                isPause = false;
//                if (contentType != Constant.Player.FM){
//                    mProgressHandler.postDelayed(mProgressRunable, 1000);
//                }
//            }
//        });
//
//        // 网络流媒体缓冲监听
//        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//            @Override
//            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
//                // i 0~100
////                Log.e("qob", "setOnBufferingUpdateListener 缓存进度" + i + "%");
//            }
//        });
//
//        // 设置播放错误监听
//        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//                //返回true 下一首的时候不会跳转两个
//                return true;
//            }
//        });
//
//        // 设置播放完成监听
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                Log.e("qob", "setOnCompletionListener 播放完成 " + mediaPlayer.getCurrentPosition() + " total " + mediaPlayer.getDuration());
//                mProgressHandler.removeCallbacks(mProgressRunable);
//                mMusicProgressSb.setProgress(0);
//                if (playStatus == Constant.Player.SINGLE_CYCLE_PLAY) {
//                    getRandomIndex();
//
//                    if (isPause) {
//                        isPause = false;
//                    }
//                    FDMusic tMusic = mMusicList.get(current_item);
//                    play(tMusic);
//                } else {
//                    nextMusic();
//                }
//            }
//        });
//
//        // 设置进度调整完成SeekComplete监听，主要是配合seekTo(int)方法
//        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//            @Override
//            public void onSeekComplete(MediaPlayer mediaPlayer) {
//                Log.e("qob", "setOnSeekCompleteListener seekTo完成");
//            }
//        });
//    }
//
//
//
//    /**
//     * 播放
//     */
//    private void play(FDMusic tMusic) {
//        try {
//            LogUtil.e("play current_item: " + current_item);
//            if (mediaPlayer == null) {
//                initMediaPlayer(mContext);
//            }
//            if (isPause) {
//                mediaPlayer.start();
//                isPause = false;
//                if (contentType != Constant.Player.FM){
//                    mMusicPlayBt.setImageResource(R.mipmap.player_ico_pause);
//                    mProgressHandler.postDelayed(mProgressRunable, 1000);
//                }else {
//                    imgFmPlay.setImageResource(R.mipmap.player_icon_fm_play);
//                }
//                PlayerMessage message = new PlayerMessage();
//                message.setMsgType(2);
//                EventBusUtils.post(message);
//            } else {
//                if (!NetUtil.isNetworkAvailable(mContext)){
//                    ToastUtils.show("网络不可用");
//                    return;
//                }
//                // 重置mediaPlayer
//                mediaPlayer.reset();
//                // 重新加载音频资源
//                mediaPlayer.setDataSource(tMusic.getMusicUri());
//                PlayerMessage message = new PlayerMessage();
//                message.setMusicUrl(tMusic.getMusicImage());
//                message.setMusicTitle(tMusic.getMusicTitle());
//                message.setMusicName(tMusic.getMusicArtist());
//                message.setContentType(contentType);
//                message.setContent(tMusic.getContent());
//                EventBusUtils.post(message);
//                // 准备播放（异步）
//                mediaPlayer.prepareAsync();
//                tvMusicRunTime.setText("00:00");
//                tvMusicTime.setText("/00:00");
//
//                mMusicPlayBt.setImageResource(R.mipmap.player_ico_pause);
//                Intent broadcastIntent = new Intent();
//                broadcastIntent.setAction(Constant.Player.keyCurrentPlayIndex);
//                broadcastIntent.putExtra(Constant.Player.keyCurrentPlayIndex, current_item);
//                mContext.sendBroadcast(broadcastIntent);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 下一首
//     */
//    private void nextMusic() {
//        getRandomIndex();
//
//        mProgressHandler.removeCallbacks(mProgressRunable);
//        mMusicProgressSb.setProgress(0);
//
//        if (isPause) {
//            isPause = false;
//        }
//
//        Log.e("qob", "nextMusic current_item: " + current_item);
//        if (playStatus == Constant.Player.RANDOM_PLAY) {
//            int ramdomNum = getMusicRandom();
//            current_item = ramdomNum;
//        } else {
//            current_item++;
//        }
//        if (current_item >= mMusicList.size()) {
//            current_item = 0;
//        }
//        FDMusic tMusic = mMusicList.get(current_item);
//        play(tMusic);
//    }
//
//    private void getRandomIndex() {
//
//        Random random = new Random();
//        if (contentType == Constant.Player.JOKE){
//            int index = random.nextInt(jokeImg.length-1);
//            imgMusicBg.setImageResource(jokeImg[index]);
//            goneFmView();
//        }else if (contentType == Constant.Player.CROSS_TALLK){
//            int index = random.nextInt(bgImg.length-1);
//            imgMusicBg.setImageResource(bgImg[index]);
//            goneFmView();
//        }else if (contentType == Constant.Player.POETRY){
//            imgMusicBg.setImageResource(R.mipmap.player_poetry_bg);
//            goneFmView();
//        }else if (contentType == Constant.Player.FM){
//            ImageUtil.loadGIFImage(R.mipmap.player_fm_bg,imgMusicBg);
//            visFmView();
//        }
//    }
//
//
//    private void visFmView(){
//        if (mMusicNextBt.getVisibility() == View.VISIBLE){
//            mMusicNextBt.setVisibility(View.GONE);
//        }
//        if (mMusicPreBt.getVisibility() == View.VISIBLE){
//            mMusicPreBt.setVisibility(View.GONE);
//        }
//        if (mMusicPlayBt.getVisibility() == View.VISIBLE){
//            mMusicPlayBt.setVisibility(View.GONE);
//        }
//        if (mMusicRepeatBt.getVisibility() == View.VISIBLE){
//            mMusicRepeatBt.setVisibility(View.GONE);
//        }
//        if (mMusicProgressSb.getVisibility() == View.VISIBLE){
//            mMusicProgressSb.setVisibility(View.GONE);
//        }
//        if (tvMusicRunTime.getVisibility() == View.VISIBLE){
//            tvMusicRunTime.setVisibility(View.GONE);
//        }
//        if (tvMusicTime.getVisibility() == View.VISIBLE){
//            tvMusicTime.setVisibility(View.GONE);
//        }
//        if (imgFmPlay.getVisibility() == View.GONE){
//            imgFmPlay.setVisibility(View.VISIBLE);
//        }
//
//    }
//
//
//
//    private void goneFmView(){
//        if (mMusicNextBt.getVisibility() == View.GONE){
//            mMusicNextBt.setVisibility(View.VISIBLE);
//        }
//        if (mMusicPreBt.getVisibility() == View.GONE){
//            mMusicPreBt.setVisibility(View.VISIBLE);
//        }
//        if (mMusicPlayBt.getVisibility() == View.GONE){
//            mMusicPlayBt.setVisibility(View.VISIBLE);
//        }
//        if (mMusicRepeatBt.getVisibility() == View.GONE){
//            mMusicRepeatBt.setVisibility(View.VISIBLE);
//        }
//        if (mMusicProgressSb.getVisibility() == View.GONE){
//            mMusicProgressSb.setVisibility(View.VISIBLE);
//        }
//        if (tvMusicRunTime.getVisibility() == View.GONE){
//            tvMusicRunTime.setVisibility(View.VISIBLE);
//        }
//        if (tvMusicTime.getVisibility() == View.GONE){
//            tvMusicTime.setVisibility(View.VISIBLE);
//        }
//        if (imgFmPlay.getVisibility() == View.VISIBLE){
//            imgFmPlay.setVisibility(View.GONE);
//        }
//
//    }
//
//
//    private int getMusicRandom() {
//        musicRandom.clear();
//        for (int i = 0; i < mMusicList.size(); i++) {
//            if (current_item != i) {
//                musicRandom.add(i);
//            }
//        }
//        Random random = new Random();
//        int index = random.nextInt(musicRandom.size() - 1);
//        int randomNum = musicRandom.get(index);
//        return randomNum;
//    }
//
//
//    /**
//     * 暂停播放
//     *
//     * @return 当前播放的位置
//     */
//    public int pause() {
//        if (isPause) {
//            return -1;
//        }
//        mProgressHandler.removeCallbacks(mProgressRunable);
//        isPause = true;
//        if (mediaPlayer != null) {
//            mediaPlayer.pause();
//        }
//        if (contentType == Constant.Player.FM){
//            imgFmPlay.setImageResource(R.mipmap.player_icon_fm_pause);
//        }else {
//            mMusicPlayBt.setImageResource(R.mipmap.player_ico_play);
//        }
//
//        PlayerMessage message = new PlayerMessage();
//        message.setMsgType(1);
//        EventBusUtils.post(message);
//
//        return current_item;
//    }
//
//
//    public void stop(){
//        if (mediaPlayer != null) {
//            if (mediaPlayer.isPlaying()) {
//                mediaPlayer.stop();
//            }
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//
//
//}
