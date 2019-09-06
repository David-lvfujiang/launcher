package com.fenda.encyclopedia.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.router.RouterPath;
import com.fenda.encyclopedia.R;
import com.fenda.encyclopedia.model.EncyclopediaSharesBean;
import com.fenda.common.base.BaseActivity;
/**
 * @author: david.lvfujiang
 * @date: 2019/9/4
 * @describe: 股票Activity
 */
@Route(path = RouterPath.Encyclopedia.ENCYCLOPEDIA_SHARES_ACTIVITY)
public class EncyclopediaSharesActivity extends BaseActivity implements View.OnClickListener {
    @Autowired
    EncyclopediaSharesBean shares;
    private final int AUDIO_CONVERSE_CLOSE = 0;
    private TextView mTvChange, mTvChangeNumber, mTvPercentage, mTvPercentageNumber, mTvDate, mTvTitle, mTvCurrent;
    private RadioButton mRadioButton;
    private ImageView mImgReturnBack;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUDIO_CONVERSE_CLOSE:    // 关闭界面
                    EncyclopediaSharesActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int onBindLayout() {
        return R.layout.activity_encyclopedia_shares;
    }

    @Override
    public void initView() {
        mTvChange = findViewById(R.id.change_tv);
        mTvChangeNumber = findViewById(R.id.change_number_tv);
        mTvPercentage = findViewById(R.id.percentage_tv);
        mTvPercentageNumber = findViewById(R.id.percentage_number_tv);
        mTvDate = findViewById(R.id.date_tv);
        mTvCurrent = findViewById(R.id.high_tv);
        mRadioButton = findViewById(R.id.encyclopedia_fall_radio);
        mImgReturnBack = findViewById(R.id.title_return_img);
        mTvTitle = findViewById(R.id.title_tv);
        mImgReturnBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获取股票实体
        if (Float.valueOf(shares.getChange().trim()) < 0) {
            mTvChange.setText(R.string.encyclopedia_fall_text);
            mTvPercentage.setText(R.string.encyclopedia_fall_percentage_text);
            mTvChangeNumber.setText(shares.getChange().replace("-", ""));
            mTvPercentageNumber.setText(shares.getPercentage().replace("-", "") + "%");
            mRadioButton.setChecked(false);
        } else {
            mTvChange.setText(R.string.encyclopedia_rise_text);
            mTvPercentage.setText(R.string.encyclopedia_rise_percentage_text);
            mTvChangeNumber.setText(shares.getChange());
            mTvPercentageNumber.setText(shares.getPercentage() + "%");
        }
        mTvTitle.setText(shares.getName());
        mTvCurrent.setText(shares.getHigh());
        mTvDate.setText(shares.getDate());
        handler.sendEmptyMessageDelayed(AUDIO_CONVERSE_CLOSE, 7000);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_return_img) {
            EncyclopediaSharesActivity.this.finish();
        }
    }

    /**
     * singleTask启动模式回调
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shares = intent.getParcelableExtra("shares");
        initData();
    }
}




