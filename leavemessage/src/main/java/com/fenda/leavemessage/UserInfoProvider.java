package com.fenda.leavemessage;

import android.net.Uri;

import com.fenda.common.BaseApplication;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.util.LogUtil;
import com.fenda.leavemessage.util.LeaveMessageGetUserUtil;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * @Author: david.lvfujiang
 * @Date: 2019/10/14
 * @Describe: 融云用户提供者
 */
public class UserInfoProvider implements RongIM.UserInfoProvider {
    /**
     * 融云在需要用户信息的地方会自动回调此方法
     *
     * @param s
     * @return
     */
    @Override
    public UserInfo getUserInfo(String s) {
        String userName = "";
        String mobile = "";
        String icon = "http://b-ssl.duitang.com/uploads/item/201711/10/20171110225150_ym2jw.jpeg";

        mobile = s;
        //根据id查询用户信息
        UserInfoBean userInfo = LeaveMessageGetUserUtil.getUserInfo(mobile);
        if (userInfo != null) {
            LogUtil.i("用户 = " + userInfo.toString());
            userName = userInfo.getUserName();
            if (userInfo.getIcon() != null && !"".equals(userInfo.getIcon()) && !"null".equals(userInfo.getIcon().trim())) {
                LogUtil.i("头像 = " + userInfo.getIcon());
                icon = userInfo.getIcon();
            }
        } else {
            LogUtil.i("找不到用户");
        }
        //创建融云的userInfo
        UserInfo userInfoRM = new UserInfo(s, userName, Uri.parse(icon));
        //刷新缓存
        // RongIM.getInstance().refreshUserInfoCache(userInfo);
        return userInfoRM;
    }
}
