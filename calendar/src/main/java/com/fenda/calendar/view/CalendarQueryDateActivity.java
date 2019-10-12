package com.fenda.calendar.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.fenda.calendar.R;
import com.fenda.common.base.BaseActivity;

public class CalendarQueryDateActivity extends BaseActivity {
    TextView mTvTime;
    String nowTime = "";
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE:    // 关闭界面
                    CalendarQueryDateActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int onBindLayout() {
        return R.layout.activity_calendar_query_date;
    }

    @Override
    public void initView() {
        mTvTime = findViewById(R.id.calendar_tv_now_time);
    }

    @Override
    public void initData() {
        mTvTime.setText(nowTime);
        // 5秒后关闭界面
        handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 5000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        nowTime = intent.getStringExtra("nowTime");
        initData();
    }
}
