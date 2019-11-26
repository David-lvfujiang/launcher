package io.rong.callkit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;

import java.util.ArrayList;
import java.util.List;

import cn.rongcloud.rtc.utils.FinLog;
import io.rong.callkit.bean.CustomizeMessage;
import io.rong.callkit.util.CallKitUtils;
import io.rong.calllib.IRongCallListener;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallMissedListener;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.calllib.message.MultiCallEndMessage;
import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IExternalModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by weiqinxiao on 16/8/15.
 */
public class RongCallModule implements IExternalModule, IRongCallListener {
    private final static String TAG = "RongCallModule";

    private RongCallSession mCallSession;
    private boolean mViewLoaded;
    private Context mContext;
    private static RongCallMissedListener missedListener;
    private LinearLayout mFloatLayout;

    public RongCallModule() {
        RLog.i(TAG, "Constructor");
    }

    Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");
    private static WindowManager mWindowManager = null;

    @Override
    public void onInitialized(String appKey) {
        RongIM.registerMessageTemplate(new CallEndMessageItemProvider());
        RongIM.registerMessageTemplate(new MultiCallEndMessageProvider());
        initMissedCallListener();
    }

    private void initMissedCallListener() {
        RongCallClient.setMissedCallListener(new RongCallMissedListener() {
            @Override
            public void onRongCallMissed(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
                if (!TextUtils.isEmpty(callSession.getInviterUserId())) {
                    if (callSession.getConversationType() == Conversation.ConversationType.PRIVATE) {
                        CallSTerminateMessage message = new CallSTerminateMessage();
                        message.setReason(reason);
                        message.setMediaType(callSession.getMediaType());

                        String extra;
                        long time = (callSession.getEndTime() - callSession.getStartTime()) / 1000;
                        if (time >= 3600) {
                            extra = String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
                        } else {
                            extra = String.format("%02d:%02d", (time % 3600) / 60, (time % 60));
                        }
                        message.setExtra(extra);

                        String senderId = callSession.getInviterUserId();
                        if (senderId.equals(callSession.getSelfUserId())) {
                            message.setDirection("MO");
                            RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), io.rong.imlib.model.Message.SentStatus.SENT, message, callSession.getStartTime(), null);
                        } else {
                            message.setDirection("MT");
                            io.rong.imlib.model.Message.ReceivedStatus receivedStatus = new io.rong.imlib.model.Message.ReceivedStatus(0);
                            receivedStatus.setRead();
                            RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), senderId, receivedStatus, message, callSession.getStartTime(), null);
                        }
                    } else if (callSession.getConversationType() == Conversation.ConversationType.GROUP) {
                        MultiCallEndMessage multiCallEndMessage = new MultiCallEndMessage();
                        multiCallEndMessage.setReason(reason);
                        if (callSession.getMediaType() == RongCallCommon.CallMediaType.AUDIO) {
                            multiCallEndMessage.setMediaType(RongIMClient.MediaType.AUDIO);
                        } else if (callSession.getMediaType() == RongCallCommon.CallMediaType.VIDEO) {
                            multiCallEndMessage.setMediaType(RongIMClient.MediaType.VIDEO);
                        }
                        RongIM.getInstance().insertMessage(callSession.getConversationType(), callSession.getTargetId(), callSession.getCallerUserId(), multiCallEndMessage, callSession.getStartTime(), null);
                    }
                }
                if (missedListener != null) {
                    missedListener.onRongCallMissed(callSession, reason);
                }
            }
        });
    }

    public static void setMissedCallListener(RongCallMissedListener listener) {
        missedListener = listener;
    }

    @Override
    public void onConnected(String token) {
        RongCallClient.getInstance().setVoIPCallListener(RongCallProxy.getInstance());
        // 开启音视频日志，如果不需要开启，则去掉下面这句。
        RongCallClient.getInstance().setEnablePrintLog(true);
        RongCallClient.getInstance().setVideoBitRate(350, 1000);
        RongCallClient.getInstance().setVideoProfile(RongCallCommon.CallVideoProfile.VIDEO_PROFILE_480P);
    }

    @Override
    public void onCreate(final Context context) {

        mContext = context;
        mViewLoaded = true;
        final IRongReceivedCallListener callListener = new IRongReceivedCallListener() {
            @Override
            public void onReceivedCall(final RongCallSession callSession) {
                handleCallModel(callSession);
                FinLog.d("VoIPReceiver", "onReceivedCall");
                if (mViewLoaded) {
                    FinLog.d("VoIPReceiver", "onReceivedCall->onCreate->mViewLoaded=true");
                    startVoIPActivity(mContext, callSession, false);
                } else {
                    RongCallProxy.getInstance().setCallListener(RongCallModule.this);
                    FinLog.d("VoIPReceiver", "onReceivedCall->onCreate->mViewLoaded=false");
                    String callId = callSession.getInviterUserId();
                    UserInfoBean bean = ContentProviderManager.getInstance(mContext, mUri).queryUserById(callId);
                    if (bean != null) {
                        String name = bean.getUserName();
                        showMonitorHint(name);
                    }
                    mCallSession = callSession;
                }
            }

            @Override
            public void onCheckPermission(RongCallSession callSession) {
                FinLog.d("VoIPReceiver", "onCheckPermissions");
                if (requestCallPermissions()) {
                    handleCallModel(callSession);
                }
                mCallSession = callSession;
                if (mViewLoaded) {
                    startVoIPActivity(mContext, callSession, true);
                }
            }
        };

        RongCallClient.setReceivedCallListener(callListener);
    }


    @TargetApi(23)
    boolean requestCallPermissions() {
        String[] permissions = null;

        permissions = CallKitUtils.getCallpermissions();

        boolean result = false;
        if (permissions != null) {
            boolean granted = CallKitUtils.checkPermissions(mContext, permissions);
            Log.i(TAG, "BaseActivty requestCallPermissions granted=" + granted);
            if (granted) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 处理呼叫模式（正常音视频和视频监控）
     *
     * @param callSession
     */
    private void handleCallModel(RongCallSession callSession) {
        String extra = callSession.getExtra();
        if ("monitor".equals(extra)) {
            mViewLoaded = false;
            RongCallClient.getInstance().acceptCall(callSession.getCallId());

        } else if ("".equals(extra) || "multi".equals(extra)) {
            mViewLoaded = true;
        }
    }

    /**
     * 此方法的目的是，防止 voip 通话界面被会话或者会话列表界面覆盖。
     * 所有要等待会话或者会话列表加载出后，再显示voip 通话界面。
     * <p>
     * 当会话列表或者会话界面加载出来后，此方法会被回调。
     * 如果开发者没有会话或者会话列表界面，只需要将下面的 mViewLoaded 在 onCreate 时设置为 true 即可。
     */
    @Override
    public void onViewCreated() {
        mViewLoaded = true;
//        if (mCallSession != null) {
//            startVoIPActivity(mContext, mCallSession, false);
//        }
    }

    @Override
    public List<IPluginModule> getPlugins(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = new ArrayList<>();
        try {
            if (RongCallClient.getInstance().isVoIPEnabled(mContext)) {
                pluginModules.add(new AudioPlugin());
                pluginModules.add(new VideoPlugin());
            }
        } catch (Exception e) {
            e.printStackTrace();
            RLog.i(TAG, "getPlugins()->Error :" + e.getMessage());
        }
        return pluginModules;
    }

    @Override
    public void onDisconnected() {

    }

    /**
     * 隐藏监控提示
     */
    private void dissmissMonitorHint() {
        if (mWindowManager != null && mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
    }

    /**
     * 弹出监控提示
     */
    private void showMonitorHint(String name) {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 类型
        int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = 80;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        mFloatLayout = (LinearLayout) View.inflate(mContext, R.layout.float_layout, null);
        TextView tvHint = mFloatLayout.findViewById(R.id.tvHint);
        tvHint.setText(name + "正通过手机APP观看家庭情况");
        mWindowManager.addView(mFloatLayout, params);
    }

    /**
     * 启动通话界面
     *
     * @param context                  上下文
     * @param callSession              通话实体
     * @param startForCheckPermissions android6.0需要实时获取应用权限。
     *                                 当需要实时获取权限时，设置startForCheckPermissions为true，
     *                                 其它情况下设置为false。
     */
    private void startVoIPActivity(Context context, final RongCallSession callSession, boolean startForCheckPermissions) {
        RLog.d("VoIPReceiver", "startVoIPActivity");
        FinLog.d("VoIPReceiver", "startVoIPActivity");
        String action;
        if (callSession.getConversationType().equals(Conversation.ConversationType.DISCUSSION)
                || callSession.getConversationType().equals(Conversation.ConversationType.GROUP)
                || callSession.getConversationType().equals(Conversation.ConversationType.NONE)) {
            if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.VIDEO)) {
                action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_MULTIVIDEO;
            } else {
                action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_MULTIAUDIO;
            }
            Intent intent = new Intent(action);
            intent.putExtra("callSession", callSession);
            intent.putExtra("callAction", RongCallAction.ACTION_INCOMING_CALL.getName());
            if (startForCheckPermissions) {
                intent.putExtra("checkPermissions", true);
            } else {
                intent.putExtra("checkPermissions", false);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(context.getPackageName());
            context.startActivity(intent);
        } else {
            if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.VIDEO)) {
                action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEVIDEO;
            } else {
                action = RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO;
            }
            Intent intent = new Intent(action);
            intent.putExtra("callSession", callSession);
            intent.putExtra("callAction", RongCallAction.ACTION_INCOMING_CALL.getName());
            if (startForCheckPermissions) {
                intent.putExtra("checkPermissions", true);
            } else {
                intent.putExtra("checkPermissions", false);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(context.getPackageName());
            context.startActivity(intent);
        }
        mCallSession = null;
    }

    @Override
    public void onCallOutgoing(RongCallSession rongCallSession, SurfaceView surfaceView) {

    }

    @Override
    public void onCallConnected(RongCallSession rongCallSession, SurfaceView surfaceView) {
    }

    @Override
    public void onCallDisconnected(RongCallSession rongCallSession, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {
        dissmissMonitorHint();
    }

    @Override
    public void onRemoteUserRinging(String s) {

    }

    @Override
    public void onRemoteUserJoined(String s, RongCallCommon.CallMediaType callMediaType, int i, SurfaceView surfaceView) {

    }

    @Override
    public void onRemoteUserInvited(String s, RongCallCommon.CallMediaType callMediaType) {

    }

    @Override
    public void onRemoteUserLeft(String s, RongCallCommon.CallDisconnectedReason callDisconnectedReason) {

    }

    @Override
    public void onMediaTypeChanged(String s, RongCallCommon.CallMediaType callMediaType, SurfaceView surfaceView) {

    }

    @Override
    public void onError(RongCallCommon.CallErrorCode callErrorCode) {
        if (callErrorCode == RongCallCommon.CallErrorCode.OPEN_CAMERA_FAILED) {
            Message message = new Message();
            message.setTargetId(mCallSession.getTargetId());
            message.setConversationType(Conversation.ConversationType.PRIVATE);
            message.setContent(new CustomizeMessage("cameraError"));
            RongIM.getInstance().sendMessage(message, (String) null, (String) null, (IRongCallback.ISendMessageCallback) null);
        }
    }

    @Override
    public void onRemoteCameraDisabled(String s, boolean b) {

    }

    @Override
    public void onNetworkReceiveLost(String s, int i) {
    }

    @Override
    public void onNetworkSendLost(int i, int i1) {

    }

    @Override
    public void onFirstRemoteVideoFrame(String s, int i, int i1) {

    }
}
