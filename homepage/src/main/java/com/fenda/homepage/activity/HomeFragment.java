package com.fenda.homepage.activity;

import android.widget.ImageView;

import com.fenda.common.base.BaseFragment;
import com.fenda.homepage.R;

/**
 * @author mirrer.wangzhonglin
 * @Date 2019/9/24 17:32
 * @Description
 */
public class HomeFragment extends BaseFragment {

    private ImageView mImgItemBg;

    @Override
    public int onBindLayout() {
        return R.layout.homepage_home_item;
    }

    @Override
    public void initView() {
        mImgItemBg = mRootView.findViewById(R.id.iv_item_bg);

    }

    @Override
    public void initData() {

    }
}
