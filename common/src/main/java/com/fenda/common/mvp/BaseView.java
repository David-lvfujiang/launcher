package com.fenda.common.mvp;

import android.content.Context;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 14:02
  * @Description
  *
  */
public interface BaseView {

    /*******内嵌加载*******/
    void showLoading(String title);
    void stopLoading();
    void showErrorTip(String msg);

}
