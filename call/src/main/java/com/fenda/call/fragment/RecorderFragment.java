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

import com.fenda.common.bean.CallRecoderBean;
import io.rong.callkit.config.Constant;
import com.fenda.common.util.DbUtil;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 通话记录Fragment
 */
public class RecorderFragment extends BaseFragment {

    private RecyclerView mRvRecordeList;
    private RecorderAdapter mAdapter;
    private List<CallRecoderBean> mAllDatas;
    private List<CallRecoderBean> mDatas;

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
        mAllDatas = DbUtil.getInstance(mContext).listAllCallRecoderByPhone();
        mDatas = sortData(mAllDatas);
        mAdapter = new RecorderAdapter(mContext, mDatas);
        mRvRecordeList.setLayoutManager(new LinearLayoutManager(mContext));
        mRvRecordeList.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncEvent(String syncContact) {
        if (CallService.SYNC_CONTACTS.equals(syncContact) || Constant.SYNCRECORDER.equals(syncContact)) {
            mAllDatas = DbUtil.getInstance(mContext).listAllCallRecoderByPhone();
            mDatas = sortData(mAllDatas);
            mAdapter.setNewData(mDatas);
        }
    }

    private List<CallRecoderBean> sortData(List<CallRecoderBean> datas) {
        List<CallRecoderBean> result = new ArrayList<>();
        CallRecoderBean tempBean = new CallRecoderBean();
        if (datas != null) {
            for (int i = 0; i < datas.size(); i++) {
                CallRecoderBean bean = datas.get(i);
                if (i == 0) {
                    tempBean = datas.get(i);
                    bean.setCount(1);
                    result.add(bean);

                } else {
                    if (tempBean.getCallStatus() == bean.getCallStatus() && tempBean.getPhone().equals(bean.getPhone())) {
                        CallRecoderBean lastBean = result.get(result.size() - 1);
                        int count = lastBean.getCount() + 1;
                        lastBean.setCount(count);
                    } else {
                        tempBean = bean;
                        bean.setCount(1);
                        result.add(bean);
                    }
                }
            }
        }
        return result;
    }
}
