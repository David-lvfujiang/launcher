package com.fenda.settings.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/30 11:32
 */
@Route(path = RouterPath.SETTINGS.SettingsVolumeActivity)
public class SettingsVolumeActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsVolumeActivity";

    private ImageView volumeBackIv;
    private ImageView volumeIconIv;

    SeekBar volumeBar;

    private AudioManager mAudioManager;
    private int maxVolume, currentVolume;
    private boolean mRegistered;

    MyVolumeReceiver mVolumeReceiver;
    //    MyVolumeSeekBarChangeListener myVolumeSeekBarChangeListener;
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_volume_layout;
    }

    @Override
    public void initView() {
        volumeBackIv = findViewById(R.id.volume_back_iv);
        volumeBar = findViewById(R.id.volume_seekbar);
        volumeIconIv = findViewById(R.id.volume_icon);
        mVolumeReceiver = new MyVolumeReceiver();
//        myVolumeSeekBarChangeListener = new MyVolumeSeekBarChangeListener();
        //获取系统的Audio管理者
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtil.d(TAG, "current volume = " + currentVolume);
        //seekbar设置最大值为最大音量，这样设置当前进度时不用换算百分比了
        volumeBar.setMax(maxVolume);
        volumeBar.setProgress(currentVolume);
        myRegisterVolumeReceiver();//注册同步更新的广播
//        volumeBar.setOnSeekBarChangeListener(new MyVolumeSeekBarChangeListener());
    }

    @Override
    public void initData() {
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //设置媒体音量为当前seekbar进度
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if(i == 0) {
                    volumeIconIv.setImageResource(R.mipmap.settings_volume_mute);
                } else {
                    volumeIconIv.setImageResource(R.mipmap.settings_volume);
                }
                LogUtil.d(TAG, "volume = " + i);
                LogUtil.d(TAG, "currentVolume 2 = " + currentVolume);
                volumeBar.setProgress(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void initListener() {
        volumeBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 注册当音量发生变化时接收的广播
     */
    private void myRegisterVolumeReceiver(){
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("android.media.VOLUME_CHANGED_ACTION") ;
        this.registerReceiver(mVolumeReceiver, filter) ;
        mRegistered = true;
    }
    /**
         * 解注册音量广播监听器，需要与 registerReceiver 成对使用
         */
    public void unregisterVolumeReceiver() {
        if (mRegistered) {
            try {
                this.unregisterReceiver(mVolumeReceiver);
//            myVolumeSeekBarChangeListener = null;
                mRegistered = false;
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    /**
     * 处理音量变化时的界面显示
     * @author long
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if(("android.media.VOLUME_CHANGED_ACTION").equals(intent.getAction())){
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) ;// 当前的媒体音量
                volumeBar.setProgress(currVolume) ;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterVolumeReceiver();
        super.onDestroy();
    }
}
