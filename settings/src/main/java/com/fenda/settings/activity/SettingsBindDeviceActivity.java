package com.fenda.settings.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.QRcodeUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.tcp.CThreadPoolExecutor;
import com.fenda.protocol.tcp.ClientBootstrap;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.protocol.tcp.bus.EventBusUtils;
import com.fenda.protocol.util.DeviceIdUtil;
import com.fenda.settings.R;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.presenter.SettingsPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/2 11:28
 */
@Route(path = RouterPath.SETTINGS.SettingsBindDeviceActivity)
public class SettingsBindDeviceActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsBindDeviceActivity";

    private ImageView ivDisQrcode;
    private TextView tvDisVcodeNum;
    private TextView tvDisVcodeText;

    private final String mContentET = "http://www.fenda.com/?sn=" + DeviceIdUtil.getDeviceId();
//    private final String mContentET = "http://www.fenda.com/?sn=7";
    private String baseEvn = SettingsContant.TEST_BASE_URL;
    public static final int DISABLE_EXPAND = 0x00010000;
    public static final int DISABLE_NONE = 0x00000000;

    private long [] mHits = null;
    private boolean mShow;

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_bind_device_layout;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean initStatusBar() {
        final View decorView = getWindow().getDecorView();
        final int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;

        decorView.setSystemUiVisibility(uiOption);

        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void  onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(uiOption);
                }
            }
        });
        setStatusBarDisable(DISABLE_EXPAND);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView() {
        tvDisVcodeText = findViewById(R.id.bind_vcode);
        ivDisQrcode = findViewById(R.id.create_qr_iv);
        tvDisVcodeNum = findViewById(R.id.bind_QRsn);

        LogUtil.d(TAG, "env = " + baseEvn);
        if("http://192.168.100.127:8998/smartsound/".equals(baseEvn)) {
            tvDisVcodeText.setText("验证码(测试环境)");
            tvDisVcodeText.setTextColor(getColor(R.color.settings_colorAccent));
            tvDisVcodeText.setTextSize(22);
        } else if("http://192.168.43.34:8998/smartsound/".equals(baseEvn)){
            tvDisVcodeText.setText("验证码(192.198.43.34)");
            tvDisVcodeText.setTextColor(getColor(R.color.settings_colorAccent));
            tvDisVcodeText.setTextSize(22);
        } else if("http://192.168.43.37:8998/smartsound/".equals(baseEvn)){
            tvDisVcodeText.setText("验证码(192.198.43.37)");
            tvDisVcodeText.setTextColor(getColor(R.color.settings_colorAccent));
            tvDisVcodeText.setTextSize(22);
        }

        IVoiceRequestProvider mIVoiceRequestProvider = (IVoiceRequestProvider) ARouter.getInstance().build(RouterPath.VOICE.REQUEST_PROVIDER).navigation();
        if (mIVoiceRequestProvider != null) {
            mIVoiceRequestProvider.closeVoice();
        }
//        sendMicDisableBroad();
    }

    @Override
    public void initData() {
        mPresenter.queryDeviceInfo();
    }

    @Override
    public void initListener() {
        ivDisQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEnterMainNoBind();
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage<BaseTcpMessage> message) {
        if (message.getCode() == TCPConfig.MessageType.MANGER_SUCCESS) {
            LogUtil.d(TAG, "bind onReceiveEvent = " + message);

            AppUtils.saveBindedDevice(getApplicationContext(), true);
            mPresenter.getContactsList();
        }
    }
    private void setStatusBarDisable(int disableStatus) {//调用statusBar的disable方法
        @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
        try {
            Class<?> statusBarManager = Class.forName
                    ("android.app.StatusBarManager");
            Method expand = statusBarManager.getMethod("disable", int.class);
            expand.invoke(service, disableStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        setStatusBarDisable(DISABLE_NONE);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void onEnterMainNoBind() {
        String mBindIntent2 = "multiClicks";
        if (mHits == null) {
            mHits = new long[10];
        }
        //把从第二位至最后一位之间的数字复制到第一位至倒数第一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //记录一个时间
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        //一秒内连续点击。
        if (SystemClock.uptimeMillis() - mHits[0] <= 3500) {
            //这里说明一下，我们在进来以后需要还原状态，否则如果点击过快，第六次，第七次 都会不断进来触发该效果。重新开始计数即可
            mHits = null;
            if (mShow) {
                //这里是你具体的操作
                ToastUtils.show("设备未绑定，进入主界面！");
                ARouter.getInstance().build(RouterPath.HomePage.HOMEPAGE_MAIN)
                        .withString("BIND_INTENT", mBindIntent2)
                        .navigation();
                mShow = false;
            } else {
                //这里也是你具体的操作
                ToastUtils.show("点击次数不对哦，请3秒后重试！");
                mShow = true;
            }
            //这里一般会把mShow存储到sp中。
        }
    }

    @Override
    public void updateDeviceNameSuccess(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoSuccess(BaseResponse<SettingsQueryDeviceInfoResponse> response) {
        LogUtil.d(TAG, "ok loginIm,BaseResponse  = " + response.getData());
        SettingsQueryDeviceInfoResponse queryDeviceInfoResponse = response.getData();
        SPUtils.put(getApplicationContext(), Constant.Settings.USER_ID, queryDeviceInfoResponse.getId());
        SPUtils.put(getApplicationContext(), Constant.Settings.DEVICE_NAME, queryDeviceInfoResponse.getName());
        SPUtils.put(getApplicationContext(), Constant.Settings.DEVICE_ICON, queryDeviceInfoResponse.getIcon());
        SPUtils.put(getApplicationContext(), Constant.Settings.VCODE, queryDeviceInfoResponse.getVcode());
        SPUtils.put(getApplicationContext(), Constant.Settings.RONGYUNCLOUDTOKEN, queryDeviceInfoResponse.getRongcloud_token());

        LogUtil.d(TAG, "RONGYUNCLOUDTOKEN = " + queryDeviceInfoResponse.getRongcloud_token());

        if (!TextUtils.isEmpty(queryDeviceInfoResponse.getRongcloud_token())) {
            // 调用音视频服务接口登录IM
            ICallProvider loginService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
            if (loginService != null) {
                LogUtils.v(TAG, "login rongCloudToken ");
                loginService.login(queryDeviceInfoResponse.getRongcloud_token());
            }
        }

        ClientBootstrap bootstrap = ClientBootstrap.getInstance();
        bootstrap.init(mContext, queryDeviceInfoResponse.getId(), SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);

        LogUtils.v(TAG, "userId ===  " + queryDeviceInfoResponse.getId());
        final String filePath2 = QRcodeUtil.getFileRoot(SettingsBindDeviceActivity.this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
//        二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        CThreadPoolExecutor.runInBackground(new Runnable() {
            @Override
            public void run() {
                boolean success = QRcodeUtil.createQRImage(mContentET, 1000, 1000, null, filePath2);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivDisQrcode.setImageBitmap(BitmapFactory.decodeFile(filePath2));
                        }
                    });
                }
            }
        });

        tvDisVcodeNum.setText(response.getData().getVcode());
    }

    @Override
    public void unbindDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void registerDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void changeNickNameSuccess(BaseResponse response) {

    }

    @Override
    public void deleteLinkmanFromDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void getContactsListSuccess(BaseResponse<List<UserInfoBean>> response) {
        ContentProviderManager.getInstance(SettingsBindDeviceActivity.this, Uri.parse(ContentProviderManager.BASE_URI + "/user")).insertUsers(response.getData());
        //调用更新语音

        LogUtil.e("getContactsListSuccess ============ ");

        EventBusUtils.post(Constant.Common.BIND_SUCCESS);

    }

    @Override
    public void haveRegisterDevice(BaseResponse response) {

    }

    @Override
    public void updateDeviceNameFailure(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoFailure(BaseResponse response) {
        LogUtil.d(TAG, " fail loginIm,BaseResponse  = " + response.getData());
    }

    @Override
    public void unbindDeviceFailure(BaseResponse response) {

    }

    @Override
    public void registerDeviceFailure(BaseResponse response) {

    }

    @Override
    public void changeNickNameFailure(BaseResponse response) {

    }

    @Override
    public void deleteLinkmanFromDeviceFailure(BaseResponse response) {

    }

    @Override
    public void getContactsListFailure(BaseResponse<List<UserInfoBean>> response) {

    }

}
