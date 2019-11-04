package com.fenda.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.basebean.player.MusicPlayBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.news.adapter.ViewPagerCardAdapter;
import com.fenda.news.view.TextSwitchView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;
import java.util.List;
import java.util.Random;


/**
 * @author matt.liaojianpeng
 */
@Route(path = RouterPath.NEWS.NEWS_ACTIVITY)
public class NewsActivity extends BaseActivity {

    @Autowired
    List<FDMusic> newsListData;

    private MusicPlayBean mMusicPlayBean;
//    private List<FDMusic> newsListData;
    private int mCurrentItem;
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private WifiManager.WifiLock mWifiLock;
    private boolean isPause;
    private ViewPagerCardAdapter mViewPagerCardAdapter;
//    private TextView tvNewsBack;
    private ViewPager vpNewsList;
    private LinearLayout linNewsBg;
    private TextSwitchView tsNewsTips;

    private int [] newsBg = {R.mipmap.news_bg_01, R.mipmap.news_bg_02, R.mipmap.news_bg_03};
    private String []newsTips = {
            "可以试试“你好小乐，返回”",
            "可以试试“你好小乐，播放下一条”",
            "可以试试“你好小乐，播放上一条”",
            "可以试试“你好小乐，暂停播放”"
    };
    @Override
    public int onBindLayout() {
        return R.layout.activity_news;
    }

    @Override
    public void initView() {
        mMusicPlayBean = getIntent().getParcelableExtra(NewsPlay.keyNews);
        if (mMusicPlayBean!=null && newsListData==null){
            newsListData = mMusicPlayBean.getFdMusics();
        }
        linNewsBg = findViewById(R.id.lin_news_bg);
//        tvNewsBack = findViewById(R.id.tv_news_back);
        tsNewsTips = findViewById(R.id.ts_news_tips);
//        tvNewsBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NewsActivity.this.finish();
//            }
//        });
        vpNewsList = findViewById(R.id.view_page);
        vpNewsList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                play();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPagerCardAdapter = new ViewPagerCardAdapter(this,newsListData);
        vpNewsList.setAdapter(mViewPagerCardAdapter);
        //预加载3个
        vpNewsList.setOffscreenPageLimit(3);
        //设置viewpage之间的间距
        vpNewsList.setPageMargin(30);
        vpNewsList.setClipChildren(false);

        tsNewsTips.setResources(newsTips);
        tsNewsTips.setTextStillTime(5000);
    }

    @Override
    public void initData() {
        play();
        initMediaPlayer();
        randomBg();
        initTitle("今日资讯");
        NewsPlay.isNewsAcitivytOpen = true;
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        List<FDMusic> tMusicList = getIntent().getParcelableExtra(NewsPlay.keyNews);
        newsListData.clear();
        if (newsListData != null && tMusicList != null){
            newsListData.addAll(tMusicList);
        }
        if (mViewPagerCardAdapter != null){
            mViewPagerCardAdapter.notifyDataSetChanged();
        }
        randomBg();
        mCurrentItem = 0;
        play();

    }

