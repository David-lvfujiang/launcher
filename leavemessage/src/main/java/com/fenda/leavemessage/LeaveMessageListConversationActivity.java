package com.fenda.leavemessage;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;

import com.fenda.common.base.BaseActivity;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/18
 * @Describe: 聊天会话列表
 */
public class LeaveMessageListConversationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImgReturnBack;

    @Override
    public int onBindLayout() {
        return R.layout.activity_leave_message_list_conversation;
    }

    @Override
    public void initView() {
        mImgReturnBack = findViewById(R.id.leave_message_list_back_img);
        mImgReturnBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        FragmentManager fragmentManage = getSupportFragmentManager();
        ConversationListFragment fragement = (ConversationListFragment) fragmentManage.findFragmentById(R.id.conversationlist);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();
        fragement.setUri(uri);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.leave_message_list_back_img) {
            finish();
        }
    }

}
