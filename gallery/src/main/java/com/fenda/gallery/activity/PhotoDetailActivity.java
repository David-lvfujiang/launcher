package com.fenda.gallery.activity;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.base.BaseActivity;
import com.fenda.common.view.HackyViewPager;
import com.fenda.gallery.R;
import com.fenda.gallery.bean.PhoneCameraBean;
import com.fenda.gallery.fragment.PhotoDetailFragment;

import java.util.ArrayList;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:50
 * @Description 相册详情页面
 */
public class PhotoDetailActivity extends BaseActivity implements View.OnClickListener {


    private HackyViewPager mVpContent;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private ArrayList<PhoneCameraBean> mDatas;
    private int mPageIndex;
    private int mStyle;


    @Override
    public int onBindLayout() {
        return R.layout.gallery_activity_photo_detal;
    }

    @Override
    public void initView() {
        mVpContent = findViewById(R.id.hacky_page);
        mTvTitle = findViewById(R.id.tvTitle);
        mIvBack = findViewById(R.id.ivBack);
    }

    @Override
    public void initListener() {
        super.initListener();
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent mIntent = getIntent();
        mDatas = mIntent.getParcelableArrayListExtra("list");
        mPageIndex = mIntent.getIntExtra("index", 0);
        mStyle = mIntent.getIntExtra("mStyle", 0);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), mDatas);
        mVpContent.setAdapter(adapter);
        mTvTitle.setText(getResources().getString(R.string.gallery_page_indicator, mPageIndex + 1, mDatas.size()));
        mVpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                String title = getResources().getString(R.string.gallery_page_indicator, i + 1, mDatas.size());
                mTvTitle.setText(title);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mVpContent.setOffscreenPageLimit(1);
        mVpContent.setCurrentItem(mPageIndex);

    }


    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.ivBack) {
            finish();
        }
    }

    private class ImagePagerAdapter extends FragmentPagerAdapter {
        private ArrayList<PhoneCameraBean> mList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<PhoneCameraBean> mList) {
            super(fm);
            this.mList = mList;
        }

        @Override
        public Fragment getItem(int i) {
            PhoneCameraBean bean = mList.get(i);
            return PhotoDetailFragment.newInstance(bean, mStyle);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }
    }
}
