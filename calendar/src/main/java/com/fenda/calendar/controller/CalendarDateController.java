package com.fenda.calendar.controller;

import com.fenda.calendar.data.CalendarDate;
import com.fenda.calendar.data.Lunar;
import com.fenda.calendar.data.Solar;
import com.fenda.calendar.utils.CalendarUtils;
import com.fenda.calendar.utils.LunarSolarConverter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by joybar on 2/24/16.
 */
public class CalendarDateController {

    public static List<CalendarDate> getCalendarDate(int year, int month) {
        List<CalendarDate> mListDate = new ArrayList<>();
        List<CalendarUtils.CalendarSimpleDate> list = null;
        try {
            list = CalendarUtils.getEverydayOfMonth(year, month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count = list.size();

        for (int i = 0; i < count; i++) {
            Solar solar = new Solar();
            solar.solarYear = list.get(i).getYear();
            solar.solarMonth = list.get(i).getMonth();
            solar.solarDay = list.get(i).getDay();
            Lunar lunar = LunarSolarConverter.SolarToLunar(solar);
            mListDate.add(new CalendarDate( month == list.get(i).getMonth(), false,solar,lunar));
        }

        return mListDate;
    }


}
