package com.fenda.encyclopedia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.fenda.common.util.LogUtil;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 自定义ScrollView
 */
public class EncyclopediaAutoScrollView extends ScrollView {

    private int paddingTop = 0;
    private final int MSG_SCROLL = 10;
    private final int MSG_SCROLL_Loop = 11;
    private ISmartScrollChangedListener mSmartScrollChangedListener;

    //是否能滑动
    private boolean scrollAble = true;
    //是否自动滚动
    private boolean autoToScroll = true;
    //是否循环滚动
    private boolean scrollLoop = false;
    //多少秒后开始滚动，默认5秒
    private int fistTimeScroll = 5000;
    //多少毫秒滚动一个像素点
    private int scrollRate = 500;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SCROLL:
                    if (scrollAble && autoToScroll) {
                        scrollTo(0, paddingTop);
                        paddingTop += 1;
                        mHandler.removeMessages(MSG_SCROLL);
                        mHandler.sendEmptyMessageDelayed(MSG_SCROLL, scrollRate);
                    }
                    break;
                case MSG_SCROLL_Loop:
                    paddingTop = 0;
                    autoToScroll = true;
                    mHandler.sendEmptyMessageDelayed(MSG_SCROLL, fistTimeScroll);
                    break;
                default:
                    break;

            }

        }
    };

    public EncyclopediaAutoScrollView(Context context) {
        this(context, null);
    }

    public EncyclopediaAutoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EncyclopediaAutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EncyclopediaAutoScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    public void setmSmartScrollChangedListener(ISmartScrollChangedListener mSmartScrollChangedListener) {
        this.mSmartScrollChangedListener = mSmartScrollChangedListener;
    }

    /**
     * 定义监听接口
     */
    public interface ISmartScrollChangedListener {
        //在底部
        void onScrolledToBottom();

        //在顶部
        void onScrolledToTop();

    }

    /**
     * 设置是否可以滚动
     *
     * @param autoToScroll
     */
    public void setAutoToScroll(boolean autoToScroll) {
        this.autoToScroll = autoToScroll;
    }

    public Boolean getAutoToScroll() {
        return autoToScroll;
    }

    /**
     * 设置开始滚动的时间
     *
     * @param fistTimeScroll
     */
    public void setFistTimeScroll(int fistTimeScroll) {
        this.fistTimeScroll = fistTimeScroll;
        mHandler.removeMessages(MSG_SCROLL);
        mHandler.sendEmptyMessageDelayed(MSG_SCROLL, fistTimeScroll);
    }

    /**
     * 设置滚动的速度
     *
     * @param scrollRate
     */
    public void setScrollRate(int scrollRate) {
        this.scrollRate = scrollRate;
    }

    /**
     * 设置是否循环滚动
     *
     * @param scrollLoop
     */
    public void setScrollLoop(boolean scrollLoop) {
        this.scrollLoop = scrollLoop;
    }

    /**
     * 滚动回调
     *
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount() - 1);
        //获取textView的高度
        int childViewHight = view.getBottom();
        childViewHight -= (getMeasuredHeight() + getScrollY());
        if (childViewHight == 20) {
            if (this.mSmartScrollChangedListener != null) {
                //内容超过ScrollView，并且自动滑动到底部回调
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    /**
     * 重绘回调，每滚动一次调一次
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        View view = (View) getChildAt(getChildCount() - 1);
        int childViewHight = view.getBottom();
        LogUtil.e("" + childViewHight);
        LogUtil.e("" + getMeasuredHeight());
        if (childViewHight <= getMeasuredHeight()) {
            if (this.mSmartScrollChangedListener != null) {
                //内容不超过ScrollView回调
                mSmartScrollChangedListener.onScrolledToTop();
            }
            //当textView高度大于ScrollView，并且误差在10dp内ScrollView不会自动滚动
        } else if (childViewHight < getMeasuredHeight() + 10 && childViewHight > getMeasuredHeight()) {
            this.autoToScroll = false;
        }
    }
}