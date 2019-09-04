package com.fenda.homepage.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.fenda.homepage.R;
import com.fenda.homepage.bean.ApplyBean;

import java.util.List;

/**
 * @author：Matt.Liao
 * 日期时间: 2019/9/1 17:45
 * 内容描述:
 * 版本：
 * 包名：
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.Holder>{
    private List<ApplyBean> mList;
    private RecyclerView recyclerView;
    public GridAdapter(List<ApplyBean> list){
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submenu_item_recyclerview, null);
        final Holder holder = new Holder(view);
        //对加载的子项注册监听事件
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                String applyId = mList.get(position).getApplyId();
                onItemClickListener.onItemClick(view ,applyId);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.mApplyNameTv.setText(mList.get(position).getApplyName());
        holder.mApplyImageIv.setImageResource(mList.get(position).getApplyImage());
    }

    @Override
    public int getItemCount() {
        return  mList == null ? 0 : mList.size();
    }


    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 定义RecyclerView选项单击事件的回调接口
     */
    public interface OnItemClickListener{

        /**
         * @param view  当前单击的View
         * @param applyId  单击的View的应用id
         */
        void onItemClick(View view, String applyId);
    }

    class Holder extends RecyclerView.ViewHolder {
        private TextView mApplyNameTv;
        private ImageView mApplyImageIv;
        private View fruitView;    //表示我们自定义的控件的视图
        public Holder(View itemView) {
            super(itemView);
            fruitView = itemView;
            mApplyNameTv = itemView.findViewById(R.id.tv_apply_name);
            mApplyImageIv = itemView.findViewById(R.id.iv_apply_image);
        }
    }
}
