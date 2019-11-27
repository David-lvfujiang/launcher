package com.fenda.settings.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;
import com.fenda.settings.activity.SettingsDeviceContractsNickNameActivity;

import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/11/27 11:50
 */
public class SettingsContactsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserInfoBean> mDatas;
    private static final String TAG = "SettingsContactsAdapter";

    public SettingsContactsAdapter(Context context, List<UserInfoBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder;
        holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.settings_device_bind_contracts_item_layout, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final UserInfoBean constactsListBean = mDatas.get(i);
        final ViewHolder holder1= (ViewHolder) viewHolder;
        if(constactsListBean != null){

            String iconPath = constactsListBean.getIcon();
            ImageUtil.loadImg(mContext, holder1.contractIcon, iconPath);

            if(i == 0){
                holder1.contractName.setText(constactsListBean.getUserName() + "(管理员)");
            } else {
                holder1.contractName.setText(constactsListBean.getUserName());
            }

            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String clickedName = (String) holder1.contractName.getText();
                    LogUtil.d(TAG, "clicked contract name  = " + clickedName + " = " + constactsListBean.getUserName());
                    LogUtil.d(TAG, "clicked contract icon = " + constactsListBean.getIcon());
                    Intent contractInfoIntent = new Intent(mContext, SettingsDeviceContractsNickNameActivity.class);
                    contractInfoIntent.putExtra("ContractName", clickedName);
                    contractInfoIntent.putExtra("ContractIcon", constactsListBean.getIcon());
                    mContext.startActivity(contractInfoIntent);
//                    mContext.finish();
                }
            });
        }
    }

    public void setNewData(List<UserInfoBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView contractName;
        public ImageView contractIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            contractName= itemView.findViewById(R.id.bind_contacts_name);
            contractIcon = itemView.findViewById(R.id.bind_contacts_icon_11);
        }
    }
}
