package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/4 9:28
 * @Description
 */
public interface IRemindProvider extends IProvider {
    /**创建闹钟**/
    void createAlarm (String data);
    /**查询闹钟**/
    void queryAlarm (String data);
    /**删除闹钟完成**/
    void deleteAlarmComplete (String data);
    /**开始删除闹钟**/
    void deleteAlarmStart (String data);
    /**关闭闹钟**/
    void closeAlarm (String data);
    /**关闭提醒**/
    void closeRemind (String data);
    /**闹铃响铃**/
    void alarmRing (String data);
    /**提醒响铃**/
    void remindRing (String data);


}
