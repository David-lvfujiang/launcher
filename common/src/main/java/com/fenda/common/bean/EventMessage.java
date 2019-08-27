package com.fenda.common.bean;

import com.fenda.common.base.BaseEvent;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/26 20:28
  * @Description
  *
  */
public class EventMessage<T> extends BaseEvent<T> {


    public EventMessage(int code, T data) {
        super(code, data);
    }
}
