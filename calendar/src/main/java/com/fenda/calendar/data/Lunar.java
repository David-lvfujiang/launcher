package com.fenda.calendar.data;

/**
 * Created by joybar on 9/12/16.
 */
public class Lunar {
    public boolean isleap;
    public int lunarDay;
    public int lunarMonth;
    public int lunarYear;
    //甲子年
    public String lunarYearToJiaZi;
    public boolean isLFestival;
    public String lunarFestivalName;//农历节日


    final static String chineseNumber[] =
            {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    /**
     * 显示农历日期
     *
     * @param day
     * @return
     */
    public  String getChinaDayString(int day) {
        String chineseTen[] =
                {"初", "十", "廿", "三"};
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30) {
            return "";
        }
        if (day == 10) {
            return "初十";
        } else {
            return chineseTen[day / 10] + chineseNumber[n];
        }
    }

    /**
     * 显示农历月份
     *
     * @param month
     * @return
     */
    public String getLunarMonthString(int month) {
        String LunarMonthString[] = {"春节", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
        return LunarMonthString[month-1];
    }

    /**
     * 判断是否为每月的第一天
     * @param day
     * @return
     */
    public boolean isFristDay(int day){
        if (day == 1) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        return "Lunar [isleap=" + isleap + ", lunarDay=" + lunarDay
                + ", lunarMonth=" + lunarMonth + ", lunarYear=" + lunarYear
                + ", isLFestival=" + isLFestival + ", lunarFestivalName="
                + lunarFestivalName + "]";
    }
}
