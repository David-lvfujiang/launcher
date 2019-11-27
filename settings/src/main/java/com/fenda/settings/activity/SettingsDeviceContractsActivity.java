package com.fenda.settings.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;
import com.fenda.settings.adapter.SettingsContactsAdapter;
import com.fenda.settings.service.SettingsService;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:35
 */
@Route(path = RouterPath.SETTINGS.SettingsDeviceContractsActivity)
public class SettingsDeviceContractsActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsDeviceContractsActivity";

    private RecyclerView rvContrants;
    private TextView tvCancel;
    private ImageView ivAddContacts;

    private List<UserInfoBean> mContractList;
    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");
    private SettingsContactsAdapter mSettingsContactsAdapter;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_device_cantracts_layout;
    }

    @Override
    public void initView() {
        rvContrants = findViewById(R.id.bind_contacts_listview);
        tvCancel = findViewById(R.id.bind_contacts_back_iv);
        ivAddContacts = findViewById(R.id.add_contacts_tv);

        mContractList = new ArrayList<>();
        ivAddContacts.bringToFront();
        mContractList = ContentProviderManager.getInstance(getApplicationContext(), mUri).queryUser(null, null);
        //设置为垂直5列
        rvContrants.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
        //设置默认动画
        rvContrants.setItemAnimator(new DefaultItemAnimator());
        mSettingsContactsAdapter = new SettingsContactsAdapter(mContext, mContractList);
        rvContrants.setAdapter(mSettingsContactsAdapter);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        ivAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(SettingsDeviceContractsActivity.this, SettingsDeviceAddContractsQRActivity.class);
                startActivity(addIntent);
//                finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent backBindInfoIntent = new Intent(SettingsDeviceContractsActivity.this, SettingsDeviceCenterActivity.class);
//                startActivity(backBindInfoIntent);
                finish();
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(String syncContact) {
        if (syncContact.equals(SettingsService.SETTINGS_SYNC_CONTACTS)) {
            mContractList = ContentProviderManager.getInstance(mContext, Constant.Common.URI).queryUser(null, null);
            mSettingsContactsAdapter.setNewData(mContractList);
        }
    }
}
