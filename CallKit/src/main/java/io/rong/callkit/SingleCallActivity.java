package io.rong.callkit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.util.LogUtil;
import com.fenda.protocol.tcp.bus.EventBusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.rongcloud.rtc.engine.view.RongRTCVideoView;
import cn.rongcloud.rtc.utils.FinLog;
import io.rong.callkit.bean.CallRecoderBean;
import io.rong.callkit.bean.UserInfoBean;
import io.rong.callkit.db.ContentProviderManager;
import io.rong.callkit.util.BluetoothUtil;
import io.rong.callkit.util.CallKitUtils;
import io.rong.callkit.util.DbUtil;
import io.rong.callkit.util.GlideUtils;
import io.rong.callkit.util.HeadsetInfo;
import io.rong.calllib.CallUserProfile;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.calllib.message.CallSTerminateMessage;
import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

import static io.rong.callkit.util.CallKitUtils.isDial;

public class SingleCallActivity extends BaseCallActivity implements Handler.Callback {
    private static final String TAG = "VoIPSingleActivity";
    private LayoutInflater inflater;
    private RongCallSession callSession;
    private FrameLayout mLPreviewContainer;
    private FrameLayout mSPreviewContainer;
    private FrameLayout mButtonContainer;
    private LinearLayout mUserInfoContainer;
    private TextView mConnectionStateTextView;
    private Boolean isInformationShow = false;
    private SurfaceView mLocalVideo = null;
    private boolean muted = false;
    private boolean handFree = false;
    private boolean startForCheckPermissions = false;
    private boolean isReceiveLost = false;
    private boolean isSendLost = false;
    private SoundPool mSoundPool = null;

    private int EVENT_FULL_SCREEN = 1;

    private String targetId = "";
    private RongCallCommon.CallMediaType mediaType;
    private RongCallCommon.CallMediaType remoteMediaType;
    public static final int DISABLE_EXPAND = 0x00010000;
    public static final int DISABLE_NONE = 0x00000000;
    int userType;
    SurfaceView remoteVideo;
    String remoteUserId;
    Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");
    private long mCallTime;
    private boolean onCallConnected = false;
    private DbUtil mDbUtil;
    // 通话状态,默认未接
    private int mCallStatus = 0;
    // 呼叫模式（0 呼入 1呼出）
    private int mCallMode;
    private UserInfoBean userInfo;

