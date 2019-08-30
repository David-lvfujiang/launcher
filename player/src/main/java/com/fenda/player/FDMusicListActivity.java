package com.fenda.player;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.player.adapter.FDMusicAdpater;
import com.fenda.player.bean.FDMusic;

import java.util.ArrayList;
/**
  * @author mirrer.wangzhonglin
  * @Date 2019/8/29 17:03
  * @Description 
  *
  */
@Route(path = RouterPath.PLAYER.FDMusicListActivity)
public class FDMusicListActivity extends BaseActivity implements View.OnClickListener  {

    private TextView tvBlck;
    private RecyclerView mRecycleListView;
    private FDMusicAdpater mMusicAdpater;
    private ArrayList<FDMusic> mMusicList = new ArrayList<>();
    private ListBroadcastReceiver mListBroadcastReceiver;
    private int contentType;
    private LinearLayoutManager manager;
    private int tCurrentIndex;



    @Override
    public int onBindLayout() {
        return R.layout.player_activity_fdmusic_list;
    }

    @Override
    public void initView() {

        mRecycleListView = findViewById(R.id.rlv_music_list);
        tvBlck           = findViewById(R.id.tv_navbar_blck);
        
        
        
       
    }


    @Override
    public void initListener() {
        tvBlck.setOnClickListener(this);

        mMusicAdpater.setOnItemClickListener(new FDMusicAdpater.OnItemClickListener() {
            @Override
            public void itemClickListener(int position) {
                MoveToPosition(manager,mRecycleListView,position);
            }
        });
    }

    @Override
    public void initData() {
        Intent mIntent = getIntent();
        mMusicList = mIntent.getParcelableArrayListExtra(Constant.Player.keyDataMusicList);
        tCurrentIndex = mIntent.getIntExtra(Constant.Player.keyCurrentPlayIndex, 0);
        contentType = mIntent.getIntExtra(Constant.Player.keyContentType,0);

        if (mMusicList != null){
            manager = new LinearLayoutManager(this);
            mRecycleListView.setLayoutManager(manager);
            mRecycleListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            mMusicAdpater = new FDMusicAdpater(this, mMusicList, tCurrentIndex,contentType);
            mRecycleListView.setAdapter(mMusicAdpater);
            if (tCurrentIndex != 0){
                MoveToPosition(manager,mRecycleListView,tCurrentIndex);
            }
        }
        
    }





    private   void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {
         int firstItem = manager.findFirstVisibleItemPosition();
         int lastItem = manager.findLastVisibleItemPosition();
         if (n <= firstItem) {
             mRecyclerView.scrollToPosition(n);
         } else if (n <= lastItem) {
             int top = mRecyclerView.getChildAt(n - firstItem).getTop();
             mRecyclerView.scrollBy(0, top);
             } else {
                mRecyclerView.scrollToPosition(n);
         }
    }



    @Override
    protected void onResume() {
        super.onResume();

        mListBroadcastReceiver = new ListBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Player.keyBroadcastMusicList);
        intentFilter.addAction(Constant.Player.keyCurrentPlayIndex);
        registerReceiver(mListBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mListBroadcastReceiver != null){
            unregisterReceiver(mListBroadcastReceiver);
            mListBroadcastReceiver = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_navbar_blck:
                finish();
                break;
        }

    }





    private class ListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constant.Player.keyBroadcastMusicList)){

                ArrayList tMusicList = intent.getParcelableArrayListExtra(Constant.Player.keyDataMusicList);
                mMusicList.clear();
                mMusicList.addAll(tMusicList);

                mMusicAdpater.updateData(mMusicList, 0);
            }else if (intent.getAction().equals(Constant.Player.keyCurrentPlayIndex)){

                int tCurrentIndex = intent.getIntExtra(Constant.Player.keyCurrentPlayIndex, 0);
                mMusicAdpater.updateCurrentIndex(tCurrentIndex);

                Log.e("qob", "ListBroadcastReceiver  tCurrentIndex " + tCurrentIndex);
            }
        }
    }

}
