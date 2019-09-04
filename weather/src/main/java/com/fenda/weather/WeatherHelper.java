package com.fenda.weather;

import android.content.Context;
import android.content.Intent;

import com.example.weather.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WeatherHelper {

    /**
     * 天气界面是否已打开
     */
    public static String keyBroadcastWeatherAction = "keyBroadcastWeatherAction";



    public static String keyWeatherCity        = "keyWeatherCity";
    public static String keyWeatherName        = "keyWeatherName";
    public static String keyWeatherTemperature = "keyWeatherTemperature";
    public static String keyWeatherForecastDateArray   = "keyWeatherForecastDateArray";   //7天天气日期
    public static String keyWeatherForecastTempArray = "keyWeatherForecastTempArray";     //7天天气气温



    /********  keyWeatherCode  ******/
    public static final int kWeatherCode_Sunny = 100;      //晴

    public static final int kWeatherCode_RainLow = 120;       //小雨
    public static final int kWeatherCode_RainBig = 121;       //大雨
    public static final int kWeatherCode_RainMid = 122;       //中雨

    public static final int kWeatherCode_SnowMid = 140;       //中雪
    public static final int kWeatherCode_SnowBig = 141;       //大雪
    public static final int kWeatherCode_SnowLow = 142;       //小雪

    public static final int kWeatherCode_Cloudy = 160;        //多云
    public static final int kWeatherCode_Yin = 180;           //阴

    public static void openWeather(Context context, HashMap<String, Object> weatherMap){

        if (weatherMap != null){
            Intent tIntent = new Intent(context, WeatherActivity.class);
            tIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            tIntent.putExtra(keyWeatherCity, (String)weatherMap.get(keyWeatherCity));
            tIntent.putExtra(keyWeatherTemperature, (String)weatherMap.get(keyWeatherTemperature));

            tIntent.putExtra(keyWeatherName, (String[])weatherMap.get(keyWeatherName));
            tIntent.putExtra(keyWeatherForecastDateArray, (String[])weatherMap.get(keyWeatherForecastDateArray));
            tIntent.putExtra(keyWeatherForecastTempArray, (String[])weatherMap.get(keyWeatherForecastTempArray));
            context.startActivity(tIntent);
        }
    }


    public static int codeFromWeahterName(String weatherName){

        int retCode = kWeatherCode_Yin;

        if (weatherName.contains("晴")){
            retCode = kWeatherCode_Sunny;
        }
        else if (weatherName.contains("阴")){
            retCode = kWeatherCode_Yin;
        }
        else if (weatherName.contains("多云")){
            retCode = kWeatherCode_Cloudy;
        }
        else if (weatherName.contains("小雨")){
            retCode = kWeatherCode_RainLow;
        }
        else if (weatherName.contains("大雨")){
            retCode = kWeatherCode_RainBig;
        }
        else if (weatherName.contains("中雨")){
            retCode = kWeatherCode_RainMid;
        }
        else if (weatherName.contains("中雪")){
            retCode = kWeatherCode_SnowMid;
        }
        else if (weatherName.contains("大雪")){
            retCode = kWeatherCode_SnowBig;
        }
        else if (weatherName.contains("小雪")){
            retCode = kWeatherCode_SnowLow;
        }


        return retCode;
    }

    public static int resourcesIdWithCode(int code){

        int retResId = 0;

        switch (code){
            case kWeatherCode_Sunny:{
                    retResId = R.mipmap.weather_bg_100;
                break;
            }
            case kWeatherCode_SnowLow:{
                retResId = R.mipmap.weather_bg_142;
                break;
            }
            case kWeatherCode_SnowMid:{
                retResId = R.mipmap.weather_bg_140;
                break;
            }
            case kWeatherCode_SnowBig:{
                retResId = R.mipmap.weather_bg_141;
                break;
            }
            case kWeatherCode_RainLow:{
                retResId = R.mipmap.weather_bg_120;
                break;
            }
            case kWeatherCode_RainMid:{
                retResId = R.mipmap.weather_bg_122;
                break;
            }
            case kWeatherCode_RainBig:{
                retResId = R.mipmap.weather_bg_121;
                break;
            }
            case kWeatherCode_Cloudy:{
                retResId = R.mipmap.weather_bg_160;
                break;
            }
            case kWeatherCode_Yin:{
                retResId = R.mipmap.weather_bg_180;
                break;
            }
            default:{
                break;
            }
        }

        return retResId;
    }

    public static int iconIdWithCode(int code, boolean isOn){

        int retResId = 0;

        switch (code){
            case kWeatherCode_Sunny:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_100_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_100;
                }
                break;
            }
            case kWeatherCode_SnowLow:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_142_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_142;
                }
                break;
            }
            case kWeatherCode_SnowMid:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_140_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_140;
                }

                break;
            }
            case kWeatherCode_SnowBig:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_141_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_141;
                }

                break;
            }
            case kWeatherCode_RainLow:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_120_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_120;
                }

                break;
            }
            case kWeatherCode_RainMid:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_122_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_122;
                }
                break;
            }
            case kWeatherCode_RainBig:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_121_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_121;
                }
                break;
            }
            case kWeatherCode_Cloudy:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_160_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_160;
                }
                break;
            }
            case kWeatherCode_Yin:{
                if (isOn){
                    retResId = R.mipmap.weather_icon_180_on;
                }
                else {
                    retResId = R.mipmap.weather_icon_180;
                }
                break;
            }
            default:{
                break;
            }
        }

        return retResId;
    }


    public static String dateToWeek(String datetime) {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}