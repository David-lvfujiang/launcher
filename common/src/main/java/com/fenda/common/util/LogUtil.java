package com.fenda.common.util;


import com.orhanobut.logger.Logger;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/27 17:54
  * @Description 日志打印类,只在debug模式会打印
  *
  */
public class LogUtil {


    /**
     * information
     * @param msg
     */
    public static void i (String msg){
        Logger.i(msg);
    }

    /**
     * debug
     * @param msg
     */
    public static void d (String msg){
        Logger.d(msg);
    }


    /**
     * debug
     * @param msg
     */
    public static void d (Object msg){
        Logger.d(msg);
    }


    /**
     * error
     * @param msg
     */
    public static void e (Object msg){
        Logger.d(msg);
    }

    /**
     * error
     * @param msg
     */
    public static void e (String msg){
        Logger.e(msg);
    }

    /**
     * warning
     * @param msg
     */
    public static void w (String msg){
        Logger.w(msg);
    }

    /**
     * verbose
     * @param msg
     */
    public static void v (String msg){
        Logger.v(msg);
    }





}
