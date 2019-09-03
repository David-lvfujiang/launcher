package com.fenda.call.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description FragmentPagerAdapter
 */
public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentlist;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.mFragmentlist = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentlist.size();
    }
}
