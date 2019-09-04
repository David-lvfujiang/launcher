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
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;
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
    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");

    Button setNickNameSureBtn, setNickNameCancelBtn;
    EditText setNickNameEdit;

    String setedNickName;
    private String newUserNameIntent;
    private String clickNickNameIntent;
    private String clickNickNameIcon;
    private String getUserIdName;
    private String UserNameId;
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
        setNickNameSureBtn = findViewById(R.id.set_nickname_sure_btn);
        setNickNameEdit = findViewById(R.id.set_nickname_edit);
        setNickNameCancelBtn = findViewById(R.id.set_nickname_cancel_btn);
    }

    @Override
    public void initData() {
        //4). 得到intent对象
        Intent intent = getIntent();
        //5). 通过intent读取额外数据
        newUserNameIntent = intent.getStringExtra("newAddUserName");   //新用户
        clickNickNameIntent = intent.getStringExtra("clickChangedNickName");  //已经加入的用户
        clickNickNameIcon = intent.getStringExtra("clickChangedNickNameIcon");  //已经加入的用户

        LogUtil.d(TAG,  "new add user name  = " + newUserNameIntent);

        //不是最新加入的成员
        if(newUserNameIntent == null){
            setNickNameCancelBtn.setVisibility(View.VISIBLE);
            if((clickNickNameIntent.indexOf("管理员")) == -1) {
                //非管理员用户
                getUserIdName = clickNickNameIntent;
                setNickNameEdit.setText(clickNickNameIntent);
            } else {
                //管理员用户
                getUserIdName = clickNickNameIntent.replace("(管理员)","");
                setNickNameEdit.setText(getUserIdName);
            }
            UserNameId = ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).getUserID(getUserIdName);
        } else {
//            setNickNameEdit.setText(newUserNameIntent);

            UserNameId = ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).getUserID(newUserNameIntent);
            LogUtil.d(TAG, "set nick name userId = " + UserNameId);
        }
    }

    @Override
    public void initListener() {
        setNickNameSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setedNickName = setNickNameEdit.getText().toString();
                LogUtil.d(TAG, "set nick name = " + setedNickName);
                SettingChangeContractNickNameRequest changeContractNickNameRequest = new SettingChangeContractNickNameRequest();
                changeContractNickNameRequest.setNickName(setedNickName);
                changeContractNickNameRequest.setUserId(UserNameId);
                mPresenter.changeContractNickName(changeContractNickNameRequest);
            }
        });

        setNickNameCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backTolistIntent = new Intent(SettingsContractsNickNameEditActivity.this, SettingsDeviceContractsNickNameActivity.class);
                backTolistIntent.putExtra("cancelSetNickName", clickNickNameIntent);
                backTolistIntent.putExtra("cancelSetNickNameIcon", clickNickNameIcon);
                startActivity(backTolistIntent);
                finish();
            }
        });

        final int maxTextCount = 10;
        setNickNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setNickNameEdit.removeTextChangedListener(this);//**** 注意的地方
                if (s.length() >= maxTextCount) {
                    setNickNameEdit.setText(s.toString().substring(0, maxTextCount));
                    setNickNameEdit.setSelection(maxTextCount);

                    Toast toast = Toast.makeText(getApplicationContext(),getString(R.string.settings_nickname_edittext_num_limit_name),Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                setNickNameEdit.addTextChangedListener(this);//****  注意的地方
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
            if (message != null && message.getData() != null) {
                BaseTcpMessage baseTcpMessage = message.getData();
                String msg = baseTcpMessage.getMsg();
                SetttingsRepairContactNicknameBean bean = GsonUtil.GsonToBean(msg, SetttingsRepairContactNicknameBean.class);
                if (bean != null) {
                    ContentProviderManager.getInstance(SettingsContractsNickNameEditActivity.this, mUri).updateNickNameByUserID(bean.getNickName(), bean.getUserId());
                }
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
