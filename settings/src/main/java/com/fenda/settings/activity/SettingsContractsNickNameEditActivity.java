package com.fenda.settings.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.settings.R;
import com.fenda.settings.bean.SetttingsRepairContactNicknameBean;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.request.SettingChangeContractNickNameRequest;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;
import com.fenda.settings.presenter.SettingsPresenter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/4 10:08
 */
@Route(path = RouterPath.SETTINGS.SettingsContractsNickNameEditActivity)
public class SettingsContractsNickNameEditActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsContractsNickNameEditActivity";

    private Button btnSetNickNameSure;
    private Button btnSetNickNameCancel;
    private EditText etSetNickName;

    private String mSetedNickName;
    private String mClickNickNameIntent;
    private String mClickNickNameIcon;
    private String mUserNameId;

    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_contracts_nick_name_edit_layout;
    }

    @Override
    public void initView() {
        btnSetNickNameSure = findViewById(R.id.set_nickname_sure_btn);
        etSetNickName = findViewById(R.id.set_nickname_edit);
        btnSetNickNameCancel = findViewById(R.id.set_nickname_cancel_btn);
    }

    @Override
    public void initData() {
        String mNewUserNameIntent;
        String mGetUserIdName;

        Intent intent = getIntent();
        //新用户
        mNewUserNameIntent = intent.getStringExtra("newAddUserName");
        //已经加入的用户
        mClickNickNameIntent = intent.getStringExtra("clickChangedNickName");
        //已经加入的用户
        mClickNickNameIcon = intent.getStringExtra("clickChangedNickNameIcon");

        LogUtil.d(TAG,  "new add user name  = " + mNewUserNameIntent);
        LogUtil.d(TAG,  "mClickNickNameIcon  = " + mClickNickNameIcon);

        //不是最新加入的成员
        if(mNewUserNameIntent == null){
            btnSetNickNameCancel.setVisibility(View.VISIBLE);
            if(!mClickNickNameIntent.contains("管理员")) {
                //非管理员用户
                mGetUserIdName = mClickNickNameIntent;
                etSetNickName.setText(mClickNickNameIntent);
            } else {
                //管理员用户
                mGetUserIdName = mClickNickNameIntent.replace("(管理员)","");
                etSetNickName.setText(mGetUserIdName);
            }
            mUserNameId = ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).getUserID(mGetUserIdName);
        } else {
            mUserNameId = ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).getUserID(mNewUserNameIntent);
            LogUtil.d(TAG, "set nick name userId = " + mUserNameId);
        }
    }

    @Override
    public void initListener() {
        btnSetNickNameSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSetedNickName = etSetNickName.getText().toString();
                LogUtil.d(TAG, "set nick name = " + mSetedNickName);
                SettingChangeContractNickNameRequest changeContractNickNameRequest = new SettingChangeContractNickNameRequest();
                changeContractNickNameRequest.setNickName(mSetedNickName);
                changeContractNickNameRequest.setUserId(mUserNameId);
                mPresenter.changeContractNickName(changeContractNickNameRequest);
            }
        });

        btnSetNickNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backTolistIntent = new Intent(SettingsContractsNickNameEditActivity.this, SettingsDeviceContractsNickNameActivity.class);
                backTolistIntent.putExtra("cancelSetNickName", mClickNickNameIntent);
                backTolistIntent.putExtra("cancelSetNickNameIcon", mClickNickNameIcon);
                startActivity(backTolistIntent);
                finish();
            }
        });

        final int maxTextCount = 10;
        etSetNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etSetNickName.removeTextChangedListener(this);//**** 注意的地方
                if (s.length() >= maxTextCount) {
                    etSetNickName.setText(s.toString().substring(0, maxTextCount));
                    etSetNickName.setSelection(maxTextCount);

                    Toast toast = Toast.makeText(getApplicationContext(),getString(R.string.settings_nickname_edittext_num_limit_name),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                etSetNickName.addTextChangedListener(this);//****  注意的地方
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(final EventMessage<BaseTcpMessage> message) {
        //修改家庭圈昵称通知
        if (message.getCode() == TCPConfig.MessageType.CHANGE_NICK_NAME) {
            LogUtil.d(TAG, "修改家庭圈昵称通知");

            if (message.getData() != null) {
                BaseTcpMessage baseTcpMessage = message.getData();
                String msg = baseTcpMessage.getMsg();
                SetttingsRepairContactNicknameBean bean = GsonUtil.GsonToBean(msg, SetttingsRepairContactNicknameBean.class);
                if (bean != null) {
                    ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).updateNickNameByUserID(bean.getNickName(), bean.getUserId());
                    ICallProvider callService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
                    if (callService!=null){
                        callService.syncFamilyContacts();
                    }
                }
            }
        } else if(message.getCode() == TCPConfig.MessageType.USER_EXIT_FAMILY){
            LogUtil.d(TAG, "普通成员退出家庭通知");

//                普通成员退出家庭通知
            ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).clear();
            mPresenter.getContactsList();

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
        ToastUtils.show("修改成功~");
        Intent setNickNameSucessIntent = new Intent(SettingsContractsNickNameEditActivity.this, SettingsDeviceContractsActivity.class);
        startActivity(setNickNameSucessIntent);
        finish();
    }

    @Override
    public void deleteLinkmanFromDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void getContactsListSuccess(BaseResponse<List<UserInfoBean>> response) {
        LogUtil.d(TAG, "getContactsListSuccess");
        ContentProviderManager.getInstance(mContext, Constant.Common.URI).insertUsers(response.getData());

        Intent mIntent = new Intent(SettingsContractsNickNameEditActivity.this, SettingsDeviceContractsActivity.class);
        startActivity(mIntent);
        finish();

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
