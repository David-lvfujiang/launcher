package com.fenda.common.bus;

import com.fenda.common.base.BaseEvent;
import com.fenda.common.bean.EventMessage;

import org.greenrobot.eventbus.EventBus;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/26 20:30
  * @Description Bus工具类
  *
  */
public class EventBusUtils {

    private EventBusUtils() {
    }

    /**
     * 注册 EventBus
     *
     * @param subscriber
     */
    public static void register(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(subscriber)) {
            eventBus.register(subscriber);
        }
    }

    /**
     * 解除注册 EventBus
     *
     * @param subscriber
     */
    public static void unregister(Object subscriber) {
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(subscriber)) {
            eventBus.unregister(subscriber);
        }
    }

    /**
     * 发送事件消息
     *
     * @param event
     */
    public static void post(EventMessage event) {
        EventBus.getDefault().post(event);
    }
    /**
     * 发送事件消息
     *
     * @param event
     */
    public static void post(BaseEvent event) {
        EventBus.getDefault().post(event);
    }


    public static void post(String type){
        EventBus.getDefault().post(type);
    }
    /**
     * 发送事件消息
     * @param type
     */
    public static void post(Object type){
        EventBus.getDefault().post(type);
    }

    /**
     * 发送粘性事件消息
     *
     * @param event
     */
    public static void postSticky(EventMessage event) {
        EventBus.getDefault().postSticky(event);
    }
}
