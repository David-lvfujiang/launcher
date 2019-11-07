package com.fenda.homepage.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.fenda.homepage.R;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/11/7 14:10
 * @Description
 */
public class PullView extends View {

    private Paint mPaint;
    //箭头的长度
    private static final int LENGTH = 30;
    //画笔的宽度
    private static final int PAINT_SIZE = 6;
    //滑动的大小
    private int scrollSize = 0;
    private int height;
    private int width;

    public PullView(Context context) {
        super(context);
    }

    public PullView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context,R.color.white));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(PAINT_SIZE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    public PullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0){
            width = getMeasuredWidth();
        }
        int lineWidth = (width/2) - LENGTH;
        if (height == 0){
            height = getMeasuredHeight();
        }
        int lineHeightStartY = height/2;
        int lineWidthEndX = width/2;
        //初始化箭头朝下,根据滑动的距离开始变化
        int lineHeightStopY = (height - height/4) - scrollSize;
        canvas.drawLine(lineWidth,lineHeightStartY,lineWidthEndX,lineHeightStopY,mPaint);
        int lineWidthX = (width/2) + LENGTH;

        canvas.drawLine(lineWidthX,lineHeightStartY,lineWidthEndX,lineHeightStopY,mPaint);


    }

    /**
     * 设置滑动的比例
     * @param progress
     */
    public void setProgress(float progress){
        scrollSize = (int) ((height/2)*progress);
        invalidate();
    }
}
