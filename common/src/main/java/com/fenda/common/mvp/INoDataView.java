package com.fenda.common.mvp;

import android.support.annotation.DrawableRes;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 15:26
  * @Description 
  *
  */
public interface INoDataView {
    /**
     * 显示无数据View
     */
    void showNoDataView();

    /**
     * 隐藏无数据View
     *
     */
    void hideNoDataView();

    /**
     * 显示指定资源的无数据View
     * @param resid
     */
    void showNoDataView(@DrawableRes int resid);
}
