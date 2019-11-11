package com.fenda.player;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.NetUtil;
import com.fenda.player.bean.PlayerMessage;
import com.fenda.player.fragment.LyricFragment;
import com.fenda.player.fragment.PlayerFragment;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/30 11:25
  * @Description
  *
  */
@Route(path = RouterPath.PLAYER.MUSIC)
public class MusicActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mMusicPreBt;
    private ImageView mMusicPlayBt;
    private ImageView mMusicNextBt;
    private ImageView mMusicRepeatBt;
    private ImageView mMusicListBt;
    private SeekBar mMusicProgressSb;
    private ImageView imgFmPlay;


    private LinearLayout linText;
    private LinearLayout linMusicControl;
    private ImageView imgMusicBg;
    private TextSwitcher tsHint;
    private ViewPager pagerPlay;
    private TextView tvMusicTime;
    private TextView tvMusicRunTime;
    private RelativeLayout relaPlay;
    private LinearLayout linPage;
    private TextView tvPageOne;
    private TextView tvPageTwo;


    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;
    private boolean isPause = false;
    private int current_item = 0;

    private AudioManager manager;

    private ArrayList<FDMusic> mMusicList = new ArrayList<>();

    private boolean isPlay = true;
    private int audioManagerSatatus;
    private boolean isAnimation = false;

    private int curStr;
    private int[] bgImg = {R.mipmap.player_xiangsheng_bg_01, R.mipmap.player_xiangsheng_bg_02, R.mipmap.player_xiangsheng_bg_03, R.mipmap.player_xiangsheng_bg_04, R.mipmap.player_xiangsheng_bg_05};
    private int[] jokeImg = {R.mipmap.player_joke_bg1, R.mipmap.player_joke_bg2, R.mipmap.player_joke_bg3, R.mipmap.player_joke_bg4, R.mipmap.player_joke_bg5};
    private String[] texts = {"你好小乐,我想听郭德纲的相声", "你好小乐,播放听岳云鹏的相声", "你好小乐,返回上一页", "你好小乐,回到首页", "你好小乐,下一个", "你好小乐,暂停播放", "你好小乐,我想听单人相声"};

    private int playStatus = Constant.Player.LIST_CYCLE_PLAY;
    private List<Integer> musicRandom = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private PlayerFragment playerFragment;
    private LyricFragment lyricFragment;
    /**
     * 内容类型 相声、诗词、笑话、广播
     */
    private int contentType;


    private Intent mIntent;
    private int runTimeSecond;
    private MyFragmentPagerAdapter adapter;
    private Handler mProgressHandler = new Handler();

    private boolean isEventPuase;

    private Runnable mProgressRunable = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null) {
//                Log.e("qob", "当前进度 " + mediaPlayer.getCurrentPosition() + " 总进度 " + mediaPlayer.getDuration());
                if (mediaPlayer.getDuration() == 0 || mediaPlayer.getCurrentPosition() == 0) {
                    mMusicProgressSb.setProgress(0);
                    if (contentType != Constant.Player.FM){
                        mProgressHandler.postDelayed(this, 1000);
                    }
                    runTimeSecond = 0;
                } else {
                    runTimeSecond = mediaPlayer.getCurrentPosition();
//                    LogUtils.e("in - "+in +" runTimeSecond - "+ runTimeSecond+" duration - "+duration);
                    mMusicProgressSb.setProgress(runTimeSecond);
                    runTimeSecond = runTimeSecond/1000;
                    if (contentType != Constant.Player.FM){
                        mProgressHandler.postDelayed(this, 1000);
                    }
                    if (runTimeSecond < 60){
                        if (runTimeSecond < 10){
                            tvMusicRunTime.setText("00:0"+runTimeSecond);
                        }else {
                            tvMusicRunTime.setText("00:"+runTimeSecond);
                        }
                    } else {
                        int second = runTimeSecond % 60;
                        int minute = runTimeSecond / 60;
                        String text = (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : String.valueOf(second));
                        tvMusicRunTime.setText(text);
                    }
                }
            }

        }
    };

    @Override
    public int onBindLayout() {
        return R.layout.player_activity_music_main;
    }

    @Override
    public void initView() {
        mMusicPreBt     = findViewById(R.id.bt_music_pre);
        mMusicPlayBt    = findViewById(R.id.bt_music_play);
        mMusicNextBt    = findViewById(R.id.bt_music_next);
        mMusicRepeatBt  = findViewById(R.id.bt_music_repeat);
        mMusicListBt    = findViewById(R.id.bt_music_list);
        mMusicProgressSb = findViewById(R.id.pb_music_playProgress);

        linText         = findViewById(R.id.lin_text);
        linMusicControl = findViewById(R.id.lin_music_control);
        imgMusicBg      = findViewById(R.id.con_music);
        tsHint          = findViewById(R.id.tw_text_list);
        pagerPlay        = findViewById(R.id.pager_play);
        tvMusicTime     = findViewById(R.id.tv_music_time);
        tvMusicRunTime  = findViewById(R.id.tv_music_run_time);
        relaPlay        = findViewById(R.id.rela_play);
        linPage         = findViewById(R.id.lin_page);
        tvPageOne       = findViewById(R.id.tv_page_one);
        tvPageTwo       = findViewById(R.id.tv_page_two);
        imgFmPlay       = findViewById(R.id.img_fm_play);


        mMusicPreBt.setOnClickListener(this);
        mMusicPlayBt.setOnClickListener(this);
        mMusicNextBt.setOnClickListener(this);
        mMusicRepeatBt.setOnClickListener(this);
        mMusicListBt.setOnClickListener(this);

        addListener();
        mIntent = getIntent();


    }

    @Override
    public void initData() {
        current_item = 0;
        if (mMusicList != null){
            mMusicList.clear();
        }
        MusicPlayBean bean = mIntent.getParcelableExtra(Constant.Player.keyDataMusicKey);
        String title = bean.getMsgName();
        contentType = bean.getMsgType();
        mMusicList.addAll(bean.getFdMusics());
        Log.e("qob", "MusicActivity List " + mMusicList);
        initTitle(title);
        getRandomIndex();

        setPlayStatus();

        initMediaPlayer();
        FDMusic tMusic = mMusicList.get(current_item);
        PlayerMessage message = new PlayerMessage();
        message.setMusicName(tMusic.getMusicArtist());
        message.setMusicTitle(tMusic.getMusicTitle());
        message.setMusicUrl(tMusic.getMusicImage());
        message.setContentType(contentType);
        playerFragment = PlayerFragment.getInstance(message);

        addFragment(tMusic);
        play(tMusic,true);

    }


    private void addFragment(FDMusic tMusic) {
        if (fragmentList != null){
            fragmentList.clear();
        }
        fragmentList.add(playerFragment);
        if (contentType == Constant.Player.POETRY){
            lyricFragment = LyricFragment.getInstance(tMusic);
            fragmentList.add(lyricFragment);
        }
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        pagerPlay.setAdapter(adapter);
        if (fragmentList.size() > 1){
            linPage.setVisibility(View.VISIBLE);
            tvPageOne.setEnabled(true);
            tvPageTwo.setEnabled(false);
        }
    }
    private void notifyFragment(FDMusic fdMusic) {
        fragmentList.clear();
        fragmentList.add(playerFragment);
        if (contentType == Constant.Player.POETRY){
            lyricFragment = LyricFragment.getInstance(fdMusic);
            fragmentList.add(lyricFragment);
        }else {
            if (lyricFragment != null){
                lyricFragment = null;
            }
        }
        adapter.notifyDataSetChanged();
//        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
//        pagerPlay.setAdapter(adapter);
        if (fragmentList.size() > 1){
            linPage.setVisibility(View.VISIBLE);
            tvPageOne.setEnabled(true);
            tvPageTwo.setEnabled(false);
        }else {
            linPage.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("qob", "MainActivity onResume");
        isPlay = true;

    }



    private void addListener() {
        tsHint.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
                TextView tv = new TextView(MusicActivity.this);
                tv.setLayoutParams(params);
                tv.setTextSize(20);
                tv.setTextColor(ContextCompat.getColor(MusicActivity.this, R.color.player_white));
                return tv;
            }
        });
        relaPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linText.getVisibility() == View.GONE) {
                    nextAnimation();
                } else {
                    linText.setVisibility(View.GONE);
                    linMusicControl.setVisibility(View.VISIBLE);
                }
            }
        });
        pagerPlay.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (linPage.getVisibility() == View.VISIBLE) {
                    if (position == 0) {
                        tvPageOne.setEnabled(true);
                        tvPageTwo.setEnabled(false);
                    } else {
                        tvPageOne.setEnabled(false);
                        tvPageTwo.setEnabled(true);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        imgFmPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPause) {
                    FDMusic tMusic = mMusicList.get(current_item);
                    play(tMusic,true);
                } else {
                    pause(false);
                }
            }
        });

        mMusicProgressSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    LogUtil.e("progress = " + progress);
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });



    }

    private void nextAnimation() {
        next();
        linMusicControl.setVisibility(View.GONE);
        linText.setVisibility(View.VISIBLE);
        if (!isAnimation) {
            isAnimation = true;
            // 设置转换时的淡入和淡出动画效果（可选）
            Animation in = AnimationUtils.loadAnimation(MusicActivity.this, R.anim.player_music_hint_in);
            Animation out = AnimationUtils.loadAnimation(MusicActivity.this, R.anim.player_music_hint_out);
            tsHint.setInAnimation(in);
            tsHint.setOutAnimation(out);
            Observable.interval(5, TimeUnit.SECONDS).map(new Function<Long, Long>() {
                @Override
                public Long apply(Long aLong) throws Exception {
                    return aLong;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            next();
                        }
                    });
        }


    }


    private void next() {
        tsHint.setText(texts[curStr = (++curStr % texts.length)]);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("qob", "MusicActivity onStart");
        BaseApplication.getBaseInstance().setMusicPlay(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("qob", "MusicActivity onStop");
        if (isPlay) {
            BaseApplication.getBaseInstance().setMusicPlay(false);
            stop();
            finish();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        mIntent = intent;
//        initData();
        current_item = 0;
        MusicPlayBean bean = mIntent.getParcelableExtra(Constant.Player.keyDataMusicKey);
        stop();
        List<FDMusic> tMusicList = bean.getFdMusics();
        String title = bean.getMsgName();
        contentType = bean.getMsgType();
        mMusicList.clear();
        mMusicList.addAll(tMusicList);
        initTitle(title);
        mProgressHandler.removeCallbacks(mProgressRunable);
        mMusicProgressSb.setProgress(0);
        FDMusic tMusic = mMusicList.get(current_item);
        notifyFragment(tMusic);
        Log.e("qob", "MyBroadcastReceiver List " + mMusicList);
        getRandomIndex();
        isPause = false;
        play(tMusic,true);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (wifiLock != null && wifiLock.isHeld()) {
            wifiLock.release();
        }

        mProgressHandler.removeCallbacks(mProgressRunable);
        manager.abandonAudioFocus(changeListener);
    }





    private void setPlayStatus() {
        if (playStatus == Constant.Player.LIST_CYCLE_PLAY) {
            mMusicRepeatBt.setImageResource(R.mipmap.player_ico_list_cycle);
        } else if (playStatus == Constant.Player.SINGLE_CYCLE_PLAY) {
            mMusicRepeatBt.setImageResource(R.mipmap.player_ico_single_cycle);
        } else {
            mMusicRepeatBt.setImageResource(R.mipmap.player_ico_random);
        }
    }


    private void getRandomIndex() {

        Random random = new Random();
        if (contentType == Constant.Player.JOKE){
            int index = random.nextInt(jokeImg.length-1);
            imgMusicBg.setImageResource(jokeImg[index]);
            goneFmView();
        }else if (contentType == Constant.Player.CROSS_TALLK){
            int index = random.nextInt(bgImg.length-1);
            imgMusicBg.setImageResource(bgImg[index]);
            goneFmView();
        }else if (contentType == Constant.Player.POETRY){
            imgMusicBg.setImageResource(R.mipmap.player_poetry_bg);
            goneFmView();
        }else if (contentType == Constant.Player.FM){
            ImageUtil.loadGIFImage(R.mipmap.player_fm_bg,imgMusicBg,R.mipmap.player_fm_bg);
            visFmView();
        }else {
            imgMusicBg.setImageResource(R.mipmap.player_xiangsheng_bg_02);
        }
    }

    private void visFmView(){
        if (mMusicNextBt.getVisibility() == View.VISIBLE){
            mMusicNextBt.setVisibility(View.GONE);
        }
        if (mMusicPreBt.getVisibility() == View.VISIBLE){
            mMusicPreBt.setVisibility(View.GONE);
        }
        if (mMusicPlayBt.getVisibility() == View.VISIBLE){
            mMusicPlayBt.setVisibility(View.GONE);
        }
        if (mMusicRepeatBt.getVisibility() == View.VISIBLE){
            mMusicRepeatBt.setVisibility(View.GONE);
        }
        if (mMusicProgressSb.getVisibility() == View.VISIBLE){
            mMusicProgressSb.setVisibility(View.GONE);
        }
        if (tvMusicRunTime.getVisibility() == View.VISIBLE){
            tvMusicRunTime.setVisibility(View.GONE);
        }
        if (tvMusicTime.getVisibility() == View.VISIBLE){
            tvMusicTime.setVisibility(View.GONE);
        }
        if (imgFmPlay.getVisibility() == View.GONE){
            imgFmPlay.setVisibility(View.VISIBLE);
        }

    }

    private void goneFmView(){
        if (mMusicNextBt.getVisibility() == View.GONE){
            mMusicNextBt.setVisibility(View.VISIBLE);
        }
        if (mMusicPreBt.getVisibility() == View.GONE){
            mMusicPreBt.setVisibility(View.VISIBLE);
        }
        if (mMusicPlayBt.getVisibility() == View.GONE){
            mMusicPlayBt.setVisibility(View.VISIBLE);
        }
        if (mMusicRepeatBt.getVisibility() == View.GONE){
            mMusicRepeatBt.setVisibility(View.VISIBLE);
        }
        if (mMusicProgressSb.getVisibility() == View.GONE){
            mMusicProgressSb.setVisibility(View.VISIBLE);
        }
        if (tvMusicRunTime.getVisibility() == View.GONE){
            tvMusicRunTime.setVisibility(View.VISIBLE);
        }
        if (tvMusicTime.getVisibility() == View.GONE){
            tvMusicTime.setVisibility(View.VISIBLE);
        }
        if (imgFmPlay.getVisibility() == View.VISIBLE){
            imgFmPlay.setVisibility(View.GONE);
        }

    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_music_pre){
            //上一曲
            preMusic();
        }else if (id == R.id.bt_music_play){
            if (isPause) {
                FDMusic tMusic = mMusicList.get(current_item);
                play(tMusic,true);
            } else {
                pause(false);
            }
        }else if (id == R.id.bt_music_next){
            //下一曲
            nextMusic();
        }else if (id == R.id.bt_music_repeat){
            if (playStatus == Constant.Player.LIST_CYCLE_PLAY) {
                playStatus = Constant.Player.SINGLE_CYCLE_PLAY;
            } else if (playStatus == Constant.Player.SINGLE_CYCLE_PLAY) {
                playStatus = Constant.Player.RANDOM_PLAY;
            } else if (playStatus == Constant.Player.RANDOM_PLAY) {
                playStatus = Constant.Player.LIST_CYCLE_PLAY;
            }
            setPlayStatus();
        }else if (id == R.id.bt_music_list){
            isPlay = false;
            Intent tIntent = new Intent(this, MusicListActivity.class);
            tIntent.putExtra(Constant.Player.keyDataMusicList, mMusicList);
            tIntent.putExtra(Constant.Player.keyCurrentPlayIndex, current_item);
            tIntent.putExtra(Constant.Player.keyContentType, contentType);
            startActivity(tIntent);
        }


    }

    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        manager.requestAudioFocus(changeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
        mediaPlayer.setVolume(0.5f, 0.5f);

        // 设置是否循环播放
        mediaPlayer.setLooping(false);

        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
//        // 当播放的时候一直让屏幕变亮
//        mediaPlayer.setScreenOnWhilePlaying(true);

        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
        wifiLock = ((WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
        wifiLock.acquire();


        initMediaPlayerListener();


    }

    private void initMediaPlayerListener() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int time = mp.getDuration();
                int length = time / 1000;
                int second = length % 60;
                int minute = length / 60;

                tvMusicTime.setText("/" + minute + ":" + (second < 10 ? "0" + second : second));
                mMusicProgressSb.setMax(time);
                LogUtil.e("MediaPlayer -----》" + time);
                mediaPlayer.start();
                isPause = false;
                if (contentType != Constant.Player.FM){
                    mProgressHandler.postDelayed(mProgressRunable, 1000);
                }
            }
        });

        // 网络流媒体缓冲监听
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                // i 0~100
//                Log.e("qob", "setOnBufferingUpdateListener 缓存进度" + i + "%");
            }
        });

        // 设置播放错误监听
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                //返回true 下一首的时候不会跳转两个
                return true;
            }
        });

        // 设置播放完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.e("qob", "setOnCompletionListener 播放完成 " + mediaPlayer.getCurrentPosition() + " total " + mediaPlayer.getDuration());
                mProgressHandler.removeCallbacks(mProgressRunable);
                mMusicProgressSb.setProgress(0);
                if (playStatus == Constant.Player.SINGLE_CYCLE_PLAY) {
                    getRandomIndex();

                    mProgressHandler.removeCallbacks(mProgressRunable);
                    mMusicProgressSb.setProgress(0);

                    if (isPause) {
                        isPause = false;
                    }
                    FDMusic tMusic = mMusicList.get(current_item);
                    play(tMusic,true);
                } else {
                    nextMusic();
                }
            }
        });

        // 设置进度调整完成SeekComplete监听，主要是配合seekTo(int)方法
        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                Log.e("qob", "setOnSeekCompleteListener seekTo完成");
            }
        });
    }

    // 上一首
    public void preMusic() {
        getRandomIndex();

        mProgressHandler.removeCallbacks(mProgressRunable);
        mMusicProgressSb.setProgress(0);

        if (isPause){
            isPause = false;
        }

        Log.e("qob", "preMusic current_item: " + current_item);

        if (playStatus == Constant.Player.RANDOM_PLAY){
            int ramdomNum = getMusicRandom();
            current_item = ramdomNum;
        }else {
            current_item--;
        }
        if (current_item < 0) {
            current_item = mMusicList.size() - 1;
        }
        FDMusic tMusic = mMusicList.get(current_item);
        play(tMusic,true);

    }

    /**
     * 播放
     */
    private void play(FDMusic tMusic,boolean isSendPlay) {

        try {
            isEventPuase = false;
            Log.e("qob", "play current_item: " + current_item);

            if (mediaPlayer == null) {
                initMediaPlayer();
            }

            if (isPause) {
                mediaPlayer.start();
                isPause = false;

                if (contentType != Constant.Player.FM){
                    mMusicPlayBt.setImageResource(R.mipmap.player_ico_pause);
                    mProgressHandler.postDelayed(mProgressRunable, 1000);
                }else {
                    imgFmPlay.setImageResource(R.mipmap.player_icon_fm_play);
                }
                if (isSendPlay){
                    PlayerMessage message = new PlayerMessage();
                    message.setMsgType(2);
                    EventBusUtils.post(message);
                }


            } else {
                if (!NetUtil.isNetworkAvailable(this)){
                    showToast("网络不可用");
                    return;
                }

                // 重置mediaPlayer
                mediaPlayer.reset();
                // 重新加载音频资源
                mediaPlayer.setDataSource(tMusic.getMusicUri());

                LogUtil.e(tMusic.toString());
                PlayerMessage message = new PlayerMessage();
                message.setMusicUrl(tMusic.getMusicImage());
                message.setMusicTitle(tMusic.getMusicTitle());
                message.setMusicName(tMusic.getMusicArtist());
                message.setContentType(contentType);
                message.setContent(tMusic.getContent());
                LogUtil.e("PlayerMessage musicActivity = "+message.toString());
                EventBusUtils.post(message);
                // 准备播放（异步）
                mediaPlayer.prepareAsync();
                runTimeSecond = 0;
                tvMusicRunTime.setText("00:00");
                tvMusicTime.setText("/00:00");

                mMusicPlayBt.setImageResource(R.mipmap.player_ico_pause);


                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(Constant.Player.keyCurrentPlayIndex);
                broadcastIntent.putExtra(Constant.Player.keyCurrentPlayIndex, current_item);
                sendBroadcast(broadcastIntent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 暂停播放
     *
     * @return 当前播放的位置
     */
    public int pause(boolean isManager) {
        if (isPause) {
            return -1;
        }
        mProgressHandler.removeCallbacks(mProgressRunable);
        isPause = true;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        if (contentType == Constant.Player.FM){
            imgFmPlay.setImageResource(R.mipmap.player_icon_fm_pause);
        }else {
            mMusicPlayBt.setImageResource(R.mipmap.player_ico_play);
        }
        if (!isManager){
            PlayerMessage message = new PlayerMessage();
            message.setMsgType(1);
            EventBusUtils.post(message);
        }



        return current_item;
    }


    /**
     * 下一首
     */
    private void nextMusic() {
        getRandomIndex();

        mProgressHandler.removeCallbacks(mProgressRunable);
        mMusicProgressSb.setProgress(0);

        if (isPause) {
            isPause = false;
        }

        Log.e("qob", "nextMusic current_item: " + current_item);
        if (playStatus == Constant.Player.RANDOM_PLAY) {
            int ramdomNum = getMusicRandom();
            current_item = ramdomNum;
        } else {
            current_item++;
        }
        if (current_item >= mMusicList.size()) {
            current_item = 0;
        }
        FDMusic tMusic = mMusicList.get(current_item);
        play(tMusic,true);
    }

    private int getMusicRandom() {
        musicRandom.clear();
        for (int i = 0; i < mMusicList.size(); i++) {
            if (current_item != i) {
                musicRandom.add(i);
            }
        }
        Random random = new Random();
        int index = random.nextInt(musicRandom.size() - 1);
        int randomNum = musicRandom.get(index);
        return randomNum;
    }



    private void stop(){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(MusicPlayBean bean) {
        LogUtil.i(bean.toString());
        String action = bean.getAidlMsgType();
        LogUtil.d("action = "+action);
        if (action.equals(Constant.Player.keyBroadcastMusicList)){
            stop();
            List<FDMusic> tMusicList = bean.getFdMusics();
            String title = bean.getMsgName();
            contentType = bean.getMsgType();
            mMusicList.clear();
            mMusicList.addAll(tMusicList);
            initTitle(title);
            mProgressHandler.removeCallbacks(mProgressRunable);
            mMusicProgressSb.setProgress(0);
            FDMusic tMusic = mMusicList.get(current_item);
            notifyFragment(tMusic);
            Log.e("qob", "MyBroadcastReceiver List " + mMusicList);
            current_item = 0;
            getRandomIndex();
            play(tMusic,true);
        } else if (action.equals(Constant.Player.keyBroadcastSelectItem)) {

            mProgressHandler.removeCallbacks(mProgressRunable);
            mMusicProgressSb.setProgress(0);

            int tCurrentIndex = bean.getCurrentItem();
            current_item = tCurrentIndex;

            Log.e("qob", "MyBroadcastReceiver List " + current_item + " keyBroadcastSelectItem");
            FDMusic tMusic = mMusicList.get(current_item);
            play(tMusic,true);
        }else if (action.equals(Constant.Player.closeVoiceBroadcast)){
            if (audioManagerSatatus != AudioManager.AUDIOFOCUS_GAIN){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            pause(false);
        }else if (action.equals(Constant.Player.VOICE_PLAY)){
            FDMusic tMusic = mMusicList.get(current_item);
            play(tMusic,true);
        }else if (action.equals(Constant.Player.VOICE_PAUSE)){
            isEventPuase = true;
            pause(false);
        }else if (action.equals(Constant.Player.VOICE_REPLAY)){
            isPause = false;
            FDMusic tMusic = mMusicList.get(current_item);
            play(tMusic,true);
        }else if (action.equals(Constant.Player.VOICE_STOP)){
            MusicActivity.this.finish();
        }else if (action.equals(Constant.Player.VOICE_PREV)){
            preMusic();
        }else if (action.equals(Constant.Player.VOICE_NEXT)){
            nextMusic();
        }else if (action.equals(Constant.Player.VOICE_LOOP_LIST_PLAY)){
            //列表循环
            playStatus = Constant.Player.LIST_CYCLE_PLAY;
            setPlayStatus();

        }else if (action.equals(Constant.Player.VOICE_LOOP_SINGLE_PLAY)){
            //单曲循环
            playStatus = Constant.Player.SINGLE_CYCLE_PLAY;
            setPlayStatus();

        }else if (action.equals(Constant.Player.VOICE_RANDOM_PLAY)){
            //随机播放
            playStatus = Constant.Player.RANDOM_PLAY;
            setPlayStatus();

        }else if (action.equals(Constant.Player.VOICE_FORWARD)){
            //快进
            int relativeTime = bean.getRelativeTime();
            int absoluteTime = bean.getAbsoluteTime();
            if (mediaPlayer != null){
                int position = getSeekToForwardPosition(relativeTime, absoluteTime);
                mediaPlayer.seekTo(position);
            }
        }else if (action.equals(Constant.Player.VOICE_BACKWARD)){
            //快退
            int relativeTime = bean.getRelativeTime();
            int absoluteTime = bean.getAbsoluteTime();
            if (mediaPlayer != null){
                int position = getSeekToBackwardPosition(relativeTime,absoluteTime);
                mediaPlayer.seekTo(position);
            }
        }else if (action.equals(Constant.Player.CLOSE_MUSIC_ACTIVITY)){
            this.finish();
        }

    }

    /**
     * 快退
     * @param relativeTime
     * @return
     */
    private int getSeekToBackwardPosition(int relativeTime,int absoluteTime) {
        if (relativeTime == 0 && absoluteTime == 0){
                return mediaPlayer.getCurrentPosition() - 10000;
        }else {
            if (relativeTime > 0){
                return mediaPlayer.getCurrentPosition() - (relativeTime * 1000);
            }else if (absoluteTime > 0){
                return absoluteTime * 1000;

            }
        }
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 快进
     * @param relativeTime
     * @param absoluteTime
     * @return
     */
    private int getSeekToForwardPosition(int relativeTime, int absoluteTime) {
        if (relativeTime == 0 && absoluteTime == 0){
            return mediaPlayer.getCurrentPosition() + 10000;
        }else {
            if (relativeTime > 0){
                return mediaPlayer.getCurrentPosition() + (relativeTime * 1000);
            }else if (absoluteTime > 0){
                return absoluteTime * 1000;
            }
        }
        return mediaPlayer.getCurrentPosition();
    }


    AudioManager.OnAudioFocusChangeListener changeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                audioManagerSatatus = focusChange;
                if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                    // 展示失去音频焦点，暂停播放等待重新获得音频焦点
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        mediaPlayer.setVolume(0.1f, 0.1f);
                        pause(true);
                    }

                    // Pause playback

                } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                    // 获得音频焦点
                    if (mediaPlayer != null && isPause && !isEventPuase) {
//                        mediaPlayer.setVolume(0.5f, 0.5f);
                        FDMusic music = mMusicList.get(current_item);
                        play(music,false);
                    }

                    // Resume playback

                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                    // 长久的失去音频焦点，释放MediaPlayer
                    stop();


                } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                    //失去焦点，降低音量
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                        mediaPlayer.setVolume(0.1f, 0.1f);
                        pause(true);
                    }
                }
            }
        };


        private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
            public MyFragmentPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        }


    }

