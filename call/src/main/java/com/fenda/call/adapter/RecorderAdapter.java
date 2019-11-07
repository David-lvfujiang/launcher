package com.fenda.call.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.call.R;
import com.fenda.call.utils.ImConnectUtil;
import com.fenda.common.util.DateUtils;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.view.CircleImageView;

import java.util.List;

import io.rong.callkit.RongCallKit;
import io.rong.callkit.bean.CallRecoderBean;
import io.rong.calllib.RongCallCommon;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/2 10:21
 * @Description 通话记录Adapter
 */
public class RecorderAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<CallRecoderBean> mDatas;

    public RecorderAdapter(Context context, List<CallRecoderBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        MyRecorderHolder holder = new MyRecorderHolder(LayoutInflater.from(
                mContext).inflate(R.layout.call_item_recorder, viewGroup,
                false));
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        MyRecorderHolder holder = (MyRecorderHolder) viewHolder;
        final CallRecoderBean bean = mDatas.get(position);
        final String userId = bean.getPhone();
        String name = bean.getName();
        String phone=bean.getPhone();
        holder.tvTime.setText(DateUtils.getNewChatTime(bean.getCallTime()));
        int count=bean.getCount();
        if (!TextUtils.isEmpty(name)) {
            if (count==1) {
                holder.tvName.setText(name);
            }else{
                holder.tvName.setText(name+"("+count+")");
            }
        } else if (!TextUtils.isEmpty(phone)){
            if (count==1) {
                holder.tvName.setText(phone);
            }else{
                holder.tvName.setText(phone+"("+count+")");
            }
        }
        String icon = bean.getIcon();
        if (TextUtils.isEmpty(icon)) {
            holder.civHead.setImageResource(R.mipmap.call_portrait_01);
        } else {
            ImageUtil.loadImg(mContext, holder.civHead, icon);
        }
        if (getItemCount() == 1) {
            holder.vTopLine.setVisibility(View.INVISIBLE);
            holder.vBottomLine.setVisibility(View.INVISIBLE);
            holder.tvDot.setVisibility(View.INVISIBLE);
        } else {
            if (position == 0) {
                holder.vTopLine.setVisibility(View.INVISIBLE);
                holder.tvDot.setVisibility(View.VISIBLE);
                holder.vBottomLine.setVisibility(View.VISIBLE);
            } else if (position == getItemCount() - 1) {
                holder.tvDot.setVisibility(View.VISIBLE);
                holder.vTopLine.setVisibility(View.VISIBLE);
                holder.vBottomLine.setVisibility(View.INVISIBLE);
            }else {
                holder.tvDot.setVisibility(View.VISIBLE);
                holder.vTopLine.setVisibility(View.VISIBLE);
                holder.vBottomLine.setVisibility(View.VISIBLE);
            }
        }
        int callStatus = bean.getCallStatus();

        if (callStatus == 0) {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.tvName.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        final int callType = bean.getCallType();
        int callMode = bean.getCallMode();
        if (callType == RongCallCommon.CallMediaType.AUDIO.getValue() && callMode == 0) {
            // 音频呼入
            holder.ivStatus.setImageResource(R.mipmap.call_ico_call);

        } else if (callType == RongCallCommon.CallMediaType.AUDIO.getValue() && callMode == 1) {
            // 音频呼出
            holder.ivStatus.setImageResource(R.mipmap.call_ico_callphone);

        } else if (callType == RongCallCommon.CallMediaType.VIDEO.getValue() && callMode == 0) {
            // 视频呼入
            holder.ivStatus.setImageResource(R.mipmap.call_video_incoming);

        } else if (callType == RongCallCommon.CallMediaType.VIDEO.getValue() && callMode == 1) {
            // 视频呼出
            holder.ivStatus.setImageResource(R.mipmap.call_video_out);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImConnectUtil.isConectIm(mContext)) {
                    RongCallKit.startSingleCall(mContext, userId, (callType == RongCallCommon.CallMediaType.AUDIO.getValue()) ? RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO : RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setNewData(List<CallRecoderBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    class MyRecorderHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvName;
        ImageView ivStatus;
        View vTopLine;
        View vBottomLine;
        TextView tvDot;
        CircleImageView civHead;

        public MyRecorderHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvName = itemView.findViewById(R.id.tvName);
            civHead = itemView.findViewById(R.id.civHead);
            tvDot = itemView.findViewById(R.id.tvDot);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            vTopLine = itemView.findViewById(R.id.vTopLine);
            vBottomLine = itemView.findViewById(R.id.vButtomLine);
        }
    }
}
