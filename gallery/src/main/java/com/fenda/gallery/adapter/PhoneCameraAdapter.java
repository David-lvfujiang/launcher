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

import com.fenda.common.util.FastClickUtils;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.activity.PhotoDetailActivity;
import com.fenda.gallery.bean.PhoneCameraBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:51
 * @Description 本地相册Adapter
 */
public class PhoneCameraAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PhoneCameraBean> mBeanList;
    private Context mContext;
    private selectOnClickListener mListener;
    private List<String> mSelectList = new ArrayList<>();


    public PhoneCameraAdapter(ArrayList<PhoneCameraBean> beanList, Context mContext) {
        this.mBeanList = beanList;
        this.mContext = mContext;
    }

    public void setNewData(ArrayList<PhoneCameraBean> beanList) {
        if (this.mBeanList != null) {
            this.mBeanList.clear();
            this.mBeanList.addAll(beanList);
        } else {
            this.mBeanList = beanList;
        }
        notifyDataSetChanged();
    }


    public void setSelectOnClickListener(selectOnClickListener listener) {
        this.mListener = listener;
    }

    public void notifyAdapter(int index) {
        this.notifyItemChanged(index);
    }

    public void notifyAllAdapter() {
        this.notifyAll();
    }

    public List<String> getSelectList() {
        return mSelectList;
    }
    public List<PhoneCameraBean> getData() {
        return mBeanList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.gallery_item_family_photo, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MyViewHolder holder = (MyViewHolder) viewHolder;
        final PhoneCameraBean cameraBean = mBeanList.get(i);
        if (cameraBean != null) {
            String path = cameraBean.getPhotos();
            ImageUtil.loadImg(mContext, holder.imgPhoto, path);
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
                        if (mSelectList.contains(cameraBean.getPhotos())) {
                            mSelectList.remove(cameraBean.getPhotos());
                        }
                        cameraBean.setSelectStatus(1);
                    } else if (cameraBean.getSelectStatus() == 1) {
                        if (mSelectList.size() >= 9) {
                            ToastUtils.show("最多只能选择9张图片");
                            return;
                        }
                        holder.imgSelect.setImageResource(R.mipmap.gallery_photo_selected);
                        if (!mSelectList.contains(cameraBean.getPhotos())) {
                            mSelectList.add(cameraBean.getPhotos());
                        }
                        cameraBean.setSelectStatus(2);
                    }
                    if (mListener != null) {
                        mListener.selectListener(cameraBean, i);
                    }
                }
            });

            holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FastClickUtils.isFastClick()){
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", mBeanList);
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
        if (mBeanList == null)
            mBeanList = new ArrayList<>();
        return mBeanList.size();
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
