package com.fenda.calendar.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSONObject;
import com.fenda.calendar.base.ICalendarProvider;
import com.fenda.calendar.model.Calendar;
import com.fenda.calendar.view.CalendarMainActivity;
import com.fenda.common.BaseApplication;

import static com.fenda.common.router.RouterPath.Calendar.CALENDAR_PROVIDER;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 处理业务逻辑实现类
 */
@Route(path = CALENDAR_PROVIDER)
public class CalendarPresenTer implements ICalendarProvider {
    Context context;
    /**
     * 过滤json字符串获取日历信息
     * @param msg
     */
    @Override
    public void getCalendarMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);//json对象转字符串
        JSONObject object = jsonObject.getJSONObject("dm").getJSONObject("widget").getJSONObject("extra");
        String weekDay = object.getString("weekday");
        String year = object.getString("year");
        String month = object.getString("month");
        String day = object.getString("day");
        String nlmonth = object.getString("nlmonth");
        String nlday = object.getString("nlday");
        Calendar calendar = new Calendar(weekDay, year, month, day, nlmonth, nlday);
        Log.e("json",calendar.toString());

        Intent intent = new Intent();
        intent.putExtra("calendar", calendar);
        intent.setClass( BaseApplication.getInstance(), CalendarMainActivity.class);
        BaseApplication.getInstance().startActivity(intent);
        //路由跳转
        //ARouter.getInstance().build(RouterPath.Calendar.CALENDAR_ACTIVITY).navigation();

    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
