package com.fenda.remind;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.constant.Constant;
import com.fenda.protocol.tcp.bus.EventBusUtils;
import com.fenda.remind.bean.AlarmBean;
import com.fenda.remind.util.AlarmUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class AlarmActivity extends Activity {
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_activity_alarm);
        initView();
        initData();
        addListener();


    }

    private void initView(){
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

    private void initData() {
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
        }

    }


    private void addListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmActivity.this.finish();
            }
        });
        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmActivity.this.finish();
                Intent intent = new Intent(Constant.Remind.ACTION_CLOSE_ALARM);
                sendBroadcast(intent);
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AlarmBean bean){
        if (bean.getType() == Constant.Remind.CLOSE_ALARM){
            this.finish();
        }

    }


    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();
    }
}
