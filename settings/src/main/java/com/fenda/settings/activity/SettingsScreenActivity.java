package com.fenda.settings.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fenda.common.base.BaseMvpActivity;
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

    private ImageView ivBack;
    private ListView lvScreenList;

    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_screen_layout;
    }

    @Override
    public void initView() {
        ivBack = findViewById(R.id.screen_back_iv);
        lvScreenList = findViewById(R.id.screen_listview);
    }

    @Override
    public void initData() {
        String[] screenNamesDis = new String[] {getString(R.string.settings_screen_name_autointo), getString(R.string.settings_screen_name_style)};
        String[] screenStatusDis = new String[]{" ", " "};

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
                    startActivity(mIntent);
                } else if(setClickedListName.equals(getString(R.string.settings_screen_name_style))){
                    Intent mIntent = new Intent(SettingsScreenActivity.this, SettingsScreenStyleActivity.class);
                    startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
