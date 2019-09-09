package com.fenda.encyclopedia.view;

import android.content.Intent;
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
public class EncyclopediaQuestiionActivity extends BaseActivity implements View.OnClickListener {
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
    }

    @Override
    public void initData() {
        mTvContent.setText(content);
        mTvTitle.setText(title);
        mAutoScrollView.setAutoToScroll(true);//设置可以自动滑动
        mAutoScrollView.setFistTimeScroll(5000);//设置第一次自动滑动的时间
        mAutoScrollView.setScrollRate(50);//设置滑动的速率
        mAutoScrollView.setScrollLoop(false);//设置是否循环滑动
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_return_img) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        content = intent.getStringExtra("content");
        title = intent.getStringExtra("title");
        initData();
        super.onNewIntent(intent);
    }
}
