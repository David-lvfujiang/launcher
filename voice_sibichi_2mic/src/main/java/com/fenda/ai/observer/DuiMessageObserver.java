package com.fenda.ai.observer;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.MessageObserver;
import com.fenda.common.BaseApplication;

/**
 * 客户端MessageObserver, 用于处理客户端动作的消息响应.
 */
public class DuiMessageObserver implements MessageObserver {
    private final String Tag = "DuiMessageObserver";

    public interface MessageCallback {
        void onMessage();
        void onState(String message, String state);
    }

    private MessageCallback mMessageCallback;

    private String[] mSubscribeKeys = new String[]{"sys.dialog.start","sys.dialog.state","sys.dialog.error","sys.dialog.end","context.input.text","context.output.text",
            "context.widget.list","context.widget.content","context.widget.web","context.widget.media","context.widget.custom","asr.speech.result","dm.output","context.widget.text"};

    /**
     * 注册当前更新消息
     * @param messageCallback
     */
    public void regist(MessageCallback messageCallback) {
        mMessageCallback = messageCallback;
        DDS.getInstance().getAgent().subscribe(mSubscribeKeys, this);
    }

    /**
     *  注销当前更新消息
     */
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }


    @Override
    public void onMessage(String message, String data) {
//        LogUtil.d(Tag, "message : " + message + " data : " + data);
                if (mMessageCallback != null && !BaseApplication.getBaseInstance().isCall()) {
                    mMessageCallback.onState(message,data);
                }
        }
    }


