package com.fenda.homepage.model;

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
    private int length = 30;
    private int scrollIndex = 0;
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
        mPaint.setStrokeWidth(6);
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
        int lineWidth = (width/2)-length;
        if (height == 0){
            height = getMeasuredHeight();
        }
        int lineHeightStart = height/2;
        int lineHeightStop = height/4 + scrollIndex;
        canvas.drawLine(lineWidth,lineHeightStart,width/2,lineHeightStop,mPaint);
        int lineWidthY = (width/2)+length;

        canvas.drawLine(lineWidthY,lineHeightStart,width/2,lineHeightStop,mPaint);


    }


    public void setProgress(int progress){
        scrollIndex = (height/2)*progress;
        invalidate();
    }
}
