package com.fenda.common.mvp;


import com.trello.rxlifecycle2.LifecycleProvider;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 14:02
  * @Description
  *
  */
public class BaseModel {


    private LifecycleProvider lifecycle;
    public void injectLifecycle(LifecycleProvider lifecycle) {
        this.lifecycle = lifecycle;
    }

    public LifecycleProvider getLifecycle() {
        return lifecycle;
    }


    public void destory(){}
}
