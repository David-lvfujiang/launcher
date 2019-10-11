package com.fenda.leavemessage;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.basebean.leavemessage.LeaveMessageBean;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/18
 * @Describe: 消息提示activity
 */
@Route(path = RouterPath.Leavemessage.LEAVEMESSAGE_DIALOG_ACTIVITY)
public class LeavemessageDialogActivity extends BaseActivity implements View.OnClickListener {
    Button mBtlook, mBtCancel;
    TextView mTvMessageContent;
    @Autowired
    String userId;
    @Autowired
    String userName;
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            LogUtil.e("数量变化：" + i);
            LeaveMessageBean leaveMessageBean = new LeaveMessageBean(i);
            EventBusUtils.post(leaveMessageBean);
        }
    };

    @Override
    public int onBindLayout() {
        ARouter.getInstance().inject(this);
        return R.layout.activity_leavemessage_dialog;
    }

    @Override
    public void initView() {
        mBtCancel = findViewById(R.id.leave_message_btn_cancel);
        mBtlook = findViewById(R.id.leave_message_btn_look);
        mTvMessageContent = findViewById(R.id.leave_message_tv);
        mBtCancel.setOnClickListener(this);
        mBtlook.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvMessageContent.setText(userName + "给您留言了");
        RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.leave_message_btn_look) {
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, userId, userName);
            finish();
        }
        if (view.getId() == R.id.leave_message_btn_cancel) {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除监听
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);

    }
}
