package com.fenda.homepage.data;

import com.fenda.homepage.R;
import com.fenda.homepage.bean.ApplyBean;

import java.util.List;

/**
 * author : matt.Ljp
 * date : 2019/9/9 20:52
 * description :
 */
public class UndevelopedApplyData {
    public static List<ApplyBean> dataList (List<ApplyBean> mList) {
        mList.add(new ApplyBean("小睡眠", R.mipmap.submenu_ico_sleep));
        mList.add(new ApplyBean("声音博物馆", R.mipmap.submenu_ico_voice));
        mList.add(new ApplyBean("交通限行", R.mipmap.submenu_ico_traffic_restrictions));
        mList.add(new ApplyBean("汇率", R.mipmap.submenu_ico_exchange_rate));
        mList.add(new ApplyBean("食物营养查询", R.mipmap.submenu_icon_food));
        mList.add(new ApplyBean("心灵鸡汤", R.mipmap.submenu_ico_xinlingjitang));
        mList.add(new ApplyBean("快递查询", R.mipmap.submenu_ico_express));
        return mList;
    }
}
