package com.fenda.ai.observer;


import android.net.Uri;
import android.text.TextUtils;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.CommandObserver;
import com.aispeech.dui.plugin.music.MusicPlugin;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.ai.skill.Util;
import com.fenda.common.BaseApplication;
import com.fenda.common.baserx.RxSchedulers;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.ICallProvider;
import com.fenda.common.provider.INewsProvider;
import com.fenda.common.provider.IWeatherProvider;
import com.fenda.common.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 客户端CommandObserver, 用于处理客户端动作的执行以及快捷唤醒中的命令响应.
 * 例如在平台配置客户端动作： command://call?phone=$phone$&name=#name#,
 * 那么在CommandObserver的onCall方法中会回调topic为"call", data为
 */
public class DuiCommandObserver implements CommandObserver {
    private static final String TAG = "DuiCommandObserver";
    private static final String SYS_VOICE_CALL = "call_voice";
    private static final String SYS_VIDEO_CALL = "call_video";
    private static final String SYS_END_CALL = "end_call";
    private static final String COMMAND_WEATHER = "command_weather";

    private ICallProvider callProvider;

    private IWeatherProvider weatherProvider;


    public DuiCommandObserver() {
    }

    /**
     * 注册当前更新消息
      */
    public void regist() {
        DDS.getInstance().getAgent().subscribe(new String[]{
                       SYS_VIDEO_CALL,SYS_VOICE_CALL,COMMAND_WEATHER,SYS_END_CALL
                },
                this);
    }

    /**
     *  注销当前更新消息
     *
     */
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    @Override
    public void onCall(String command, String data) {
//        LogUtil.e(TAG, "command: " + command + "  data: " + data);


        ResultOnCall(command, data);
    }

    private void ResultOnCall(String command, String data) {
        if (command.equals(SYS_VOICE_CALL)){
           try {
               if (!BaseApplication.getInstance().isCall()){
                   call(data,0);
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }else if (command.equals(SYS_VIDEO_CALL)){
           try {
               if (!BaseApplication.getInstance().isCall()){
                   call(data,1);
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }else if (command.equals(SYS_END_CALL)){
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("");
                }
            }).compose(RxSchedulers.<String>io_main())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            if (callProvider == null){
                                callProvider = ARouter.getInstance().navigation(ICallProvider.class);
                            }
                            if (callProvider != null){
                                callProvider.endCall();
                            }
                        }
                    });

        }
    }

    private void call(String data, final int type) throws JSONException {
        if (Util.isTaskQQmusic(BaseApplication.getInstance())){
            MusicPlugin.get().getMusicApi().exit();
        }
        JSONObject object = new JSONObject(data);
        String nickName = object.optString("contact");
        final String phone = searchContacts(nickName);
        if (!TextUtils.isEmpty(phone.trim())){
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("");
                }
            }).compose(RxSchedulers.<String>io_main())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            callProvider = ARouter.getInstance().navigation(ICallProvider.class);
                            if (callProvider != null){
                                callProvider.call(type,phone);
                            }
                        }
                    });
        }
    }

    private String searchContacts(String searchName) {
        ContentProviderManager manager = ContentProviderManager.getInstance(BaseApplication.getInstance(), Uri.parse(ContentProviderManager.BASE_URI+"/user"));
        List<UserInfoBean> beanList = manager.queryUser("name = ? ",new String[]{searchName});
        if (beanList != null && beanList.size() > 0){
            String phone = beanList.get(0).getMobile();
            LogUtil.i("phone = "+phone);
            return phone;
        }
        return "";
    }




}
