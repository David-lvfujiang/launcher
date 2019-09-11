package com.fenda.remind;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.protocol.tcp.bus.EventBusUtils;
import com.fenda.remind.bean.AlarmBean;
import com.fenda.remind.util.AlarmUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/30 15:58
  * @Description
  *
  */
@Route(path = RouterPath.REMIND.ALARM)
public class AlarmActivity extends BaseActivity {
    ImageView imgBack;
    ImageView imgLeftOne;
    ImageView imgLeftTwo;
    ImageView imgRightTwo;
    ImageView imgRightOne;
    TextView tvDate;
    ImageView imageView;
    TextView tvStop;
    TextView tvText;

    private ArrayList<AlarmBean> alarmBeans;
    private String type;
    private CountDownTimer timer;


    @Override
    public int onBindLayout() {
        return R.layout.remind_activity_alarm;
    }

    @Override
    public void initView(){
        imgBack = findViewById(R.id.img_back);
        imgLeftOne = findViewById(R.id.img_left_one);
        imgLeftTwo = findViewById(R.id.img_left_two);
        imgRightTwo = findViewById(R.id.img_right_two);
        imgRightOne = findViewById(R.id.img_right_one);
        tvDate      = findViewById(R.id.tv_date);
        imageView   = findViewById(R.id.imageView);
        tvStop      = findViewById(R.id.tv_stop);
        tvText      = findViewById(R.id.tv_text);


    }

    private void countDownTime() {
        CountDownTimer timer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                AlarmActivity.this.finish();
            }
        };
        timer.start();
    }

    @Override
    public void initData() {
        imageView.setImageResource(R.mipmap.remind_alarm_book);
        Intent mIntent = getIntent();
        alarmBeans = mIntent.getParcelableArrayListExtra("alarmList");
        type = mIntent.getStringExtra("alarmType");
        AlarmBean alarmBean = alarmBeans.get(0);
        AlarmUtil.setAlarmImg(alarmBean.getTime(), imgLeftOne, imgLeftTwo, imgRightOne, imgRightTwo);
        if (Constant.Remind.CREATE_REMIND.equals(type)) {
            String repeat = alarmBean.getRepeat();
            String mTime;
            if (!TextUtils.isEmpty(repeat)) {
                mTime = AlarmUtil.getRepeat(repeat);
            } else {
                mTime = AlarmUtil.getAlarmDate(alarmBean);
            }
            tvDate.setText(getResources().getString(R.string.remind_alarm_time_source, mTime, alarmBean.getPeriod()));
            countDownTime();
        } else if (Constant.Remind.ALARM_REMIND.equals(type)) {
            tvText.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
            tvDate.setVisibility(View.GONE);
            tvStop.setVisibility(View.VISIBLE);
            BaseApplication.getInstance().setRemindRing(true);
            startAlarm();
        }

    }






    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    private void startAlarm()  {
        Uri notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone ringtone = RingtoneManager.getRingtone(mContext,notify);
        if (ringtone != null && !ringtone.isPlaying()){
            ringtone.setLooping(true);
            ringtone.play();
        }

        timer = new CountDownTimer(300000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {}
            @Override
            public void onFinish() {
                timer.cancel();
                ringtone.stop();
            }
        };
        timer.start();
    }

    public void closeAlarm(){
        if (timer != null){
            timer.onFinish();
            timer = null;
        }
    }





    @Override
    public void initListener() {
        super.initListener();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAlarm();
                AlarmActivity.this.finish();
            }
        });
        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAlarm();
                AlarmActivity.this.finish();
//                AlarmActivity.this.finish();
//                Intent intent = new Intent(Constant.Remind.ACTION_CLOSE_ALARM);
//                sendBroadcast(intent);
            }
        });
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AlarmBean bean){
        if (bean.getType() == Constant.Remind.CLOSE_ALARM){
            closeAlarm();
            this.finish();
        }

    }


    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        BaseApplication.getInstance().setRemindRing(false);
        super.onDestroy();
    }
}
