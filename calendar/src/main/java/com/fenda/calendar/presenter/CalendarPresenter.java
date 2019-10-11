package com.fenda.calendar.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.fenda.calendar.model.Calendar;
import com.fenda.calendar.model.GuideBean;
import com.fenda.calendar.model.QueryLastDayBean;
import com.fenda.calendar.view.CalendarQueryDateActivity;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.ICalendarProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static com.fenda.common.router.RouterPath.Calendar.CALENDAR_QUERY_LASTDAY_ACTIVITY;
import static com.fenda.common.router.RouterPath.Calendar.HOLIDAY_ACTIVITY;


/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 处理业务逻辑实现类
 */
@Route(path = RouterPath.Calendar.CALENDAR_PROVIDER)
public class CalendarPresenter implements ICalendarProvider {
    Context context;
    private final String QUERY_LAST_DAY = "剩余天数";
    private final String QUERY_NEW_GUIDE = "新手引导";
    private final String QUERY_AGE = "查询年龄";
    private final String QUERY_ZODIAC = "生肖";
    private final String QUERY_WEEK = "查询星期";
    private final String QUERY_DATE = "日期";
    private final String QUERY_TIME = "查询时间";
    private final String QUERY_CONSTELLATION = "查询星座";
    private final String QUERY_HISTORY = "查询历史事件";
    private final String QUERY_YEAR = "查询年份";
    private final String QUERY_CONSTELLATION_TIME = "查询星座时间";
    private final String LAST_FESTIVAL_DATE = "查询节日节气";


    @Override
    public void processCalendarMsg(String msg) {
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonObject("nlu").getAsJsonObject("semantics").getAsJsonObject("request").getAsJsonArray("slots");
        JsonObject intentJson1 = (JsonObject) jsonArray.get(0);
        String intentName1 = intentJson1.get("value").getAsString();
        JsonObject intentJson2 = (JsonObject) jsonArray.get(1);
        String intentName2 = intentJson2.get("value").getAsString();

        if (QUERY_LAST_DAY.equals(intentName1) || QUERY_LAST_DAY.equals(intentName2)) {
            LogUtil.e("剩余天数");
            QueryLastDayBean bean = new Gson().fromJson(msg, QueryLastDayBean.class);
            String year = bean.getDm().getWidget().getExtra().getYear();
            String date = bean.getDm().getWidget().getExtra().getText();
            String lastDate = String.valueOf(bean.getDm().getWidget().getExtra().getDaysInterval());
            ARouter.getInstance().build(CALENDAR_QUERY_LASTDAY_ACTIVITY).withString("year", year).withString("date", date).withString("lastDate", lastDate).navigation();
            LogUtil.e(year + "年，" + date + "剩余" + lastDate + "天");

        } else if (QUERY_NEW_GUIDE.equals(intentName1) || QUERY_NEW_GUIDE.equals(intentName2)) {
            LogUtil.e("新手引导");
            processContentTextMsg(msg);
        } else if (QUERY_AGE.equals(intentName1) || QUERY_AGE.equals(intentName2)) {
            LogUtil.e("查询年龄");
            processContentTextMsg(msg);
        } else if (QUERY_ZODIAC.equals(intentName1) || QUERY_ZODIAC.equals(intentName2)) {
            LogUtil.e("生肖");
            processContentTextMsg(msg);
        } else if (QUERY_WEEK.equals(intentName1) || QUERY_WEEK.equals(intentName2)) {
            LogUtil.e("查询星期");
            getCalendarMsg(msg);
        } else if (QUERY_DATE.equals(intentName1) || QUERY_DATE.equals(intentName2)) {
            LogUtil.e("日期");
            getCalendarMsg(msg);
        } else if (QUERY_TIME.equals(intentName1) || QUERY_TIME.equals(intentName2)) {
            LogUtil.e("查询时间");
            QueryLastDayBean bean = new Gson().fromJson(msg, QueryLastDayBean.class);
            String nowTime = bean.getDm().getWidget().getText();
            Intent intent = new Intent();
            intent.setClass(BaseApplication.getBaseInstance(), CalendarQueryDateActivity.class);
            intent.putExtra("nowTime", nowTime);
            BaseApplication.getBaseInstance().startActivity(intent);
        } else if (QUERY_CONSTELLATION.equals(intentName1) || QUERY_CONSTELLATION.equals(intentName2)) {
            processContentTextMsg(msg);
        } else if (QUERY_HISTORY.equals(intentName1) || QUERY_HISTORY.equals(intentName2)) {
            processContentTextMsg(msg);
        } else if (QUERY_YEAR.equals(intentName1) || QUERY_YEAR.equals(intentName2)) {
            LogUtil.e("查询年份");
            processContentTextMsg(msg);
        } else if (QUERY_CONSTELLATION_TIME.equals(intentName1) || QUERY_CONSTELLATION_TIME.equals(intentName2)) {
            LogUtil.e("查询星座时间");
            processContentTextMsg(msg);
        } else if (LAST_FESTIVAL_DATE.equals(intentName1) || LAST_FESTIVAL_DATE.equals(intentName2)) {
            LogUtil.e("查询节日节气");
            processContentTextMsg(msg);
        }
    }

    /**
     * 处理文本消息
     *
     * @param msg
     */
    public void processContentTextMsg(String msg) {
        GuideBean bean = new Gson().fromJson(msg, GuideBean.class);
        String content = bean.getDm().getNlg();
        String title = bean.getDm().getInput();
        ARouter.getInstance().build(RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY).withString("content", content).withString("title", title).navigation();
    }

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

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
