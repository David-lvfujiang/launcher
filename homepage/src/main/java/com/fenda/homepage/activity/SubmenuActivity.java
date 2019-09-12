package com.fenda.homepage.activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ToastUtils;
import com.fenda.homepage.Adapter.GridAdapter;
import com.fenda.homepage.R;
import com.fenda.homepage.bean.ApplyBean;
import com.fenda.homepage.data.AllApplyData;
import com.fenda.homepage.data.Constant;
import com.fenda.homepage.data.UndevelopedApplyData;
import com.fenda.homepage.scrollview.ObservableScrollView;
import com.fenda.homepage.scrollview.ScrollViewListener;
import com.fenda.protocol.tcp.bean.BaseTcpMessage;
import com.fenda.protocol.tcp.bean.EventMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author matt.liaojianpeng
 */
public class SubmenuActivity extends BaseActivity implements View.OnTouchListener, ScrollViewListener , View.OnClickListener {
    @Autowired
    List<ApplyBean> mApplyList;
    List<ApplyBean> mUndevelopedApplyList;

    private RecyclerView mSubmenuListRv;
    private RecyclerView mSubmenuListRv2;
    private LinearLayout mSubmenuBackLl;
    private GridAdapter mGridAdapter;
    private GridAdapter mGridAdapter2;
    private ObservableScrollView mScrollView;
    private ImageView submenuDropLeft;
    private ImageView submenuDropRight;
    IVoiceRequestProvider initVoiceProvider;
    @Override
    public int onBindLayout() {
        return R.layout.activity_submenu;
    }

    @Override
    protected void initCommonView() {
        super.initCommonView();
        mSubmenuBackLl = findViewById(R.id.ll_submenu_back);
        mSubmenuListRv = findViewById(R.id.rv_submenu_list);
        mSubmenuListRv2 = findViewById(R.id.rv_submenu_list2);
        submenuDropLeft = findViewById(R.id.iv_submenu_drop_left);
        submenuDropRight = findViewById(R.id.iv_submenu_drop_right);
        mSubmenuBackLl.setOnTouchListener(this);
        submenuDropLeft.setOnClickListener(this);
        submenuDropRight.setOnClickListener(this);
        mScrollView = findViewById(R.id.sv_submenu);
        mScrollView.setScrollViewListener(this);
    }

