package com.example.calendar.base;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface ICalendarProvider extends IProvider {

    public void getMsg(String json);
}
