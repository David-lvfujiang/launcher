package com.fenda.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.util.ImageUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.activity.PhotoDetailActivity;
import com.fenda.gallery.bean.DayPhoteInfoBean;
import com.fenda.gallery.bean.PhoneCameraBean;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class FamilyPhotoAdapter extends SectionedRecyclerViewAdapter<FamilyPhotoAdapter.MyHeaderViewHolder, FamilyPhotoAdapter.MyItemViewHolder, FamilyPhotoAdapter.MyFooterViewHolder> {

    private Context mContext;
    private List<DayPhoteInfoBean> mDatas;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void itemClickListener(PhoneCameraBean bean);

    }

    public FamilyPhotoAdapter(Context context, List<DayPhoteInfoBean> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    /**
     * header或者footer的个数
     *
     * @return
     */
    @Override
    protected int getSectionCount() {
        return mDatas.size();
    }

    /**
     * 每个header或者footer中包含具体的内容个数
     *
     * @return
     */
    @Override
    protected int getItemCountForSection(int section) {
        return mDatas.get(section).getList().size();
    }

    /**
     * 是否显示footer
     *
     * @param section
     * @return
     */
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    /**
     * 渲染具体的HeaderViewHolder
     *
     * @param parent   HeaderViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    protected MyHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rv_header, parent, false);
        return new MyHeaderViewHolder(itemView);
    }

    /**
     * 渲染具体的FooterViewHolder
     *
     * @param parent   FooterViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    protected MyFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_rv_footer, parent, false);
        return new MyFooterViewHolder(itemView);
    }

    /**
     * 渲染具体的ItemViewHolder
     *
     * @param parent   ItemViewHolder的容器
     * @param viewType 一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    protected MyItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_family_photo, parent, false);
        return new MyItemViewHolder(itemView);
    }

    /**
     * 绑定HeaderViewHolder的数据。
     *
     * @param holder
     * @param section 数据源list的下标
     */
    @Override
    protected void onBindSectionHeaderViewHolder(MyHeaderViewHolder holder, int section) {
        DayPhoteInfoBean bean = mDatas.get(section);
        if (null == bean)
            return;
        holder.tv_header_title.setText(bean.getHeader());
    }

    /**
     * 绑定FooterViewHolder的数据。
     *
     * @param holder
     * @param section 数据源list的下标
     */
    @Override
    protected void onBindSectionFooterViewHolder(MyFooterViewHolder holder, int section) {
        DayPhoteInfoBean bean = mDatas.get(section);
        if (null == bean)
            return;
        holder.tv_footer_title.setText(bean.getFooter());
    }

    /**
     * 绑定ItemViewHolder的数据。
     *
     * @param holder
     * @param section 数据源list的下标
     */
    @Override
    protected void onBindItemViewHolder(MyItemViewHolder holder, int section, final int position) {
        DayPhoteInfoBean bean = mDatas.get(section);
        if (null == bean)
            return;
        final List<PhoneCameraBean> photoList = bean.getList();
        if (photoList == null) {
            return;
        }
       final PhoneCameraBean phoneCameraBean = photoList.get(position);
        if (phoneCameraBean == null) {
            return;
        }

        if (phoneCameraBean.getSelectStatus() == 0) {
            holder.iv_select.setVisibility(View.GONE);
        } else if (phoneCameraBean.getSelectStatus() == 1) {
            holder.iv_select.setVisibility(View.VISIBLE);
            holder.iv_select.setImageResource(R.mipmap.gallery_photo_normal);
        } else if (phoneCameraBean.getSelectStatus() == 2) {
            holder.iv_select.setVisibility(View.VISIBLE);
            holder.iv_select.setImageResource(R.mipmap.gallery_photo_selected);
        }
        holder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneCameraBean.getSelectStatus() == 1) {
                    phoneCameraBean.setSelectStatus(2);
                } else if (phoneCameraBean.getSelectStatus() == 2) {
                    phoneCameraBean.setSelectStatus(1);
                }
                if (listener != null) {
                    listener.itemClickListener(phoneCameraBean);
                }

                notifyDataSetChanged();

            }
        });
        holder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneCameraBean.getSelectStatus() == 0) {
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) photoList);
                    mBundle.putInt("index", position);
                    mBundle.putInt("style", 1);
                    Intent mIntent = new Intent(mContext, PhotoDetailActivity.class);
                    mIntent.putExtras(mBundle);
                    mContext.startActivity(mIntent);
                }
            }
        });
        ImageUtils.loadImg(mContext,holder.iv_photo,  phoneCameraBean.getThumbnail());
    }

    /**
     * ItemViewHolder
     */
    public class MyItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_photo;
        private ImageView iv_select;

        public MyItemViewHolder(View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.img_photo);
            iv_select = itemView.findViewById(R.id.img_select);
        }
    }

    /**
     * HeaderViewHolder
     */
    public class MyHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_header_title;

        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            tv_header_title = (TextView) itemView.findViewById(R.id.item_header_title);
        }
    }

    /**
     * FooterViewHolder
     */
    public class MyFooterViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_footer_title;

        public MyFooterViewHolder(View itemView) {
            super(itemView);
            tv_footer_title = (TextView) itemView.findViewById(R.id.item_footer_title);
        }
    }

    public List<DayPhoteInfoBean> getData() {
        return mDatas;
    }
}
