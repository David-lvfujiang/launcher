package com.fenda.calendar.view;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.calendar.R;
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

    private Calendar calendar;
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private TextView tvWeek, tvYear, tvMonth, tvNlDay;
    private ImageView imgMonth, imgDay;
    private int[] datas = {R.mipmap.calendar_zero_icon, R.mipmap.calendar_one_icon, R.mipmap.calendar_two_icon, R.mipmap.calendar_three_icon, R.mipmap.calendar_four_icon, R.mipmap.calendar_five_icon, R.mipmap.calendar_six_icon, R.mipmap.calendar_seven_icon, R.mipmap.calendar_eight_icon, R.mipmap.calendar_nine_icon};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE: 	// 关闭界面
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {}
        }
        ARouter.getInstance().inject(this);
        tvWeek = findViewById(R.id.week_tv);
        tvYear = findViewById(R.id.year_tv);
        tvMonth = findViewById(R.id.month_tv);
        tvNlDay = findViewById(R.id.lunar_calendar_day_tv);
        imgMonth = findViewById(R.id.calendar_month_img);
        imgDay = findViewById(R.id.calendar_day_img);
    }


    @Override
    public void initData() {
        Intent intent = getIntent();
        calendar = (Calendar) intent.getSerializableExtra("calendar");
        changeData(calendar.getWeekday(), calendar.getYear(), calendar.getMonth(), calendar.getDay(), calendar.getNlmonth(), calendar.getNlday());
    }

    /**
     * 动态修改日历
     * @param weekDay 星期
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @param nlmonth
     * @param nlday 农历日期
     */

    public void changeData(String weekDay, String year, String month, String day, String nlmonth, String nlday) {
        int monthNumber = 0;
        int dayNumber = 0;
        if (Character.isDigit(day.charAt(0))){  // 判断是否是数字
            monthNumber = Integer.parseInt(String.valueOf(day.charAt(0)));
        }
        if (Character.isDigit(day.charAt(1))){  // 判断是否是数字
            dayNumber = Integer.parseInt(String.valueOf(day.charAt(1)));
        }
        tvWeek.setText(weekDay);
        tvYear.setText(year+"年");
        tvMonth.setText(month);
        imgMonth.setImageResource(datas[monthNumber]);
        imgDay.setImageResource(datas[dayNumber]);
        tvNlDay.setText("农历"+nlmonth+nlday);
       // 5秒后关闭界面
      //  handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 5000);
    }



}
