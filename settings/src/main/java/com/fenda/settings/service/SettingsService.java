package com.fenda.settings.service;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.BaseApplication;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.ISettingsProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.AppUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.protocol.http.RxSchedulers;
import com.fenda.protocol.tcp.ClientBootstrap;
import com.fenda.settings.R;
import com.fenda.settings.activity.SettingsBindDeviceActivity;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.http.SettingsApiService;
import com.fenda.settings.model.request.SettingsRegisterDeviceRequest;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;

import io.reactivex.functions.Consumer;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/3 15:21
 */
@Route(path = RouterPath.SETTINGS.SettingsService)
public class SettingsService implements ISettingsProvider {
    private static final String TAG = "SettingsService";
    private String registeName = "FD-R03";
    private String registeMac = "00:11:22:33:44:55";
    //    String registeVersion = "V1.0";
    private String registeVersion;

    @Override
    public void deviceStatus(Context context) {
        LogUtil.d(TAG, "sn = " + SettingsContant.SETTINGS_SERIAL_NUM);
        if (AppUtils.isRegisterDevice(context) ){
            LogUtil.d(TAG, "device have registered~");
            if (AppUtils.isBindedDevice(context)) {
                LogUtil.d(TAG, "device have bind~");
                String userId = (String) SPUtils.get(BaseApplication.getInstance(), Constant.Settings.DEVICE_ID, "");
                String rongCloudToken= (String) SPUtils.get(context, Constant.Settings.RONGYUNCLOUDTOKEN, "");
                // 调用音视频服务接口登录IM
                ICallProvider loginService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
                if(loginService != null){
                    loginService.login(rongCloudToken);
                }
                LogUtil.d(TAG, "userId = " + userId);
                ClientBootstrap bootstrap = ClientBootstrap.getInstance();
                bootstrap.init(context, userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);
            } else {
                LogUtil.d(TAG, "device not bind~");
                queryDeviceInfo(context);
            }
        } else {
            LogUtil.d(TAG, "start register device");
            registerDevice(context);
        }
    }

    @Override
    public void init(Context context) {

    }

    private void registerDevice(final Context context) {
        registeVersion= AppUtils.getVerName(context);

        SettingsRegisterDeviceRequest deviceRegisterRequest = new SettingsRegisterDeviceRequest();
        deviceRegisterRequest.setDeviceId(SettingsContant.SETTINGS_SERIAL_NUM);
        deviceRegisterRequest.setName(registeName);
        deviceRegisterRequest.setClientVersion(registeVersion);
        deviceRegisterRequest.setMacAddr(registeMac);

        RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class).deviceRegister(deviceRegisterRequest)
                .compose(RxSchedulers.<BaseResponse>applySchedulers())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.getCode() == 200 || ("操作成功").equals(response.getMessage())) {  //注册成功
                            registerSuccess(context, response);
                        } else if(("设备已经注册").equals(response.getMessage())){  //已经注册
                            haveRegisterSuccess(context);
                        } else {   //注册失败
                            ToastUtils.show("注册失败，请重启后再试！");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 异常处理
                    }
                });
    }

    private void registerSuccess(Context context, BaseResponse<SettingsRegisterDeviceResponse> response) {
        LogUtils.v(TAG, "Register: device register sucess~");
        AppUtils.saveRegisterDevice(context, true);
        SPUtils.put(context, Constant.Settings.USER_ID, response.getData().getId());
        SPUtils.put(context, Constant.Settings.DEVICE_NAME, response.getData().getName());
        SPUtils.put(context, Constant.Settings.DEVICE_ICON, response.getData().getIcon());
        SPUtils.put(context, Constant.Settings.VCODE, response.getData().getVcode());
        SPUtils.put(context, Constant.Settings.RONGYUNCLOUDTOKEN, response.getData().getRongcloud_token());

        // 调用音视频服务接口登录IM
        ICallProvider loginService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
        if(loginService != null){
            loginService.login(response.getData().getRongcloud_token());
        }

        String userId = (String) SPUtils.get(BaseApplication.getInstance(), Constant.Settings.USER_ID, "");
        LogUtils.v(TAG, "userId = " + userId);
        ClientBootstrap bootstrap = ClientBootstrap.getInstance();
        bootstrap.init(context, userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);

        //相当于第一次注册，一定没有被绑定，所以直接进入绑定界面
        AppUtils.saveBindedDevice(context, false);
        Intent intent = new Intent(context, SettingsBindDeviceActivity.class);
        context.startActivity(intent);
    }

    private void haveRegisterSuccess(Context context) {
        LogUtils.v(TAG, "Register: device have registered~");
        AppUtils.saveRegisterDevice(context, true);

        //查询设备信息保存、判断是否绑定
        queryDeviceInfo(context);
    }

    private void queryDeviceInfo(final Context context) {
        RetrofitHelper.getInstance(SettingsContant.TEST_BASE_URL).getServer(SettingsApiService.class).getDeviceInfo()
                .compose(RxSchedulers.<BaseResponse<SettingsQueryDeviceInfoResponse>>applySchedulers())
                .subscribe(new Consumer<BaseResponse<SettingsQueryDeviceInfoResponse>>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.getCode() == 200) {
                            LogUtil.d(TAG, "getDeviceInfo = " + response.getData());
                            queryDeviceInfoSuccess(context,  response);
                        } else {
                            LogUtil.d(TAG, "queryDeviceInfo fail = " + response);
                            ToastUtils.show(R.string.settings_init_device_status_query_fail);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 异常处理
                        LogUtil.d(TAG, "queryDeviceInfo response throwable = " + throwable);
                    }
                });
    }

    private void queryDeviceInfoSuccess(Context context, BaseResponse<SettingsQueryDeviceInfoResponse> response) {
        SPUtils.put(context, Constant.Settings.USER_ID, response.getData().getId());
        SPUtils.put(context, Constant.Settings.DEVICE_NAME, response.getData().getName());
        SPUtils.put(context, Constant.Settings.DEVICE_ICON, response.getData().getIcon());
        SPUtils.put(context, Constant.Settings.VCODE, response.getData().getVcode());
        SPUtils.put(context, Constant.Settings.RONGYUNCLOUDTOKEN, response.getData().getRongcloud_token());

        // 调用音视频服务接口登录IM
        ICallProvider loginService = (ICallProvider) ARouter.getInstance().build(RouterPath.Call.CALL_SERVICE).navigation();
        if(loginService != null){
            loginService.login(response.getData().getRongcloud_token());
        }

        String userId = (String) SPUtils.get(context, Constant.Settings.USER_ID, "");
        LogUtil.d(TAG, "userId = " + userId);
        ClientBootstrap bootstrap = ClientBootstrap.getInstance();
        bootstrap.init(context, userId, SettingsContant.TCP_IP, SettingsContant.TCP_PORT, 0);

        //判断设备是否被绑定
        if (response.getData().getBindStatus()) {
            LogUtil.d(TAG, "Query: device have bind~");
            AppUtils.saveBindedDevice(context, true);
        } else {
            AppUtils.saveBindedDevice(context, false);

            LogUtil.d(TAG, "Query: device not bind~");
            Intent intent = new Intent(context, SettingsBindDeviceActivity.class);
            context.startActivity(intent);
        }
    }
}
