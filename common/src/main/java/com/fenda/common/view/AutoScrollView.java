package com.fenda.common.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author LiFuJiang
 * @Date 2019/8/29 16:51
 * @Description 自定义ScrollView
 */
public class AutoScrollView extends ScrollView {

    private boolean scrolledToTop = true;
    private boolean scrolledToBottom = false;
    private int paddingTop = 0;
    private final int MSG_SCROLL = 10;
    private final int MSG_SCROLL_Loop = 11;
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

    public void init() {
        paddingTop = 0;
    }

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

    public AutoScrollView(Context context) {
        this(context, null);
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }


    private ISmartScrollChangedListener mSmartScrollChangedListener;

    /**
     * 定义监听接口
     */
    public interface ISmartScrollChangedListener {
        //滑动到底部
        void onScrolledToBottom();

        //滑动到顶部
        void onScrolledToTop();

    }


    /**
     * 设置滑动到顶部或底部的监听
     *
     * @param smartScrollChangedListener
     */
    public void setScanScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }


    /**
     * 判断是否滑到底部
     */
    private void notifyScrollChangedListeners() {
        if (scrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (scrolledToBottom) {
            mHandler.removeMessages(MSG_SCROLL);
            if (!scrollLoop) {
                scrollAble = false;
            }
            if (scrollLoop) {
                mHandler.sendEmptyMessageDelayed(MSG_SCROLL_Loop, fistTimeScroll);
            }
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }


    /**
     * 设置是否可以滚动
     *
     * @param autoToScroll
     */
    public void setAutoToScroll(boolean autoToScroll) {
        this.autoToScroll = autoToScroll;
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

}