package com.fenda.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;


/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/23 14:43
 * @Description
 */
public class MyNestedScrollView extends NestedScrollView {
    private float down;
    private float move;
    private boolean isExecute;
    private boolean isScrolledToTop = true;
    private boolean isScrolledToBottom;
    private OnMoveTouchClickListener listener;
    private int moveY;

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (getScrollY() == 0) {
            isScrolledToTop = true;
            isScrolledToBottom = false;

        } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
            isScrolledToBottom = true;
            isScrolledToTop = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = false;
        }

    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_MOVE:
                move = ev.getY();
                moveY = (int) (move - down);
                if (moveY > 0 && isScrolledToTop()){
                    if (listener != null){
                        listener.moveTouchListener(this,moveY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isExecute = true;
                if (isScrolledToTop() && listener != null){
                    listener.upTouchListener(this,moveY);
                }
                down = 0;
                moveY = 0;
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //拦截按下事件
                down = ev.getY();
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }


    public void setOnClickMoveTouchListener(OnMoveTouchClickListener listener){
        this.listener = listener;
    }


    public interface OnMoveTouchClickListener{
        void moveTouchListener(NestedScrollView view, int moveY);

        void upTouchListener(NestedScrollView view, int moveY);
    }
}
