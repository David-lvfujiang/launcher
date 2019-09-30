package com.fenda.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/29 16:23
 */
public class MySimClockView extends View {
    private int width;
    private int height;
    private Paint mPaintLine;
    private Paint mPaintCircle;
    private Paint mPaintHour;
    private Paint mPaintMinute;
    private Paint mPaintSec;
    private Paint mPaintText;
    private Calendar mCalendar;
    public static final int NEED_INVALIDATE = 0X23;

    // 每隔一秒，在handler中调用一次重新绘制方法

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case NEED_INVALIDATE:
                    mCalendar = Calendar.getInstance();
                    invalidate();//告诉UI主线程重新绘制
                    handler.sendEmptyMessageDelayed(NEED_INVALIDATE,1000);
                    break;
                default:
                    break;
            }
        }
    };

    public MySimClockView(Context context) {
        super(context);
    }

    public MySimClockView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCalendar = Calendar.getInstance();

        mPaintLine = new Paint();
        mPaintLine.setColor(Color.BLUE);
        mPaintLine.setStrokeWidth(10);

        mPaintCircle = new Paint();
        //设置颜色
        mPaintCircle.setColor(Color.BLACK);
        //设置线宽
        mPaintCircle.setStrokeWidth(10);
        //设置是否抗锯齿
        mPaintCircle.setAntiAlias(false);
        //设置绘制风格
        mPaintCircle.setStyle(Paint.Style.STROKE);

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setStrokeWidth(10);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(40);

        mPaintHour = new Paint();
        mPaintHour.setStrokeWidth(12);
        mPaintHour.setColor(Color.BLACK);

        mPaintMinute = new Paint();
        mPaintMinute.setStrokeWidth(10);
        mPaintMinute.setColor(Color.BLACK);

        mPaintSec = new Paint();
        mPaintSec.setStrokeWidth(7);
        mPaintSec.setColor(Color.RED);
        //向handler发送一个消息，让它开启重绘
        handler.sendEmptyMessage(NEED_INVALIDATE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int circleRadius = 250;
        //画出大圆
        canvas.drawCircle(width / 2, height / 2, circleRadius, mPaintCircle);
        //画出圆中心
        canvas.drawCircle(width / 2, height / 2, 20, mPaintCircle);
        //依次旋转画布，画出每个刻度和对应数字
        for (int i = 1; i <= 12; i++) {
            canvas.save();//保存当前画布
            canvas.rotate(360/12*i,width/2,height/2);
            //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
            canvas.drawLine(width / 2, height / 2 - circleRadius, width / 2, height / 2 - circleRadius + 30, mPaintCircle);
            //左起：文本内容，起始位置x坐标，起始位置y坐标，画笔
            canvas.drawText(String.valueOf(+i), width / 2, height / 2 - circleRadius + 70, mPaintText);
            canvas.restore();//
        }
        //得到当前分钟数
        int minute = mCalendar.get(Calendar.MINUTE);
        //得到当前小时数
        int hour = mCalendar.get(Calendar.HOUR);
        //得到当前秒数
        int sec = mCalendar.get(Calendar.SECOND);
        //得到分针旋转的角度
        float minuteDegree = minute/60f*360;
        canvas.save();
        canvas.rotate(minuteDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - 190, width / 2, height / 2 + 40, mPaintMinute);
        canvas.restore();
        //得到时钟旋转的角度
        float hourDegree = (hour*60+minute)/12f/60*360;
        canvas.save();
        canvas.rotate(hourDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - 160, width / 2, height / 2 + 30, mPaintHour);
        canvas.restore();
        //得到秒针旋转的角度
        float secDegree = sec/60f*360;
        canvas.save();
        canvas.rotate(secDegree,width/2,height/2);
        canvas.drawLine(width/2,height/2-210,width/2,height/2+40,mPaintSec);
        canvas.restore();

    }
}
