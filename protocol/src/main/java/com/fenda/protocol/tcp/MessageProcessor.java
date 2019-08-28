package com.fenda.protocol.tcp;

import android.util.Log;

import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;


/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       MessageProcessor.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息处理器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 03:27</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class MessageProcessor implements IMessageProcessor {

    private static final String TAG = MessageProcessor.class.getSimpleName();

    private MessageProcessor() {

    }

    private static class MessageProcessorInstance {
        private static final IMessageProcessor INSTANCE = new MessageProcessor();
    }

    public static IMessageProcessor getInstance() {
        return MessageProcessorInstance.INSTANCE;
    }

    /**
     * 接收消息
     *
     * @param message
     */
    @Override
    public void receiveMsg(final BaseTcpMessage message) {
        EventBusUtils.post(new EventMessage(message.getHead().getMsgType(), message));
        Log.i(TAG, "收到推送消息BaseTcpMessage:" + message);
    }

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void sendMsg(final BaseTcpMessage message) {
        CThreadPoolExecutor.runInBackground(new Runnable() {
            @Override
            public void run() {
                boolean isActive = ClientBootstrap.getInstance().isActive();
                if (isActive) {
                    ClientBootstrap.getInstance().sendMessage(message);
                    Log.i(TAG, "发送推送消息BaseTcpMessage:" + message);
                } else {
                    Log.e(TAG, "发送消息失败");
                }
            }
        });
    }

}
