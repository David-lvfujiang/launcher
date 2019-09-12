package com.fenda.homepage.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.base.BaseActivity;
import com.fenda.common.util.ToastUtils;
import com.fenda.homepage.R;
import com.fenda.homepage.bean.PromptBean;
import com.fenda.homepage.data.Constant;
import com.fenda.homepage.data.PromptData;

import java.util.HashMap;
import java.util.Map;

/**
 * author : matt.Ljp
 * date : 2019/9/11 15:46
 * description :
 */
public class PromptActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mPromptBackIv;
    private TextView mPromptTitleTv;
    private TextView mPrompt1Tv;
    private TextView mPrompt2Tv;
    private TextView mPrompt3Tv;
    private TextView mPrompt4Tv;
    private TextView mPrompt5Tv;
    private String applyId;
    @Override
    public int onBindLayout() {
        return R.layout.activity_prompt;
    }

    @Override
    public void initView() {
        mPromptBackIv = findViewById(R.id.iv_prompt_back);
        mPromptTitleTv = findViewById(R.id.tv_prompt_title);
        mPrompt1Tv = findViewById(R.id.tv_prompt1);
        mPrompt2Tv = findViewById(R.id.tv_prompt2);
        mPrompt3Tv = findViewById(R.id.tv_prompt3);
        mPrompt4Tv = findViewById(R.id.tv_prompt4);
        mPrompt5Tv = findViewById(R.id.tv_prompt5);
        applyId = getIntent().getStringExtra("applyId");
        PromptBean promptBean = new PromptBean();
        Map<String , PromptBean> promptDataList = new HashMap<String , PromptBean>();
        promptDataList = PromptData.promptDataList(promptDataList);
        promptBean = promptDataList.get(applyId);
        if (promptBean!=null){
            mPromptTitleTv.setText(promptBean.getPromptTitle());
            mPrompt1Tv.setText(promptBean.getPrompt1());
            mPrompt2Tv.setText(promptBean.getPrompt2());
            mPrompt3Tv.setText(promptBean.getPrompt3());
            mPrompt4Tv.setText(promptBean.getPrompt4());
            mPrompt5Tv.setText(promptBean.getPrompt5());
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mPromptBackIv.setOnClickListener(this);
        mPromptTitleTv.setOnClickListener(this);
        mPrompt1Tv.setOnClickListener(this);
        mPrompt2Tv.setOnClickListener(this);
        mPrompt3Tv.setOnClickListener(this);
        mPrompt4Tv.setOnClickListener(this);
        mPrompt5Tv.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_prompt_back || resId == R.id.tv_prompt_title) {
            finish();
        } else
        {
            if ((Constant.CALENDAR).equals(applyId)){
                if (resId == R.id.tv_prompt1) {
                } else if (resId == R.id.tv_prompt2) {
                } else if (resId == R.id.tv_prompt3) {
                } else if (resId == R.id.tv_prompt4) {
                } else if (resId == R.id.tv_prompt5) {
                }
            } else if ((Constant.CALENDAR).equals(applyId)){
                if (resId == R.id.tv_prompt1) {
                } else if (resId == R.id.tv_prompt2) {
                } else if (resId == R.id.tv_prompt3) {
                } else if (resId == R.id.tv_prompt4) {
                } else if (resId == R.id.tv_prompt5) {
                }
            }
        }
    }
}
