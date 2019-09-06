package com.fenda.encyclopedia.view;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseActivity;
import com.fenda.common.router.RouterPath;
import com.fenda.encyclopedia.R;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 百科问答Activity
 */
@Route(path = RouterPath.Encyclopedia.ENCYCLOPEDIA_QUESTIION_ACTIVITY)
public class EncyclopediaQuestiionActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvContent, tvTitle;
    private ImageView imgReturnBack;
    private EncyclopediaAutoScrollView autoScrollView;
    @Autowired
    String content;
    @Autowired
    String title;
    @Override
    public int onBindLayout() {
        return R.layout.activity_encyclopedia_questiion;
    }

    @Override
    public void initView() {
        tvTitle = findViewById(R.id.title_text);
        tvContent = findViewById(R.id.content_text);
        autoScrollView = findViewById(R.id.scrollView);
        imgReturnBack = findViewById(R.id.title_return_img);
        imgReturnBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tvContent.setText(content);
        tvTitle.setText(title);
        autoScrollView.setAutoToScroll(true);//设置可以自动滑动
        autoScrollView.setFistTimeScroll(5000);//设置第一次自动滑动的时间
        autoScrollView.setScrollRate(50);//设置滑动的速率
        autoScrollView.setScrollLoop(false);//设置是否循环滑动
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_return_img) {
            finish();
        }
    }
}
