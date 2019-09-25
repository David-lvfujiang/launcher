package com.fenda.leavemessage;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.base.BaseActivity;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/18
 * @Describe: 聊天会话界面
 */
public class LeaveMessageConversationActivity extends BaseActivity implements View.OnClickListener {
    TextView mTvTitle;
    ImageView mBackImageView;
    String targetId;
    String title;

    @Override
    public int onBindLayout() {
        return R.layout.activity_leave_message_conversation;
    }

    @Override
    public void initView() {
        mTvTitle = findViewById(R.id.tv_title);
        mBackImageView = findViewById(R.id.leave_message_back_img);
        mBackImageView.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //会话界面 对方id
        targetId = getIntent().getData().getQueryParameter("targetId");
        //对方 昵称
        title = getIntent().getData().getQueryParameter("title");
        if (!TextUtils.isEmpty(title)) {
            //todo 设置标题为对方昵称
            mTvTitle.setText(title);

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.leave_message_back_img) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }
}
