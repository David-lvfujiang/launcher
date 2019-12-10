package com.fenda.ai.provider;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.SkillIntent;
import com.aispeech.dui.dds.auth.AuthInfo;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.oauth.TokenListener;
import com.aispeech.dui.oauth.TokenResult;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.remind.Event;
import com.aispeech.dui.plugin.remind.RemindPlugin;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.fenda.ai.R;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.contant.SibichiContant;
import com.fenda.ai.http.SibichiApiService;
import com.fenda.ai.modle.request.SibichiPutDeviceNameRequest;
import com.fenda.ai.service.DDSService;
import com.fenda.ai.skill.Util;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.constant.Constant;
import com.fenda.common.provider.IVoiceInitProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.protocol.util.DeviceIdUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.functions.Consumer;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/3 10:48
 * @Description
 */
@Route(path = RouterPath.VOICE.INIT_PROVIDER)
public class InitService implements IVoiceInitProvider {
    public static final String TAG = "InitService";

    private Context mContext;
    @Override
    public void init(Context context) {
        this.mContext = context;

    }


    @Override
    public void initVoice(){
        initVoice(mContext);
    }

    private void initVoice(Context mContext){
        try {
            String[] models = mContext.getAssets().list("voice");
            File mFile = new File(VoiceConstant.sVoiceDir);
            if (!mFile.exists()){
                mFile.mkdir();
            }
            for (int i = 0; i < models.length; i++) {
                assetsDataToSD("voice",models[i], VoiceConstant.sVoiceDir,mContext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mContext.startService(new Intent(mContext, DDSService.class));

    }

    /**
     * 拷贝assets下的文件的方法
     */
    private void assetsDataToSD(String assertDir, String fileName,
                                String modelDir,Context mContext) throws IOException {

        String outputPath = modelDir + File.separator + fileName;
        File mFile = new File(outputPath);

        if (!mFile.exists()){
            mFile.createNewFile();
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(outputPath);

            String assertFilePath = assertDir + File.separator + fileName;
            myInput = mContext.getAssets().open(assertFilePath);
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }
            myOutput.flush();
            myInput.close();
            myOutput.close();
        }


    }


    @Override
//    public void initAuth(String authCode, String codeVerifier, String cliendId, String userId){
    public void initAuth(){
        String authCode = (String) SPUtils.get(mContext, Constant.HomePage.DCA_AUTNCODE, "");
        String cliendId = (String) SPUtils.get(mContext, Constant.HomePage.DCA_CLIENDID, "");
        String codeVerifier = (String) SPUtils.get(mContext, Constant.HomePage.DCA_CODEVERIFIER, "");
        String userId = (String) SPUtils.get(mContext, Constant.HomePage.DCA_USERID, "");
        Log.d(TAG, "cliendId = " + cliendId);
        Log.d(TAG, "authCode = " + authCode);
        Log.d(TAG, "codeVerifier = " + codeVerifier);
        Log.d(TAG, "userId = " + userId);
        AuthInfo authInfo = new AuthInfo();
        authInfo.setClientId(cliendId);
        authInfo.setUserId(userId);
        authInfo.setAuthCode(authCode);
        authInfo.setCodeVerifier(codeVerifier);

        //若不设置，使用默认地址值 http://dui.callback
        //authInfo.setRedirectUri(redirectUri);

        try {
            DDS.getInstance().getAgent().setAuthCode(authInfo, new TokenListener() {
                @Override
                public void onSuccess(TokenResult tokenResult) {
                    Log.e(TAG, "FD---DCA auth success");
                    sendDdsDeviceName();
                }

                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "DCA auth fail = " + s);
//                    sendDdsDeviceName();
                }
            });
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
    }

    private void sendDdsDeviceName() {
        String ddsDeviceName = getDDSDeviceName();
        String ddsDeviceId =  DeviceIdUtil.getDeviceId();

        SibichiPutDeviceNameRequest sibichiPutDeviceNameRequest = new SibichiPutDeviceNameRequest();
        sibichiPutDeviceNameRequest.setDeviceName(ddsDeviceName);
        sibichiPutDeviceNameRequest.setUuid(ddsDeviceId);

        LogUtil.d(TAG, "sendDdsDeviceName = " + ddsDeviceName);
        RetrofitHelper.getInstance(SibichiContant.TEST_BASE_URL).getServer(SibichiApiService.class).sendDCAMessageToApp(sibichiPutDeviceNameRequest)
                .compose(com.fenda.protocol.http.RxSchedulers.<BaseResponse>applySchedulers())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.getCode() == 200) {
                            LogUtil.d(TAG, "sendDCAMessageToApp = " + response.getData());
                            sendDdsNameSuccess(response);
                        } else {
                            LogUtil.d(TAG, "sendDCAMessageToApp fail = " + response);
                            ToastUtils.show("同步DDS设备名称失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // 异常处理
                    }
                });
    }

    private String getDDSDeviceName(){
        String deviceName = "";
        try {
            deviceName = DDS.getInstance().getDeviceName();
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }
        return deviceName;
    }

    private void sendDdsNameSuccess(BaseResponse response) {
        LogUtils.v(TAG, "sendDdsNameSuccess: sendDdsNameSuccess");

    }



    @Override
    public void PlayWelcomeTTS() {
//        LogUtil.i("TAG",  "FD------play welcome TTS---");
        try {
            if (DDS.getInstance().isAuthSuccess()){
                String[] wakeupWords = new String[0];
                String minorWakeupWord = null;
                try {
                    wakeupWords = DDS.getInstance().getAgent().getWakeupEngine().getWakeupWords();
                    minorWakeupWord = DDS.getInstance().getAgent().getWakeupEngine().getMinorWakeupWord();
                } catch (DDSNotInitCompleteException e) {
                    e.printStackTrace();
                }
                String hiStr = "";
                if (wakeupWords != null && minorWakeupWord != null) {
                    hiStr = mContext.getString(R.string.voice_hi_str2, wakeupWords[0], minorWakeupWord);
                } else if (wakeupWords != null && wakeupWords.length == 2) {
                    hiStr = mContext.getString(R.string.voice_hi_str2, wakeupWords[0], wakeupWords[1]);
                } else if (wakeupWords != null && wakeupWords.length > 0) {
                    hiStr = mContext.getString(R.string.voice_hi_str, wakeupWords[0]);
                }
                DDS.getInstance().getAgent().getTTSEngine().speak(hiStr, 1);
            }
        } catch (DDSNotInitCompleteException e) {
            e.printStackTrace();
        }

    }
}
