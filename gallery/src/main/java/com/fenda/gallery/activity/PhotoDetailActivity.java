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


public class PhotoDetailActivity extends BaseActivity implements View.OnClickListener {


    private HackyViewPager hackyPage;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private ArrayList<PhoneCameraBean> cameraBeans;
    private int pageIndex;
    private int style;


    @Override
    public int onBindLayout() {
        return R.layout.activity_photo_detal;
    }

    @Override
    public void initView() {
        hackyPage = findViewById(R.id.hacky_page);
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
        cameraBeans = mIntent.getParcelableArrayListExtra("list");
        pageIndex = mIntent.getIntExtra("index", 0);
        style = mIntent.getIntExtra("style", 0);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), cameraBeans);
        hackyPage.setAdapter(adapter);
        mTvTitle.setText(getResources().getString(R.string.gallery_page_indicator, pageIndex + 1, cameraBeans.size()));
        hackyPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                String title = getResources().getString(R.string.gallery_page_indicator, i + 1, cameraBeans.size());
                mTvTitle.setText(title);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        hackyPage.setOffscreenPageLimit(1);
        hackyPage.setCurrentItem(pageIndex);

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
            return PhotoDetailFragment.newInstance(bean, style);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }
    }
}
