package com.fenda.ai.provider;

import android.content.Context;
import android.content.Intent;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dds.agent.SkillIntent;
import com.aispeech.dui.dds.exceptions.DDSNotInitCompleteException;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.aispeech.dui.plugin.remind.Event;
import com.aispeech.dui.plugin.remind.RemindPlugin;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.fenda.ai.VoiceConstant;
import com.fenda.ai.service.DDSService;
import com.fenda.ai.skill.Util;
import com.fenda.common.provider.IVoiceInitProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.LogUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/3 10:48
 * @Description
 */
@Route(path = RouterPath.VOICE.INIT_PROVIDER)
public class InitService implements IVoiceInitProvider {

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
        }

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
