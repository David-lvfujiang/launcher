package com.fenda.leavemessage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.bean.LeaveMessageBean;
import com.fenda.common.provider.IAppLeaveMessageProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/18
 * @Describe:
 */
@Route(path = RouterPath.Leavemessage.LEAVEMESSAGE_SERVICE)
public class LeaveMessageService implements IAppLeaveMessageProvider, RongIMClient.OnReceiveMessageListener {
    String userId;
    /**
     * 未读消息监听回调
     * @param i
     */
    private IUnReadMessageObserver observer = new IUnReadMessageObserver() {
        @Override
        public void onCountChanged(int i) {
            LogUtil.e("数量变化s：" + i);
            //给首页发送未读消息事件，更新未读消息图标
            LeaveMessageBean leaveMessageBean = new LeaveMessageBean(i);
            EventBusUtils.post(leaveMessageBean);
        }
    };

    /**
     * 融云聊天初始化方法，提供给外部调用
     */
    @Override
    public void initRongIMlistener() {
        //设置消息接收监听器
        Log.e("消息", "接收: ");
        //接收消息监听
        RongIM.setOnReceiveMessageListener(this);
        //设置内容提供者，提供用户头像、用户名
        RongIM.setUserInfoProvider(new UserInfoProvider(),false);
        //动态监听连接状态
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                switch (connectionStatus) {
                    case CONNECTED://连接成功。

                        Log.i("yuki", "--------------------连接成功");
                        break;
                    case DISCONNECTED://断开连接。
                        Log.i("yuki", "--------------------断开连接");
                        break;
                    case CONNECTING://连接中。
                        Log.i("yuki", "--------------------链接中");
                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。
                        Log.i("yuki", "--------------------网络不可用");
                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                        Log.i("yuki", "--------------------掉线");
                        break;

                }
            }

        });

    }

    @Override
    public void init(Context context) {

    }

    /**
     * 来消息监听回调
     *
     * @param message
     * @param i
     * @return
     */
    @Override
    public boolean onReceived(Message message, int i) {
        LogUtil.e(message.toString());
        userId = message.getSenderUserId();
        openConversationActivity(userId, userId);
        return true;
    }

    /**
     * 弹出弹窗
     *
     * @param userId
     */
    public void openConversationActivity(String userId, String userName) {
        LogUtil.e("新消息");
        ActivityManager activityManager = (ActivityManager) BaseApplication.getBaseInstance().getSystemService(ACTIVITY_SERVICE);
        //获取当前的activity
        ComponentName currentActivityName = activityManager.getRunningTasks(1).get(0).topActivity;
        LogUtil.e(currentActivityName.getShortClassName());
        LogUtil.e(LeaveMessageConversationActivity.class.getName());
        //判断当前的activity是否是会话页面
        if (LeaveMessageConversationActivity.class.getName().equals(currentActivityName.getShortClassName()) == false) {
            //打开提示界面
            ARouter.getInstance().build(RouterPath.Leavemessage.LEAVEMESSAGE_DIALOG_ACTIVITY).withString("userId", userId).withString("userName", userName).navigation();
        }
    }

    /**
     * 打开聊天列表的方法
     * 在首页点击留言图标时调用
     */
    @Override
    public void openConversationListActivity() {
        RongIM.getInstance().startConversationList(BaseApplication.getBaseInstance());
    }

    /**
     * 删除聊天item的方法
     * 设备解绑的时候调用，删除聊天列表的item
     *
     * @param userPhoneNumber 用户手机号码
     */
    @Override
    public void removeRongIMMessage(String userPhoneNumber) {
        RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, userPhoneNumber, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                LogUtil.e("删除成功");
                RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
                RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                LogUtil.e("删除失败");
            }
        });
    }

    /**
     * 删除所有聊天item的方法
     */
    @Override
    public void removeRongIMAllMessage() {
        RongIM.getInstance().clearConversations(new RongIMClient.ResultCallback() {
            @Override
            public void onSuccess(Object o) {
                LogUtil.e("删除成功");
                RongIM.getInstance().addUnReadMessageCountChangedObserver(observer, Conversation.ConversationType.PRIVATE);
                RongIM.getInstance().removeUnReadMessageCountChangedObserver(observer);
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        }, Conversation.ConversationType.PRIVATE);
    }
}
