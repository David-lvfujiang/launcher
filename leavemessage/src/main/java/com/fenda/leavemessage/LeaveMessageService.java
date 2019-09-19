package com.fenda.leavemessage;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.BaseApplication;
import com.fenda.common.provider.IleaveMessageProvider;
import com.fenda.common.router.RouterPath;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/9/18
 * @Describe:
 */
@Route(path = RouterPath.Leavemessage.LEAVEMESSAGE_PROVIDER)
public class LeaveMessageService implements IleaveMessageProvider, RongIMClient.OnReceiveMessageListener {
    String userId;

    @Override
    public void setRongIMMessageListener() {
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
        Log.e("消息", "onReceived: ");
        Log.e("消息", message.toString());
        userId = message.getSenderUserId();
        openConversationActivity(userId);
        return true;
    }

    /**
     * 弹出弹窗
     *
     * @param userId
     */
    public void openConversationActivity(final String userId) {
        Log.e("消息", "openConversationActivity: ");
        Handler handler = new Handler(Looper.getMainLooper());
        ActivityManager activityManager = (ActivityManager) BaseApplication.getInstance().getSystemService(ACTIVITY_SERVICE);
        //获取当前的activity
        ComponentName currentActivityName = activityManager.getRunningTasks(1).get(0).topActivity;
        Log.e("name", currentActivityName.getShortClassName());
        Log.e("name", LeaveMessageConversationActivity.class.getName());
        //判断当前的activity是否是会话页面
        if (LeaveMessageConversationActivity.class.getName().equals(currentActivityName.getShortClassName()) == false) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder build = new AlertDialog.Builder(BaseApplication.getInstance());
                    build.setTitle("留言").setMessage(userId + "给你留言了");
                    build.setPositiveButton("查看",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RongIM.getInstance().startConversation(BaseApplication.getInstance(), Conversation.ConversationType.PRIVATE, userId, userId);
                                }
                            });
                    build.setNegativeButton("忽略",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //关闭dialog
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = build.create();
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.width = 300;
                    lp.height = 300;
                    dialogWindow.setAttributes(lp);
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.show();
                }
            });
        } else {
            RongIM.getInstance().startConversation(BaseApplication.getInstance(), Conversation.ConversationType.PRIVATE, userId, userId);
        }
    }
}
