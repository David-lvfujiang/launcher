package com.fenda.homepage.data;

import com.fenda.homepage.R;
import com.fenda.homepage.bean.ApplyBean;

import java.util.List;

/**
 * 作者：Matt.Liao
 * 日期时间: 2019/9/2 10:02
 * 内容描述:
 * 版本：
 * 包名：
 */
public class AllApplyData {
    public static List<ApplyBean> dataList (List<ApplyBean> mList) {
        mList.add(new ApplyBean(Constant.SETTINGS,"设置", R.mipmap.homepage_settings));
        mList.add(new ApplyBean(Constant.CALCULATOR,"计算器", R.mipmap.submenu_ico_calculator));
        mList.add(new ApplyBean(Constant.WEATHER,"天气", R.mipmap.submenu_ico_weather));
        mList.add(new ApplyBean(Constant.CALENDAR,"日历", R.mipmap.submenu_icon_calendar));
        mList.add(new ApplyBean(Constant.PHOTO ,"相册", R.mipmap.submenu_ico_photo));
        mList.add(new ApplyBean(Constant.TIME ,"时钟", R.mipmap.submenu_ico_time));
        mList.add(new ApplyBean(Constant.FM ,"收音机", R.mipmap.submenu_icon_fm));
        mList.add(new ApplyBean(Constant.CAMERA ,"相机", R.mipmap.submenu_icon_camera));
        mList.add(new ApplyBean(Constant.PLAY,"播放器", R.mipmap.submenu_icon_play));
        mList.add(new ApplyBean(Constant.QQ_MUSIC ,"QQ音乐", R.mipmap.submenu_ico_qq_music));
        mList.add(new ApplyBean(Constant.IQIYI ,"爱奇艺", R.mipmap.submenu_ico_iqiyi));
        mList.add(new ApplyBean(Constant.NEWS ,"新闻", R.mipmap.submenu_ico_news));
        mList.add(new ApplyBean(Constant.CROSS_TALK ,"相声", R.mipmap.submenu_ico_cross_talk));
        mList.add(new ApplyBean(Constant.CHILDREN ,"儿歌", R.mipmap.submenu_ico_children));
        mList.add(new ApplyBean(Constant.TIME,"闹钟", R.mipmap.submenu_ico_time));
        mList.add(new ApplyBean(Constant.POETRY ,"诗词", R.mipmap.submenu_ico_poetry));
        mList.add(new ApplyBean(Constant.JOKE,"笑话", R.mipmap.submenu_ico_joke));
        mList.add(new ApplyBean(Constant.IDIOM ,"成语", R.mipmap.submenu_ico_idiom));
        mList.add(new ApplyBean(Constant.ENCYCLOPEDIA ,"百科", R.mipmap.submenu_ico_encyclopedia));
        mList.add(new ApplyBean(Constant.GUOXUE,"国学", R.mipmap.submenu_ico_guoxue));
        mList.add(new ApplyBean(Constant.HOLIDAYS ,"节假日查询", R.mipmap.submenu_icon_holidays));
        mList.add(new ApplyBean(Constant.REMIND ,"提醒", R.mipmap.submenu_ico_remind));
        mList.add(new ApplyBean(Constant.STORY,"故事", R.mipmap.submenu_icon_story));
        mList.add(new ApplyBean(Constant.TRANSLATION ,"翻译", R.mipmap.submenu_ico_translation));
        mList.add(new ApplyBean(Constant.STOCK ,"股票", R.mipmap.submenu_ico_stock));
        mList.add(new ApplyBean(Constant.UNITS ,"单位换算", R.mipmap.submenu_ico_units));
        mList.add(new ApplyBean(Constant.RELATIVE ,"亲戚关系计算", R.mipmap.submenu_ico_relative));
        mList.add(new ApplyBean(Constant.CONSTELLATION ,"星座运势", R.mipmap.submenu_ico_constellation));
        mList.add(new ApplyBean(Constant.CMCC ,"10086", R.mipmap.submenu_ico_10086));
        mList.add(new ApplyBean(Constant.GDYD,"广东移动营业厅", R.mipmap.submenu_ico_gdyd));
        mList.add(new ApplyBean(Constant.MIGU_MUSIC ,"咪咕音乐", R.mipmap.submenu_ico_migu_music));
        mList.add(new ApplyBean(Constant.MIGU_AIKAN ,"咪咕爱看", R.mipmap.submenu_ico_migu_aikan));
        mList.add(new ApplyBean(Constant.MIGU_ZHIBO ,"咪咕直播", R.mipmap.submenu_ico_migu_zhibo));
        mList.add(new ApplyBean(Constant.MIGU_LINGXI ,"咪咕灵犀", R.mipmap.submenu_ico_migu_lingxi));
        mList.add(new ApplyBean(Constant.MIGU_QUANQUAN ,"咪咕圈圈", R.mipmap.submenu_ico_migu_quanquan));
        mList.add(new ApplyBean(Constant.MIGU_SHANPAO ,"咪咕擅跑", R.mipmap.submenu_ico_migu_shanpao));
        mList.add(new ApplyBean(Constant.MIGU_VIDEO ,"咪咕视频", R.mipmap.submenu_ico_migu_video));
        mList.add(new ApplyBean(Constant.MIGU_MOVIE,"咪咕影院", R.mipmap.submenu_ico_migu_movie));
        mList.add(new ApplyBean(Constant.MIGU_READ,"咪咕阅读", R.mipmap.submenu_ico_migu_read));
        mList.add(new ApplyBean(Constant.MIGU_CITIC ,"咪咕中信书店", R.mipmap.submenu_ico_migu_citic));
        return mList;
    }
}
