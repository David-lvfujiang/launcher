package com.fenda.calendar.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.calendar.R;
import com.fenda.common.base.BaseActivity;

import static com.fenda.common.router.RouterPath.Calendar.CALENDAR_QUERY_LASTDAY_ACTIVITY;

@Route(path = CALENDAR_QUERY_LASTDAY_ACTIVITY)
public class CalendarQueryLastDayActivity extends BaseActivity {
    TextView mTvDate, mTvLastDay;
    @Autowired
    String year;
    @Autowired
    String date;
    @Autowired
    String lastDate;
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE:    // 关闭界面
                    CalendarQueryLastDayActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public int onBindLayout() {
        return R.layout.activity_calendar_query_last_day;
    }

    @Override
    public void initView() {
        mTvDate = findViewById(R.id.tv_day);
        mTvLastDay = findViewById(R.id.tv_last_day);
    }

    @Override
    public void initData() {
        mTvDate.setText(year+"年"+date);
        mTvLastDay.setText(lastDate);
        // 5秒后关闭界面
        handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 5000);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //清除handler
        handler.removeMessages(AUDIO_CONVERSE_CLOSE);
        setIntent(intent);
        initData();

    }
}
