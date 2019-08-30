package com.fenda.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenda.common.util.ImageUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.activity.PhotoDetailActivity;
import com.fenda.gallery.bean.PhoneCameraBean;

import java.util.ArrayList;
import java.util.List;


public class PhoneCameraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PhoneCameraBean> beanList;
    private Context mContext;
    private selectOnClickListener listener;
    private List<String> selectList = new ArrayList<>();


    public PhoneCameraAdapter(ArrayList<PhoneCameraBean> beanList, Context mContext) {
        this.beanList = beanList;
        this.mContext = mContext;
    }

    public void setNewData(ArrayList<PhoneCameraBean> beanList) {
        if (this.beanList != null) {
            this.beanList.clear();
            this.beanList.addAll(beanList);
        } else {
            this.beanList = beanList;
        }
        notifyDataSetChanged();
    }


    public void setSelectOnClickListener(selectOnClickListener listener) {
        this.listener = listener;
    }

    public void notifyAdapter(int index) {
        this.notifyItemChanged(index);
    }

    public void notifyAllAdapter() {
        this.notifyAll();
    }

    public List<String> getSelectList() {
        return selectList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_family_photo, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MyViewHolder holder = (MyViewHolder) viewHolder;
        final PhoneCameraBean cameraBean = beanList.get(i);
        if (cameraBean != null) {
            String path = cameraBean.getPhotos();
            ImageUtils.loadImg(mContext, holder.imgPhoto, path);
            if (cameraBean.getSelectStatus() == 2) {
                holder.imgSelect.setImageResource(R.mipmap.gallery_photo_selected);
            } else if (cameraBean.getSelectStatus() == 1) {
                holder.imgSelect.setImageResource(R.mipmap.gallery_photo_normal);
            }
            holder.imgSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cameraBean.getSelectStatus() == 2) {
                        holder.imgSelect.setImageResource(R.mipmap.gallery_photo_normal);
                        if (selectList.contains(cameraBean.getPhotos())) {
                            selectList.remove(cameraBean.getPhotos());
                        }
                        cameraBean.setSelectStatus(1);
                    } else if (cameraBean.getSelectStatus() == 1) {
                        if (selectList.size() >= 9) {
                            ToastUtils.show("最多只能选择9张图片");
                            return;
                        }
                        holder.imgSelect.setImageResource(R.mipmap.gallery_photo_selected);
                        if (!selectList.contains(cameraBean.getPhotos())) {
                            selectList.add(cameraBean.getPhotos());
                        }
                        cameraBean.setSelectStatus(2);
                    }
                    if (listener != null) {
                        listener.selectListener(cameraBean, i);
                    }
                }
            });

            holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", beanList);
                    bundle.putInt("index", i);
                    Intent mIntent = new Intent(mContext, PhotoDetailActivity.class);
                    mIntent.putExtras(bundle);
                    mContext.startActivity(mIntent);


                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (beanList == null)
            beanList = new ArrayList<>();
        return beanList.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPhoto;
        public ImageView imgSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            imgSelect = itemView.findViewById(R.id.img_select);


        }

    }


    public interface selectOnClickListener {
        void selectListener(PhoneCameraBean bean, int index);
    }

}
