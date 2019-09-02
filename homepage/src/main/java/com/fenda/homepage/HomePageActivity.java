package com.fenda.homepage;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.fenda.homepage.Adapter.MainAdapter;
import com.fenda.homepage.Util.HomeUtil;

public class HomePageActivity extends Activity implements View.OnClickListener {

    TextClock mHeaderTimeTv;
    RecyclerView mTipInfoRv;
    ImageView mheaderWeatherIv;
    ImageView mAiTipIv;
    TextView mAiTipTitleTv;
    TextView mAiTipMicTv;

    private int showPageIndex;
    private Handler mCyclicRollHandler = new Handler();



    Runnable cycleRollRunabler = new Runnable() {
        @Override
        public void run() {
            Log.e("fd", "cycleRollRunabler timeout " + showPageIndex);

            if (showPageIndex + 1 >= HomeUtil.PAGE_NUM_MAX) {
                mTipInfoRv.scrollToPosition(0);
                showPageIndex = 0;

                mAiTipIv.setVisibility(View.VISIBLE);
                mAiTipMicTv.setText(R.string.cm_main_page_title_0);
                mAiTipTitleTv.setText(R.string.cm_main_page_describe_0);

                mCyclicRollHandler.postDelayed(this, HomeUtil.PAGE_SHOW_TIME);
            } else {
                mTipInfoRv.smoothScrollToPosition(showPageIndex + 1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
    }

    private void initViews() {

        mHeaderTimeTv = findViewById(R.id.tv_header_time);
        mTipInfoRv = findViewById(R.id.rv_Tipinfo);
        mheaderWeatherIv = findViewById(R.id.iv_header_weather);

        mAiTipIv = findViewById(R.id.iv_main_tip_icon);
        mAiTipTitleTv = findViewById(R.id.tv_main_item_content);
        mAiTipMicTv = findViewById(R.id.tv_ai_tiptext);

        findViewById(R.id.iv_main_jd).setOnClickListener(this);
        findViewById(R.id.iv_main_phone).setOnClickListener(this);
        findViewById(R.id.iv_main_other).setOnClickListener(this);
        findViewById(R.id.iv_main_tools).setOnClickListener(this);
        findViewById(R.id.iv_main_study).setOnClickListener(this);
        mheaderWeatherIv.setOnClickListener(this);
        showPageIndex = 0;

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mTipInfoRv.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mTipInfoRv);

        MainAdapter tMainAdapter = new MainAdapter(this);
        mTipInfoRv.setAdapter(tMainAdapter);

        mTipInfoRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                    mCyclicRollHandler.removeCallbacks(cycleRollRunabler);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int review_position = layoutManager.findFirstVisibleItemPosition();
                    Log.e("fd", "onScrollStateChanged review_position " + review_position + " showPageIndex " + showPageIndex);

                    mCyclicRollHandler.postDelayed(cycleRollRunabler, HomeUtil.PAGE_SHOW_TIME);

                    if (review_position == showPageIndex) {
                        return;
                    }
                    showPageIndex = review_position;

                    if (showPageIndex == 0){
                        mAiTipIv.setVisibility(View.VISIBLE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_0);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_0);
                    }
                    else if (showPageIndex == 1){
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_1);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_1);

                    }
                    else if (showPageIndex == 2){
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_2);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_2);
                    }
                    else if (showPageIndex == 3){
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_3);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_3);

                    }
                    else if (showPageIndex == 4){
                        mAiTipIv.setVisibility(View.GONE);
                        mAiTipMicTv.setText(R.string.cm_main_page_title_4);
                        mAiTipTitleTv.setText(R.string.cm_main_page_describe_4);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

  //              int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}
