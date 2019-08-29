package com.fenda.common.mvp;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:17
  * @Description
  *
  */
public interface  ITransView {
    /**
     * 显示背景透明小菊花View,例如删除操作
     */
    void showTransLoadingView();

    /**
     * 隐藏背景透明小菊花View
     */
    void hideTransLoadingView();
}