    @Override
    final public boolean handleMessage(Message msg) {
        if (msg.what == EVENT_FULL_SCREEN) {
            hideVideoCallInformation();
            return true;
        }
        return false;
    }

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDisable(DISABLE_EXPAND);
        //设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.rc_voip_activity_single_call);
        mDbUtil = DbUtil.getInstance(this);
        EventBus.getDefault().register(this);
        // 音视频通话时设置标志禁止其他语言操作
        BaseApplication.getBaseInstance().setCall(true);
        // 音视频通话时关闭音乐
        IVoiceRequestProvider initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
        if (initVoiceProvider != null) {
            initVoiceProvider.cancelMusic();
        }
        Log.i("AudioPlugin", "savedInstanceState != null=" + (savedInstanceState != null) + ",,,RongCallClient.getInstance() == null" + (RongCallClient.getInstance() == null));
        if (savedInstanceState != null && RongCallClient.getInstance() == null) {
            // 音视频请求权限时，用户在设置页面取消权限，导致应用重启，退出当前activity.
            Log.i("AudioPlugin", "音视频请求权限时，用户在设置页面取消权限，导致应用重启，退出当前activity");
            finish();
            return;
        }
        Intent intent = getIntent();
        mLPreviewContainer = (FrameLayout) findViewById(R.id.rc_voip_call_large_preview);
        mSPreviewContainer = (FrameLayout) findViewById(R.id.rc_voip_call_small_preview);
        mButtonContainer = (FrameLayout) findViewById(R.id.rc_voip_btn);
        mUserInfoContainer = (LinearLayout) findViewById(R.id.rc_voip_user_info);
        mConnectionStateTextView = findViewById(R.id.rc_tv_connection_state);
        startForCheckPermissions = intent.getBooleanExtra("checkPermissions", false);
        RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));

        if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
            mCallMode = 1;
            if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
                mediaType = RongCallCommon.CallMediaType.AUDIO;
            } else {
                mediaType = RongCallCommon.CallMediaType.VIDEO;
            }
        } else if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
            mCallMode = 0;
            callSession = intent.getParcelableExtra("callSession");
            mediaType = callSession.getMediaType();
        } else {
            callSession = RongCallClient.getInstance().getCallSession();
            if (callSession != null) {
                mediaType = callSession.getMediaType();
            }
        }
        if (mediaType != null) {
            inflater = LayoutInflater.from(this);
            initView(mediaType, callAction);

            if (requestCallPermissions(mediaType, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)) {
                setupIntent();
            }
        } else {
            RLog.w(TAG, "恢复的瞬间，对方已挂断");
            setShouldShowFloat(false);
            CallFloatBoxView.hideFloatBox();
            finish();
        }
    }

    private void setStatusBarDisable(int disable_status) {//调用statusBar的disable方法
        Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName
                    ("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, disable_status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        startForCheckPermissions = intent.getBooleanExtra("checkPermissions", false);
        RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));
        if (callAction == null) {
            return;
        }
        if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
            if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
                mediaType = RongCallCommon.CallMediaType.AUDIO;
            } else {
                mediaType = RongCallCommon.CallMediaType.VIDEO;
            }
        } else if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
            callSession = intent.getParcelableExtra("callSession");
            mediaType = callSession.getMediaType();
        } else {
            callSession = RongCallClient.getInstance().getCallSession();
            mediaType = callSession.getMediaType();
        }
        super.onNewIntent(intent);

        if (requestCallPermissions(mediaType, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)) {
            setupIntent();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                boolean permissionGranted;
                if (mediaType == RongCallCommon.CallMediaType.AUDIO) {
                    permissionGranted = PermissionCheckUtil.checkPermissions(this, AUDIO_CALL_PERMISSIONS);
                } else {
                    permissionGranted = PermissionCheckUtil.checkPermissions(this, VIDEO_CALL_PERMISSIONS);

                }
                if (permissionGranted) {
                    if (startForCheckPermissions) {
                        startForCheckPermissions = false;
                        RongCallClient.getInstance().onPermissionGranted();
                    } else {
                        setupIntent();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.rc_permission_grant_needed), Toast.LENGTH_SHORT).show();
                    if (startForCheckPermissions) {
                        startForCheckPermissions = false;
                        RongCallClient.getInstance().onPermissionDenied();
                    } else {
                        Log.i("AudioPlugin", "--onRequestPermissionsResult--finish");
                        finish();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {

            String[] permissions;
            if (mediaType == RongCallCommon.CallMediaType.AUDIO) {
                permissions = AUDIO_CALL_PERMISSIONS;
            } else {
                permissions = VIDEO_CALL_PERMISSIONS;
            }
            if (PermissionCheckUtil.checkPermissions(this, permissions)) {
                if (startForCheckPermissions) {
                    RongCallClient.getInstance().onPermissionGranted();
                } else {
                    setupIntent();
                }
            } else {
                if (startForCheckPermissions) {
                    RongCallClient.getInstance().onPermissionDenied();
                } else {
                    Log.i("AudioPlugin", "onActivityResult finish");
                    finish();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupIntent() {
        mCallTime = System.currentTimeMillis();
        RongCallCommon.CallMediaType mediaType;
        Intent intent = getIntent();
        RongCallAction callAction = RongCallAction.valueOf(intent.getStringExtra("callAction"));
//        if (callAction.equals(RongCallAction.ACTION_RESUME_CALL)) {
//            return;
//        }
        if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
            callSession = intent.getParcelableExtra("callSession");
            mediaType = callSession.getMediaType();
            targetId = callSession.getInviterUserId();
        } else if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
            if (intent.getAction().equals(RongVoIPIntent.RONG_INTENT_ACTION_VOIP_SINGLEAUDIO)) {
                mediaType = RongCallCommon.CallMediaType.AUDIO;
            } else {
                mediaType = RongCallCommon.CallMediaType.VIDEO;
            }
            Conversation.ConversationType conversationType = Conversation.ConversationType.valueOf(intent.getStringExtra("conversationType").toUpperCase(Locale.US));
            targetId = intent.getStringExtra("targetId");

            List<String> userIds = new ArrayList<>();
            userIds.add(targetId);
            RongCallClient.getInstance().startCall(conversationType, targetId, userIds, null, mediaType, null);
        } else { // resume call
            callSession = RongCallClient.getInstance().getCallSession();
            mediaType = callSession.getMediaType();
        }

        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            handFree = false;
        } else if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            handFree = true;
        }

        userInfo = ContentProviderManager.getInstance(this, mUri).queryUserById(targetId);
        if (userInfo != null) {
            if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO) || callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_connecting_user_portrait);
                if (userPortrait != null && userInfo.getIcon() != null) {
                    userPortrait.setResource(userInfo.getIcon(), R.drawable.rc_voip_head);
                }
                TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
                userName.setText(userInfo.getUserName());
            }
        }
        if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL) && userInfo != null) {
            ImageView iv_icoming_backgroud = (ImageView) mUserInfoContainer.findViewById(R.id.iv_icoming_backgroud);
            iv_icoming_backgroud.setVisibility(View.GONE);
            GlideUtils.showBlurTransformation(SingleCallActivity.this, iv_icoming_backgroud, null != userInfo ? Uri.parse(userInfo.getIcon()) : null);
        }

        createPowerManager();
        createPickupDetector();
    }

    private void changeToConnectedState(String userId, RongCallCommon.CallMediaType mediaType, int userType, SurfaceView remoteVideo) {
        if (userType == 2) {
            return;
        }
        mConnectionStateTextView.setVisibility(View.GONE);
        if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            mLPreviewContainer.setVisibility(View.VISIBLE);
            mLPreviewContainer.removeAllViews();
            remoteVideo.setTag(userId);

            FinLog.v(TAG, "onRemoteUserJoined mLPreviewContainer.addView(remoteVideo)");
            mLPreviewContainer.addView(remoteVideo);
            mLPreviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isInformationShow) {
                        hideVideoCallInformation();
                    } else {
                        showVideoCallInformation();
                        handler.sendEmptyMessageDelayed(EVENT_FULL_SCREEN, 5 * 1000);
                    }
                }
            });
            mSPreviewContainer.setVisibility(View.VISIBLE);
            mSPreviewContainer.removeAllViews();
            FinLog.v(TAG, "onRemoteUserJoined mLocalVideo != null=" + (mLocalVideo != null));
            if (mLocalVideo != null) {
                mLocalVideo.setZOrderMediaOverlay(true);
                mLocalVideo.setZOrderOnTop(true);
                mSPreviewContainer.addView(mLocalVideo);
            }
            /** 小窗口点击事件 **/
            mSPreviewContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        SurfaceView fromView = (SurfaceView) mSPreviewContainer.getChildAt(0);
                        SurfaceView toView = (SurfaceView) mLPreviewContainer.getChildAt(0);
                        mLPreviewContainer.removeAllViews();
                        mSPreviewContainer.removeAllViews();
                        fromView.setZOrderOnTop(false);
                        fromView.setZOrderMediaOverlay(false);
                        mLPreviewContainer.addView(fromView);
                        toView.setZOrderOnTop(true);
                        toView.setZOrderMediaOverlay(true);
                        mSPreviewContainer.addView(toView);
                        if (null != fromView.getTag() && !TextUtils.isEmpty(fromView.getTag().toString())) {
                            TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
                            userName.setText(userInfo.getUserName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            mButtonContainer.setVisibility(View.GONE);
            TextView tvSetupTime = mUserInfoContainer.findViewById(R.id.tv_setupTime);
            setupTime(tvSetupTime);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pickupDetector != null && mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            pickupDetector.register(this);
        }

        FinLog.v("AudioPlugin", "---single activity onResume-  mediaType:" + mediaType + " , isDial : " + isDial + " , isForground : " + BluetoothUtil.isForground(SingleCallActivity.this));
        if (mediaType == RongCallCommon.CallMediaType.VIDEO &&
                !isDial && BluetoothUtil.isForground(SingleCallActivity.this)) {
            RongCallClient.getInstance().setEnableLocalVideo(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FinLog.v("AudioPlugin", "---single activity onPause---");
        if (pickupDetector != null) {
            pickupDetector.unRegister();
        }
        setStatusBarDisable(DISABLE_NONE);
    }

    private void initView(RongCallCommon.CallMediaType mediaType, RongCallAction callAction) {
        LinearLayout buttonLayout = (LinearLayout) inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
        RelativeLayout userInfoLayout = null;
        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO) || callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
            userInfoLayout = (RelativeLayout) inflater.inflate(R.layout.rc_voip_audio_call_user_info_incoming, null);

        } else {
            //单人视频 or 拨打 界面
            userInfoLayout = (RelativeLayout) inflater.inflate(R.layout.rc_voip_audio_call_user_info, null);
            TextView callInfo = (TextView) userInfoLayout.findViewById(R.id.rc_voip_call_remind_info);
            CallKitUtils.textViewShadowLayer(callInfo, SingleCallActivity.this);
        }
        userInfoLayout.findViewById(R.id.rc_voip_connecting_info).setVisibility(View.VISIBLE);
        userInfoLayout.findViewById(R.id.rc_voip_connected_info).setVisibility(View.GONE);

        if (callAction.equals(RongCallAction.ACTION_RESUME_CALL) && CallKitUtils.isDial) {
            try {
                ImageView button = buttonLayout.findViewById(R.id.rc_voip_call_mute_btn);
                button.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (callAction.equals(RongCallAction.ACTION_OUTGOING_CALL)) {
            RelativeLayout layout = buttonLayout.findViewById(R.id.rc_voip_call_mute);
            layout.setVisibility(View.GONE);
            RelativeLayout swithLayout = buttonLayout.findViewById(R.id.rc_voip_audio_chat);
            swithLayout.setVisibility(View.GONE);
            ImageView button = buttonLayout.findViewById(R.id.rc_voip_call_mute_btn);
            button.setEnabled(false);
        }

        if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
            mLPreviewContainer.setVisibility(View.GONE);
            mSPreviewContainer.setVisibility(View.GONE);

            if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
                buttonLayout = (LinearLayout) inflater.inflate(R.layout.rc_voip_call_bottom_incoming_button_layout, null);
//                ImageView iv_answerBtn = (ImageView) buttonLayout.findViewById(R.id.rc_voip_call_answer_btn);
//                iv_answerBtn.setBackground(CallKitUtils.BackgroundDrawable(R.drawable.rc_voip_audio_answer_selector_new, SingleCallActivity.this));

                TextView callInfo = (TextView) userInfoLayout.findViewById(R.id.rc_voip_call_remind_info);
                CallKitUtils.textViewShadowLayer(callInfo, SingleCallActivity.this);
                callInfo.setText(R.string.rc_voip_audio_call_inviting);
                onIncomingCallRinging();
            }
        } else if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
            if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
                buttonLayout = (LinearLayout) inflater.inflate(R.layout.rc_voip_call_bottom_incoming_button_layout, null);
//                ImageView iv_answerBtn = (ImageView) buttonLayout.findViewById(R.id.rc_voip_call_answer_btn);
//                iv_answerBtn.setBackground(CallKitUtils.BackgroundDrawable(R.drawable.rc_voip_vedio_answer_selector_new, SingleCallActivity.this));

                TextView callInfo = (TextView) userInfoLayout.findViewById(R.id.rc_voip_call_remind_info);
                CallKitUtils.textViewShadowLayer(callInfo, SingleCallActivity.this);
                callInfo.setText(R.string.rc_voip_video_call_inviting);
                onIncomingCallRinging();
            }
        }
        mButtonContainer.removeAllViews();
        mButtonContainer.addView(buttonLayout);
        mUserInfoContainer.removeAllViews();
        mUserInfoContainer.addView(userInfoLayout);

        if (callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
            regisHeadsetPlugReceiver();
            if (BluetoothUtil.hasBluetoothA2dpConnected() || BluetoothUtil.isWiredHeadsetOn(SingleCallActivity.this)) {
                HeadsetInfo headsetInfo = new HeadsetInfo(true, HeadsetInfo.HeadsetType.BluetoothA2dp);
                onEventMainThread(headsetInfo);
            }
        }
    }


    @Override
    public void setupTime(TextView timeView) {
        super.setupTime(timeView);
    }

    @Override
    public void onCallOutgoing(RongCallSession callSession, SurfaceView localVideo) {
        super.onCallOutgoing(callSession, localVideo);
        this.callSession = callSession;
        TextView tvRemindInfo = mUserInfoContainer.findViewById(R.id.rc_voip_call_remind_info);
        if (tvRemindInfo != null) {
            tvRemindInfo.setText(getString(R.string.rc_voip_call_waiting));
        }
        try {
            if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.VIDEO)) {

                localVideo.setTag(callSession.getSelfUserId());
                mLPreviewContainer.addView(localVideo);
                mLPreviewContainer.setVisibility(View.VISIBLE);
                if (null != userInfo && null != userInfo.getUserName()) {
                    //单人视频
//                    TextView callkit_voip_user_name_signleVideo = (TextView) mUserInfoContainer.findViewById(R.id.callkit_voip_user_name_signleVideo);
//                    callkit_voip_user_name_signleVideo.setText(SelfUserInfo.getUserName());
                    AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_connecting_user_portrait);
                    if (userPortrait != null && userInfo.getIcon() != null) {
                        userPortrait.setResource(userInfo.getIcon(), R.drawable.rc_voip_head);
                    }
                    TextView tvUsername = mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
                    tvUsername.setText(userInfo.getUserName());
                }
            } else if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
                if (null != userInfo && null != Uri.parse(userInfo.getIcon())) {
                    ImageView iv_icoming_backgroud = mUserInfoContainer.findViewById(R.id.iv_icoming_backgroud);
                    GlideUtils.showBlurTransformation(SingleCallActivity.this, iv_icoming_backgroud, null != userInfo ? Uri.parse(userInfo.getIcon()) : null);
                    iv_icoming_backgroud.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        onOutgoingCallRinging();

        regisHeadsetPlugReceiver();
        if (BluetoothUtil.hasBluetoothA2dpConnected() || BluetoothUtil.isWiredHeadsetOn(this)) {
            HeadsetInfo headsetInfo = new HeadsetInfo(true, HeadsetInfo.HeadsetType.BluetoothA2dp);
            onEventMainThread(headsetInfo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMuteEvent(String type) {
        if (type.equals(Constant.Common.OPEN_MUTE)) {
            LogUtil.i("SingleCallActivity:open_mute");
            if (onCallConnected) {
                RongCallClient.getInstance().setEnableLocalAudio(false);
                View muteV = mButtonContainer.findViewById(R.id.rc_voip_call_mute);
                if (muteV != null) {
                    muteV.setSelected(true);
                    muted = muteV.isSelected();
                }
            }
        } else if (type.equals(Constant.Common.CLOSE_MUTE)) {
            LogUtil.i("SingleCallActivity:close_mute");
            if (onCallConnected) {
                RongCallClient.getInstance().setEnableLocalAudio(true);
                View muteV = mButtonContainer.findViewById(R.id.rc_voip_call_mute);
                if (muteV != null) {
                    muteV.setSelected(false);
                    muted = muteV.isSelected();
                }
            }
        }
    }

    @Override
    public void onCallConnected(RongCallSession callSession, SurfaceView localVideo) {
        super.onCallConnected(callSession, localVideo);
//        sendMicDisableBroad();
        mCallStatus = 1;
        onCallConnected = true;
        this.callSession = callSession;
        FinLog.v(TAG, "onCallConnected----mediaType=" + callSession.getMediaType().getValue());
        if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
            LinearLayout btnLayout = (LinearLayout) inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
            btnLayout.findViewById(R.id.rc_voip_audio_chat).setVisibility(View.GONE);
            ImageView button = btnLayout.findViewById(R.id.rc_voip_call_mute_btn);
            button.setEnabled(true);
            mButtonContainer.removeAllViews();
            mButtonContainer.addView(btnLayout);
            mUserInfoContainer.findViewById(R.id.rc_voip_connected_info).setVisibility(View.VISIBLE);
            mUserInfoContainer.findViewById(R.id.rc_voip_connecting_info).setVisibility(View.GONE);
            // 连接成功开始计时
            TextView tvSetupTime = mUserInfoContainer.findViewById(R.id.tv_setupTime);
            setupTime(tvSetupTime);
            if (userInfo != null) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_connected_user_portrait);
                if (userPortrait != null && userInfo.getIcon() != null) {
                    userPortrait.setResource(userInfo.getIcon(), R.drawable.rc_voip_head);
                }
            }
        } else {
            // 二人视频通话接通后 mUserInfoContainer 中更换为无头像的布局
            mConnectionStateTextView.setVisibility(View.VISIBLE);
            mConnectionStateTextView.setText(getString(R.string.rc_voip_call_connecting));
            mUserInfoContainer.removeAllViews();
            inflater.inflate(R.layout.rc_voip_video_call_user_info, mUserInfoContainer);
            if (userInfo != null) {
                TextView userName = mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
                userName.setVisibility(View.GONE);
                userName.setText(userInfo.getUserName());
//                userName.setShadowLayer(16F, 0F, 2F, getResources().getColor(R.color.rc_voip_reminder_shadow));//callkit_shadowcolor
                CallKitUtils.textViewShadowLayer(userName, SingleCallActivity.this);
            }
            mLocalVideo = localVideo;
            // 解决音响端默认后置摄像头导致画面相反的问题
            ((RongRTCVideoView) mLocalVideo).setMirror(true);
            mLocalVideo.setTag(callSession.getSelfUserId());
        }
//        TextView tv_rc_voip_call_remind_info = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_call_remind_info);
//        CallKitUtils.textViewShadowLayer(tv_rc_voip_call_remind_info, SingleCallActivity.this);
//        tv_rc_voip_call_remind_info.setVisibility(View.GONE);
//        TextView remindInfo = null;
//        if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
//            remindInfo = mUserInfoContainer.findViewById(R.id.tv_setupTime);
//        } else {
//            remindInfo = mUserInfoContainer.findViewById(R.id.tv_setupTime_video);
//        }
//        if (remindInfo == null) {
//            remindInfo = tv_rc_voip_call_remind_info;
//        }
        RongCallClient.getInstance().setEnableLocalAudio(!muted);
        View muteV = mButtonContainer.findViewById(R.id.rc_voip_call_mute);
        if (muteV != null) {
            muteV.setSelected(muted);
        }

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager.isWiredHeadsetOn() || BluetoothUtil.hasBluetoothA2dpConnected()) {
            handFree = false;
            RongCallClient.getInstance().setEnableSpeakerphone(false);
            ImageView handFreeV = null;
            if (null != mButtonContainer) {
                handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree_btn);
            }
            if (handFreeV != null) {
                handFreeV.setSelected(false);
                handFreeV.setEnabled(false);
                handFreeV.setClickable(false);
            }
        } else {
            RongCallClient.getInstance().setEnableSpeakerphone(handFree);
            View handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree);
            if (handFreeV != null) {
                handFreeV.setSelected(handFree);
            }
        }
        stopRing();
    }

    @Override
    protected void onDestroy() {
        stopRing();
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.setReferenceCounted(false);
            wakeLock.release();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRemoteUserJoined(final String userId, RongCallCommon.CallMediaType mediaType, int userType, SurfaceView remoteVideo) {
        super.onRemoteUserJoined(userId, mediaType, userType, remoteVideo);
        FinLog.v(TAG, "onRemoteUserJoined userID=" + userId + ",mediaType=" + mediaType.name() + " , userType=" + (userType == 1 ? "正常" : "观察者"));
        this.remoteMediaType = mediaType;
        this.userType = userType;
        this.remoteVideo = remoteVideo;
        this.remoteUserId = userId;
    }

    /**
     * 当通话中的某一个参与者切换通话类型，例如由 audio 切换至 video，回调 onMediaTypeChanged。
     *
     * @param userId    切换者的 userId。
     * @param mediaType 切换者，切换后的媒体类型。
     * @param video     切换着，切换后的 camera 信息，如果由 video 切换至 audio，则为 null。
     */
    @Override
    public void onMediaTypeChanged(String userId, RongCallCommon.CallMediaType mediaType, SurfaceView video) {
        if (callSession.getSelfUserId().equals(userId)) {
            showShortToast(getString(R.string.rc_voip_switched_to_audio));
        } else {
            if (callSession.getMediaType() != RongCallCommon.CallMediaType.AUDIO) {
                RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
                callSession.setMediaType(RongCallCommon.CallMediaType.AUDIO);
                showShortToast(getString(R.string.rc_voip_remote_switched_to_audio));
            }
        }
        initAudioCallView();
        handler.removeMessages(EVENT_FULL_SCREEN);
        mButtonContainer.findViewById(R.id.rc_voip_call_mute).setSelected(muted);
    }

    /**
     * 视频转语音
     **/
    private void initAudioCallView() {

        mLPreviewContainer.removeAllViews();
        mLPreviewContainer.setVisibility(View.GONE);
        mSPreviewContainer.removeAllViews();
        mSPreviewContainer.setVisibility(View.GONE);
        mUserInfoContainer.removeAllViews();
        //显示全屏底色
        View userInfoView = inflater.inflate(R.layout.rc_voip_audio_call_user_info_incoming, mUserInfoContainer);
        TextView timeView = (TextView) userInfoView.findViewById(R.id.tv_setupTime);
        setupTime(timeView);

//        mUserInfoContainer.addView(userInfoView);
        mUserInfoContainer.findViewById(R.id.rc_voip_connected_info).setVisibility(View.VISIBLE);
        mUserInfoContainer.findViewById(R.id.rc_voip_connecting_info).setVisibility(View.GONE);
        if (userInfo != null) {
            if (callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_connected_user_portrait);
                if (userPortrait != null) {
                    userPortrait.setAvatar(userInfo.getIcon(), R.drawable.rc_voip_head);
                }
            } else {//单人视频接听layout
                ImageView iv_large_preview = mUserInfoContainer.findViewById(R.id.iv_large_preview);
                iv_large_preview.setVisibility(View.VISIBLE);
                GlideUtils.showBlurTransformation(SingleCallActivity.this, iv_large_preview, null != userInfo ? Uri.parse(userInfo.getIcon()) : null);
                iv_large_preview.setVisibility(View.GONE);
            }
        }
        mUserInfoContainer.setVisibility(View.VISIBLE);
        View button = inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
        button.findViewById(R.id.rc_voip_audio_chat).setVisibility(View.GONE);
        mButtonContainer.removeAllViews();
        mButtonContainer.addView(button);
        mButtonContainer.setVisibility(View.VISIBLE);
        // 视频转音频时默认不开启免提
        handFree = false;
        RongCallClient.getInstance().setEnableSpeakerphone(false);
//        View handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree);
//        handFreeV.setSelected(handFree);

        /**视频切换成语音 全是语音界面的ui**/
        ImageView iv_large_preview = mUserInfoContainer.findViewById(R.id.iv_icoming_backgroud);

        if (null != userInfo && callSession.getMediaType().equals(RongCallCommon.CallMediaType.AUDIO)) {
            GlideUtils.showBlurTransformation(SingleCallActivity.this, iv_large_preview, null != userInfo ? Uri.parse(userInfo.getIcon()) : null);
            iv_large_preview.setVisibility(View.GONE);
        }

        if (pickupDetector != null) {
            pickupDetector.register(this);
        }
    }

    public void onHangupBtnClick(View view) {
        unRegisterHeadsetplugReceiver();
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        if (session == null || isFinishing) {
            finish();
            FinLog.e(TAG, "_挂断单人视频出错 callSession=" + (callSession == null) + ",isFinishing=" + isFinishing);
            return;
        }
        RongCallClient.getInstance().hangUpCall(session.getCallId());
        stopRing();
    }

    public void onReceiveBtnClick(View view) {
        RongCallSession session = RongCallClient.getInstance().getCallSession();
        if (session == null || isFinishing) {
            FinLog.e(TAG, "_接听单人视频出错 callSession=" + (callSession == null) + ",isFinishing=" + isFinishing);
            finish();
            return;
        }
        RongCallClient.getInstance().acceptCall(session.getCallId());
    }

    public void hideVideoCallInformation() {
        isInformationShow = false;
        mUserInfoContainer.setVisibility(View.GONE);
        mButtonContainer.setVisibility(View.GONE);
    }

    public void
    showVideoCallInformation() {
        isInformationShow = true;
        mUserInfoContainer.setVisibility(View.VISIBLE);
        mButtonContainer.setVisibility(View.VISIBLE);
        LinearLayout btnLayout = (LinearLayout) inflater.inflate(R.layout.rc_voip_call_bottom_connected_button_layout, null);
        btnLayout.findViewById(R.id.rc_voip_call_mute).setSelected(muted);
//        btnLayout.findViewById(R.id.rc_voip_handfree).setVisibility(View.GONE);
        // 音响端只有前置摄像头隐藏摄像头按钮
//        btnLayout.findViewById(R.id.rc_voip_camera).setVisibility(View.GONE);
        mButtonContainer.removeAllViews();
        mButtonContainer.addView(btnLayout);
        View view = btnLayout.findViewById(R.id.rc_voip_audio_chat);
        view.setVisibility(View.VISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongCallClient.getInstance().changeCallMediaType(RongCallCommon.CallMediaType.AUDIO);
                callSession.setMediaType(RongCallCommon.CallMediaType.AUDIO);
                initAudioCallView();
            }
        });
    }

    public void onHandFreeButtonClick(View view) {
        CallKitUtils.speakerphoneState = !view.isSelected();
        RongCallClient.getInstance().setEnableSpeakerphone(!view.isSelected());//true:打开免提 false:关闭免提
        view.setSelected(!view.isSelected());
        handFree = view.isSelected();
    }

    public void onMuteButtonClick(View view) {
        RongCallClient.getInstance().setEnableLocalAudio(view.isSelected());
        view.setSelected(!view.isSelected());
        muted = view.isSelected();
    }

    @Override
    public void onCallDisconnected(RongCallSession callSession, RongCallCommon.CallDisconnectedReason reason) {
        super.onCallDisconnected(callSession, reason);
//        sendMicAbleBroad();
        // 音视频通话结束时设置标志打开其他语言操作
        BaseApplication.getBaseInstance().setCall(false);
        String senderId;
        String extra = "";

        isFinishing = true;
        if (callSession == null) {
            RLog.e(TAG, "onCallDisconnected. callSession is null!");
            postRunnableDelay(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            return;
        }
        senderId = callSession.getInviterUserId();
        switch (reason) {
            case HANGUP:
            case REMOTE_HANGUP:
                long time = getTime();
                if (time >= 3600) {
                    extra = String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
                } else {
                    extra = String.format("%02d:%02d", (time % 3600) / 60, (time % 60));
                }
                break;
        }
        cancelTime();
        saveCallRecorder2DB();
        if (!TextUtils.isEmpty(senderId)) {
            CallSTerminateMessage message = new CallSTerminateMessage();
            message.setReason(reason);
            message.setMediaType(callSession.getMediaType());
            message.setExtra(extra);
            long serverTime = System.currentTimeMillis() - RongIMClient.getInstance().getDeltaTime();
            if (senderId.equals(callSession.getSelfUserId())) {
                message.setDirection("MO");
                RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), io.rong.imlib.model.Message.SentStatus.SENT, message, serverTime, null);
            } else {
                message.setDirection("MT");
                io.rong.imlib.model.Message.ReceivedStatus receivedStatus = new io.rong.imlib.model.Message.ReceivedStatus(0);
                receivedStatus.setRead();
                RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.PRIVATE, callSession.getTargetId(), senderId, receivedStatus, message, serverTime, null);
            }
        }
        postRunnableDelay(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    /**
     * 保存通话记录到数据库
     */
    private void saveCallRecorder2DB() {
        CallRecoderBean bean = new CallRecoderBean();
        bean.setCallMode(mCallMode);
        bean.setCallStatus(mCallStatus);
        bean.setCallTime(mCallTime);
        bean.setCommunicateTime(time);
        bean.setCallType(mediaType.getValue());
        if (userInfo != null) {
            String name = userInfo.getUserName();
            String phone = userInfo.getMobile();
            String icon = userInfo.getIcon();
            bean.setIcon(icon);
            bean.setName(name);
            bean.setPhone(phone);

        } else {
            bean.setPhone(targetId);

        }
        mDbUtil.insertCallRecoder(bean);
        EventBusUtils.post(io.rong.callkit.config.Constant.SYNCRECORDER);
    }

    public void sendMicDisableBroad() {
        Log.i("smartCall", "sendMicDisableBroad action:com.fenda.smartcall.ACTION_MIC_ENABLE");
        Intent intent = new Intent("com.fenda.smartcall.ACTION_MIC_ENABLE");
        sendBroadcast(intent);

    }

    @Override
    public void onFirstRemoteVideoFrame(String userId, int height, int width) {
        FinLog.d(TAG, "onFirstRemoteVideoFrame for user::" + userId);
        if (userId.equals(remoteUserId)) {
            changeToConnectedState(userId, remoteMediaType, userType, remoteVideo);
        }
    }

    public void sendMicAbleBroad() {
        Log.i("smartCall", "sendMicAbleBroad action:com.fenda.smartcall.ACTION_MIC_ABLE");
        Intent intent = new Intent("com.fenda.smartcall.ACTION_MIC_ABLE");
        sendBroadcast(intent);

    }

    @Override
    public void onNetworkSendLost(int lossRate, int delay) {
        isSendLost = lossRate > 15;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshConnectionState();
            }
        });
    }

    @Override
    public void onNetworkReceiveLost(String userId, int lossRate) {
        isReceiveLost = lossRate > 15;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshConnectionState();
            }
        });
    }

    private Runnable mCheckConnectionStableTask = new Runnable() {
        @Override
        public void run() {
            boolean isConnectionStable = !isSendLost && !isReceiveLost;
            if (isConnectionStable) {
                mConnectionStateTextView.setVisibility(View.GONE);
            }
        }
    };

    private void refreshConnectionState() {
        if ((isSendLost || isReceiveLost) && mediaType == RongCallCommon.CallMediaType.VIDEO) {
            if (mConnectionStateTextView.getVisibility() == View.GONE) {
                mConnectionStateTextView.setVisibility(View.VISIBLE);
                if (isSendLost) {
                    mConnectionStateTextView.setText(getString(R.string.rc_voip_myself_unstable_call_connection));
                } else if (isReceiveLost) {
                    mConnectionStateTextView.setText(getString(R.string.rc_voip_opposite_unstable_call_connection));
                }
//                if (mSoundPool != null) {
//                    mSoundPool.release();
//                }
//                mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
//                mSoundPool.load(this, R.raw.voip_network_error_sound, 0);
//                mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                    @Override
//                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                        soundPool.play(sampleId, 1F, 1F, 0, 0, 1F);
//                    }
//                });
            }
            mConnectionStateTextView.removeCallbacks(mCheckConnectionStableTask);
            mConnectionStateTextView.postDelayed(mCheckConnectionStableTask, 3000);
        }
    }

    @Override
    public void onRestoreFloatBox(Bundle bundle) {
        super.onRestoreFloatBox(bundle);
        FinLog.v("AudioPlugin", "---single activity onRestoreFloatBox---");
        if (bundle == null) {
            return;
        }
        muted = bundle.getBoolean("muted");
        handFree = bundle.getBoolean("handFree");

        setShouldShowFloat(true);
        callSession = RongCallClient.getInstance().getCallSession();
        if (callSession == null) {
            setShouldShowFloat(false);
            finish();
            return;
        }
        RongCallCommon.CallMediaType mediaType = callSession.getMediaType();
        RongCallAction callAction = RongCallAction.valueOf(getIntent().getStringExtra("callAction"));
        inflater = LayoutInflater.from(this);
        initView(mediaType, callAction);
        targetId = callSession.getTargetId();
        UserInfoBean userInfo = ContentProviderManager.getInstance(this, mUri).queryUserById(targetId);
        if (userInfo != null) {
            TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
            userName.setText(userInfo.getUserName());
            if (mediaType.equals(RongCallCommon.CallMediaType.AUDIO)) {
                AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
                if (userPortrait != null) {
                    userPortrait.setAvatar(userInfo.getIcon(), R.drawable.rc_voip_head);
                }
            } else if (mediaType.equals(RongCallCommon.CallMediaType.VIDEO)) {
                if (null != callAction && callAction.equals(RongCallAction.ACTION_INCOMING_CALL)) {
                    ImageView iv_large_preview = mUserInfoContainer.findViewById(R.id.iv_large_preview);
                    iv_large_preview.setVisibility(View.GONE);
                    GlideUtils.showBlurTransformation(SingleCallActivity.this, iv_large_preview, null != userInfo ? Uri.parse(userInfo.getIcon()) : null);
                }
            }
        }
        SurfaceView localVideo = null;
        SurfaceView remoteVideo = null;
        String remoteUserId = null;
        for (CallUserProfile profile : callSession.getParticipantProfileList()) {
            if (profile.getUserId().equals(RongIMClient.getInstance().getCurrentUserId())) {
                localVideo = profile.getVideoView();
            } else {
                remoteVideo = profile.getVideoView();
                remoteUserId = profile.getUserId();
            }
        }
        if (localVideo != null && localVideo.getParent() != null) {
            ((ViewGroup) localVideo.getParent()).removeView(localVideo);
        }
        onCallOutgoing(callSession, localVideo);
        if (!(boolean) bundle.get("isDial")) {
            onCallConnected(callSession, localVideo);
        }
        if (remoteVideo != null && remoteVideo.getParent() != null) {
            ((ViewGroup) remoteVideo.getParent()).removeView(remoteVideo);
            //onRemoteUserJoined(remoteUserId, mediaType, 1, remoteVideo);
            // 处理退出后台后看不到对方画面问题
            changeToConnectedState(remoteUserId, mediaType, 1, remoteVideo);
        }
    }

    @Override
    public String onSaveFloatBoxState(Bundle bundle) {
        super.onSaveFloatBoxState(bundle);
        callSession = RongCallClient.getInstance().getCallSession();
        if (callSession == null) {
            return null;
        }
        bundle.putBoolean("muted", muted);
        bundle.putBoolean("handFree", handFree);
        bundle.putInt("mediaType", callSession.getMediaType().getValue());

        return getIntent().getAction();
    }

    public void onMinimizeClick(View view) {
        super.onMinimizeClick(view);
    }

    public void onSwitchCameraClick(View view) {
        RongCallClient.getInstance().switchCamera();
    }

    @Override
    public void onBackPressed() {
        return;
//        List<CallUserProfile> participantProfiles = callSession.getParticipantProfileList();
//        RongCallCommon.CallStatus callStatus = null;
//        for (CallUserProfile item : participantProfiles) {
//            if (item.getUserId().equals(callSession.getSelfUserId())) {
//                callStatus = item.getCallStatus();
//                break;
//            }
//        }
//        if (callStatus != null && callStatus.equals(RongCallCommon.CallStatus.CONNECTED)) {
//            super.onBackPressed();
//        } else {
//            RongCallClient.getInstance().hangUpCall(callSession.getCallId());
//        }
    }

    public void onEventMainThread(UserInfo userInfo) {
        if (isFinishing()) {
            return;
        }
        if (targetId != null && targetId.equals(userInfo.getUserId())) {
            TextView userName = (TextView) mUserInfoContainer.findViewById(R.id.rc_voip_user_name);
            if (userInfo.getName() != null)
                userName.setText(userInfo.getName());
//            AsyncImageView userPortrait = (AsyncImageView) mUserInfoContainer.findViewById(R.id.rc_voip_user_portrait);
//            if (userPortrait != null && userInfo.getPortraitUri() != null) {
//                userPortrait.setResource(userInfo.getPortraitUri().toString(), R.drawable.rc_default_portrait);
//            }
//            ImageView iv_large_preview=mUserInfoContainer.findViewById(R.id.iv_large_preview);
//            GlideUtils.blurTransformation(SingleCallActivity.this,iv_large_preview,null!=userInfo?userInfo.getPortraitUri():null);
        }
    }

