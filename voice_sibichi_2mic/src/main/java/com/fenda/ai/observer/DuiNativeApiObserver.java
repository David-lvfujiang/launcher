package com.fenda.ai.observer;

import android.content.Context;
import android.net.Uri;

import com.aispeech.dui.dds.DDS;
import com.aispeech.dui.dsk.duiwidget.ContentWidget;
import com.aispeech.dui.dsk.duiwidget.ListWidget;
import com.aispeech.dui.dsk.duiwidget.NativeApiObserver;
import com.fenda.common.BaseApplication;
import com.fenda.common.util.LogUtil;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * 注册NativeApiObserver, 用于客户端响应DUI平台技能配置里的资源调用指令, 同一个NativeApiObserver可以处理多个native api.
 * 目前demo中实现了打电话的功能逻辑
 */
public class DuiNativeApiObserver implements NativeApiObserver {
    private static final String TAG = "DuiNativeApiObserver";
    private static final String NATIVE_API_CONTACT = "sys.query.contacts";
    private static final String NATIVE_API_CALL_NAME = "query_callname";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_PHONE = "phone";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_FLAG = "flag";
    private static final String DB_LOCATION = "location.db";
    private Context mContext;

    public DuiNativeApiObserver() {
        mContext = BaseApplication.getInstance();
    }

    /**
     * 注册当前更新消息
      */
    public void regist() {
        DDS.getInstance().getAgent().subscribe(new String[]{NATIVE_API_CONTACT,NATIVE_API_CALL_NAME},
                this);
    }

    /**
     *  注销当前更新消息
      */
    public void unregist() {
        DDS.getInstance().getAgent().unSubscribe(this);
    }

    /**
     * onQuery方法执行时，需要调用feedbackNativeApiResult来向DUI平台返回执行结果，表示一个native api执行结束。
     * native api的执行超时时间为10s
     * @param nativeApi
     * @param data
     */
    @Override
    public void onQuery(String nativeApi, String data) {
//        LogUtil.e(TAG, "nativeApi: " + nativeApi + "  data: " + data);
        if (NATIVE_API_CONTACT.equals(nativeApi)) {
            String searchName = null;
            ListWidget searchNums = null;
            try {
                JSONObject obj = null;
                obj = new JSONObject(data);
                searchName = obj.optString("联系人");
                searchNums = searchContacts("李丽");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            LogUtil.e(TAG, "query back:" + searchName + "-" + (searchNums != null ? searchNums.toString() : "null"));

            DDS.getInstance().getAgent().feedbackNativeApiResult(nativeApi, searchNums);

        }else if (NATIVE_API_CALL_NAME.equals(nativeApi)){
//            JSONObject object = null;
//            try {
//                object = new JSONObject(data);
//                String name = object.optString("name");
//                DuiWidget searNum = searchContacts(name);
//                DDS.getInstance().getAgent().feedbackNativeApiResult(nativeApi, searNum);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }
    }

    private ListWidget searchContacts(String searchName) {
//        ContentProviderManager manager = ContentProviderManager.getInstance(FDApplication.getContext(), Uri.parse(ContentProviderManager.BASE_URI+"/user"));
//        List<UserInfoBean> beanList = manager.queryUser("name = ? ",new String[]{searchName});
        ListWidget widget = new ListWidget();
//        if (beanList.size() > 0){
//            for (UserInfoBean bean : beanList) {
//                ContentWidget contentWidget = new ContentWidget()
//                        .addExtra(PARAM_NAME, searchName)
//                        .addExtra(PARAM_PHONE, bean.getMobile())
//                        .addExtra(PARAM_TYPE,String.valueOf(bean.getUserType()));
//                widget.addContentWidget(contentWidget);
//            }
//
//        }
        return widget;



    }



}
