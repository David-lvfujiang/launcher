package com.fenda.homepage.model;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/11/5 14:34
 * @Description
 */
public class MyRecycleView extends RecyclerView {

    private static final String TAG = "MyRecycleView";

    private boolean isTop;
    private float downY;
    private float downX;

    private int MIX_SCROLL;
    private float moveY;
    private boolean isEvent;

    public MyRecycleView(@NonNull Context context) {
        super(context);
    }

    public MyRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MIX_SCROLL = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setMotionListener(){
        addOnScrollListener(new MotionOnScrollListener());
    }





    private class MotionOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            // We only start the spring animation when we hit the top/bottom, to ensure
            // that all of the animations start at the same time.
            if (dy < 0 && !canScrollVertically(-1)) {
                //滑到顶部
                isTop = true;
                Log.i(TAG,"滑动顶部.....");

            } else if (dy > 0 && !canScrollVertically(1)) {
                //滑到底部
                Log.i(TAG,"滑动底部.....");

            }else {
                Log.i(TAG,"滑动中....");
                isTop = false;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = e.getY();
                downX = e.getX();
                Log.i(TAG,"进入了ACTION_DOWN = "+TAG);
                break;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                moveY = e.getY() - downY;
                Log.i(TAG,"isTop = "+isTop+ " moveY = "+moveY);
               if (moveY >  (MIX_SCROLL) && isTop){
                    Log.i(TAG,"进入上下滑动.....");
                    //下滑
                    isEvent = true;
                    return true;
                }
            case MotionEvent.ACTION_UP:
                downX = 0;
                downY = 0;
                break;

        }

        return super.onTouchEvent(e);
    }

    public boolean isTop(){
        return isTop;
    }

    public boolean  getEvent(){
        return isEvent;
    }

    public void setEvent(boolean isEvent){
        this.isEvent = isEvent;
    }


}
