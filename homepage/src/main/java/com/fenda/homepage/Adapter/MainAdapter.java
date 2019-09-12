package com.fenda.homepage.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fenda.homepage.R;
import com.fenda.homepage.Util.GsdFastBlur;
import com.fenda.homepage.Util.HomeUtil;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.HomeItemHolder> {
    private Context mContext;
//    private int bgImageValueArr[] = {R.mipmap.cm_bg_a1, R.mipmap.cm_bg_a2, R.mipmap.cm_bg_a3, R.mipmap.cm_bg_04, R.mipmap.cm_bg_05};
//
//
//    private  static Bitmap[] beforeBmp = new Bitmap[HomeUtil.PAGE_NUM_MAX];
//    private  static Bitmap[] afterBmp = new Bitmap[HomeUtil.PAGE_NUM_MAX];
    public MainAdapter(Context context){
        mContext = context;
//        for (int a=0;a<getItemCount();a++){
//            beforeBmp[a] = BitmapFactory.decodeResource(mContext.getResources(),bgImageValueArr[a]);
//            afterBmp[a] = GsdFastBlur.fastblur(mContext,beforeBmp[a],50);
//        }
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
        int bgImageValueArr[] = {R.mipmap.cm_bg_a1, R.mipmap.cm_bg_a2, R.mipmap.cm_bg_a3, R.mipmap.cm_bg_04, R.mipmap.cm_bg_05};
//        viewHolder.homeItemBgIv.setBackground(new BitmapDrawable(afterBmp[i]));
        viewHolder.homeItemBgIv.setImageResource(bgImageValueArr[i]);
    }

    @Override
    public int getItemCount() {
        return HomeUtil.PAGE_NUM_MAX;
    }

    class HomeItemHolder extends RecyclerView.ViewHolder{

        private ImageView homeItemBgIv;

        public HomeItemHolder(@NonNull View itemView) {
            super(itemView);

            homeItemBgIv = itemView.findViewById(R.id.iv_item_bg);
        }
    }
}
