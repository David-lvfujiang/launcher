package com.fenda.calendar.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSONObject;
import com.fenda.calendar.model.Calendar;
import com.fenda.calendar.model.QueryLastDayBean;
import com.fenda.calendar.view.CalendarQueryDateActivity;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.ICalendarProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import static com.fenda.common.router.RouterPath.Calendar.CALENDAR_QUERY_LASTDAY_ACTIVITY;


/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 处理业务逻辑实现类
 */
@Route(path = RouterPath.Calendar.CALENDAR_PROVIDER)
public class CalendarPresenter implements ICalendarProvider {
    Context context;
    private final String QUERY_LAST_DAY = "查询天数";
    private final String QUERY_NEW_GUIDE = "新手引导";
    private final String QUERY_AGE = "查询年龄";
    private final String QUERY_ZODIAC = "查询生肖";
    private final String QUERY_WEEK = "查询星期";
    private final String QUERY_DATE = "查询日期";
    private final String QUERY_TIME = "查询时间";
    private final String QUERY_CONSTELLATION = "查询星座";
    private final String QUERY_HISTORY = "查询历史事件";
    private final String QUERY_YEAR = "查询年份";
    private final String QUERY_CONSTELLATION_TIME = "查询星座时间";
    private final String LAST_FESTIVAL_DATE = "查询节日节气";

    @Override
    public void processCalendarMsg(String msg) {
        processIntent( getIntentName(msg), msg);
    }

    /**
     * 获取意图
     * @param msg
     * @return
     */
    public String getIntentName(String msg) {
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
        String intentName = jsonObject.getAsJsonObject("dm").get("intentName").getAsString();
        return intentName;
    }

    /**
     * 处理意图
     * @param name
     * @param msg
     */
    public void processIntent(String name, String msg) {
        switch (name) {
            case QUERY_LAST_DAY: {
                LogUtil.e("查询天数");
                JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
                String year = jsonObject.getAsJsonObject("dm").getAsJsonObject("widget").getAsJsonObject("extra").get("year").getAsString();
                String date = jsonObject.getAsJsonObject("dm").getAsJsonObject("widget").getAsJsonObject("extra").get("text").getAsString();
                String lastDate = jsonObject.getAsJsonObject("dm").getAsJsonObject("widget").getAsJsonObject("extra").get("daysInterval").getAsString();
                ARouter.getInstance().build(CALENDAR_QUERY_LASTDAY_ACTIVITY).withString("year", year).withString("date", date).withString("lastDate", lastDate).navigation();
                LogUtil.e(year + "年，" + date + "剩余" + lastDate + "天");
                break;
            }
            case QUERY_NEW_GUIDE:
                LogUtil.e("新手引导");
                processContentTextMsg(msg);
                break;
            case QUERY_AGE:
                LogUtil.e("查询年龄");
                processContentTextMsg(msg);
                break;
            case QUERY_ZODIAC:
                LogUtil.e("查询生肖");
                processContentTextMsg(msg);
                break;
            case QUERY_WEEK:
                LogUtil.e("查询星期");
                getCalendarMsg(msg);
                break;
            case QUERY_DATE:
                LogUtil.e("查询日期");
                getCalendarMsg(msg);
                break;
            case QUERY_TIME: {
                LogUtil.e("查询时间");
                JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
                String nowTime = jsonObject.getAsJsonObject("dm").getAsJsonObject("widget").get("text").getAsString();
                Intent intent = new Intent();
                intent.setClass(BaseApplication.getBaseInstance(), CalendarQueryDateActivity.class);
                intent.putExtra("nowTime", nowTime);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApplication.getBaseInstance().startActivity(intent);
                break;
            }
            case QUERY_CONSTELLATION:
                processContentTextMsg(msg);
                break;
            case QUERY_HISTORY:
                processContentTextMsg(msg);
                break;
            case QUERY_YEAR:
                LogUtil.e("查询年份");
                processContentTextMsg(msg);
                break;
            case QUERY_CONSTELLATION_TIME:
                LogUtil.e("查询星座时间");
                processContentTextMsg(msg);
                break;
            case LAST_FESTIVAL_DATE:
                LogUtil.e("查询节日节气");
                processContentTextMsg(msg);
                break;
            default:
                break;
        }
    }

    /**
     * 处理文本消息
     *
     * @param msg
     */
    public void processContentTextMsg(String msg) {
        JsonObject jsonObject = new JsonParser().parse(msg).getAsJsonObject();
        String content = jsonObject.getAsJsonObject("dm").get("nlg").getAsString();
        String title = jsonObject.getAsJsonObject("dm").get("input").getAsString();
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
