package com.fenda.call.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenda.call.R;


/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 自定义拨号号码组合控件
 */
public class NumberView extends LinearLayout {
    private View mItemView;
    private TextView mTvNumber;
    private TextView mTvLetter;
    private String mNumber;
    private String mLetter;
    private OnNumberClickListener mNumberClickListener;

    public String getNumber() {
        return mNumber;
    }

    public void setOnNumberClickListener(OnNumberClickListener onNumberClickListener) {
        this.mNumberClickListener = onNumberClickListener;
    }

    public interface OnNumberClickListener {
        void onNumberClick(View view);
    }

    public NumberView(Context context) {
        super(context);
    }

    public NumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItemView = LayoutInflater.from(context).inflate(R.layout.call_number_layout, this, true);
        mItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumberClickListener != null) {
                    mNumberClickListener.onNumberClick(NumberView.this);
                }
            }
        });
        //加载自定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberItem);
        mNumber = a.getString(R.styleable.NumberItem_number);
        mLetter = a.getString(R.styleable.NumberItem_letter);
        a.recycle();
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //获取子控件
        mTvNumber = findViewById(R.id.tvNumber);
        mTvLetter = findViewById(R.id.tvLetter);

        //将从资源文件中加载的属性设置给子控件
        if (!TextUtils.isEmpty(mNumber)) {
            mTvNumber.setText(mNumber);
        }
        if (!TextUtils.isEmpty(mLetter)) {
            mTvLetter.setText(mLetter);
        }

    }
}
