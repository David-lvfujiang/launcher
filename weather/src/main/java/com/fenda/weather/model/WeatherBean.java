package com.fenda.weather.model;

import java.util.List;

public class WeatherBean {
    /**
     * 数据的数据
     */
    private Extra extra;

    /**
     * 城市名称
     */
    private String cityName;

    public static class Extra {
        private List<DataBena> future;

        public Extra(List<DataBena> future) {
            this.future = future;
        }

        public List<DataBena> getFuture() {
            return future;
        }
    }


    /**
     * {"tempNight":"26",
     * "predictDate":"2019-07-23",
     * "tempDay":"32",
     * "windDirDay":"无持续风向",
     * "windLevelDay":"3",
     * "updatetime":"2019-07-23 13:00:00",
     * "temptip":"天气炎热，外出要注意防晒",
     * "conditionDay":"多云",
     * "conditionNight":"多云",
     * "conditionDayNight":"多云"}
     */
    public static class DataBena {

        private String week;
        private String temperature;
        private String date;
        private String weather;
        private String windLevel;
        private String wind;

        public DataBena(String week, String temperature, String date, String weather, String windLevel, String wind) {
            this.week = week;
            this.temperature = temperature;
            this.date = date;
            this.weather = weather;
            this.windLevel = windLevel;
            this.wind = wind;
        }

        public DataBena() {

        }

        public String getWeek() {
            return week;
        }

        public String getTemperature() {
            return temperature;
        }

        public String getDate() {
            return date;
        }

        public String getWeather() {
            return weather;
        }

        public String getWindLevel() {
            return windLevel;
        }

        public String getWind() {
            return wind;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public void setWindLevel(String windLevel) {
            this.windLevel = windLevel;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        @Override
        public String toString() {
            return "DataBena{" +
                    "week='" + week + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", date='" + date + '\'' +
                    ", weather='" + weather + '\'' +
                    ", windLevel='" + windLevel + '\'' +
                    ", wind='" + wind + '\'' +
                    '}';
        }
    }

    /**
     * "aqi" : "{"value":"22",
     * "pm10C":"19.0",
     * "coC":"5",
     * "cityName":"深圳市",
     * "AQL":"优",
     * "no2":"15.0",
     * "tip":"享受新鲜空气吧",
     * "rank":"69\/578",
     * "so2C":"4.0"}"
     */

    public WeatherBean(Extra extra, String cityName) {
        this.extra = extra;
        this.cityName = cityName;
    }

    public Extra getExtra() {
        return extra;
    }

    public String getCityName() {
        return cityName;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "extra=" + extra +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
