package com.fenda.calendar.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.fenda.calendar.model.Calendar;
import com.fenda.common.provider.ICalendarProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;

import static com.fenda.common.router.RouterPath.Calendar.HOLIDAY_ACTIVITY;


/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 处理业务逻辑实现类
 */
@Route(path = RouterPath.Calendar.CALENDAR_PROVIDER)
public class CalendarPresenter implements ICalendarProvider {
    Context context;

    /**
     * 日历信息
     *
     * @param msg
     */
    @Override
    public void getCalendarMsg(String msg) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);//json对象转字符串
            JSONObject object = jsonObject.getJSONObject("dm").getJSONObject("widget").getJSONObject("extra");
            String weekDay = object.getString("weekday");
            String year = object.getString("year");
            String month = object.getString("month");
            String day = object.getString("day");
            String nlmonth = object.getString("nlmonth");
            String nlday = object.getString("nlday");
            Calendar calendar = new Calendar(weekDay, year, month, day, nlmonth, nlday);
            Log.e("json", calendar.toString());
            ARouter.getInstance().build(RouterPath.Calendar.CALENDAR_ACTIVITY).withParcelable("calendar", calendar).navigation();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

    }

    /**
     * 节假日信息
     *
     * @param msg
     */
    @Override
    public void getHolidayMsg(String msg) {
        try {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //标题
        String title = jsonObject.getJSONObject("dm").get("input").toString();
        JSONObject holidayJson = jsonObject.getJSONObject("dm").getJSONObject("widget").getJSONObject("extra").getJSONObject("holiday");
        //放假时间
        String holidayStartingTime = holidayJson.get("b").toString();
        //收假时间

        String holidayEndTime = holidayJson.get("e").toString();
        //节日名
        // String festivalName = jsonObject.getJSONObject("nlu").getJSONObject("semantics").get("text").toString();
        Log.e("TAG", title);
        Log.e("TAG", holidayStartingTime);
        Log.e("TAG", holidayEndTime);
        // Log.e("TAG", festivalName);
        ARouter.getInstance().build(HOLIDAY_ACTIVITY).withString("holidayStartTime", holidayStartingTime).withString("holidayEndTime", holidayEndTime).navigation();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
    }


    @Override
    public void init(Context context) {
        this.context = context;
    }
}
