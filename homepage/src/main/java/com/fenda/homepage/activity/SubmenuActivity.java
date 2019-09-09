package com.fenda.homepage.activity;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ToastUtils;
import com.fenda.homepage.Adapter.GridAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.bean.ApplyBean;
import com.fenda.homepage.data.AllApplyData;
import com.fenda.homepage.data.Constant;


import java.util.ArrayList;
import java.util.List;

/**
 * @author matt.liaojianpeng
 */
public class SubmenuActivity extends BaseActivity {
    @Autowired
    List<ApplyBean> mApplyList;

    private RecyclerView mSubmenuListRv;
    private ImageView mSubmenuBackIv;
    private GridAdapter mGridAdapter;

    @Override
    public int onBindLayout() {
        return R.layout.activity_submenu;
    }

    @Override
    protected void initCommonView() {
        super.initCommonView();
        mSubmenuBackIv = findViewById(R.id.iv_submenu_back);
        mSubmenuListRv = findViewById(R.id.rv_submenu_list);
    }

    @Override
    public void initView() {
        mApplyList = new ArrayList<>();
        mApplyList = AllApplyData.dataList(mApplyList);
        //这里的第二个参数4代表的是网格的列数
        mSubmenuListRv.setLayoutManager(new GridLayoutManager(this, 4));
        mGridAdapter = new GridAdapter(mApplyList);
        mSubmenuListRv.setAdapter(new GridAdapter(mApplyList));
        mGridAdapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( View view, String applyId) {
                if(applyId.equals(Constant.SETTINGS)){
                    ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
                } else if (applyId.equals(Constant.CALCULATOR)){
                    ToastUtils.show("计算器");
                }else if (applyId.equals(Constant.SETTINGS)){
                    ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
                }
                else if (applyId.equals(Constant.WEATHER)) {
                    ToastUtils.show("天气");
                } else if (applyId.equals(Constant.CALENDAR)) {
                    ToastUtils.show("日历");
                } else if (applyId.equals(Constant.PHOTO)) {
                    ToastUtils.show("相册");
                    ARouter.getInstance().build(RouterPath.Gallery.GALLERY_CATOGORY).navigation();
                } else if (applyId.equals(Constant.TIME)) {
                    ToastUtils.show("时钟");
                } else if (applyId.equals(Constant.FM)) {
                    ToastUtils.show("收音机");
                } else if (applyId.equals(Constant.CAMERA)) {
                    ToastUtils.show("相机");
                } else if (applyId.equals(Constant.PLAY)) {
                    ToastUtils.show("播放器");
                } else if (applyId.equals(Constant.QQ_MUSIC)) {
                    ToastUtils.show("QQ音乐");
                } else if (applyId.equals(Constant.IQIYI)) {
                    ToastUtils.show("爱奇艺");
                } else if (applyId.equals(Constant.NEWS)) {
                    ToastUtils.show("新闻");
                } else if (applyId.equals(Constant.CROSS_TALK)) {
                    ToastUtils.show("相声");
                } else if (applyId.equals(Constant.CHILDREN)) {
                    ToastUtils.show("儿歌");
                } else if (applyId.equals(Constant.POETRY)) {
                    ToastUtils.show("诗词");
                } else if (applyId.equals(Constant.JOKE)) {
                    ToastUtils.show("笑话");
                } else if (applyId.equals(Constant.IDIOM)) {
                    ToastUtils.show("成语");
                } else if (applyId.equals(Constant.ENCYCLOPEDIA)) {
                    ToastUtils.show("百科");
                } else if (applyId.equals(Constant.GUOXUE)) {
                    ToastUtils.show("国学");
                } else if (applyId.equals(Constant.HOLIDAYS)) {
                    ToastUtils.show("节假日查询");
                } else if (applyId.equals(Constant.REMIND)) {
                    ToastUtils.show("提醒");
                } else if (applyId.equals(Constant.STORY)) {
                    ToastUtils.show("故事");
                } else if (applyId.equals(Constant.TRANSLATION)) {
                    ToastUtils.show("翻译");
                } else if (applyId.equals(Constant.STOCK)) {
                    ToastUtils.show("股票");
                } else if (applyId.equals(Constant.UNITS)) {
                    ToastUtils.show("单位换算");
                } else if (applyId.equals(Constant.RELATIVE)) {
                    ToastUtils.show("亲戚关系计算");
                } else if (applyId.equals(Constant.CONSTELLATION)) {
                    ToastUtils.show("星座运势");
                } else if (applyId.equals(Constant.CMCC)) {
                    ToastUtils.show("10086");
                } else if (applyId.equals(Constant.GDYD)) {
                    ToastUtils.show("广东移动营业厅");
                } else if (applyId.equals(Constant.MIGU_MUSIC)) {
                    ToastUtils.show("咪咕音乐");
                } else if (applyId.equals(Constant.MIGU_AIKAN)) {
                    ToastUtils.show("咪咕爱看");
                } else if (applyId.equals(Constant.MIGU_ZHIBO)) {
                    ToastUtils.show("咪咕直播");
                } else if (applyId.equals(Constant.MIGU_LINGXI)) {
                    ToastUtils.show("咪咕灵犀");
                } else if (applyId.equals(Constant.MIGU_QUANQUAN)) {
                    ToastUtils.show("咪咕圈圈");
                } else if (applyId.equals(Constant.MIGU_SHANPAO)) {
                    ToastUtils.show("咪咕擅跑");
                } else if (applyId.equals(Constant.MIGU_VIDEO)) {
                    ToastUtils.show("咪咕视频");
                } else if (applyId.equals(Constant.MIGU_MOVIE)) {
                    ToastUtils.show("咪咕影院");
                } else if (applyId.equals(Constant.MIGU_READ)) {
                    ToastUtils.show("咪咕阅读");
                } else if (applyId.equals(Constant.MIGU_CITIC)) {
                    ToastUtils.show("咪咕中信书店");
                }
            }
        });
        mSubmenuListRv.setAdapter(mGridAdapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mSubmenuBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
