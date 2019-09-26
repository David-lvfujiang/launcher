package com.fenda.remind.provider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IAlarmProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.remind.AlarmListActivity;
import com.fenda.remind.bean.AlarmBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author matt.Ljp
 * @time 2019/9/26 10:36
 * @description
 */
@Route(path = RouterPath.REMIND.ALARM_RUERY)
public class AlarmQuery implements IAlarmProvider {
    private Context mContext = BaseApplication.getInstance();

    @Override
    public void queryAlarm(String data) {
        if (data!=null){
            ArrayList<AlarmBean> mList = getAlarmBeanList(data);
            Bundle mBundle = new Bundle();
            mBundle.putParcelableArrayList(Constant.Remind.ALARM_LIST, mList);
            mBundle.putString(Constant.Remind.ALARM_TYPE, Constant.Remind.QUERY_REMIND);
            Intent mIntent = new Intent(mContext, AlarmListActivity.class);
            mIntent.putExtras(mBundle);
            mContext.startActivity(mIntent);
        }else {
            Intent mIntent = new Intent(mContext, AlarmListActivity.class);
            mContext.startActivity(mIntent);
        }

    }

    @Override
    public void init(Context context) {

    }
    private ArrayList<AlarmBean> getAlarmBeanList(String data) {
        ArrayList<AlarmBean> mList = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                AlarmBean bean = getAlarmBean(object);
                mList.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }
    private AlarmBean getAlarmBean(JSONObject object) {
        AlarmBean bean = new AlarmBean();
        bean.setId(object.optInt("id"));
        bean.setDate(object.optString("date"));
        bean.setRepeat(object.optString("repeat"));
        bean.setRepeat_asr(object.optString("repeat_asr"));
        bean.setPeriod(object.optString("period"));
        bean.setTime(object.optString("time"));
        bean.setObject(object.optString("object"));
        bean.setEvent(object.optString("event"));
        bean.setVid(object.optString("vid"));
        bean.setTimestamp(object.optLong("timestamp"));
        bean.setOperation(object.optString("operation"));
        bean.setRecent_tsp(object.optLong("recent_tsp"));
        bean.setTime_interval(object.optString("time_interval"));
        return bean;
    }
}
