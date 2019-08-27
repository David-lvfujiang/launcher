package com.fenda.common.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  19:52
 * @Description This is KeyboardUtil
 */
public class KeyboardUtil {


    /**
     * 显示软键盘
     *
     * @param context
     */
    public static void showSoftInput(Context context) {
        // 显示软键盘
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示软键盘
     *
     * @param context
     */
    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param view
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager immHide =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        immHide.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
