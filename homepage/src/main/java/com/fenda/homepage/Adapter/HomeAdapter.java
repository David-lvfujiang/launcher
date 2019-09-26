package com.fenda.homepage.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/24 17:35
 * @Description
 */
public class HomeAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public HomeAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
