package com.fenda.call.fragment;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fenda.call.R;
import com.fenda.call.adapter.ContactListAdapter;
import com.fenda.common.base.BaseFragment;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 联系人Fragment
 */
public class ContactsFragment extends BaseFragment {

    private RecyclerView mRvContactList;
    private List<UserInfoBean> mDatas;
    private ContactListAdapter mAdapter;
    Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");


    @Override
    public int onBindLayout() {
        return R.layout.call_fragment_contacts;
    }

    @Override
    public void initView() {
        mRvContactList = mRootView.findViewById(R.id.rv_contact_list);
    }


    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        mDatas = ContentProviderManager.getInstance(mContext, mUri).queryUser(null, null);
        mRvContactList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ContactListAdapter(mContext, mDatas);
        mRvContactList.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
