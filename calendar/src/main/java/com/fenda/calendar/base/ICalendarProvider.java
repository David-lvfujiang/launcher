package com.fenda.calendar.base;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ICalendarProvider extends IProvider {

    public void getCalendarMsg(String json);
}