    @Override
    public void initView() {
        mApplyList = new ArrayList<>();
        mApplyList = AllApplyData.dataList(mApplyList);
        mUndevelopedApplyList = new ArrayList<>();
        mUndevelopedApplyList = UndevelopedApplyData.dataList(mUndevelopedApplyList);
        //这里的第二个参数4代表的是网格的列数
        mSubmenuListRv.setLayoutManager(new GridLayoutManager(mContext, 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        mSubmenuListRv2.setLayoutManager(new GridLayoutManager(mContext, 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mGridAdapter = new GridAdapter(mApplyList);
        mGridAdapter2 = new GridAdapter(mUndevelopedApplyList);
        mSubmenuListRv.setAdapter(mGridAdapter);
        mSubmenuListRv2.setAdapter(mGridAdapter2);
        mGridAdapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick( View view, String applyId) {
                Intent intent = new Intent(SubmenuActivity.this,PromptActivity.class);
                if(applyId.equals(Constant.SETTINGS)){
                    ARouter.getInstance().build(RouterPath.SETTINGS.SettingsActivity).navigation();
                } else if (applyId.equals(Constant.CALCULATOR)){
                    ToastUtils.show("计算器");
                }
                else if (applyId.equals(Constant.WEATHER)) {
//                    ToastUtils.show("天气");
                    ARouter.getInstance().build(RouterPath.Weather.WEATHER_MAIN).navigation();
                    initVoiceProvider.nowWeather();
                } else if (applyId.equals(Constant.CALENDAR)) {
                    //                    ToastUtils.show("日历");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.PHOTO)) {
                    //                    ToastUtils.show("相册");
                    ARouter.getInstance().build(RouterPath.Gallery.GALLERY_CATOGORY).navigation();
                } else if (applyId.equals(Constant.TIME)) {
                    ToastUtils.show("时钟");
                } else if (applyId.equals(Constant.FM)) {
                    //                    ToastUtils.show("收音机");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.CAMERA)) {
//                    ToastUtils.show("相机");
                    PackageManager packageManager = getPackageManager();
                    Intent packageIntent = packageManager.getLaunchIntentForPackage("com.android.camera2");
                    startActivity(packageIntent);
                } else if (applyId.equals(Constant.PLAY)) {
//                    ToastUtils.show("播放器");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.QQ_MUSIC)) {
                    //                    ToastUtils.show("QQ音乐");
                    if (initVoiceProvider != null){
                        initVoiceProvider.openQQMusic();
                    }
                } else if (applyId.equals(Constant.IQIYI)) {
                    //                    ToastUtils.show("爱奇艺");
                    if (initVoiceProvider != null){
                        initVoiceProvider.openAqiyi();
                    }
                } else if (applyId.equals(Constant.NEWS)) {
//                    ToastUtils.show("新闻");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.CROSS_TALK)) {
                    //                    ToastUtils.show("相声");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.CHILDREN)) {
                    //                    ToastUtils.show("儿歌");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.POETRY)) {
                    //                    ToastUtils.show("诗词");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.JOKE)) {
//                    ToastUtils.show("笑话");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.IDIOM)) {
                    //                    ToastUtils.show("成语");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.ENCYCLOPEDIA)) {
                    //                    ToastUtils.show("百科");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.GUOXUE)) {
                    //                    ToastUtils.show("国学");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.HOLIDAYS)) {
                    //                    ToastUtils.show("节假日查询");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.REMIND)) {
//                    ToastUtils.show("提醒");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.STORY)) {
                    //                    ToastUtils.show("故事");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.TRANSLATION)) {
                    //                    ToastUtils.show("翻译");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.STOCK)) {
                    //                    ToastUtils.show("股票");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.UNITS)) {
                    //                    ToastUtils.show("单位换算");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.RELATIVE)) {
                    //                    ToastUtils.show("亲戚关系计算");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
                } else if (applyId.equals(Constant.CONSTELLATION)) {
                    //                    ToastUtils.show("星座运势");
                    intent.putExtra("applyId", applyId);
                    startActivity(intent);
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
        mGridAdapter2.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String applyId) {
                ToastUtils.show("努力开发中，敬请期待。。。");
            }
        });

    }

    @Override
    public void initData() {

        if (initVoiceProvider == null) {
            initVoiceProvider = ARouter.getInstance().navigation(IVoiceRequestProvider.class);
        }

    }
    @Override
    public void onEvent(final EventMessage<BaseTcpMessage> message) {
        if (message.getCode() == com.fenda.common.constant.Constant.Common.INIT_VOICE_SUCCESS){
            // @todo  勿删 语音初始化成功后会回调这里,在语音成功之前调用会导致应用崩溃
            if (initVoiceProvider != null){
                initVoiceProvider.requestWeather();
                initVoiceProvider.openVoice();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        //如果这个方法消费了这个这个event事件，就返回True，否则false。
        return super.onTouchEvent(event);
    }

    float startY = 0;
    float endY = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventAction = event.getAction();

        if(eventAction==MotionEvent.ACTION_DOWN){
            startY = event.getY();

        } else if (eventAction==MotionEvent.ACTION_UP){
            endY = event.getY();
            if ((endY-startY)>50) {
                finish();
                overridePendingTransition(R.anim.submenu_push_up_in,R.anim.submenu_push_up_out);
            }
        }
        return true;
    }


    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        //        Log.v("ashgdfaskdfh","y="+y);
        //        Log.v("ashgdfaskdfh","oldx="+oldy);
        Animation rotateAnimation = new RotateAnimation(y/125,oldy/125,0,18);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(0);
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDetachWallpaper(true);
        submenuDropLeft.startAnimation(rotateAnimation);
        Animation rotateAnimation2 = new RotateAnimation(-(y/125),-(oldy/125),30,18);
        rotateAnimation2.setFillAfter(true);
        rotateAnimation2.setDuration(0);
        rotateAnimation2.setRepeatCount(0);
        rotateAnimation2.setInterpolator(new LinearInterpolator());
        rotateAnimation2.setDetachWallpaper(true);
        submenuDropRight.startAnimation(rotateAnimation2);

    }

    @Override
    public void onClick(View v) {
        finish();
        overridePendingTransition(R.anim.submenu_push_up_in,R.anim.submenu_push_up_out);
    }
}
