package com.fenda.encyclopedia.view;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;


import com.fenda.encyclopedia.R;
import com.fenda.encyclopedia.model.EncyclopediaShares;
import com.fenda.common.base.BaseActivity;

/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 股票Activity
 */
public class EncyclopediaSharesActivity extends BaseActivity implements View.OnClickListener {
    private EncyclopediaShares shares;
    private TextView tvChange, tvChangeNumber, tvPercentage, tvPercentageNumber, tvDate, tvTitle;
    private RadioButton radioButton;
    private ImageView imgReturnBack;

    @Override
    public int onBindLayout() {
        return R.layout.activity_encyclopedia_shares;
    }

    @Override
    public void initView() {
        tvChange = findViewById(R.id.change_tv);
        tvChangeNumber = findViewById(R.id.change_number_tv);
        tvPercentage = findViewById(R.id.percentage_tv);
        tvPercentageNumber = findViewById(R.id.percentage_number_tv);
        tvDate = findViewById(R.id.date_tv);
        radioButton = findViewById(R.id.encyclopedia_fall_radio);
        imgReturnBack = findViewById(R.id.title_return_img);
        tvTitle = findViewById(R.id.title_text);
        imgReturnBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获取股票实体
        Intent intent = getIntent();
        shares = (EncyclopediaShares) intent.getSerializableExtra("shares");
        if (Float.valueOf(shares.getChange().trim()) < 0) {
            tvChange.setText(R.string.encyclopedia_fall_text);
            tvPercentage.setText(R.string.encyclopedia_fall_percentage_text);
            tvChangeNumber.setText(shares.getChange().replace("-", ""));
            tvPercentageNumber.setText(shares.getPercentage().replace("-", "") + "%");
            radioButton.setChecked(false);
        } else {
            tvChange.setText(R.string.encyclopedia_rise_text);
            tvPercentage.setText(R.string.encyclopedia_rise_percentage_text);
            tvChangeNumber.setText(shares.getChange());
            tvPercentageNumber.setText(shares.getPercentage() + "%");
        }
        tvTitle.setText(shares.getName());
        tvDate.setText(shares.getDate());//设置时间
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_return_img) {
            finish();
        }
    }
}




