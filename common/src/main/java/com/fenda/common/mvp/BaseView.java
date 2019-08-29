package com.fenda.common.mvp;

import android.content.Context;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 14:02
  * @Description
  *
  */
public interface BaseView extends ILoadView,INetErrView,INoDataView,ITransView{

    void initView();
    void initListener();
    void initData();
    Context getContext();

}
