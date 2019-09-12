package com.fenda.encyclopedia.view;

import android.content.Intent;
import android.os.Build;
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
import com.fenda.encyclopedia.R;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 百科问答Activity
 */
@Route(path = RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY)
public class EncyclopediaQuestiionActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {
    private TextView mTvContent, mTvTitle;
    private ImageView mImgReturnBack;
    private EncyclopediaAutoScrollView mAutoScrollView;
    @Autowired
    String content;
    @Autowired
    String title;

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
        mAutoScrollView.setOnTouchListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initData() {
        //支持Html格式
        mTvContent.setText(Html.fromHtml(content, 0));
        mTvTitle.setText(title);
        //滚动条自动滚动
        mAutoScrollView.setAutoToScroll(true);
        //开始滚动时间
        mAutoScrollView.setFistTimeScroll(5000);
        //滚动的速率
        mAutoScrollView.setScrollRate(50);
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

}
