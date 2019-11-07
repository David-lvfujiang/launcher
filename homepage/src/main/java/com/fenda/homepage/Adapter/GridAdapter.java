package com.fenda.homepage.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fenda.common.util.ToastUtils;
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
public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private List<ApplyBean> mList;
    private RecyclerView recyclerView;
    private static final int ITEM_TYPE_BOTTOM = 1;
    private static final int ITEM_TYPE_CONTENT = 0;
    public GridAdapter(List<ApplyBean> list){
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_CONTENT){
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
            }else {
                View bottomView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pull,null);
                FooterViewHolder viewHolder = new FooterViewHolder(bottomView);
                return viewHolder;
            }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder){
            Holder contentHolder = (Holder) holder;
            contentHolder.mApplyNameTv.setText(mList.get(position).getApplyName());
            contentHolder.mApplyImageIv.setImageResource(mList.get(position).getApplyImage());
        }else if (holder instanceof FooterViewHolder){
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.lin1.setOnClickListener(this);
            footerHolder.lin2.setOnClickListener(this);
            footerHolder.lin3.setOnClickListener(this);
            footerHolder.lin4.setOnClickListener(this);
            footerHolder.lin5.setOnClickListener(this);
            footerHolder.lin6.setOnClickListener(this);
            footerHolder.lin7.setOnClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return  mList == null ? 0 : mList.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position >= mList.size()){
//            return ITEM_TYPE_BOTTOM;
//        }
//        return ITEM_TYPE_CONTENT;
//    }

    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        ToastUtils.show("努力开发中，敬请期待。。。");
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


    class FooterViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout lin1;
        private LinearLayout lin2;
        private LinearLayout lin3;
        private LinearLayout lin4;
        private LinearLayout lin5;
        private LinearLayout lin6;
        private LinearLayout lin7;

        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            lin1 = itemView.findViewById(R.id.lin1);
            lin2 = itemView.findViewById(R.id.lin2);
            lin3 = itemView.findViewById(R.id.lin3);
            lin4 = itemView.findViewById(R.id.lin4);
            lin5 = itemView.findViewById(R.id.lin5);
            lin6 = itemView.findViewById(R.id.lin6);
            lin7 = itemView.findViewById(R.id.lin7);
        }
    }
}
