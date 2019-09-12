package com.fenda.calendar.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.calendar.R;
import com.fenda.common.base.BaseActivity;

import static com.fenda.common.router.RouterPath.Calendar.HOLIDAY_ACTIVITY;

/**
 * @Describe 节假日查询activity
 * @Author David-LvFuJiang
 * @Data 2019.9.11
 */
@Route(path = HOLIDAY_ACTIVITY)
public class HolidayMainActivity extends BaseActivity {
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private TextView mTvHolidayStartTime, mTtvHolidayEndTime;
    @Autowired
    String holidayStartTime;
    @Autowired
    String holidayEndTime;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE:    // 关闭界面
                    HolidayMainActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int onBindLayout() {
        return R.layout.activity_holiday_main;
    }

    @Override
    public void initView() {
        mTvHolidayStartTime = findViewById(R.id.holiday_start_time_tv);
        mTtvHolidayEndTime = findViewById(R.id.holiday_end_time_tv);
    }

    @Override
    public void initData() {
        changeData();
    }

    public void changeData() {
        mTvHolidayStartTime.setText(holidayStartTime);
        mTtvHolidayEndTime.setText(holidayEndTime);
        handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 5000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeData();
    }


}
