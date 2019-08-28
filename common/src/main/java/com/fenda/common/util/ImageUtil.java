package com.fenda.common.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.fenda.common.R;

import java.io.File;

/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  16:06
 * @Description 图片工具类
 */
public class ImageUtil {


    /**
     * 普通加载
     * @param url
     * @param imageView
     */
    public static void loadImage( String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆形图片
     * @param url
     * @param imageView
     */
    public static void loadCircleImage( String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆角图片
     * @param url
     * @param imageView
     */
    public static  void loadRoundImage( String url, ImageView imageView) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(20))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    /**
     * 加载图片并监听
     * @param url
     * @param imageView
     * @param listener
     */
    public static void loadImageWithResqust(String url,ImageView imageView,RequestListener listener){
        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .listener(listener);

    }
    /**
     * 加载GIF图片
     * @param resource
     * @param imageView
     */
    public static void loadGIFImage(int resource,ImageView imageView){
        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
                .asGif()
                .load(resource)
                .apply(options);



    }


    /**
     * 加载文件图片
     * @param file
     * @param imageView
     */
    public static void loadFileImage(File file, ImageView imageView){
        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
                .asFile()
                .load(file)
                .apply(options);



    }





}
