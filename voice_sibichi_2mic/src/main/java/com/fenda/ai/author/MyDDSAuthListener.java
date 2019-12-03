package com.fenda.ai.author;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.aispeech.ailog.Constant;
import com.aispeech.dui.dds.DDSAuthListener;
import com.fenda.ai.VoiceConstant;
import com.fenda.common.util.LogUtil;

/**
  * @author mirrer.wangzhonglin
  * @Date 2019/9/2 8:50
  * @Description 
  *
  */
public class MyDDSAuthListener implements DDSAuthListener {
    private static final String TAG = "MyDDSAuthListener";

    private Context mContext;

    public MyDDSAuthListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onAuthSuccess() {
        // 发送一个认证成功的广播
        mContext.sendBroadcast(new Intent(VoiceConstant.ACTION_AUTH_SUCCESS));
    }

    @Override
    public void onAuthFailed(final String errId, final String error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LogUtil.e("授权错误");
                Toast.makeText(mContext,
                        "授权错误:" + errId + ":\n" + error + "\n请查看手册处理", Toast.LENGTH_SHORT).show();
            }
        });
        // 发送一个认证失败的广播
        mContext.sendBroadcast(new Intent(VoiceConstant.ACTION_AUTH_FAILED));
    }
}
