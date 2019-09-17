package com.fenda.common.util;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/16 17:31
 * @Description 防快速点击工具类
 */
public class FastClickUtils {
    private static long lastClickTime = 0;//上次点击的时间
    private static int spaceTime = 800;//时间间隔

    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();//当前系统时间
        boolean isAllowClick;//是否允许点击
        if (currentTime - lastClickTime > spaceTime) {
            isAllowClick = false;
        } else {
            isAllowClick = true;
        }
        lastClickTime = currentTime;
        return isAllowClick;
    }
}
