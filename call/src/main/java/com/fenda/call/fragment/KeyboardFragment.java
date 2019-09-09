package com.fenda.call.fragment;

import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;


import com.fenda.call.R;
import com.fenda.call.utils.ImConnectUtil;
import com.fenda.call.view.NumberView;
import com.fenda.common.base.BaseFragment;

import java.lang.reflect.Method;

import io.rong.callkit.RongCallKit;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 拨号盘Fragment
 */
public class KeyboardFragment extends BaseFragment implements NumberView.OnNumberClickListener, View.OnClickListener {


    private EditText mEtNumber;
    private NumberView mNvOne;
    private NumberView mNvTwo;
    private NumberView mNvThree;
    private NumberView mNvStar;
    private NumberView mNvFour;
    private NumberView mNvFive;
    private NumberView mNvSix;
    private NumberView mNvJing;
    private NumberView mNvSeven;
    private NumberView mNvEight;
    private NumberView mNvNine;
    private NumberView mNvZero;
    private ImageView mIvDelete;
    private ImageView mIvCall;

    @Override
    public int onBindLayout() {
        return R.layout.call_fragment_keyboard;
    }

    @Override
    public void initView() {
        hideSoftKeyboard();
        mEtNumber = mRootView.findViewById(R.id.etNumber);
        mNvOne = mRootView.findViewById(R.id.nvOne);
        mNvTwo = mRootView.findViewById(R.id.nvTwo);
        mNvThree = mRootView.findViewById(R.id.nvThree);
        mNvStar = mRootView.findViewById(R.id.nvStar);
        mNvFour = mRootView.findViewById(R.id.nvFour);
        mNvFive = mRootView.findViewById(R.id.nvFive);
        mNvSix = mRootView.findViewById(R.id.nvSix);
        mNvJing = mRootView.findViewById(R.id.nvJing);
        mNvSeven = mRootView.findViewById(R.id.nvSeven);
        mNvEight = mRootView.findViewById(R.id.nvEight);
        mNvNine = mRootView.findViewById(R.id.nvNine);
        mNvZero = mRootView.findViewById(R.id.nvZero);
        mIvDelete = mRootView.findViewById(R.id.ivDelete);
        mIvCall = mRootView.findViewById(R.id.ivCall);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        super.initListener();
        mNvOne.setOnNumberClickListener(this);
        mNvTwo.setOnNumberClickListener(this);
        mNvThree.setOnNumberClickListener(this);
        mNvFour.setOnNumberClickListener(this);
        mNvFive.setOnNumberClickListener(this);
        mNvSix.setOnNumberClickListener(this);
        mNvSeven.setOnNumberClickListener(this);
        mNvEight.setOnNumberClickListener(this);
        mNvNine.setOnNumberClickListener(this);
        mNvZero.setOnNumberClickListener(this);
        mNvStar.setOnNumberClickListener(this);
        mNvJing.setOnNumberClickListener(this);
        mIvDelete.setOnClickListener(this);
        mIvCall.setOnClickListener(this);
    }

    public void hideSoftKeyboard() {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mEtNumber.setInputType(InputType.TYPE_NULL);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(mEtNumber, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNumberClick(View view) {
        int index = mEtNumber.getSelectionStart();
        Editable editable = mEtNumber.getText();
        NumberView numberView = (NumberView) view;
        String number = numberView.getNumber();
        editable.insert(index, number);

    }


    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.ivDelete) {
            int index = mEtNumber.getSelectionStart();
            if (index != 0) {

                Editable editable = mEtNumber.getText();
                editable.delete(index - 1, index);
            }
        } else if (resId == R.id.ivCall) {
            String callNuber = mEtNumber.getText().toString().trim();
            if (ImConnectUtil.isConectIm(mContext)) {
                RongCallKit.startSingleCall(mContext, callNuber, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
            }
        }
    }
}
