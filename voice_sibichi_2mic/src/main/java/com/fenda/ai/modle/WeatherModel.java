package com.fenda.ai.modle;

import android.os.RemoteException;

import com.fenda.common.basebean.weather.WeatherBean;
import com.google.gson.Gson;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/3 16:00
 * @Description
 */
public class WeatherModel {






    public void weather(String type, String text) throws RemoteException {
        try{
            WeatherBean bean = new Gson().fromJson(text, WeatherBean.class);


            final HashMap tWeatherArgMap = new HashMap();
            WeatherBean.DataBena weatherData = bean.getForecast().get(0);
//            tWeatherArgMap.put(FDWeatherHelper.keyWeatherCity, bean.getCityName());
//            tWeatherArgMap.put(FDWeatherHelper.keyWeatherTemperature, weatherData.getTempDay());

            String[] weatherNameArr = new String[7];
            String[] weatherDateArr = new String[7];
            String[] weatherTempRangeArr = new String[7];

            try {
                for (int i = 0; i < 7; i++) {
                    WeatherBean.DataBena tWeatherData = bean.getForecast().get(i);
                    weatherNameArr[i] = tWeatherData.getConditionDayNight();
                    weatherDateArr[i] = tWeatherData.getPredictDate();
                    weatherTempRangeArr[i] = tWeatherData.getTempNight() + "℃ ~ " + tWeatherData.getTempDay() + "℃";
                }
            }
            catch (Exception ex){
                for (int i = 0; i < 7; i++) {
                    weatherNameArr[i] = "30";
                    weatherDateArr[i] = "";
                    weatherTempRangeArr[i] = "";
                }
            }

//            tWeatherArgMap.put(FDWeatherHelper.keyWeatherName, weatherNameArr);
//            tWeatherArgMap.put(FDWeatherHelper.keyWeatherForecastDateArray, weatherDateArr);
//            tWeatherArgMap.put(FDWeatherHelper.keyWeatherForecastTempArray, weatherTempRangeArr);


            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("");
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
//                            FDWeatherHelper.openWeather(mContext, tWeatherArgMap);
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
