package com.fenda.leavemessage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.bean.LeaveMessageBean;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.model.Conversation;
import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/24
 * @Describe:
 */
public class LeaveMessageReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        RongIM.getInstance().addUnReadMessageCountChangedObserver(
                new IUnReadMessageObserver() {
                    @Override
                    public void onCountChanged(int i) {
                        LogUtil.e("数量变化：" + i);
                        LeaveMessageBean leaveMessageBean =   new LeaveMessageBean(i);
                        EventBusUtils.post(leaveMessageBean);
                    }
                }, Conversation.ConversationType.PRIVATE);
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;
    }
    /**
     * 弹出弹窗
     *
     * @param userId
     */
    public void openConversationActivity(String userId, String userName) {
        LogUtil.e("新消息");
        ActivityManager activityManager = (ActivityManager) BaseApplication.getInstance().getSystemService(ACTIVITY_SERVICE);
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
}