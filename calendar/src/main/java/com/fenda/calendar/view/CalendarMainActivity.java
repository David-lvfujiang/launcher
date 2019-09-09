package com.fenda.calendar.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import com.fenda.calendar.R;
import com.fenda.calendar.model.Calendar;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;

import java.lang.reflect.Field;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 日历
 */
@Route(path = RouterPath.Calendar.CALENDAR_ACTIVITY)
public class CalendarMainActivity extends BaseActivity {
    @Autowired
    Calendar calendar;
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private TextView mTvWeekDay, mTvYear, mTvMonth, mTvNlDay;
    private ImageView mImgMonth, mImgDay;
    private int[] datas = {R.mipmap.calendar_zero_icon, R.mipmap.calendar_one_icon, R.mipmap.calendar_two_icon, R.mipmap.calendar_three_icon, R.mipmap.calendar_four_icon, R.mipmap.calendar_five_icon, R.mipmap.calendar_six_icon, R.mipmap.calendar_seven_icon, R.mipmap.calendar_eight_icon, R.mipmap.calendar_nine_icon};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE:    // 关闭界面
                    CalendarMainActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int onBindLayout() {
        ARouter.getInstance().inject(this);
        return R.layout.calendar_main_activity;
    }

    @Override
    public void initView() {
        //消除状态栏底部的阴影
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }
        ARouter.getInstance().inject(this);
        mTvWeekDay = findViewById(R.id.week_tv);
        mTvYear = findViewById(R.id.year_tv);
        mTvMonth = findViewById(R.id.month_tv);
        mTvNlDay = findViewById(R.id.lunar_calendar_day_tv);
        mImgMonth = findViewById(R.id.calendar_month_img);
        mImgDay = findViewById(R.id.calendar_day_img);
    }


    @Override
    public void initData() {
        if (calendar != null) {
            changeData(calendar.getWeekday(), calendar.getYear(), calendar.getMonth(), calendar.getDay(), calendar.getNlmonth(), calendar.getNlday());
        }
    }

    /**
     * 动态修改日历
     *
     * @param weekDay 星期
     * @param year    年份
     * @param month   月份
     * @param day     日期
     * @param nlmonth
     * @param nlday   农历日期
     */

    public void changeData(String weekDay, String year, String month, String day, String nlmonth, String nlday) {
        int monthNumber = 0;
        int dayNumber = 0;

        if (day.length() > 1) {
            monthNumber = Integer.parseInt(String.valueOf(day.charAt(0)));
            dayNumber = Integer.parseInt(String.valueOf(day.charAt(1)));
            mImgMonth.setImageResource(datas[monthNumber]);
            mImgDay.setImageResource(datas[dayNumber]);
        } else {
            dayNumber = Integer.parseInt(String.valueOf(day.charAt(0)));
            mImgMonth.setImageResource(datas[0]);
            mImgDay.setImageResource(datas[dayNumber]);
        }
        mTvWeekDay.setText(weekDay);
        mTvYear.setText(year + "年");
        mTvMonth.setText(month);
        mTvNlDay.setText("农历" + nlmonth + nlday);
        // 5秒后关闭界面
          handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 6000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        calendar = getIntent().getParcelableExtra("calendar");
        initData();

    }
}
