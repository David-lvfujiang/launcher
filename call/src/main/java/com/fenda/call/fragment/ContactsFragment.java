package com.fenda.call.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fenda.call.R;
import com.fenda.call.adapter.ContactListAdapter;
import com.fenda.call.service.CallService;
import com.fenda.common.base.BaseFragment;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.callkit.RongCallKit;


/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 联系人Fragment
 */
public class ContactsFragment extends BaseFragment {

    private RecyclerView mRvContactList;
    private List<UserInfoBean> mDatas;
    private ContactListAdapter mAdapter;
    private ArrayList<String> mAllUserIds;

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
        mAllUserIds = new ArrayList<>();
        mDatas = ContentProviderManager.getInstance(mContext, Constant.Common.URI).queryUser(null, null);
        for (UserInfoBean bean : mDatas) {
            mAllUserIds.add(bean.getMobile());
        }
        RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
            @Override

            public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {

                //返回群组成员userId的集合
                return mAllUserIds;

            }
        });
        mRvContactList.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ContactListAdapter(mContext, mDatas);
        mRvContactList.setAdapter(mAdapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(String syncContact) {
        if (syncContact.equals(CallService.SYNC_CONTACTS)) {
            mDatas = ContentProviderManager.getInstance(mContext, Constant.Common.URI).queryUser(null, null);
            mAllUserIds.clear();
            for (UserInfoBean bean : mDatas) {
                mAllUserIds.add(bean.getMobile());
            }
            RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
                @Override

                public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {

                    //返回群组成员userId的集合
                    return mAllUserIds;

                }
            });
            mAdapter.setNewData(mDatas);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
