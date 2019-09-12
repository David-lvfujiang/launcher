package com.fenda.settings.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.constant.Constant;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/11 17:07
 */
public class SettingsScreenActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsScreenActivity";

    private TextView tvScreen;
    private ImageView ivBack;
    private ListView lvScreenList;

    private String mSeclectedTimeRadioBtn;
    private String mSeclectedStyleRadioBtn;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_screen_layout;
    }

    @Override
    public void initView() {
        tvScreen = findViewById(R.id.screen_info_tv);
        ivBack = findViewById(R.id.screen_back_iv);
        lvScreenList = findViewById(R.id.screen_listview);
    }

    @Override
    public void initData() {

        mSeclectedTimeRadioBtn = (String) SPUtils.get(getApplicationContext(), Constant.Settings.SCREEN_TIME, "");
        mSeclectedStyleRadioBtn = (String) SPUtils.get(getApplicationContext(), Constant.Settings.SCREEN_STYLE, "");

        if(mSeclectedTimeRadioBtn == ""){
            mSeclectedTimeRadioBtn = getString(R.string.settings_standby_3_1);
        }
        if (mSeclectedStyleRadioBtn == ""){
            mSeclectedStyleRadioBtn = getString(R.string.settings_screen_style_time_num);
        }
        LogUtil.d(TAG,  "mSeclectedTimeRadioBtn = " + mSeclectedTimeRadioBtn);
        LogUtil.d(TAG,  "mSeclectedStyleRadioBtn = " + mSeclectedStyleRadioBtn);
        String[] screenNamesDis = new String[] {getString(R.string.settings_screen_name_autointo), getString(R.string.settings_screen_name_style)};
        String[] screenStatusDis = new String[]{mSeclectedTimeRadioBtn, mSeclectedStyleRadioBtn};

        List<Map<String, Object>> listitem = new ArrayList<>();

        for (int i = 0; i < screenNamesDis.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", screenNamesDis[i]);
            map.put("state", screenStatusDis[i]);
            listitem.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listitem, R.layout.settings_screen_items_layout, new String[]{"name", "state"}, new int[]{R.id.screen_items, R.id.screen_items_info});
        lvScreenList.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lvScreenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getItemAtPosition(i);
                String setClickedListName = map.get("name").toString();

                if(setClickedListName.equals(getString(R.string.settings_screen_name_autointo))) {
                    Intent mIntent = new Intent(SettingsScreenActivity.this, SettingsScreenIntoStandbyActivity.class);
                    mIntent.putExtra("SECLECT_TIME_RADIOBTN", mSeclectedTimeRadioBtn);
                    startActivity(mIntent);
                    finish();
                } else if(setClickedListName.equals(getString(R.string.settings_screen_name_style))){
                    Intent mIntent = new Intent(SettingsScreenActivity.this, SettingsScreenStyleActivity.class);
                    mIntent.putExtra("SECLECT_STYLE_RADIOBTN", mSeclectedStyleRadioBtn);
                    startActivity(mIntent);
                    finish();
                }
            }
        });

    }

    @Override
    public void showErrorTip(String msg) {

    }
}
