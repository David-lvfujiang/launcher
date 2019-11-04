package com.fenda.player.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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


/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class LyricFragment extends BaseFragment implements View.OnTouchListener {

    TextView tvTitle;
    TextView tvAuthor;
    LinearLayout linContent;
    AutoScrollView mAutoScrollView;
    int mPosX = 0, mPosY = 0, mCurPosX = 0, mCurPosY = 0;
    private static final long TIME_INTERVAL = 500L;
    long downTime = 0;

    public static LyricFragment getInstance(FDMusic music) {
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
        FDMusic music = getArguments().getParcelable("poetry");
        initLyricData(music.getMusicTitle(), music.getMusicArtist(), music.getContent());
    }

    /**
     * 设置滚动
     */
    public void settingScroll() {
        //获取焦点
        mAutoScrollView.requestFocus();
        //滚动条重初始化
        mAutoScrollView.init();
        //滚到顶部
        mAutoScrollView.fullScroll(ScrollView.FOCUS_UP);
        //滚动条自动滚动
        mAutoScrollView.setAutoToScroll(true);
        //开始滚动时间
        mAutoScrollView.setFistTimeScroll(9000);
        //滚动的速率
        mAutoScrollView.setScrollRate(240);
        //是否循环滑动
        mAutoScrollView.setScrollLoop(false);
        LogUtils.e("滚动 ");

    }

    private void initLyricData(String title, String author, String text) {
        settingScroll();
        LogUtils.e("播放");
        tvTitle.setText(title);
        tvAuthor.setText(author);
        linContent.removeAllViews();
        if (!TextUtils.isEmpty(text) && text.indexOf("。") != -1) {
            String[] contents = text.split("。");
            for (int i = 0; i < contents.length; i++) {
                String item = contents[i];
                if (item.indexOf("！") != -1) {
                    String[] mItems = item.split("！");
                    for (int i1 = 0; i1 < mItems.length; i1++) {
                        String mText = mItems[i1];
                        TextView tv = new TextView(getActivity());
                        tv.setTextSize(30);
                        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.player_white));
                        if (i1 == mItems.length - 1) {
                            tv.setText(mText + "。");
                        } else {
                            tv.setText(mText + "！");

                        }
                        tv.setGravity(Gravity.CENTER);
                        linContent.addView(tv);
                    }
                } else {
                    TextView tv = new TextView(getActivity());
                    tv.setTextSize(30);
                    tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.player_white));
                    tv.setText(item + "。");
                    tv.setGravity(Gravity.CENTER);
                    linContent.addView(tv);
                }

            }

        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvnet(PlayerMessage message) {
        initLyricData(message.getMusicTitle(), message.getMusicName(), message.getContent());

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
