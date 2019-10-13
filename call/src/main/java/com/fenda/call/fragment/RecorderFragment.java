package com.fenda.call.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fenda.call.R;
import com.fenda.call.adapter.RecorderAdapter;
import com.fenda.call.service.CallService;
import com.fenda.common.base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.rong.callkit.bean.CallRecoder;
import io.rong.callkit.db.MySqliteOpenHelper;
import io.rong.callkit.db.SqliteManager;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 通话记录Fragment
 */
public class RecorderFragment extends BaseFragment {

    private RecyclerView mRvRecordeList;
    private List<CallRecoder> mDatas;

    private RecorderAdapter mAdapter;
    private SqliteManager mSqliteManager;


    @Override
    public int onBindLayout() {
        return R.layout.call_fragment_recorder;
    }

    @Override
    public void initView() {
        mRvRecordeList = mRootView.findViewById(R.id.rv_recorde_list);
    }

    @Override
    public void initData() {
        mDatas = new ArrayList<>();
        mSqliteManager = new SqliteManager(getActivity());
        mAdapter = new RecorderAdapter(mContext, mDatas);
        mRvRecordeList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvRecordeList.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(String syncContact) {
        if (syncContact.equals(CallService.SYNC_CONTACTS)) {
            mDatas = mSqliteManager.query(MySqliteOpenHelper.DB_TABE);
            mAdapter.setNewData(mDatas);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatas = mSqliteManager.query(MySqliteOpenHelper.DB_TABE);
        mAdapter.setNewData(mDatas);
    }
}
