package com.fenda.call.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.call.R;
import com.fenda.call.utils.ImConnectUtil;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.util.ImageUtils;

import java.util.List;

import io.rong.callkit.RongCallKit;
/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 联系人adapter
 */
public class ContactListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserInfoBean> mDatas;
    private static final String TAG = "ContactListAdapter";

    public ContactListAdapter(Context context, List<UserInfoBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.call_item_contact, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final UserInfoBean bean = mDatas.get(position);
        MyViewHolder holder = (MyViewHolder) viewHolder;
        final String userId = bean.getMobile();
        int userType = bean.getUserType();
        holder.tvName.setText(bean.getUserName());
        ImageUtils.loadImg(mContext,holder.ivHead, bean.getIcon());
        if (userType == 1) {
            holder.tvRole.setVisibility(View.VISIBLE);
            holder.tvPermissions.setVisibility(View.VISIBLE);
        } else if (userType == 2) {
            holder.tvRole.setVisibility(View.GONE);
            holder.tvPermissions.setVisibility(View.GONE);
        }
        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImConnectUtil.isConectIm(mContext)) {
                    RongCallKit.startSingleCall(mContext, userId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                    Log.d(TAG, "onClick: startSingleCall id" + userId);
                }

            }
        });
        holder.ivAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImConnectUtil.isConectIm(mContext)) {
                    RongCallKit.startSingleCall(mContext, userId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvRole;
        TextView tvPermissions;
        ImageView ivAudio;
        ImageView ivVideo;
        ImageView ivHead;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvRole = view.findViewById(R.id.tvRole);
            tvPermissions = view.findViewById(R.id.tvPermissions);
            ivAudio = view.findViewById(R.id.ivAudio);
            ivVideo = view.findViewById(R.id.ivVideo);
            ivHead = view.findViewById(R.id.civHead);
        }
    }
}
