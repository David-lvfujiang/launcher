package com.fenda.remind.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.UFormat;
import android.os.Bundle;
import android.os.RemoteException;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IRemindProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.protocol.tcp.bus.EventBusUtils;
import com.fenda.remind.AlarmActivity;
import com.fenda.remind.AlarmListActivity;
import com.fenda.remind.bean.AlarmBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 8:35
 * @Description
 */
@Route(path = RouterPath.REMIND.ALARM_SERVICE)
public class AlarmService implements IRemindProvider {


    private Context mContext;


    @Override
    public void createAlarm(String data) {
        ArrayList<AlarmBean> mList = getAlarmBeanList(data);
        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList("alarmList",mList);
        mBundle.putString("alarmType",Constant.Remind.CREATE_REMIND);
        Intent mIntent = new Intent(mContext, AlarmActivity.class);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);


    }

    private ArrayList<AlarmBean> getAlarmBeanList(String data) {
        ArrayList<AlarmBean> mList = new ArrayList<>();
        JSONArray array = null;
        try {
            array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.optJSONObject(i);
                AlarmBean bean = getAlarmBean(object);
                mList.add(bean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mList;
    }

    @Override
    public void queryAlarm(String data) {
        ArrayList<AlarmBean> mList = getAlarmBeanList(data);
        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList(Constant.Remind.ALARM_LIST,mList);
        mBundle.putString(Constant.Remind.ALARM_TYPE,Constant.Remind.QUERY_REMIND);
        Intent mIntent = new Intent(mContext, AlarmListActivity.class);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);

    }

    @Override
    public void deleteAlarmComplete(String data) {
        ArrayList<AlarmBean> mList = getAlarmBeanList(data);
        if (mList.size() > 0){
            AlarmBean bean = mList.get(0);
            bean.setType(2);
            EventBusUtils.post(bean);
        }
    }

    @Override
    public void deleteAlarmStart(String data) {
        final ArrayList<AlarmBean> mList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(data);
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.optJSONObject(i);
                AlarmBean bean = getAlarmBean(object);
                bean.setType(1);
                mList.add(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((Activity)mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle mBundle = new Bundle();
                mBundle.putParcelableArrayList(Constant.Remind.ALARM_LIST,mList);
                mBundle.putString(Constant.Remind.ALARM_TYPE,Constant.Remind.DELETE_REMIND);
                Intent mIntent = new Intent(mContext, AlarmListActivity.class);
                mIntent.putExtras(mBundle);
                mContext.startActivity(mIntent);
            }
        });
    }



    @Override
    public void closeAlarm(String type)  {
        AlarmBean bean = new AlarmBean();
        bean.setType(Constant.Remind.CLOSE_ALARM);
        EventBusUtils.post(bean);


    }

    @Override
    public void closeRemind(String data) {
        AlarmBean bean = new AlarmBean();
        bean.setType(Constant.Remind.CLOSE_ALARM);
        EventBusUtils.post(bean);
    }

    @Override
    public void alarmRing(String data) {
        try {
            startRemindRing(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void startRemindRing(String data) throws JSONException {
        ArrayList<AlarmBean> mList = new ArrayList<>();
        JSONObject object = new JSONObject(data);
        AlarmBean bean = getAlarmBean(object);
        mList.add(bean);
        Bundle mBundle = new Bundle();
        mBundle.putParcelableArrayList("alarmList",mList);
        mBundle.putString("alarmType", Constant.Remind.ALARM_REMIND);
        Intent mIntent = new Intent(mContext, AlarmActivity.class);
        mIntent.putExtras(mBundle);
        mContext.startActivity(mIntent);
    }

    @Override
    public void remindRing(String data) {
        try {
            startRemindRing(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    @Override
    public void init(Context context) {
        this.mContext = context;

    }
}