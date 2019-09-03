package com.fenda.call.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.call.R;
import com.fenda.call.adapter.MyFragmentPagerAdapter;
import com.fenda.call.fragment.ContactsFragment;
import com.fenda.call.fragment.KeyboardFragment;
import com.fenda.call.fragment.RecorderFragment;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.common.view.LazyViewPager;

import java.util.ArrayList;
import java.util.List;
/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 首页
 */
@Route(path = RouterPath.Call.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity implements View.OnClickListener {


    private ImageView mIvBack;
    private RadioGroup mRgMenu;
    private LazyViewPager mVpContent;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    public int onBindLayout() {
        return R.layout.call_activity_main;
    }

    @Override
    public void initView() {
        mIvBack = findViewById(R.id.iv_back);
        mRgMenu = findViewById(R.id.radio_group);
        mVpContent = findViewById(R.id.view_pager);
    }

    @Override
    public void initData() {
        ContactsFragment contactsFragment = new ContactsFragment();
        RecorderFragment recorderFragment = new RecorderFragment();
        KeyboardFragment keyboardFragment = new KeyboardFragment();
        mFragmentList.add(contactsFragment);
        mFragmentList.add(recorderFragment);
        mFragmentList.add(keyboardFragment);
        mVpContent.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
    }

    @Override
    public void initListener() {
        super.initListener();
        mRgMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.contacts) {
                    mVpContent.setCurrentItem(0);
                } else if (checkedId == R.id.recorder) {
                    mVpContent.setCurrentItem(1);
                } else if (checkedId == R.id.keyborad) {
                    mVpContent.setCurrentItem(2);
                }
            }
        });
        mVpContent.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mRgMenu.check(R.id.contacts);
                } else if (position == 1) {
                    mRgMenu.check(R.id.recorder);
                } else if (position == 2) {
                    mRgMenu.check(R.id.keyborad);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_back) {
            finish();
        }
    }
}
