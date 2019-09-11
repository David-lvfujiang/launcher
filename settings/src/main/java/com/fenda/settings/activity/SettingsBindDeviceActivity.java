package com.fenda.settings.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.fenda.common.provider.ISettingsProvider;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.QRcodeUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.protocol.tcp.ClientBootstrap;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
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

    private ImageView ivDisQRcode;
    private TextView tvDisVcodeNum;
    private TextView tvDisVcodeText;

    private final String mContentET = "http://www.fenda.com/?sn=" + SettingsContant.SETTINGS_SERIAL_NUM;
    private String baseEvn = SettingsContant.TEST_BASE_URL;
    public static final int DISABLE_EXPAND = 0x00010000;
    public static final int DISABLE_NONE = 0x00000000;

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_bind_device_layout;
    }

    @Override
    public void initStatusBar() {
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
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView() {
        tvDisVcodeText = findViewById(R.id.bind_vcode);
        ivDisQRcode = findViewById(R.id.create_qr_iv);
        tvDisVcodeNum = findViewById(R.id.bind_QRsn);

        LogUtil.d(TAG, "env = " + baseEvn);
        if("http://192.168.100.127:8998/smartsound/".equals(baseEvn)) {
            tvDisVcodeText.setText("验证码(测试环境)");
            tvDisVcodeText.setTextColor(getColor(R.color.settings_colorAccent));
            tvDisVcodeText.setTextSize(22);
        } else if("http://192.168.43.34:8998/smartsound/".equals(baseEvn) || "http://192.168.43.37:8998/smartsound/".equals(baseEvn)){
            tvDisVcodeText.setText("验证码(192.198.43.34)");
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

        String userId = (String) SPUtils.get(BaseApplication.getInstance(), Constant.Settings.USER_ID,"");
        LogUtil.d(TAG, "userId = " + userId);
        ClientBootstrap bootstrap = ClientBootstrap.getInstance();
        bootstrap.init(mContext, userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage<BaseTcpMessage> message) {
        if (message.getCode() == TCPConfig.MessageType.MANGER_SUCCESS) {
            mPresenter.getContactsList();
            LogUtil.d(TAG, "bind onReceiveEvent = " + message);
            AppUtils.saveBindedDevice(getApplicationContext(), true);
            ARouter.getInstance().build(RouterPath.HomePage.HOMEPAGE_MAIN).navigation();

//            startActivity(new Intent(SettingsBindDeviceActivity.this, SettingsActivity.class));
            finish();
        }
    }

    public void sendMicDisableBroad() {
        LogUtil.d(TAG, "sendMicDisableBroad action:com.fenda.smartcall.ACTION_MIC_ENABLE");
        Intent intent = new Intent("com.fenda.smartcall.ACTION_MIC_ENABLE");
        sendBroadcast(intent);
    }

    public void sendMicAbleBroad() {
        LogUtil.d(TAG, "sendMicAbleBroad action:com.fenda.smartcall.ACTION_MIC_ABLE");
        Intent intent = new Intent("com.fenda.smartcall.ACTION_MIC_ABLE");
        sendBroadcast(intent);
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
//        setStatusBarDisable(DISABLE_EXPAND);
    }

    @Override
    public void onPause() {
        super.onPause();
//        setStatusBarDisable(DISABLE_NONE);
    }

    @Override
    protected void onStop() {
        setStatusBarDisable(DISABLE_NONE);
        super.onStop();
    }

    @Override
    protected void onDestroy() {

//        sendMicAbleBroad();
        super.onDestroy();
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

       // 调用音视频服务接口登录IM
        ICallProvider loginService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
        if(loginService != null){
            LogUtil.d(TAG, "ICallProvider 登录IM");
            loginService.login(queryDeviceInfoResponse.getRongcloud_token());
        } else {
            LogUtil.d(TAG, "ICallProvider 登录IM为空");
        }
        final String filePath2 = QRcodeUtil.getFileRoot(SettingsBindDeviceActivity.this) + File.separator + "qr_" + System.currentTimeMillis() + ".jpg";
//        二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean success = QRcodeUtil.createQRImage(mContentET, 1000, 1000, null, filePath2);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivDisQRcode.setImageBitmap(BitmapFactory.decodeFile(filePath2));
                        }
                    });
                }
            }
        }).start();
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