//    @Override
//    public void showOnGoingNotification() {
//        Intent intent = new Intent(getIntent().getAction());
//        Bundle bundle = new Bundle();
//        onSaveFloatBoxState(bundle);
//        intent.putExtra("floatbox", bundle);
//        intent.putExtra("callAction", RongCallAction.ACTION_RESUME_CALL.getName());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationUtil.showNotification(this, "todo", "coontent", pendingIntent, CALL_NOTIFICATION_ID);
//    }

    public void onEventMainThread(HeadsetInfo headsetInfo) {
        if (headsetInfo == null || !BluetoothUtil.isForground(SingleCallActivity.this)) {
            FinLog.v("bugtags", "SingleCallActivity 不在前台！");
            return;
        }
        FinLog.v("bugtags", "Insert=" + headsetInfo.isInsert() + ",headsetInfo.getType=" + headsetInfo.getType().getValue());
        try {
            if (headsetInfo.isInsert()) {
                RongCallClient.getInstance().setEnableSpeakerphone(false);
                ImageView handFreeV = null;
                if (null != mButtonContainer) {
                    handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree_btn);
                }
                if (handFreeV != null) {
                    handFreeV.setSelected(false);
                    handFreeV.setEnabled(false);
                    handFreeV.setClickable(false);
                }
                if (headsetInfo.getType() == HeadsetInfo.HeadsetType.BluetoothA2dp) {
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.setMode(AudioManager.MODE_IN_COMMUNICATION);
                    am.startBluetoothSco();
                    am.setBluetoothScoOn(true);
                    am.setSpeakerphoneOn(false);
                }
            } else {
                if (headsetInfo.getType() == HeadsetInfo.HeadsetType.WiredHeadset &&
                        BluetoothUtil.hasBluetoothA2dpConnected()) {
                    return;
                }
                RongCallClient.getInstance().setEnableSpeakerphone(false);
                ImageView handFreeV = mButtonContainer.findViewById(R.id.rc_voip_handfree_btn);
                if (handFreeV != null) {
                    handFreeV.setSelected(false);
                    handFreeV.setEnabled(true);
                    handFreeV.setClickable(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FinLog.v("bugtags", "SingleCallActivity->onEventMainThread Error=" + e.getMessage());
        }
    }
}
