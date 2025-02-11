package com.fenda.calendar.view;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.calendar.R;
import com.fenda.calendar.data.CalendarDate;
import com.fenda.calendar.fragment.CalendarViewFragment;
import com.fenda.calendar.fragment.CalendarViewPagerFragment;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author matt.Ljp
 * @time 2019/9/25 9:25
 * @description
 */
@Route(path = RouterPath.Calendar.Perpetual_CALENDAR_ACTIVITY)
public class PerpetualCalendarActivity extends BaseActivity implements
        CalendarViewPagerFragment.OnPageChangeListener,
        CalendarViewFragment.OnDateClickListener,
        CalendarViewFragment.OnDateCancelListener {

    private boolean isChoiceModelSingle = false;
    private List<CalendarDate> mListDate = new ArrayList<>();
    private MyHandler handler = new MyHandler(this);

    public static class MyHandler extends Handler {
        private final WeakReference<PerpetualCalendarActivity> mActivity;

        private MyHandler(PerpetualCalendarActivity activity){
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PerpetualCalendarActivity activity = mActivity.get();
            if (activity != null){
                activity.initDataView();
            }

        }


    }

    @Override
    public int onBindLayout() {
        return R.layout.perpetual_calendar_activity;
    }

    @Override
    public void initView() {
        initTitle("万年历");
//        tvDate = findViewById(R.id.tv_perpetual_calendar_year_month);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        },300);
    }

    private void initDataView() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // Fragment fragment = new CalendarViewPagerFragment();
        CalendarViewPagerFragment fragment = CalendarViewPagerFragment.newInstance(true);
        ft.replace(R.id.fl_content, fragment);
//        ft.commit();
        ft.commitAllowingStateLoss();
    }


    @Override
    public void initData() {
//        findViewById(R.id.calculator_iv_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    @Override
    public void onDateClick(CalendarDate calendarDate) {
        int year = calendarDate.getSolar().solarYear;
        int month = calendarDate.getSolar().solarMonth;
        int day = calendarDate.getSolar().solarDay;
    }

//    @Override
//    public void onDateCancel(CalendarDate calendarDate) {
//        int count = mListDate.size();
//        for (int i = 0; i < count; i++) {
//            CalendarDate date = mListDate.get(i);
//            if (date.getSolar().solarDay == calendarDate.getSolar().solarDay) {
//                mListDate.remove(i);
//                break;
//            }
//        }
//    }

    @Override
    public void onPageChange(int year, int month) {
//        tvDate.setText(year + "年" + month + "月");
        mListDate.clear();
    }

    @Override
    public void onDateCancel(CalendarDate calendarDate) {

    }
}
