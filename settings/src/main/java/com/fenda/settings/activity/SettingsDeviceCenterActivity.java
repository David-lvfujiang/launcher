package com.fenda.settings.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.response.SettingsGetContractListResponse;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;
import com.fenda.settings.presenter.SettingsPresenter;
import com.fenda.settings.utils.SettingsWifiUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:10
 */
@Route(path = RouterPath.SETTINGS.SettingsDeviceCenterActivity)
public class SettingsDeviceCenterActivity extends BaseMvpActivity<SettingsPresenter, SettingsModel> implements SettingsContract.View {
    private static final String TAG = "SettingsDeviceCenterActivity";

    ImageView bindInfoBack;
    TextView bindInfoItems, bindInfoStatus;
    ListView bindInfoListView;
    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_device_center_layout;
    }

    @Override
    public void initView() {
        bindInfoBack = findViewById(R.id.bind_info_back_iv);
        bindInfoItems = findViewById(R.id.bind_info_items);
        bindInfoStatus = findViewById(R.id.bind_info_items_status);
        bindInfoListView = findViewById(R.id.bind_info_listview);
    }

    @Override
    public void initData() {
        String deviceName = (String) SPUtils.get(getApplicationContext(), Constant.Settings.DEVICE_NAME, "");

        String[] bindInfoNamesDis = new String[] {getString(R.string.settings_device_center_devicename), getString(R.string.settings_device_center_add_family), getString(R.string.settings_device_center_dismiss_family)};
        String[] bindInfoStatusDis = new String[]{"", "" ,""};

        bindInfoStatusDis[0]  = deviceName;

        List<Map<String, Object>> listitem = new ArrayList<>();

        for (int i = 0; i < bindInfoNamesDis.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", bindInfoNamesDis[i]);
            map.put("state", bindInfoStatusDis[i]);
            listitem.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, listitem, R.layout.settings_device_center_items_layout,
                new String[]{"name", "state"}, new int[]{R.id.bind_info_items, R.id.bind_info_items_status});
        //第一个参数是上下文对象，第二个是listitem， 第三个是指定每个列表项的布局文件，第四个是指定Map对象中定义的两个键（这里通过字符串数组来指定），第五个是用于指定在布局文件中定义的id（也是用数组来指定）

        bindInfoListView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        bindInfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent = new Intent(SettingsDeviceCenterActivity.this, SettingsActivity.class);
                startActivity(Intent);
                finish();
            }
        });
        bindInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                String setClickedListName = map.get("name").toString();
                LogUtil.d(TAG, "select name is =" + setClickedListName);
                if(setClickedListName.equals(getString(R.string.settings_device_center_devicename))) {
                    if(SettingsWifiUtil.isWifiEnabled(getApplicationContext())){
                        Intent nameIntent = new Intent(SettingsDeviceCenterActivity.this, SettingsChangeDeviceNameActivity.class);
                        startActivity(nameIntent);
                        finish();
                    } else {
                        ToastUtils.show(getString(R.string.settings_device_center_nowifi_no_changedevicename));
                    }
                } else if(setClickedListName.equals(getString(R.string.settings_device_center_add_family))) {
                    Intent contactsIntent = new Intent(SettingsDeviceCenterActivity.this, SettingsDeviceContractsActivity.class);
                    startActivity(contactsIntent);
                }  else if(setClickedListName.equals(getString(R.string.settings_device_center_dismiss_family))) {
                    if (SettingsWifiUtil.isWifiEnabled(getApplicationContext())) {
                        LogUtil.d(TAG, "wifi is connect");

                        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(SettingsDeviceCenterActivity.this);
                        normalDialog.setMessage(getString(R.string.settings_device_center_dismiss_family_confirm));
                        normalDialog.setPositiveButton(getString(R.string.settings_sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtils.show("解绑");
                                mPresenter.unbindDevice();
                                //...To-do
//                                RetrofitHelper.getInstance().getServer().unBindDevice(Constant.SERIAL_NUM)
//                                        .compose(RxSchedulers.<BaseResponse>applySchedulers())
//                                        .subscribe(new Consumer<BaseResponse>() {
//                                            @Override
//                                            public void accept(BaseResponse response) throws Exception {
//                                                if (response.getCode() == 200) {
//
//                                                } else {
//                                                    ToastUtils.show(response.getMessage());
//                                                }
//                                            }
//                                        }, new Consumer<Throwable>() {
//                                            @Override
//                                            public void accept(Throwable throwable) throws Exception {
//                                                // 异常处理
//                                            }
//                                        });
                            }
                        });
                        normalDialog.setNegativeButton(getString(R.string.settings_cancel),
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
                        ToastUtils.show(getString(R.string.settings_device_center_nowifi_no_unbind));
                    }
                }
            }
        });
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
        startActivity(new Intent(SettingsDeviceCenterActivity.this, SettingsBindDeviceActivity.class));
        LogUtil.d(TAG, "module unbind  ");
        finish();
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
    public void getContactsListSuccess(BaseResponse<SettingsGetContractListResponse> response) {

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
    public void getContactsListFailure(BaseResponse<SettingsGetContractListResponse> response) {

    }
}
