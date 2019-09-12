package com.fenda.common.bean;

public class WeatherWithHomeBean {

    private String weatherTempNum;
    private int weatherIconId;

    public String getWeatherTempNum() {
        return weatherTempNum;
    }

    public void setWeatherTempNum(String weatherTempNum) {
        this.weatherTempNum = weatherTempNum;
    }

    public int getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(int weatherIconId) {
        this.weatherIconId = weatherIconId;
    }

    public WeatherWithHomeBean(String tempNum, int iconId){
        weatherTempNum = tempNum;
        weatherIconId = iconId;
    }
}
