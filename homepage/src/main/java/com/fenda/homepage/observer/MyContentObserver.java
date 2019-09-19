package com.fenda.homepage.observer;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/18 17:07
 * @Description
 */
public class MyContentObserver extends ContentObserver {

    private ContentProviderManager manager;
    private IVoiceRequestProvider provider;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public MyContentObserver(Handler handler,ContentProviderManager manager) {
        super(handler);
        this.manager = manager;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        if (manager != null){
            List<UserInfoBean> beanList = manager.queryUser(null,null);
            List<String> ncNames = new ArrayList<>();
            for (UserInfoBean bean : beanList) {
                ncNames.add(bean.getUserName());
            }
            if (provider == null){
                provider = (IVoiceRequestProvider) ARouter.getInstance().build(RouterPath.VOICE.REQUEST_PROVIDER).navigation();
            }
            if (provider != null){
                provider.updateVocab(ncNames);
            }

        }

    }
}
