package com.fenda.settings.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.settings.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/10 15:30
 */
public class SettingsChangeBtNameAcivity extends BaseMvpActivity {
    private static final String TAG = "SettingsChangeBtNameAcivity";

    private TextView tvSureBtn;
    private TextView tvCancelBtn;
    private EditText etBtName;

    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private String mIntentBtName;
    private String mChangedBtName;
    private int mBtNameMaxLength = 30;


    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_change_bt_name_layout;
    }

    @Override
    public void initView() {
        tvCancelBtn = findViewById(R.id.change_device_bt_name_cancel);
        tvSureBtn = findViewById(R.id.change_device_bt_name_sure);
        etBtName = findViewById(R.id.edit_device_bt_name);
    }

    @Override
    public void initData() {
        Intent mIntent = getIntent();
        mIntentBtName = mIntent.getStringExtra("BT_NAME");
        LogUtil.d(TAG, "mIntentBtName = " + mIntentBtName);
        etBtName.setText(mIntentBtName);
//
//        etBtName.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int count, int after) {
//
//                int mTextMaxlenght = 0;
//                Editable editable = etBtName.getText();
//                String str = editable.toString().trim();
//                //得到最初字段的长度大小，用于光标位置的判断
//                int selEndIndex = Selection.getSelectionEnd(editable);
//                // 取出每个字符进行判断，如果是字母数字和标点符号则为一个字符加1，
//                //如果是汉字则为两个字符
//                for (int i = 0; i < str.length(); i++) {
//                    char charAt = str.charAt(i);
//                    //32-122包含了空格，大小写字母，数字和一些常用的符号，
//                    //如果在这个范围内则算一个字符，
//                    //如果不在这个范围比如是汉字的话就是两个字符
//                    if (charAt >= 32 && charAt <= 122) {
//                        mTextMaxlenght++;
//                    } else {
//                        mTextMaxlenght += 2;
//                    }
//                    // 当最大字符大于40时，进行字段的截取，并进行提示字段的大小
//                    if (mTextMaxlenght > 30) {
//                    // 截取最大的字段
//                        String newStr = str.substring(0, i);
//                        etBtName.setText(newStr);
//                    // 得到新字段的长度值
//                        editable = etBtName.getText();
//                        int newLen = editable.length();
//                        if (selEndIndex > newLen) {
//                            selEndIndex = editable.length();
//                        }
//                    // 设置新光标所在的位置
//                        Selection.setSelection(editable, selEndIndex);
////                        Toast.makeText(SettingsChangeBtNameAcivity.this, "最大长度为30个字符或15个汉字！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    @Override
    public void initListener() {
        tvCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(SettingsChangeBtNameAcivity.this, SettingsBluetoothActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
        tvSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChangedBtName = etBtName.getText().toString();
                if(!mBluetoothAdapter.setName(mChangedBtName)) {
                    ToastUtils.show(R.string.settings_change_bt_name_fail);
                }
                Intent mIntent = new Intent(SettingsChangeBtNameAcivity.this, SettingsBluetoothActivity.class);
                startActivity(mIntent);
                finish();

//                else {
//                    ToastUtils.show(R.string.settings_change_bt_name_fail);
//                }
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }
}
