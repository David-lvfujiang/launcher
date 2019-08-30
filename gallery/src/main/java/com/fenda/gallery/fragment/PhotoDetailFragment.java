package com.fenda.gallery.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.fenda.common.base.BaseFragment;
import com.fenda.common.util.ImageUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.bean.PhoneCameraBean;

import java.text.DecimalFormat;


public class PhotoDetailFragment extends BaseFragment {


    private ImageView imgDetail;

    @Override
    public int onBindLayout() {
        return R.layout.fragment_photo_detail;
    }

    @Override
    public void initView() {
        imgDetail = mRootView.findViewById(R.id.img_detail);

    }

    @Override
    public void initData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            PhoneCameraBean cameraBean = mBundle.getParcelable("bean");
            int style = mBundle.getInt("style");
            if (style == 1) {
                ImageUtils.loadImg(mCotext, imgDetail, cameraBean.getPhotos());
            } else {
                ImageUtils.loadImg(mCotext, imgDetail, cameraBean.getPhotos());
            }

        }
    }

    public static PhotoDetailFragment newInstance(PhoneCameraBean cameraBean, int style) {
        final PhotoDetailFragment detailFragment = new PhotoDetailFragment();
        Bundle mBunld = new Bundle();
        mBunld.putInt("style", style);
        mBunld.putParcelable("bean", cameraBean);
        detailFragment.setArguments(mBunld);
        return detailFragment;


    }


    /**
     * 得到bitmap的大小
     */
    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();
    }


    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
