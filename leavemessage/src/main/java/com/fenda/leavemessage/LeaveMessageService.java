package com.fenda.leavemessage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.IAppLeaveMessageProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/18
 * @Describe:
 */
@Route(path = RouterPath.Leavemessage.LEAVEMESSAGE_SERVICE)
public class LeaveMessageService implements IAppLeaveMessageProvider, RongIMClient.OnReceiveMessageListener {
    String userId;

    @Override
    public void initRongIMlistener() {
        //设置消息接收监听器
        Log.e("消息", "接收: ");
        RongIM.setOnReceiveMessageListener(this);
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
     * 消息监听回调
     *
     * @param message
     * @param i
     * @return
     */
    @Override
    public boolean onReceived(Message message, int i) {
        LogUtil.e(message.toString());
        userId = message.getSenderUserId();
        openConversationActivity(userId, "留言");
//        MessageContent content = message.getContent();
//        UserInfo userInfo = content.getUserInfo();
//        String userName = userInfo.getName();
//        LogUtil.e(userInfo.getName());
//        if (userInfo.getName() == null)
//        {
//            openConversationActivity(userId, "留言");
//        } else{
//            openConversationActivity(userId, userName);
//        }
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

    @Override
    public void openConversationListActivity() {
        RongIM.getInstance().startConversationList(BaseApplication.getBaseInstance());
    }

}
