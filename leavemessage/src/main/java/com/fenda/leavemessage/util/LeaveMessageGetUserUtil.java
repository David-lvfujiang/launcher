package com.fenda.leavemessage.util;

import android.net.Uri;

import com.fenda.common.BaseApplication;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;

import java.util.List;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/14
 * @Describe:
 */
public class LeaveMessageGetUserUtil {
    /**
     * 根据号码获取用户信息
     * @param userId
     * @return
     */
    public static UserInfoBean getUserInfo(String userId) {
        ContentProviderManager manager = ContentProviderManager.getInstance(BaseApplication.getBaseInstance(), Uri.parse(ContentProviderManager.BASE_URI + "/user"));
        List<UserInfoBean> beanList = manager.queryUser("phone = ? ", new String[]{userId});
        if (beanList != null && beanList.size() > 0) {
            return beanList.get(0);
        }
        return null;

    }
}
