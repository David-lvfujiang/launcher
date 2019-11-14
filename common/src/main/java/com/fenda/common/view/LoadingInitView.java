package com.fenda.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fenda.common.R;
import com.fenda.common.util.ImageUtil;


/**
 * Description: <LoadingView><br>
 * Author:      mxdl<br>
 * Date:        2019/3/25<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class LoadingInitView extends RelativeLayout {
    public LoadingInitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.common_view_init_loading, this);
        ImageView imgLoading = findViewById(R.id.img_init_loading);
        ImageUtil.loadGIFImage(R.mipmap.loading, imgLoading, R.mipmap.loading);
    }
}
