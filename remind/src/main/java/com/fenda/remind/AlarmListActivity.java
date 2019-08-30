package com.fenda.remind;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.fenda.common.constant.Constant;
import com.fenda.protocol.tcp.bus.EventBusUtils;
import com.fenda.remind.adapter.AlarmAdapter;
import com.fenda.remind.bean.AlarmBean;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class AlarmListActivity extends Activity {


    ImageView imgAlarmBack;
    TextView tvAlarmEdit;
    RecyclerView alarmList;
    private ArrayList<AlarmBean> alarmBeans;
    private String alarmType;

    private AlarmAdapter mAdapter;
    private CountDownTimer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_activity_alarm_list);
        EventBusUtils.register(this);
        alarmBeans = getIntent().getParcelableArrayListExtra(Constant.Remind.ALARM_LIST);
        alarmType   = getIntent().getStringExtra(Constant.Remind.ALARM_TYPE);
        initView();
        initData();
        addListener();
        countDownTime();

    }


    private void initView(){
        imgAlarmBack    = findViewById(R.id.img_alarm_back);
        tvAlarmEdit     = findViewById(R.id.tv_alarm_edit);
        alarmList       = findViewById(R.id.alarm_list);

    }


    private void initData() {
        if (Constant.Remind.DELETE_REMIND.equals(alarmType)){
            tvAlarmEdit.setText("完成");
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        alarmList.setLayoutManager(manager);
        alarmList.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AlarmAdapter(this, alarmBeans);
        alarmList.setAdapter(mAdapter);
    }


    private void addListener(){
        imgAlarmBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null){
                    timer.cancel();
                }
                AlarmListActivity.this.finish();
            }
        });

        tvAlarmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvAlarmEdit.getText().toString();
                if (text.equals("编辑")) {
                    tvAlarmEdit.setText("完成");
                }else {
                    tvAlarmEdit.setText("编辑");
                }
                if (alarmBeans != null ){
                    for (AlarmBean bean : alarmBeans) {
                        if (bean.getType() == 0){
                            bean.setType(1);
                        }else if (bean.getType() == 1){
                            bean.setType(0);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (timer != null){
                    timer.cancel();
                    timer = null;
                    countDownTime();
                }



            }
        });
    }

    private void countDownTime() {
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                AlarmListActivity.this.finish();
            }
        };
        timer.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEevent(AlarmBean bean){
        if (bean.getType() == Constant.Remind.DELETE_ALARM){
            if (alarmBeans != null){
                for (AlarmBean alarmBean : alarmBeans) {
                    if (alarmBean.getVid().equals(bean.getVid())){
                        alarmBeans.remove(alarmBean);
                    }
                }
            }
            if (mAdapter != null){
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onDestroy() {
        EventBusUtils.unregister(this);
        super.onDestroy();

    }
}
