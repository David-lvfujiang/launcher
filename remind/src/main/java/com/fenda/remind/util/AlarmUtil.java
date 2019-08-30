package com.fenda.remind.util;

import android.text.TextUtils;
import android.widget.ImageView;


import com.fenda.remind.R;
import com.fenda.remind.bean.AlarmBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class AlarmUtil {



    public static int getTimeToImg(String num){
        int number = Integer.parseInt(num);
        switch (number){
            case 0:
                return R.mipmap.remind_zero;
            case 1:
                return R.mipmap.remind_one;
            case 2:
                return R.mipmap.remind_two;
            case 3:
                return R.mipmap.remind_three;
            case 4:
                return R.mipmap.remind_four;
            case 5:
                return R.mipmap.remind_five;
            case 6:
                return R.mipmap.remind_six;
            case 7:
                return R.mipmap.remind_seven;
            case 8:
                return R.mipmap.remind_eight;
            case 9:
                return R.mipmap.remind_nine;
        }
        return R.mipmap.remind_zero;
    }



    public static void setAlarmImg(String time, ImageView left1,ImageView left2,ImageView right1,ImageView right2){
        if (!TextUtils.isEmpty(time)){
            String[] timeSplit = time.split(":");
            String leftTime = timeSplit[0];
            String leftStr1 = leftTime.substring(0,leftTime.length()-1);
            left1.setImageResource(getTimeToImg(leftStr1));
            String leftStr2 = leftTime.substring(1);
            left2.setImageResource(getTimeToImg(leftStr2));
            String rightTime = timeSplit[1];
            String rightStr1 = rightTime.substring(0,rightTime.length()-1);
            right1.setImageResource(getTimeToImg(rightStr1));
            String rightStr2 = rightTime.substring(1);
            right2.setImageResource(getTimeToImg(rightStr2));


        }
    }

    public static String getAlarmDate(AlarmBean alarmBean) {
        String mTime;
        String date = alarmBean.getDate();
        Date mDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            mDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
        mTime = dateFormat.format(mDate);
        return mTime;
    }


    public static String getRepeat(String repeat){
        StringBuilder builder = new StringBuilder();
        if (repeat.contains("W")){
            builder.append("每周");
            for (int i = 1; i < 8; i++) {
                if (repeat.contains(String.valueOf(i))){
                    if (i == 1){
                        builder.append("一");
                    }else if (i == 2){
                        builder.append("二");
                    }else if (i == 3){
                        builder.append("三");
                    }else if (i == 4){
                        builder.append("四");
                    }else if (i == 5){
                        builder.append("五");
                    }else if (i == 6){
                        builder.append("六");
                    }else if (i == 7){
                        builder.append("日");
                    }
                    builder.append("、");
                }
            }
            if (builder.toString().length() == 16){
                return "每天";
            }
        }else if (repeat.contains("M")){
            builder.append("每月");
            for (int i = 1; i < 31; i ++){
                if (repeat.contains(String.valueOf(i))){
                    builder.append(i);
                    builder.append("号、");
                }
            }
        }
        String str = builder.toString();
        int index = str.lastIndexOf("、");
        if (index != -1){
            str = str.substring(0,index);
        }
        return str;

    }
}
