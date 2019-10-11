package com.fenda.common.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 业务接口
 */
public interface ICalendarProvider extends IProvider {

    public void processCalendarMsg(String msg);
    public void getCalendarMsg(String json);
}
