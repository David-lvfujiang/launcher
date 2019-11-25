package com.fenda.call.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.call.R;
import com.fenda.call.utils.ImConnectUtil;
import com.fenda.common.BaseApplication;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.constant.Constant;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.SPUtils;

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
    //定义二种常量  表示二种条目类型
    public static final int TYPE_DEVICE = 0;
    public static final int TYPE_CONTACT = 1;

    public ContactListAdapter(Context context, List<UserInfoBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_DEVICE) {
            holder = new MyDeviceViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.call_item_device, parent,
                    false));
            return holder;
        } else if (viewType == TYPE_CONTACT) {
            holder = new MyContactViewHolder(LayoutInflater.from(
                    mContext).inflate(R.layout.call_item_contact, parent,
                    false));

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof MyDeviceViewHolder) {
            MyDeviceViewHolder holder = (MyDeviceViewHolder) viewHolder;
            String deviceName = (String) SPUtils.get(BaseApplication.getInstance(), Constant.Settings.DEVICE_NAME, "");
            holder.tvName.setText(deviceName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳转至扫码界面
                    ARouter.getInstance().build(RouterPath.SETTINGS.SettingsDeviceAddContractsQRActivity).with(ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle()).navigation();
//                    mContext.startActivity(new Intent(mContext, SelectContactsActivity.class));
                }
            });
        } else if (viewHolder instanceof MyContactViewHolder) {
            final UserInfoBean bean = mDatas.get(position - 1);
            MyContactViewHolder holder = (MyContactViewHolder) viewHolder;
            final String userId = bean.getMobile();
            int userType = bean.getUserType();
            holder.tvName.setText(bean.getUserName());
            ImageUtil.loadImg(mContext, holder.ivHead, bean.getIcon());
            if (userType == 1) {
                holder.tvRole.setVisibility(View.VISIBLE);
            } else if (userType == 2) {
                holder.tvRole.setVisibility(View.GONE);
            }
            final String groupId = (String) SPUtils.get(mContext, Constant.Settings.USER_ID, "");
            holder.ivVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImConnectUtil.isConectIm(mContext)) {
                        RongCallKit.startSingleCall(mContext, userId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO);
//                        ArrayList<String> userIds = new ArrayList<>();
//                        userIds.add(userId);
//                        RongCallKit.startMultiCall(mContext, Conversation.ConversationType.GROUP, groupId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_VIDEO, userIds);
                    }

                }
            });
            holder.ivAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImConnectUtil.isConectIm(mContext)) {
                        RongCallKit.startSingleCall(mContext, userId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO);
//                        ArrayList<String> userIds = new ArrayList<>();
//                        userIds.add(userId);
//                        RongCallKit.startMultiCall(mContext, Conversation.ConversationType.GROUP, groupId, RongCallKit.CallMediaType.CALL_MEDIA_TYPE_AUDIO, userIds);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 1 : mDatas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_DEVICE;
        } else {
            return TYPE_CONTACT;
        }

    }

    public void setNewData(List<UserInfoBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    class MyDeviceViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivHead;

        public MyDeviceViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvDeviceName);
            ivHead = view.findViewById(R.id.ivDeciceIcon);
        }
    }

    class MyContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvRole;
        ImageView ivAudio;
        ImageView ivVideo;
        ImageView ivHead;

        public MyContactViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvRole = view.findViewById(R.id.tvRole);
            ivAudio = view.findViewById(R.id.ivAudio);
            ivVideo = view.findViewById(R.id.ivVideo);
            ivHead = view.findViewById(R.id.civHead);
        }
    }
}
