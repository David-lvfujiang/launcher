package debug;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.chinamobile.smartgateway.andsdk.device.serviceimpl.AndSdkImpl;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.protocol.tcp.ClientBootstrap;
import com.fenda.protocol.util.DeviceIdUtil;
import com.fenda.settings.R;
import com.fenda.settings.activity.SettingsActivity;
import com.fenda.settings.bean.SettingsAndlinkDeviceInfo;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.request.SettingsRegisterDeviceRequest;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;
import com.fenda.settings.presenter.SettingsPresenter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/19 11:31
 */
public class SettingsLauncherActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsLauncherActivity";

    private Button btnStartSet;

    private String mDeviceMac;
    private String mDeviceMac1;
    private String mDeviceMac2;
    private Gson tGson;
    private SettingsAndlinkDeviceInfo.ChipModel tChipModel;
    private SettingsAndlinkDeviceInfo.DeviceExtInfo deviceExtInfo;
    private SettingsAndlinkDeviceInfo tDevcieInfo;


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_launcher_layout;
    }

    @Override
    public void initView() {
        btnStartSet = findViewById(R.id.start_set_btn);
    }

    @Override
    public void initData() {
        if (SettingsDebugSPUtils.isRegisterDevice(getApplicationContext()) ){
            LogUtil.d(TAG, "device have registered~");
            btnStartSet.setVisibility(View.VISIBLE);
            String userId = (String) SettingsDebugSPUtils.get(BaseApplication.getInstance(), SettingsContant.USER_ID, "");

            LogUtil.d(TAG, "userId = " + userId);
            ClientBootstrap bootstrap = ClientBootstrap.getInstance();
            bootstrap.init(getApplicationContext(), userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);
        } else {
            LogUtil.d(TAG, "start register device");
            mDeviceMac = DeviceIdUtil.getLocalMac();
            LogUtil.d(TAG, "mDeviceMac = " + mDeviceMac);

            SettingsRegisterDeviceRequest mRegisterDeviceRequest = new SettingsRegisterDeviceRequest();
            mRegisterDeviceRequest.setClientVersion("1.0.0");
            mRegisterDeviceRequest.setDeviceId(DeviceIdUtil.getDeviceId());
            mRegisterDeviceRequest.setName("FD-R03");
            mRegisterDeviceRequest.setMacAddr(mDeviceMac);
            mPresenter.registerDevice(mRegisterDeviceRequest);
        }

    }

    private void initAndlink() {
        mDeviceMac1 = DeviceIdUtil.getLocalMac();
        LogUtil.d(TAG, "mDeviceMac1 = " + mDeviceMac1);
        mDeviceMac2 = mDeviceMac1.replace(":", "");
        LogUtil.d(TAG, "mDeviceMac2 = " + mDeviceMac2);

        tChipModel = new SettingsAndlinkDeviceInfo.ChipModel();
        tChipModel.type = "WiFi";
        tChipModel.factory = "rockchip";
        tChipModel.model = "rk3326";

        ArrayList tChips = new ArrayList();
        tChips.add(tChipModel);

        deviceExtInfo = new SettingsAndlinkDeviceInfo.DeviceExtInfo();
        deviceExtInfo.cmei = "";
        deviceExtInfo.authMode = "0";
        deviceExtInfo.manuDate = "2019-07";
        deviceExtInfo.OS = "Android";
        deviceExtInfo.netCheckMode = "";
        deviceExtInfo.chips = tChips;

        tDevcieInfo = new SettingsAndlinkDeviceInfo();
        tDevcieInfo.deviceMac = mDeviceMac2;
        tDevcieInfo.deviceType = "500929";
        tDevcieInfo.productToken = "JUyy3SiJ3yx6hImp";
        tDevcieInfo.andlinkToken = "RMm2sEhc9v23H8cc";
        tDevcieInfo.firmwareVersion = "f1.0";
        tDevcieInfo.autoAP = "0";
        tDevcieInfo.softAPMode = "";
        tDevcieInfo.softwareVersion = "1.0.0";
        tDevcieInfo.deviceExtInfo = deviceExtInfo;

        tGson = new Gson();

        AndSdkImpl.getInstance().init(getApplication(), tGson.toJson(tDevcieInfo));
//        String info = AndSdkImpl.getInstance().getDeviceInfo();

    }


    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void initListener() {
        btnStartSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent andlinkIntent = new Intent(SettingsLauncherActivity.this, SettingsActivity.class);
                startActivity(andlinkIntent);
            }
        });
    }

    @Override
    public void updateDeviceNameSuccess(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoSuccess(BaseResponse<SettingsQueryDeviceInfoResponse> response) {
        LogUtil.d(TAG, "查询设备信息成功");
        SettingsDebugSPUtils.put(getApplicationContext(), SettingsContant.USER_ID, response.getData().getId());

        String userId = (String) SettingsDebugSPUtils.get(getApplicationContext(), SettingsContant.USER_ID, "");
        LogUtil.d(TAG, "userId = " + userId);
        ClientBootstrap bootstrap = ClientBootstrap.getInstance();
        bootstrap.init(getApplicationContext(), userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);

        initAndlink();

        btnStartSet.setVisibility(View.VISIBLE);
    }

    @Override
    public void unbindDeviceSuccess(BaseResponse response) {

    }

    @Override
    public void registerDeviceSuccess(BaseResponse<SettingsRegisterDeviceResponse> response) {
        LogUtil.d(TAG, "设备注册成功");
        SettingsDebugSPUtils.saveRegisterDevice(getApplicationContext(), true);
        String userId1 = response.getData().getId();
        LogUtil.d(TAG, "userId1 = " + userId1);
        SettingsDebugSPUtils.put(getApplicationContext(), SettingsContant.USER_ID, userId1);

        String userId = (String) SettingsDebugSPUtils.get(BaseApplication.getInstance(), response.getData().getId(), "");
        LogUtils.v(TAG, "userId = " + userId);
        ClientBootstrap bootstrap = ClientBootstrap.getInstance();
        bootstrap.init(getApplicationContext(), userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);
        initAndlink();
        btnStartSet.setVisibility(View.VISIBLE);
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
        LogUtil.d(TAG, "设备已经注册");
        SettingsDebugSPUtils.saveRegisterDevice(getApplicationContext(), true);

        mPresenter.queryDeviceInfo();
    }

    @Override
    public void updateDeviceNameFailure(BaseResponse response) {

    }

    @Override
    public void queryDeviceInfoFailure(BaseResponse<SettingsQueryDeviceInfoResponse> response) {
        LogUtil.d(TAG, "查询设备信息失败");

    }

    @Override
    public void unbindDeviceFailure(BaseResponse response) {

    }

    @Override
    public void registerDeviceFailure(BaseResponse<SettingsRegisterDeviceResponse> response) {
        LogUtil.d(TAG, "设备注册失败");
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
