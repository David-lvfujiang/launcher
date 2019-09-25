package com.fenda.leavemessage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.IAppLeaveMessageProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;

import io.rong.imkit.MainActivity;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIMClientWrapper;
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
        //RongIM.init(BaseApplication.getContext());
        RongIM.setOnReceiveMessageListener(this);

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
        Log.e("消息", "接收1: ");
        LogUtil.e(message.toString());
        userId = message.getSenderUserId();
        MessageContent content = message.getContent();
        UserInfo userInfo = content.getUserInfo();
        String userName = userInfo.getName();
        Log.e("TAG", userInfo.getName());
        openConversationActivity(userId, userName);
        return true;
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
