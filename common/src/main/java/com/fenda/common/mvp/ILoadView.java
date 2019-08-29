package com.fenda.common.mvp;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:16
  * @Description 
  *
  */
public interface ILoadView {
    /**
     * 显示初始加载的View，初始进来加载数据需要显示的View
     */
    void showInitLoadView();

    /**
     * 隐藏初始加载的View
     */
    void hideInitLoadView();
}
