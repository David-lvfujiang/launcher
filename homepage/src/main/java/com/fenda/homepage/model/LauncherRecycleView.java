package com.fenda.homepage.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/11/6 10:03
 * @Description
 */
public class LauncherRecycleView extends RecyclerView {

    private int MIX_SCROLL;
    private float downY;
    private float downX;
    private float moveY;
    private boolean isEvent;
    private float moveX;
    private boolean isXEvent;

    public LauncherRecycleView(@NonNull Context context) {
        super(context);
    }

    public LauncherRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MIX_SCROLL = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public LauncherRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public boolean getInterceptEvent(){
        return isEvent;
    }

    public void setInterceptEvent(boolean isEvent){
        this.isEvent = isEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        switch (e.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                downY = e.getY();
//                downX = e.getX();
//                isXEvent = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX = e.getX() - downX;
//                moveY = e.getY() - downY;
//                 if (moveY < (-MIX_SCROLL) && !isXEvent){
//                    Log.i("LauncherRecycleView","进入上下滑动.....");
//                    //上滑
//                    isEvent = true;
//                    return true;
//                }else if (Math.abs(moveX) > MIX_SCROLL*2){
//                    //左右滑动
//                    Log.i("LauncherRecycleView","进入左右滑动.....");
//                    isXEvent = true;
//                }
//            case MotionEvent.ACTION_UP:
//                downX = 0;
//                downY = 0;
//                isXEvent = false;
//                break;
//
//        }

        return super.onTouchEvent(e);
    }
}
