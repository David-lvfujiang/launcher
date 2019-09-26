package com.fenda.settings.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.QRcodeUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.protocol.http.RxSchedulers;
import com.fenda.protocol.tcp.CThreadPoolExecutor;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.protocol.tcp.bean.Head;
import com.fenda.protocol.util.DeviceIdUtil;
import com.fenda.settings.R;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.dialog.SettingsBindContactsDialog;
import com.fenda.settings.http.SettingsApiService;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.request.SettingsAgreeUserAddRequest;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;
import com.fenda.settings.presenter.SettingsPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.fenda.common.util.QRcodeUtil.getFileRoot;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/4 10:40
 */
@Route(path = RouterPath.SETTINGS.SettingsDeviceAddContractsQRActivity)
public class SettingsDeviceAddContractsQRActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsDeviceAddContractsQRActivity";

    private ImageView ivDisQrcode;
    private TextView tvDisVcode;
    private TextView tvBack;

    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_device_add_contracts_qr_layout;
    }

    @Override
    public void initView() {
        tvBack = findViewById(R.id.enlarge_back);
        ivDisQrcode = findViewById(R.id.enlarge_QR);
        tvDisVcode = findViewById(R.id.enlarge_vcode);
    }

    @Override
    public void initData() {

        String vcode2 = (String) SPUtils.get(getApplicationContext(), Constant.Settings.VCODE, "");

        String vcode1=getString(R.string.settings_add_qr_bind_vcode) + vcode2;
        LogUtil.d(TAG, "get vcode from SPUtils = "  + vcode1);

        tvDisVcode.setText(vcode1);

        final String contentEt= "http://www.fenda.com/?sn=" + DeviceIdUtil.getDeviceId();

        final String filePath = getFileRoot(SettingsDeviceAddContractsQRActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        CThreadPoolExecutor.runInBackground(new Runnable() {
            @Override
            public void run() {
                boolean success = QRcodeUtil.createQRImage(contentEt,1000, 1000, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ivDisQrcode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        });
    }

    @Override
    public void initListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsDeviceAddContractsQRActivity.this, SettingsDeviceContractsActivity.class));
                finish();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(final EventMessage<BaseTcpMessage> message) {
        //普通成员申请加入家庭确认通知
        if (message.getCode() == TCPConfig.MessageType.FAMILY_APPLY_ADD) {
            LogUtil.d(TAG, "普通成员申请加入家庭确认通知 = " + message);
            final SettingsBindContactsDialog deviceDialog = new SettingsBindContactsDialog(this);
            deviceDialog.setOnConfirmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceDialog.dismiss();
                    LogUtil.d(TAG, "agree user bind device ");
                    BaseTcpMessage dataMsg = message.getData();
                    String msgContent = dataMsg.getMsg();
                    Head headMsg = dataMsg.getHead();
                    String mId = String.valueOf(headMsg.getMsgId());
                    String msgType = String.valueOf(headMsg.getMsgType());
                    String userId = String.valueOf(headMsg.getUserId());

                    SettingsAgreeUserAddRequest agreeUserAddRequest = new SettingsAgreeUserAddRequest();
                    agreeUserAddRequest.setDeviceId(DeviceIdUtil.getDeviceId());
                    agreeUserAddRequest.setId(mId);
                    agreeUserAddRequest.setMessageContent(msgContent);
                    agreeUserAddRequest.setMessageType(msgType);
                    agreeUserAddRequest.setSendUserId(userId);
                    mPresenter.agreeUserAddDevice(agreeUserAddRequest, true);
                }
            });
            deviceDialog.setOnCancelListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deviceDialog.dismiss();
                    LogUtil.d(TAG, "disagree user bind device ");
                    BaseTcpMessage dataMsg = message.getData();
                    String msgContent = dataMsg.getMsg();
                    Head headMsg = dataMsg.getHead();
                    String mId = String.valueOf(headMsg.getMsgId());
                    String msgType = String.valueOf(headMsg.getMsgType());
                    String userId = String.valueOf(headMsg.getUserId());

                    SettingsAgreeUserAddRequest agreeUserAddRequest = new SettingsAgreeUserAddRequest();
                    agreeUserAddRequest.setDeviceId(DeviceIdUtil.getDeviceId());
                    agreeUserAddRequest.setId(mId);
                    agreeUserAddRequest.setMessageContent(msgContent);
                    agreeUserAddRequest.setMessageType(msgType);
                    agreeUserAddRequest.setSendUserId(userId);
                    mPresenter.agreeUserAddDevice(agreeUserAddRequest, false);
//                    finish();
                }
            });
            deviceDialog.show();
        } else if (message.getCode() == TCPConfig.MessageType.NEW_USER_ADD) { //新人加入家庭通知
            LogUtil.d(TAG, "新人加入家庭通知 " + message);
            ContentProviderManager.getInstance(SettingsDeviceAddContractsQRActivity.this, mUri).clear();

            RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class).getContactsList()
                    .compose(RxSchedulers.<BaseResponse<List<UserInfoBean>>>applySchedulers())
                    .subscribe(new Consumer<BaseResponse<List<UserInfoBean>>>() {
                        @Override
                        public void accept(BaseResponse<List<UserInfoBean>> response) throws Exception {
                            if (response.getCode() == 200) {
                                ContentProviderManager.getInstance(SettingsDeviceAddContractsQRActivity.this, mUri).insertUsers(response.getData());

                                BaseTcpMessage dataMsg = message.getData();
                                String msgContent = dataMsg.getMsg();

                                String result = msgContent.substring(msgContent.indexOf("【")+1, msgContent.indexOf("】"));
                                LogUtil.d(TAG, "新人加入家庭通知 msg 22= " + result);

                                Intent setNickNameIntent = new Intent(SettingsDeviceAddContractsQRActivity.this, SettingsContractsNickNameEditActivity.class);
                                setNickNameIntent.putExtra("newAddUserName", result);
                                startActivity(setNickNameIntent);
                                finish();
                            } else {
                                ToastUtils.show(response.getMessage());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            // 异常处理
                        }
                    });
        }
    }
    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void updateDeviceNameSuccess(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoSuccess(BaseResponse<SettingsQueryDeviceInfoResponse> response) {

    }

    @Override
    public void unbindDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void registerDeviceSuccess(BaseResponse<SettingsRegisterDeviceResponse> response) {

    }

    @Override
    public void changeNickNameSuccess(BaseResponse response) {

    }

    @Override
    public void deleteLinkmanFromDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void getContactsListSuccess(BaseResponse<List<UserInfoBean>> response) {
    }

    @Override
    public void haveRegisterDevice(BaseResponse<SettingsRegisterDeviceResponse> response) {

    }

    @Override
    public void updateDeviceNameFailure(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoFailure(BaseResponse<SettingsQueryDeviceInfoResponse> response) {

    }

    @Override
    public void unbindDeviceFailure(BaseResponse response) {

    }

    @Override
    public void registerDeviceFailure(BaseResponse<SettingsRegisterDeviceResponse> response) {

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
