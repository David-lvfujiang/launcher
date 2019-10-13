package com.fenda.settings.activity;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamobile.smartgateway.andsdk.device.serviceimpl.AndSdkImpl;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.bean.AndlinkDeviceInfo;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.QRcodeUtil;
import com.fenda.protocol.tcp.CThreadPoolExecutor;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.fenda.protocol.util.DeviceIdUtil;
import com.fenda.settings.R;
import com.fenda.settings.constant.SettingsContant;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import static com.fenda.common.util.QRcodeUtil.getFileRoot;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/10 9:51
 */
public class SettingsAndLinkQRCodeActivity extends BaseMvpActivity{
    private static final String TAG = "SettingsAndLinkQRCodeActivity";

    private ImageView ivQrcode;
    private Button tvUnbind;
    private Button tvInit;

    private TextView tvBack;
    private TextView tvDisEnv;

    private String baseEvn = SettingsContant.TEST_BASE_URL;
    private static String devMac = "D0C5D364BEE5";


    @Override
    protected void initPresenter() {
    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_andlink_qrcode_layout;
    }

    @Override
    public void initView() {
        tvDisEnv = findViewById(R.id.andlink_env_dis);
        ivQrcode = findViewById(R.id.andlink_enlarge_QR);
        tvBack = findViewById(R.id.andlink_back);
        tvUnbind = findViewById(R.id.andlink_unbind);
        tvInit = findViewById(R.id.andlink_init);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData() {
        if("http://192.168.100.127:8998/smartsound/".equals(baseEvn)) {
            tvDisEnv.setText("测试环境");
            tvDisEnv.setTextColor(getColor(R.color.settings_colorAccent));
        } else if("http://192.168.43.34:8998/smartsound/".equals(baseEvn)){
            tvDisEnv.setText("192.198.43.34");
            tvDisEnv.setTextColor(getColor(R.color.settings_colorAccent));
        } else if("http://192.168.43.37:8998/smartsound/".equals(baseEvn)){
            tvDisEnv.setText("192.198.43.37");
            tvDisEnv.setTextColor(getColor(R.color.settings_colorAccent));

        } else {
            tvDisEnv.setVisibility(View.GONE);
        }

        final String contentEt = baseEvn + "apiForChinaMobile/getDeviceInfo?dt=500929&deviceId=" + DeviceIdUtil.getDeviceId();
        LogUtil.d(TAG, "contentET url = " + contentEt);

        final String filePath = getFileRoot(SettingsAndLinkQRCodeActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        CThreadPoolExecutor.runInBackground(new Runnable() {
            @Override
            public void run() {
                boolean success = QRcodeUtil.createQRImage(contentEt,1000, 1000, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivQrcode.setImageBitmap(BitmapFactory.decodeFile(filePath));
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
            public void onClick(View view) {
                finish();
            }
        });

        tvUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d(TAG, "unbind btn clicked!");
                AndSdkImpl.getInstance().reset(1);
            }
        });

        tvInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndlinkDeviceInfo.ChipModel tChipModel = new AndlinkDeviceInfo.ChipModel();
                tChipModel.type = "WiFi";
                tChipModel.factory = "rockchip";
                tChipModel.model = "rk3326";

                ArrayList tChips = new ArrayList();
                tChips.add(tChipModel);

                AndlinkDeviceInfo.DeviceExtInfo deviceExtInfo = new AndlinkDeviceInfo.DeviceExtInfo();
                deviceExtInfo.cmei = "864226033993999";
                deviceExtInfo.authMode = "0";
                deviceExtInfo.manuDate = "2019-07";
                deviceExtInfo.OS = "Android";
                deviceExtInfo.netCheckMode = "";
                deviceExtInfo.chips = tChips;

                AndlinkDeviceInfo tDevcieInfo = new AndlinkDeviceInfo();
                tDevcieInfo.deviceMac = devMac;
                tDevcieInfo.deviceType = "500929";
                tDevcieInfo.productToken = "JUyy3SiJ3yx6hImp";
                tDevcieInfo.andlinkToken = "RMm2sEhc9v23H8cc";
                tDevcieInfo.firmwareVersion = "f1.0";
                tDevcieInfo.autoAP = "0";
                tDevcieInfo.softAPMode = "";
                tDevcieInfo.softwareVersion = "1.0.0";
                tDevcieInfo.deviceExtInfo = deviceExtInfo;

                Gson tGson = new Gson();

                AndSdkImpl.getInstance().init(getApplication(), tGson.toJson(tDevcieInfo));
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage<BaseTcpMessage> message) {
        if (message.getCode() == TCPConfig.MessageType.ANDLINK_NETWORK_OK) {
            LogUtil.d(TAG, "中国移动网络配置成功通知");
            boolean isSave = AndSdkImpl.getInstance().saveKeyInfo(getApplication(), "RLCvVEReNwlGpCOVw-e4ZkazVYNzjlDqP5TBXRVkDZcJ3bwRDG-w2cf8tKpUKizb", "https://cgw.komect.com:443");
            LogUtil.d(TAG, "isSave = " + isSave);
        }
    }


    @Override
    protected void onDestroy() {
        //厂商需在onDestroy方法第一句调用，做必要的清理工作
//        AndSdkImpl.getInstance().stop(this);
        AndSdkImpl.getInstance().reset(0);

        super.onDestroy();
    }

}
