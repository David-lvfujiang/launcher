package com.fenda.settings.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.MessageBean;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.CircleImageView;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.settings.R;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.request.SettingDeleteLinkmanRequest;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;
import com.fenda.settings.presenter.SettingsPresenter;
import com.fenda.settings.utils.SettingsWifiUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/3 20:01
 */
@Route(path = RouterPath.SETTINGS.SettingsDeviceContractsNickNameActivity)
public class SettingsDeviceContractsNickNameActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsDeviceContractsNickNameActivity";

    private ImageView ivBack;
    private TextView tvDisNickName;
    private TextView tvDisUserName;
    private TextView tvModifyNickName;
    private TextView tvDelectThisUser;
    private TextView tvAdminUnbind;
    private CircleImageView civUserIcon;

    private String mDisNickName;
    private String mIntentUserName2;
    private String mIntentIcon;

    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_contracts_nick_name_layout;
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.change_nameinfo_back);
        tvDisNickName = findViewById(R.id.nick_name);
        tvDisUserName = findViewById(R.id.user_name);
        tvModifyNickName = findViewById(R.id.modify_nick_name);
        tvDelectThisUser = findViewById(R.id.delect_this_user);
        tvAdminUnbind = findViewById(R.id.admin_unbind_this);
        civUserIcon = findViewById(R.id.nic_name_icon);
        mDisNickName = tvDisNickName.getText().toString();
    }

    @Override
    public void initData() {
        String mIntentIconTmp;
        String mIntentUserName;

        Intent intent = getIntent();
        mIntentUserName = intent.getStringExtra("ContractName");
        mIntentIcon = intent.getStringExtra("ContractIcon");
        String cancelNickNameIntent = intent.getStringExtra("cancelSetNickName");
        String cancelNickNameIntentIcon = intent.getStringExtra("cancelSetNickNameIcon");
        String changedNickNameIntent = intent.getStringExtra("ChangedEditNickName");

        LogUtil.d(TAG, "intentUserName = " + mIntentUserName);
        LogUtil.d(TAG, "changedNickNameIntent = " + changedNickNameIntent);
        LogUtil.d(TAG, "disNickName = " + mDisNickName);
        LogUtil.d(TAG, "cancelNickNameIntent = " + cancelNickNameIntent);
        LogUtil.d(TAG, "cancelNickNameIntentIcon = " + cancelNickNameIntentIcon);
        LogUtil.d(TAG, "mIntentIcon = " + mIntentIcon);

//        if(mIntentIcon == null){
//            mIntentIconTmp = cancelNickNameIntentIcon;
//        } else {
//            mIntentIconTmp = mIntentIcon;
//        }
        if (mIntentIcon != null) {
            SPUtils.put(getApplicationContext(), Constant.Settings.CONTRACTS_ICON, mIntentIcon);
        }

        mIntentIconTmp = (String) SPUtils.get(getApplicationContext(), Constant.Settings.CONTRACTS_ICON, "");
        LogUtil.d(TAG, "mIntentIconTmp = " + mIntentIconTmp);

        if (mIntentUserName == null) {
            tvDisNickName.setText(changedNickNameIntent);
            mIntentUserName2 = changedNickNameIntent;
            if (changedNickNameIntent == null) {
                tvDisNickName.setText(cancelNickNameIntent);
                mIntentUserName2 = cancelNickNameIntent;

                if (!cancelNickNameIntent.contains("管理员")) {
                    //非管理员用户
                    tvDelectThisUser.setVisibility(View.VISIBLE);
                } else {
                    //管理员用户
                    tvAdminUnbind.setVisibility(View.VISIBLE);
                }
            } else {
                if (!changedNickNameIntent.contains("管理员")) {
                    //非管理员用户
                    tvDelectThisUser.setVisibility(View.VISIBLE);
                } else {
                    //管理员用户
                    tvAdminUnbind.setVisibility(View.VISIBLE);
                }
            }
        } else {
            tvDisNickName.setText(mIntentUserName);
            mIntentUserName2 = mIntentUserName;
            if (!mIntentUserName.contains("管理员")) {
                //非管理员用户
                tvDelectThisUser.setVisibility(View.VISIBLE);
            } else {
                //管理员用户
                tvAdminUnbind.setVisibility(View.VISIBLE);
            }
        }
        tvDisUserName.setText(getString(R.string.settings_contract_info_username) + mIntentUserName);
        ImageUtil.loadImg(getApplicationContext(), civUserIcon, mIntentIconTmp);

    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent backIntent = new Intent(SettingsDeviceContractsNickNameActivity.this, SettingsDeviceContractsActivity.class);
//                startActivity(backIntent);
                finish();
            }
        });
        tvModifyNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clickChangeNickNameIntent = new Intent(SettingsDeviceContractsNickNameActivity.this, SettingsContractsNickNameEditActivity.class);
                clickChangeNickNameIntent.putExtra("clickChangedNickName", mIntentUserName2);
                clickChangeNickNameIntent.putExtra("clickChangedNickNameIcon", mIntentIcon);
                startActivity(clickChangeNickNameIntent);
                finish();
            }
        });
        tvDelectThisUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String delecteUserName;
                if (!mIntentUserName2.contains("管理员")) {
                    //非管理员用户
                    delecteUserName = mIntentUserName2;
                } else {
                    //管理员用户
                    delecteUserName = mIntentUserName2.replace("(管理员)", "");
                }
                final String delecteUserId = ContentProviderManager.getInstance(SettingsDeviceContractsNickNameActivity.this, mUri).getUserID(delecteUserName);
                if (SettingsWifiUtil.isWifiEnabled(getApplicationContext())) {
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(SettingsDeviceContractsNickNameActivity.this);
                    normalDialog.setMessage(getString(R.string.settings_delecte_user_tips));
                    normalDialog.setPositiveButton(getString(R.string.settings_nickname_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SettingDeleteLinkmanRequest deleteLinkmanRequest = new SettingDeleteLinkmanRequest();
                            deleteLinkmanRequest.setUserId(delecteUserId);
                            mPresenter.deleteLinkmanFromDevice(deleteLinkmanRequest);
                        }
                    });
                    normalDialog.setNegativeButton(getString(R.string.settings_nickname_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                    LogUtil.d(TAG, "delecte device cancel");
                                }
                            });
                    // 显示
                    normalDialog.show();
                } else {
                    ToastUtils.show(getString(R.string.settings_delecte_nowifi_tips));
                }
            }
        });
        tvAdminUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsWifiUtil.isWifiEnabled(getApplicationContext())) {
                    LogUtil.d(TAG, "wifi is connect");
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(SettingsDeviceContractsNickNameActivity.this);
                    normalDialog.setMessage(getString(R.string.settings_dismiss_family_confirm));
                    normalDialog.setPositiveButton(getString(R.string.settings_nickname_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mPresenter.unbindDevice();
                        }
                    });
                    normalDialog.setNegativeButton(getString(R.string.settings_nickname_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //...To-do
                                    LogUtil.d(TAG, "Unbind device cancel");
                                }
                            });
                    // 显示
                    normalDialog.show();
                } else {
                    ToastUtils.show(getString(R.string.settings_nowifi_no_unbind));
                }
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(final EventMessage<BaseTcpMessage> message) {
        //普通成员退出家庭通知
        if (message.getCode() == TCPConfig.MessageType.USER_EXIT_FAMILY) {
            LogUtil.d(TAG, " 普通成员退出家庭通知 = " + message);
            // 普通成员退出家庭通知
            MessageBean messageBean = GsonUtil.GsonToBean(message.getData().getMsg(), MessageBean.class);
            if (messageBean != null && messageBean.getMessageUserInfoDTO() != null) {
                String userId = messageBean.getMessageUserInfoDTO().getUserId();
                ContentProviderManager.getInstance(mContext, Constant.Common.URI).deleteUserByUserID(userId);
//                Intent delectIntent = new Intent(SettingsDeviceContractsNickNameActivity.this, SettingsDeviceContractsActivity.class);
//                startActivity(delectIntent);
                finish();
            }
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
        ToastUtils.show(getString(R.string.settings_delecte_linkman_success));
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
