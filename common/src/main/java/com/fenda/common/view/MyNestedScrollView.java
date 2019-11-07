package com.fenda.common.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.fenda.common.router.RouterPath;


/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/23 14:43
 * @Description
 */
public class MyNestedScrollView extends ScrollView {
    private static final String TAG = "MyNestedScrollView";
    private float down;
    private float move;
    private boolean isExecute;
    private boolean isScrolledToTop = true;
    private boolean isScrolledToBottom;
    private OnMoveTouchClickListener listener;
    private int moveY;
    private boolean isScroll;
    private int MIX_SCROLL;

    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MIX_SCROLL = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i(TAG,"getScrollY() ===="+getScrollY());

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

//        switch (ev.getAction()){
//            case MotionEvent.ACTION_MOVE:
//                move = ev.getY();
//                moveY = (int) (move - down);
//                Log.i("TAG","MyNestedScroll moveY = "+moveY+" isScrolledToTop = "+isScrolledToTop());
//                if (isScrolledToTop() && moveY > MIX_SCROLL){
////                    if (listener != null){
////                        listener.moveTouchListener(this,moveY);
////                    }
//                    isScroll = true;
//                    Log.i("TAG","进入控制");
//                }else {
//                    isScroll = false;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                down = 0;
//                moveY = 0;
//                break;
//        }
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


    public void setScroll(boolean isScroll){
        this.isScroll = isScroll;
    }
    public boolean getScroll(){
        return isScroll;
    }


    public interface OnMoveTouchClickListener{
        void moveTouchListener(NestedScrollView view, int moveY);

        void upTouchListener(NestedScrollView view, int moveY);
    }
}
