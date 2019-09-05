package com.fenda.common.basebean.weather;

import java.util.List;

public class WeatherBean {
    /**
     * 今天的数据
     */
    private List<DataBena> forecastChoose;
    /**多天的数据
     *
     */
    private List<DataBena> forecast;
    /**空气质量数据
     *
     */
    private AqiBean aqi;
    /**
     * 城市名称
     */
    private String cityName;



    /**
     *  {"tempNight":"26",
     *             "predictDate":"2019-07-23",
     *             "tempDay":"32",
     *             "windDirDay":"无持续风向",
     *             "windLevelDay":"3",
     *             "updatetime":"2019-07-23 13:00:00",
     *             "temptip":"天气炎热，外出要注意防晒",
     *             "conditionDay":"多云",
     *             "conditionNight":"多云",
     *             "conditionDayNight":"多云"}
     */
    public static class DataBena{

        private String tempNight;
        private String tempDay;
        private String predictDate;
        private String windDirDay;
        private String windLevelDay;
        private String updatetime;
        private String temptip;
        private String conditionDay;
        private String conditionNight;
        private String conditionDayNight;

        public DataBena(String tempNight, String tempDay, String predictDate, String windDirDay, String windLevelDay, String updatetime, String temptip, String conditionDay, String conditionNight, String conditionDayNight) {
            this.tempNight = tempNight;
            this.tempDay = tempDay;
            this.predictDate = predictDate;
            this.windDirDay = windDirDay;
            this.windLevelDay = windLevelDay;
            this.updatetime = updatetime;
            this.temptip = temptip;
            this.conditionDay = conditionDay;
            this.conditionNight = conditionNight;
            this.conditionDayNight = conditionDayNight;
        }

        public DataBena() {

        }

        public String getTempNight() {
            return tempNight;
        }

        public void setTempNight(String tempNight) {
            this.tempNight = tempNight;
        }

        public String getTempDay() {
            return tempDay;
        }

        public void setTempDay(String tempDay) {
            this.tempDay = tempDay;
        }

        public String getPredictDate() {
            return predictDate;
        }

        public void setPredictDate(String predictDate) {
            this.predictDate = predictDate;
        }

        public String getWindDirDay() {
            return windDirDay;
        }

        public void setWindDirDay(String windDirDay) {
            this.windDirDay = windDirDay;
        }

        public String getWindLevelDay() {
            return windLevelDay;
        }

        public void setWindLevelDay(String windLevelDay) {
            this.windLevelDay = windLevelDay;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getTemptip() {
            return temptip;
        }

        public void setTemptip(String temptip) {
            this.temptip = temptip;
        }

        public String getConditionDay() {
            return conditionDay;
        }

        public void setConditionDay(String conditionDay) {
            this.conditionDay = conditionDay;
        }

        public String getConditionNight() {
            return conditionNight;
        }

        public void setConditionNight(String conditionNight) {
            this.conditionNight = conditionNight;
        }

        public String getConditionDayNight() {
            return conditionDayNight;
        }

        public void setConditionDayNight(String conditionDayNight) {
            this.conditionDayNight = conditionDayNight;
        }

        @Override
        public String toString() {
            return "DataBena{" +
                    "tempNight='" + tempNight + '\'' +
                    ", tempDay='" + tempDay + '\'' +
                    ", predictDate='" + predictDate + '\'' +
                    ", windDirDay='" + windDirDay + '\'' +
                    ", windLevelDay='" + windLevelDay + '\'' +
                    ", updatetime='" + updatetime + '\'' +
                    ", temptip='" + temptip + '\'' +
                    ", conditionDay='" + conditionDay + '\'' +
                    ", conditionNight='" + conditionNight + '\'' +
                    ", conditionDayNight='" + conditionDayNight + '\'' +
                    '}';
        }
    }

    /**
     *  "aqi" : "{"value":"22",
     * 				  "pm10C":"19.0",
     * 				  "coC":"5",
     * 				  "cityName":"深圳市",
     * 				  "AQL":"优",
     * 				  "no2":"15.0",
     * 				  "tip":"享受新鲜空气吧",
     * 				  "rank":"69\/578",
     * 				  "so2C":"4.0"}"
     */
    public static class AqiBean{

        private String value;
        private String pm10C;
        private String coC;
        private String cityName;
        private String AQL;
        private String no2;
        private String tip;
        private String rank;
        private String so2C;

        public AqiBean(String value, String pm10C, String coC, String cityName, String AQL, String no2, String tip, String rank, String so2C) {
            this.value = value;
            this.pm10C = pm10C;
            this.coC = coC;
            this.cityName = cityName;
            this.AQL = AQL;
            this.no2 = no2;
            this.tip = tip;
            this.rank = rank;
            this.so2C = so2C;
        }

        public AqiBean() {
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getPm10C() {
            return pm10C;
        }

        public void setPm10C(String pm10C) {
            this.pm10C = pm10C;
        }

        public String getCoC() {
            return coC;
        }

        public void setCoC(String coC) {
            this.coC = coC;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAQL() {
            return AQL;
        }

        public void setAQL(String AQL) {
            this.AQL = AQL;
        }

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getSo2C() {
            return so2C;
        }

        public void setSo2C(String so2C) {
            this.so2C = so2C;
        }

        @Override
        public String toString() {
            return "AqiBean{" +
                    "value='" + value + '\'' +
                    ", pm10C='" + pm10C + '\'' +
                    ", coC='" + coC + '\'' +
                    ", cityName='" + cityName + '\'' +
                    ", AQL='" + AQL + '\'' +
                    ", no2='" + no2 + '\'' +
                    ", tip='" + tip + '\'' +
                    ", rank='" + rank + '\'' +
                    ", so2C='" + so2C + '\'' +
                    '}';
        }
    }

    public WeatherBean(List<DataBena> forecastChoose, List<DataBena> forecast, AqiBean aqi, String cityName) {
        this.forecastChoose = forecastChoose;
        this.forecast = forecast;
        this.aqi = aqi;
        this.cityName = cityName;
    }

    public WeatherBean(List<DataBena> forecastChoose, List<DataBena> forecast, AqiBean aqi) {
        this.forecastChoose = forecastChoose;
        this.forecast = forecast;
        this.aqi = aqi;
    }

    public WeatherBean() {

    }

    public List<DataBena> getForecastChoose() {
        return forecastChoose;
    }

    public void setForecastChoose(List<DataBena> forecastChoose) {
        this.forecastChoose = forecastChoose;
    }

    public List<DataBena> getForecast() {
        return forecast;
    }

    public void setForecast(List<DataBena> forecast) {
        this.forecast = forecast;
    }

    public AqiBean getAqi() {
        return aqi;
    }

    public void setAqi(AqiBean aqi) {
        this.aqi = aqi;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "forecastChoose=" + forecastChoose +
                ", forecast=" + forecast +
                ", aqi=" + aqi +
                '}';
    }
}
