package com.fenda.settings.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fenda.common.util.DensityUtil;
import com.fenda.settings.R;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/4 10:48
 */
public class SettingsBindContactsDialog extends Dialog {
    private Context mContext;
    private TextView tvConfirm;
    private TextView tvContent;
    private TextView tvCancel;
    public SettingsBindContactsDialog(Context context)
    {
        super(context, R.style.ACPLDialog );
        mContext = context;
        initView();
        addListener();
    }

    private void initView(){
        View mView = LayoutInflater.from(mContext).inflate(R.layout.settings_dialog_bind_contacts,null);
        tvConfirm = mView.findViewById(R.id.tv_confirm);
        tvCancel  = mView.findViewById(R.id.tv_cancel);
        tvContent = mView.findViewById(R.id.tv_content);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (DensityUtil.getScreenWidth(mContext)*0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(mView,params);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

    }

    public void setTvContent(String text){
        tvContent.setText(text);
    }

    public void setTvConcelVisibiltyGone(){
        tvCancel.setVisibility(View.GONE);
    }

    public void setTvSureContent(String string){
        tvConfirm.setText(string);
    }

    private void addListener(){

    }

    public void setOnConfirmListener(View.OnClickListener confirmListener){
        tvConfirm.setOnClickListener(confirmListener);
    }
    public void setOnCancelListener(View.OnClickListener cancelListener){
        tvCancel.setOnClickListener(cancelListener);
    }

}
