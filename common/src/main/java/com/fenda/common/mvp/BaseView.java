package com.fenda.common.mvp;


/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 14:02
  * @Description
  *
  */
public interface BaseView {

    /*******内嵌加载*******/
    void showLoading(String title);
    void hideLoading();
    void showErrorTip(String msg);
    void showNetError();
    void hideNetError();
    void showContent();
    void hideContent();

}
