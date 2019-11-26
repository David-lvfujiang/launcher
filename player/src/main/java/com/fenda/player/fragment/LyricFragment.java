package com.fenda.player.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fenda.common.base.BaseFragment;
import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.view.AutoScrollView;
import com.fenda.player.R;
import com.fenda.player.bean.PlayerMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;


/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class LyricFragment extends BaseFragment implements View.OnTouchListener {

    TextView tvTitle;
    TextView tvAuthor;
    LinearLayout linContent;
    AutoScrollView mAutoScrollView;
    FDMusic music;
    int rowNumber;
    int mPosX = 0, mPosY = 0, mCurPosX = 0, mCurPosY = 0;
    private static final long TIME_INTERVAL = 500L;
    long downTime = 0;

    public static LyricFragment getInstance(FDMusic music) {
        Log.e("LyricFragment", music.getMusicTime() + "");

        LyricFragment fragment = new LyricFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("poetry", music);
        fragment.setArguments(mBundle);
        return fragment;
    }


    @Override
    public int onBindLayout() {
        return R.layout.player_fragment_lyric;
    }

    @Override
    public void initView() {
        tvTitle = mRootView.findViewById(R.id.tv_title);
        tvAuthor = mRootView.findViewById(R.id.tv_author);
        linContent = mRootView.findViewById(R.id.lin_content);
        mAutoScrollView = mRootView.findViewById(R.id.auto_scrollView);
        mAutoScrollView.setOnTouchListener(this);

    }

    @Override
    public void initData() {
        music = getArguments().getParcelable("poetry");
        initLyricData(music.getMusicTitle(), music.getMusicArtist(), music.getContent());
    }

    /**
     * 设置滚动
     */
    public void settingScroll(int playTime) {
        View view = (View) mAutoScrollView.getChildAt(mAutoScrollView.getChildCount() - 1);
        //获取textView的高度
        int childViewHight = view.getBottom();
        Log.e("高度", "" + childViewHight);
        Log.e("高度", "" + rowNumber);
        //获取焦点
        mAutoScrollView.requestFocus();
        //滚动条重初始化
        mAutoScrollView.init();
        //滚到顶部
        mAutoScrollView.fullScroll(ScrollView.FOCUS_UP);
        //滚动条自动滚动
        mAutoScrollView.setAutoToScroll(true);
        //开始滚动时间
        mAutoScrollView.setFistTimeScroll(15000);
        //根据时长与行高计算每一行的时间
        int rate = playTime / rowNumber;
        LogUtils.e("musicActivity " + playTime + "," + rate);
        mAutoScrollView.setScrollRate(rate * 27);
        if (rowNumber > 100) {
            mAutoScrollView.setScrollRate(rate * 24);
        }
        //是否循环滑动
        mAutoScrollView.setScrollLoop(false);
        LogUtils.e("滚动 ");

    }

    private void initLyricData(String title, String author, String text) {
        LogUtils.e("播放");
        if (!TextUtils.isEmpty(text) && text.indexOf("。") != -1) {
            tvTitle.setText(title);
            tvAuthor.setText(author);
            linContent.removeAllViews();
            String[] contents = text.split("。");
            rowNumber = 0;
            rowNumber = contents.length;
            for (int i = 0; i < contents.length; i++) {
                String item = contents[i];
                if (item.indexOf("！") != -1) {
                    String[] mItems = item.split("！");
                    rowNumber = rowNumber + mItems.length;
                    for (int i1 = 0; i1 < mItems.length; i1++) {
                        String mText = mItems[i1].trim();
                        TextView tv = new TextView(getActivity());
                        tv.setTextSize(30);
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.player_white));
                        if (i1 == mItems.length - 1) {
                            tv.setText(mText + "。");
                        } else {
                            tv.setText(mText + "！");

                        }
                        tv.setGravity(Gravity.CENTER);
                        //   tv.setBackgroundColor(Color.RED);
                        linContent.addView(tv);
                    }
                } else if (item.indexOf("？") != -1) {
                    String[] mItems = item.split("？");
                    rowNumber = rowNumber + mItems.length;
                    for (int i1 = 0; i1 < mItems.length; i1++) {
                        String mText = mItems[i1].trim();
                        TextView tv = new TextView(getActivity());
                        tv.setTextSize(30);
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.player_white));
                        if (i1 == mItems.length - 1) {
                            tv.setText(mText + "。");
                        } else {
                            tv.setText(mText + "？");

                        }
                        tv.setGravity(Gravity.CENTER);
                        //   tv.setBackgroundColor(Color.RED);
                        linContent.addView(tv);
                    }
                } else {
                    TextView tv = new TextView(getActivity());
                    tv.setTextSize(30);
                    tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.player_white));
                    tv.setText(item.trim() + "。");
                    tv.setGravity(Gravity.CENTER);
                    // tv.setBackgroundColor(Color.RED);
                    linContent.addView(tv);
                }
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvnet(PlayerMessage message) {
        Log.e("onEvnet", message.toString());
        if (message.getMsgType() == 1) {
            //暂停
            mAutoScrollView.setPause(true);
        } else if (message.getMsgType() == 2) {
            mAutoScrollView.setPause(false);
        } else {
            if (message.getPlaytime() > 0) {
                settingScroll(message.getPlaytime());
            } else {
                initLyricData(message.getMusicTitle(), message.getMusicName(), message.getContent());
            }
        }

    }


    @Override
    public void onDestroyView() {
        EventBusUtils.unregister(this);
        super.onDestroyView();
    }

    /**
     * 点击、滑动监听
     *
     * @param view
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int i = motionEvent.getAction();
        if (i == MotionEvent.ACTION_DOWN) {
            //记录触屏最开的位置
            mPosX = (int) motionEvent.getX();
            mPosY = (int) motionEvent.getY();
            //判断是否连续点击，是则停止滚动
            if (System.currentTimeMillis() - downTime > TIME_INTERVAL) {
                downTime = System.currentTimeMillis();
            } else {
                mAutoScrollView.setAutoToScroll(false);
            }
            LogUtil.e(mPosY + "");
        }
        if (i == MotionEvent.ACTION_MOVE) {
            mCurPosX = (int) motionEvent.getX();
            mCurPosY = (int) motionEvent.getY();
            //判断是否向下或者向上滑动，是则停止滚动
            if (Math.abs(mCurPosY - mPosY) > 50) {
                LogUtil.e(mCurPosY + "");
                LogUtil.e("向下、向下滑动");
                mAutoScrollView.setAutoToScroll(false);
            }
        }

        return false;
    }


}
