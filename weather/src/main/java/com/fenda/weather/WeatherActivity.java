package com.fenda.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;

import java.util.ArrayList;

@Route(path = RouterPath.Weather.WEATHER_MAIN)
public class WeatherActivity extends BaseActivity implements View.OnClickListener {

    private TextView mCityNameTv;
    private TextView mTempTv;
    private TextView mWeatherNameTv;
    private ImageView mWeahterBgIv;

    private ImageView mWeekIconIv0;
    private ImageView mWeekIconIv1;
    private ImageView mWeekIconIv2;
    private ImageView mWeekIconIv3;
    private ImageView mWeekIconIv4;
    private ImageView mWeekIconIv5;
    private ImageView mWeekIconIv6;

    private TextView mWeekDateTv1;
    private TextView mWeekDateTv2;
    private TextView mWeekDateTv3;
    private TextView mWeekDateTv4;
    private TextView mWeekDateTv5;
    private TextView mWeekDateTv6;

    private TextView mWeekTempTv0;
    private TextView mWeekTempTv1;
    private TextView mWeekTempTv2;
    private TextView mWeekTempTv3;
    private TextView mWeekTempTv4;
    private TextView mWeekTempTv5;
    private TextView mWeekTempTv6;

    private ArrayList<ImageView> mWeekIconIvArr = new ArrayList();
    private ArrayList<TextView> mWeekDateTvArr = new ArrayList<>();
    private ArrayList<TextView> mWeekTempTvArr = new ArrayList<>();


    @Override
    public int onBindLayout() {
        return R.layout.weather_activity;
    }

    @Override
    public void initView(){
        findViewById(R.id.tv_navbar_back).setOnClickListener(this);

        mWeahterBgIv = findViewById(R.id.gif_weather_bg);
        mCityNameTv = findViewById(R.id.tv_weather_name);
        mTempTv = findViewById(R.id.tv_weather_curtemp);
        mWeatherNameTv = findViewById(R.id.tv_weather_type);

        mWeekIconIv0 = findViewById(R.id.iv_weather_icon);
        mWeekIconIv1 = findViewById(R.id.iv_week1_icon);
        mWeekIconIv2 = findViewById(R.id.iv_week2_icon);
        mWeekIconIv3 = findViewById(R.id.iv_week3_icon);
        mWeekIconIv4 = findViewById(R.id.iv_week4_icon);
        mWeekIconIv5 = findViewById(R.id.iv_week5_icon);
        mWeekIconIv6 = findViewById(R.id.iv_week6_icon);
        mWeekIconIvArr.add(mWeekIconIv0);
        mWeekIconIvArr.add(mWeekIconIv1);
        mWeekIconIvArr.add(mWeekIconIv2);
        mWeekIconIvArr.add(mWeekIconIv3);
        mWeekIconIvArr.add(mWeekIconIv4);
        mWeekIconIvArr.add(mWeekIconIv5);
        mWeekIconIvArr.add(mWeekIconIv6);

        mWeekDateTv1 = findViewById(R.id.tv_week1_date);
        mWeekDateTv2 = findViewById(R.id.tv_week2_date);
        mWeekDateTv3 = findViewById(R.id.tv_week3_date);
        mWeekDateTv4 = findViewById(R.id.tv_week4_date);
        mWeekDateTv5 = findViewById(R.id.tv_week5_date);
        mWeekDateTv6 = findViewById(R.id.tv_week6_date);

        mWeekDateTvArr.add(mWeekDateTv1);
        mWeekDateTvArr.add(mWeekDateTv2);
        mWeekDateTvArr.add(mWeekDateTv3);
        mWeekDateTvArr.add(mWeekDateTv4);
        mWeekDateTvArr.add(mWeekDateTv5);
        mWeekDateTvArr.add(mWeekDateTv6);

        mWeekTempTv0 = findViewById(R.id.tv_weather_temp);
        mWeekTempTv1 = findViewById(R.id.tv_week1_temp);
        mWeekTempTv2 = findViewById(R.id.tv_week2_temp);
        mWeekTempTv3 = findViewById(R.id.tv_week3_temp);
        mWeekTempTv4 = findViewById(R.id.tv_week4_temp);
        mWeekTempTv5 = findViewById(R.id.tv_week5_temp);
        mWeekTempTv6 = findViewById(R.id.tv_week6_temp);
        mWeekTempTvArr.add(mWeekTempTv0);
        mWeekTempTvArr.add(mWeekTempTv1);
        mWeekTempTvArr.add(mWeekTempTv2);
        mWeekTempTvArr.add(mWeekTempTv3);
        mWeekTempTvArr.add(mWeekTempTv4);
        mWeekTempTvArr.add(mWeekTempTv5);
        mWeekTempTvArr.add(mWeekTempTv6);
    }

    @Override
    public void initData() {
        updateView(getIntent());
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_navbar_back){
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        updateView(getIntent());
    }

    private void updateView(Intent weatherIntent){


        try{
            String tkeyCity = weatherIntent.getStringExtra(WeatherHelper.keyWeatherCity);
            String tkeyTmep = weatherIntent.getStringExtra(WeatherHelper.keyWeatherTemperature);
            String[] tWeatherNameArr = weatherIntent.getStringArrayExtra(WeatherHelper.keyWeatherName);
            String[] tWeatherDateArr = weatherIntent.getStringArrayExtra(WeatherHelper.keyWeatherForecastDateArray);
            String[] tWeatherTempArr = weatherIntent.getStringArrayExtra(WeatherHelper.keyWeatherForecastTempArray);

            mCityNameTv.setText(tkeyCity);
            mTempTv.setText(tkeyTmep);

            for (int i = 0; i < tWeatherNameArr.length; i++){
                String tWeatherName = tWeatherNameArr[i];
                String tWeatherDate = tWeatherDateArr[i];
                String tWeatherTemp = tWeatherTempArr[i];
                int tWeatherCode = WeatherHelper.codeFromWeahterName(tWeatherName);


                if (i == 0){
                    mWeatherNameTv.setText(tWeatherName);
                    mWeahterBgIv.setBackgroundResource(WeatherHelper.resourcesIdWithCode(tWeatherCode));
                    mWeekIconIvArr.get(i).setImageResource(WeatherHelper.iconIdWithCode(tWeatherCode, true));
                }
                else {
                    mWeekDateTvArr.get(i - 1).setText(WeatherHelper.dateToWeek(tWeatherDate));  //显示日期
                    mWeekIconIvArr.get(i).setImageResource(WeatherHelper.iconIdWithCode(tWeatherCode, false));
                }

                mWeekTempTvArr.get(i).setText(tWeatherTemp);  //范围天气
            }


        }catch (Exception ex){
            Log.e("fd", "updateView " + ex);
        }
    }
}
