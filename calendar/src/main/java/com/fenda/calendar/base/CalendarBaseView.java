package com.fenda.calendar.base;

import com.alibaba.fastjson.JSONObject;
import com.fenda.common.mvp.BaseView;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description view接口
 */
public interface CalendarBaseView extends BaseView {

    /**
     *
     * @param weekDay 星期
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @param nlmontn 农历月份
     * @param nlday 农历日期
     */
    public void changeData(String weekDay, String year, String month, String day, String nlmontn, String nlday) ;

}