    /**
     * 切换背景
     */
    private void randomBg(){
        Random random = new Random();
        int index = random.nextInt(newsBg.length-1);
        linNewsBg.setBackgroundResource(newsBg[index]);
    }
    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        if (mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
        }
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(changeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        // 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
        mMediaPlayer.setVolume(0.5f, 0.5f);
        // 设置是否循环播放
        mMediaPlayer.setLooping(false);
        // 设置设备进入锁状态模式-可在后台播放或者缓冲音乐-CPU一直工作
        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        // 当播放的时候一直让屏幕变亮
        mMediaPlayer.setScreenOnWhilePlaying(true);
        // 如果你使用wifi播放流媒体，你还需要持有wifi锁
        mWifiLock = ((WifiManager) getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "wifilock");
        mWifiLock.acquire();

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer.start();
                isPause = false;
            }
        });
        // 网络流媒体缓冲监听
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                // i 0~100
                Log.e("qob", "setOnBufferingUpdateListener 缓存进度" + i + "%");
            }
        });

        // 设置播放错误监听
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.reset();
                Log.e("qob", "setOnErrorListener");
                return true;
            }
        });

        // 设置播放完成监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.e("qob", "setOnCompletionListener 播放完成 " + mediaPlayer.getCurrentPosition() + " total " + mediaPlayer.getDuration());
                int index = mCurrentItem;

                index++;
                if (index >= newsListData.size()){
                    index = 0;
                }
                vpNewsList.setCurrentItem(index);
                //                  nextMusic();
            }
        });

        // 设置进度调整完成SeekComplete监听，主要是配合seekTo(int)方法
        mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mediaPlayer) {
                Log.e("qob", "setOnSeekCompleteListener seekTo完成");
            }
        });
    }
    AudioManager.OnAudioFocusChangeListener changeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                // 展示失去音频焦点，暂停播放等待重新获得音频焦点
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.setVolume(0.1f,0.1f);
                }
                // Pause playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // 获得音频焦点
                if (mMediaPlayer != null){
                    mMediaPlayer.setVolume(0.5f, 0.5f);
                }

                // Resume playback
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // 长久的失去音频焦点，释放MediaPlayer
                if (mMediaPlayer != null){
                    stop();
                }

            }else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                //失去焦点，降低音量
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.setVolume(0.1f,0.1f);
                }
            }
        }
    };

    /**
     * 上一首
     */
    public void preMusic() {

        if (isPause){
            isPause = false;
        }

        Log.e("qob", "preMusic current_item: " + mCurrentItem);
        mCurrentItem--;
        if (mCurrentItem < 0){
            mCurrentItem = newsListData.size() - 1;
        }
        vpNewsList.setCurrentItem(mCurrentItem);
    }

    /**
     * 播放
     */
    private void play() {
        try {
            Log.e("qob", "play current_item: " + mCurrentItem);
            if (mMediaPlayer == null){
                initMediaPlayer();
            }
            if (isPause) {
                mMediaPlayer.start();
                isPause = false;
            } else {
                // 重置mediaPlayer
                mMediaPlayer.reset();
                // 重新加载音频资源
                FDMusic tMusic = newsListData.get(mCurrentItem);
                mMediaPlayer.setDataSource(tMusic.getMusicUri());
                // 准备播放（异步）
                mMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     * @return 当前播放的位置
     */
    public int pause() {
        if(isPause){
            return -1;
        }

        isPause = true;
        if (mMediaPlayer != null){
            mMediaPlayer.pause();
        }

        return mCurrentItem;
    }

    /**
     * 下一首
     */
    private void nextMusic() {
        if (isPause){
            isPause = false;
        }

        Log.e("qob", "nextMusic current_item: " + mCurrentItem);
        mCurrentItem++;
        if (mCurrentItem >= newsListData.size()){
            mCurrentItem = 0;
        }
        vpNewsList.setCurrentItem(mCurrentItem);
    }
    /**
     * 停止播放
     */
    private void stop() {
        if (mMediaPlayer != null){
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("qob", "NewsActivity onStart");
        BaseApplication.getBaseInstance().setNewsPlay(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
        BaseApplication.getBaseInstance().setNewsPlay(false);
        stop();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(MusicPlayBean bean){
        String action = bean.getAidlMsgType();
        if (action.equals(Constant.Player.keyBroadcastMusicList)){
            stop();
            List<FDMusic> tMusicList = mMusicPlayBean.getFdMusics();
            newsListData.clear();
            if (newsListData != null && tMusicList != null){
                newsListData.addAll(tMusicList);
            }
            if (mViewPagerCardAdapter != null){
                mViewPagerCardAdapter.notifyDataSetChanged();
            }
            randomBg();
            mCurrentItem = 0;
            play();
        } else if (action.equals(Constant.Player.closeVoiceBroadcast)){
            pause();
        }else if (action.equals(Constant.Player.VOICE_PLAY)){
            play();
        }else if (action.equals(Constant.Player.VOICE_PAUSE)){
            pause();
        }else if (action.equals(Constant.Player.VOICE_REPLAY)){
            isPause = false;
            play();
        }else if (action.equals(Constant.Player.VOICE_STOP)){
            stop();
            finish();
        }else if (action.equals(Constant.Player.VOICE_PREV)){
            preMusic();

        }else if (action.equals(Constant.Player.VOICE_NEXT)){
            nextMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NewsPlay.isNewsAcitivytOpen = false;
    }
}
