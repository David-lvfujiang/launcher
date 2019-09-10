package com.fenda.settings.activity;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.util.QRcodeUtil;
import com.fenda.settings.R;
import com.fenda.settings.constant.SettingsContant;

import java.io.File;

import static com.fenda.common.util.QRcodeUtil.getFileRoot;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/10 9:51
 */
public class SettingsAndLinkQRCodeActivity extends BaseMvpActivity {

    private ImageView ivQRcode;
    private TextView tvBack;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_andlink_qrcode_layout;
    }

    @Override
    public void initView() {
        ivQRcode = findViewById(R.id.andlink_enlarge_QR);
        tvBack = findViewById(R.id.andlink_back);
    }

    @Override
    public void initData() {
        final String contentET= "http://www.fenda.com/?sn=";

        final String filePath = getFileRoot(SettingsAndLinkQRCodeActivity.this) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
        //二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                boolean success = QRcodeUtil.createQRImage(contentET,1000, 1000, null, filePath);

                if (success) {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ivQRcode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void initListener() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {
    }
}
