package com.fenda.remind.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.fenda.common.provider.IVoiceRequestProvider;
import com.fenda.common.router.RouterPath;
import com.fenda.protocol.tcp.bus.EventBusUtils;
import com.fenda.remind.R;
import com.fenda.remind.bean.AlarmBean;
import com.fenda.remind.util.AlarmUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * @author WangZL
 * @Date $date$ $time$
 */
public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AlarmBean> alarmBeans;
    private OnClickItemListener listener;

    public AlarmAdapter(Context mContext, List<AlarmBean> alarmBeans) {
        this.mContext = mContext;
        this.alarmBeans = alarmBeans;
    }

    public void setOnClickItemListener(OnClickItemListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.remind_item_alarm,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final AlarmBean bean = alarmBeans.get(position);
        if (bean != null){
            String date = bean.getTime();
            int index = date.lastIndexOf(":");
            if (index != -1){
                date = date.substring(0,index);
            }
            String repeat = bean.getRepeat();
            String mTime;
            if (!TextUtils.isEmpty(repeat)){
                mTime = AlarmUtil.getRepeat(repeat);
                if (TextUtils.isEmpty(mTime)){
                    mTime = repeat;
                }
            }else {
                mTime = AlarmUtil.getAlarmDate(bean);
            }
            int type = bean.getType();
            if (type == 1){
                mHolder.imgDelete.setVisibility(View.VISIBLE);
            }else {
                mHolder.imgDelete.setVisibility(View.GONE);
            }
            mHolder.tvAlarmTime.setText(date);
            mHolder.tvWeek.setText(mTime);
            String period = bean.getPeriod();
            if (TextUtils.isEmpty(period)){
                period = AlarmUtil.getPreiod(date);
            }
            mHolder.tvAlarmDate.setText(period);
        }

        mHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = new Gson().toJson(bean);
                if (listener != null){
                    listener.onItemListener(holder.getAdapterPosition(),json);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return alarmBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvAlarmTime;
        public TextView tvAlarmDate;
        public ImageView imgDelete;
        public TextView tvWeek;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAlarmTime = itemView.findViewById(R.id.tv_time);
            tvAlarmDate = itemView.findViewById(R.id.tv_date);
            imgDelete   = itemView.findViewById(R.id.img_delete);
            tvWeek      = itemView.findViewById(R.id.tv_week);
        }
    }


    public interface OnClickItemListener{
        void onItemListener(int position,String json);
    }
}
