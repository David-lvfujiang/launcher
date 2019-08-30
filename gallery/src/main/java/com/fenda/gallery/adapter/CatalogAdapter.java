package com.fenda.gallery.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fenda.common.util.ImageUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.bean.PhoneCameraBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> catalogList;
    private Context mContext;
    private int selectIndex;
    private onClickCatalogListener listener;
    private HashMap<String, ArrayList<PhoneCameraBean>> mapBean;

    public CatalogAdapter(List<String> catalogList, Context mContext, int selectIndex, HashMap<String, ArrayList<PhoneCameraBean>> mapBean) {
        this.mapBean = mapBean;
        this.catalogList = catalogList;
        this.mContext = mContext;
        this.selectIndex = selectIndex;
    }

    public void setOnClickCatalogListener(onClickCatalogListener listener) {
        this.listener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.popup_catalog_item, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final String catalogName = catalogList.get(i);
        final ViewHolder holder = (ViewHolder) viewHolder;
        ArrayList<PhoneCameraBean> beanArrayList = mapBean.get(catalogName);
        String name = mContext.getResources().getString(R.string.gallery_picture_size, catalogName, String.valueOf(beanArrayList.size()));
        String path = beanArrayList.get(0).getPhotos();
        ImageUtils.loadImg(mContext, holder.imgPic, path);
        holder.tvName.setText(name);
        if (selectIndex == i) {
            holder.rbButton.setChecked(true);
        } else {
            holder.rbButton.setChecked(false);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.catalogListener(catalogName, i);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        if (catalogList == null)
            catalogList = new ArrayList<>();
        return catalogList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public RadioButton rbButton;
        private ImageView imgPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            rbButton = itemView.findViewById(R.id.rb_button);
            imgPic = itemView.findViewById(R.id.img_pic);
        }
    }


    public interface onClickCatalogListener {

        void catalogListener(String catalog, int position);

    }


}
