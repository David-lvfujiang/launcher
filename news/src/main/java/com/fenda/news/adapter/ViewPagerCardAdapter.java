package com.fenda.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenda.common.basebean.player.FDMusic;
import com.fenda.common.util.ImageUtil;
import com.fenda.news.R;

import java.util.ArrayList;

/**
 * @author matt.liaojianpeng
 * 日期时间: 2019/8/29 16:03
 * 内容描述:
 * 版本：
 * 包名：
 *
 */
public class ViewPagerCardAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<FDMusic> newsListData;

    public ViewPagerCardAdapter(Context mContext, ArrayList<FDMusic> newsListData) {
        this.mContext = mContext;
        this.newsListData = newsListData;
    }
    @Override
    public int getCount() {
        return newsListData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);

    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_news, null);
        TextView newsContent = view.findViewById(R.id.tv_news_content);
        TextView newsTime = view.findViewById(R.id.tv_news_time);
        ImageView newImage = view.findViewById(R.id.img_news);
        newsContent.setText(newsListData.get(position).getMusicTitle());
        String time = mContext.getResources().getString(R.string.news_time_source,newsListData.get(position).getMusicArtist(),newsListData.get(position).getMusicTime());
        newsTime.setText(time);
        ImageUtil.loadImage(newsListData.get(position).getMusicImage() ,newImage);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }
}
