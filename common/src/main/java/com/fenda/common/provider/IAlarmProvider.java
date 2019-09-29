package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author matt.Ljp
 * @time 2019/9/26 10:33
 * @description
 */
public interface IAlarmProvider extends IProvider {
    /**查询闹钟**/
    void queryAlarm(String data);
}
