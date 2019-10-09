package com.fenda.encyclopedia.view;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.encyclopedia.R;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 百科问答Activity
 */
@Route(path = RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY)
public class EncyclopediaQuestiionActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, EncyclopediaAutoScrollView.ISmartScrollChangedListener {
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private TextView mTvContent, mTvTitle;
    private ImageView mImgReturnBack;
    private EncyclopediaAutoScrollView mAutoScrollView;
    @Autowired
    String content;
    @Autowired
    String title;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE:    // 关闭界面
                    EncyclopediaQuestiionActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int onBindLayout() {
        return R.layout.activity_encyclopedia_questiion;
    }

    @Override
    public void initView() {
        mTvTitle = findViewById(R.id.title_text);
        mTvContent = findViewById(R.id.content_text);
        mAutoScrollView = findViewById(R.id.scrollView);
        mImgReturnBack = findViewById(R.id.title_return_img);
        mImgReturnBack.setOnClickListener(this);
        //设置滑动监听
        mAutoScrollView.setOnTouchListener(this);
        //设置滚动条在底部、顶部监听
        mAutoScrollView.setmSmartScrollChangedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initData() {
        //支持Html格式
        mTvContent.setText(Html.fromHtml(content.trim(), 0));
        mTvTitle.setText(title);
        //滚动条自动滚动
        mAutoScrollView.setAutoToScroll(true);
        //开始滚动时间
        mAutoScrollView.setFistTimeScroll(5000);
        //滚动的速率
        mAutoScrollView.setScrollRate(100);
        //是否循环滑动
        mAutoScrollView.setScrollLoop(false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_return_img) {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
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
        switch (motionEvent.getAction()) {
            //点击
            case MotionEvent.ACTION_DOWN:
                //关闭自动滑动
                mAutoScrollView.setAutoToScroll(false);
                break;
            //滑动
            case MotionEvent.ACTION_MOVE:
                mAutoScrollView.setAutoToScroll(false);
                break;
            default:
                break;

        }
        return false;
    }

    /**
     * 自动滑动到底部后回调方法
     */
    @Override
    public void onScrolledToBottom() {
        LogUtil.e("滑到底部");
        LogUtil.e(mAutoScrollView.getAutoToScroll() + "");
        //自动滚动到底部10秒后关闭activity
        if (mAutoScrollView.getAutoToScroll() == true) {
            handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 10000);
        }
    }

    /**
     * 内容不超过ScrollView时自动回调
     */
    @Override
    public void onScrolledToTop() {
        //12秒后自动关闭activity
        handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 15000);
    }

}
