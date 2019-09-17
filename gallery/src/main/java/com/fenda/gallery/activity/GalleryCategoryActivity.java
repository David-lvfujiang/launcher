package com.fenda.gallery.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.ImageUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.NetUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.common.view.CustomRoundAngleImageView;
import com.fenda.gallery.R;
import com.fenda.gallery.bean.PhoneCameraBean;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.Constant;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.model.GalleryModel;
import com.fenda.gallery.presenter.GalleryPresenter;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.EventMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:49
 * @Description 相册分类页面
 */
@Route(path = RouterPath.Gallery.GALLERY_CATOGORY)
public class GalleryCategoryActivity extends BaseMvpActivity<GalleryPresenter, GalleryModel> implements GalleryContract.View, View.OnClickListener {
    private ImageView mIvCancel;
    private CustomRoundAngleImageView mIvLocalCategory;
    private TextView mTvLocalCount;
    private CustomRoundAngleImageView mIvServerCategory;
    private TextView mTvServerCount;
    private LinearLayout mLLocalLayout;
    private LinearLayout mLServerLayout;
    private int mIndexPage = 1;
    private int mPageCount = 1;
    public static final int FAMILY_UPLOAD_REQUEST = 100;
    public static final int FAMILY_DELETE_REQUEST = 200;
    public static final int FAMILY_UPLOAD_RESULT = 300;
    public static final int FAMILY_DELETE_NET_RESULT = 400;
    public static final int FAMILY_DELETE_LOCAL_RESULT = 500;

    private final String[] IMAGE_PROJECT = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
    };

    @Override
    public int onBindLayout() {
        return R.layout.gallery_activity_gallery_category;
    }

    @Override
    public void initView() {
        mIvCancel = findViewById(R.id.ivCancel);
        mTvLocalCount = findViewById(R.id.tvLocalCount);
        mTvServerCount = findViewById(R.id.tvServerCount);
        mIvLocalCategory = findViewById(R.id.ivLocalCategory);
        mIvServerCategory = findViewById(R.id.ivServerCategory);
        mLLocalLayout = findViewById(R.id.llLocalLayout);
        mLServerLayout = findViewById(R.id.llServerLayout);
    }

    @Override
    public void initData() {
        initLocalPhoto();
        if (NetUtil.checkNet()) {
            getNetGalleryInfo();
        } else {
            getLocalGalleryInfo();
        }
    }


    @Override
    public void initListener() {
        super.initListener();
        mIvCancel.setOnClickListener(this);
        mLLocalLayout.setOnClickListener(this);
        mLServerLayout.setOnClickListener(this);

    }

    private void getLocalGalleryInfo() {
        String galleryCatogoryInfo = (String) SPUtils.get(mContext, Constant.PHOTO.GALLERY_CATEGORY_INFO, "");
        if (!TextUtils.isEmpty(galleryCatogoryInfo)) {
            FamilyPhotoResponse familyPhotoResponse = GsonUtil.GsonToBean(galleryCatogoryInfo, FamilyPhotoResponse.class);
            handleGalleryCategoryData(familyPhotoResponse);
        } else {
            mIvServerCategory.setImageResource(0);
            mTvServerCount.setText(mContext.getString(R.string.gallery_photo_count_indicator, 0));
        }
    }

    private void getNetGalleryInfo() {
        FamilyPhotoRequest request = new FamilyPhotoRequest();
        request.setCurrentPage(mIndexPage);
        request.setPageSize(mPageCount);
        mPresenter.getFamilyPhoto(request);
    }

    private void initLocalPhoto() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CursorLoader cursorLoader = new CursorLoader(mContext, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECT, null, null, IMAGE_PROJECT[1] + " DESC");
                Cursor data = cursorLoader.loadInBackground();
                if (data != null) {
                    int count = data.getCount();
                    mTvLocalCount.setText(mContext.getString(R.string.gallery_photo_count_indicator, count));
                    data.moveToFirst();
                    if (count > 0) {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECT[0]));
                        ImageUtil.loadImg(mContext, mIvLocalCategory, path);
                    } else {
                        mIvLocalCategory.setImageResource(0);
                    }
                }
            }
        });
    }


    @Override
    public void getFamilyPhotoSuccess(BaseResponse<FamilyPhotoResponse> response) {
        FamilyPhotoResponse data = response.getData();
        handleGalleryCategoryData(data);
        // 缓存数据到本地
        String galleryCategoryInfo = GsonUtil.GsonString(data);
        SPUtils.put(mContext, Constant.PHOTO.GALLERY_CATEGORY_INFO, galleryCategoryInfo);
    }

    private void handleGalleryCategoryData(FamilyPhotoResponse data) {
        if (data == null) {
            return;
        }
        mTvServerCount.setText(mContext.getString(R.string.gallery_photo_count_indicator, data.getTotalNum()));
        List<PhoneCameraBean> list = data.getItems();
        if (list != null && !list.isEmpty()) {
            PhoneCameraBean phoneCameraBean = list.get(0);
            if (phoneCameraBean != null) {
                String url = phoneCameraBean.getThumbnail();
                ImageUtil.loadImg(mContext, mIvServerCategory, url);
            }

        } else {
            mIvServerCategory.setImageResource(0);
        }
    }

    @Override
    public void deleteFamilyPhotoSuccess(BaseResponse response) {

    }

    @Override
    public void uploadPhotoSuccess(BaseResponse request) {

    }

    @Override
    public void getFamilyPhotoFailure(BaseResponse<FamilyPhotoResponse> response) {
        ToastUtils.show(response.getMessage());
    }

    @Override
    public void deleteFamilyPhotoFailure(BaseResponse response) {

    }

    @Override
    public void uploadPhotoFailure(BaseResponse response) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage message) {
        //相册变化通知
        if (message.getCode() == TCPConfig.MessageType.ALBUM_CHANGE) {
            LogUtils.i("收到相册变化通知");
            getNetGalleryInfo();
        } else if (message.getCode() == FAMILY_DELETE_LOCAL_RESULT) {
            LogUtils.i("删除本地相册通知");
            initLocalPhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == FAMILY_DELETE_REQUEST && resultCode == FAMILY_DELETE_NET_RESULT)
                || (requestCode == FAMILY_UPLOAD_REQUEST && resultCode == FAMILY_UPLOAD_RESULT)) {
            getNetGalleryInfo();
        }
    }


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivCancel) {
            finish();
        } else if (id == R.id.llLocalLayout) {
            startActivityForResult(new Intent(mContext, PhotosActivity.class), FAMILY_UPLOAD_REQUEST);
        } else if (id == R.id.llServerLayout) {
            startActivityForResult(new Intent(mContext, FamilyGalleryActivity.class), FAMILY_DELETE_REQUEST);
        }
    }

    @Override
    public void showErrorTip(String msg) {

    }
}