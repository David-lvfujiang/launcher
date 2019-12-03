package com.fenda.homepage.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenda.common.util.ImageUtil;
import com.fenda.homepage.R;
import com.fenda.homepage.Util.GsdFastBlur;
import com.fenda.homepage.Util.HomeUtil;
import com.fenda.homepage.bean.PageBean;

import java.util.ArrayList;
import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.HomeItemHolder> {
    private Context mContext;
    private List<PageBean> pageBeans;
    private int bgImageValueArr[] = {R.mipmap.cm_bg_a1, R.mipmap.cm_bg_a2, R.mipmap.cm_bg_a3, R.mipmap.cm_bg_04, R.mipmap.cm_bg_05};
//    private int bgImageValueArr[] = {R.mipmap.cm_bg_a1, R.mipmap.cm_bg_a2, R.mipmap.cm_bg_a3, R.mipmap.cm_bg_04, R.mipmap.cm_bg_05};
//
//
//    private  static Bitmap[] beforeBmp = new Bitmap[HomeUtil.PAGE_NUM_MAX];
//    private  static Bitmap[] afterBmp = new Bitmap[HomeUtil.PAGE_NUM_MAX];
    public MainAdapter(Context context,List<PageBean> pageBeans){
        this.pageBeans = pageBeans;
        mContext = context;

//        for (int a=0;a<getItemCount();a++){
//            beforeBmp[a] = BitmapFactory.decodeResource(mContext.getResources(),bgImageValueArr[a]);
//            afterBmp[a] = GsdFastBlur.fastblur(mContext,beforeBmp[a],50);
//        }
    }
    public MainAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public HomeItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View tView = LayoutInflater.from(
                mContext).inflate(R.layout.homepage_home_item, viewGroup,
                false);
        HomeItemHolder tHolder = new HomeItemHolder(tView);

        return tHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull HomeItemHolder viewHolder, int i) {

//        viewHolder.homeItemBgIv.setBackground(new BitmapDrawable(afterBmp[i]));
//        PageBean bean = pageBeans.get(i);
//        if (bean != null){
//            String url = bean.getUrl();
//            if (TextUtils.isEmpty(url)){
//                ImageUtil.loadImage(bean.getDefaultImg(),viewHolder.homeItemBgIv,bean.getDefaultImg());
//            }else {
//                ImageUtil.loadImage(url,viewHolder.homeItemBgIv,bean.getDefaultImg());
//            }
//        }
        viewHolder.homeItemBgIv.setImageResource(bgImageValueArr[i]);
    }

    @Override
    public int getItemCount() {
//        if (pageBeans == null){
//            pageBeans = new ArrayList<>();
//        }
//        return pageBeans.size();
        return bgImageValueArr.length;
    }

    class HomeItemHolder extends RecyclerView.ViewHolder{

        private ImageView homeItemBgIv;

        public HomeItemHolder(@NonNull View itemView) {
            super(itemView);

            homeItemBgIv = itemView.findViewById(R.id.iv_item_bg);
        }
    }
}
