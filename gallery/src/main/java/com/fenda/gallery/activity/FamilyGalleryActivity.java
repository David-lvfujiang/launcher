package com.fenda.gallery.activity;

import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.util.DateUtils;
import com.fenda.common.util.FastClickUtils;
import com.fenda.common.util.GsonUtil;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.NetUtil;
import com.fenda.common.util.SPUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.adapter.FamilyPhotoAdapter;
import com.fenda.gallery.bean.DayPhoteInfoBean;
import com.fenda.gallery.bean.PhoneCameraBean;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.Constant;
import com.fenda.gallery.http.FamilyPhotoRequest;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.model.GalleryModel;
import com.fenda.gallery.presenter.GalleryPresenter;
import com.fenda.protocol.tcp.TCPConfig;
import com.fenda.protocol.tcp.bean.EventMessage;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kevin.wangzhiqiang
 * @Date 2019/9/3 10:49
 * @Description 相册列表页面
 */
public class FamilyGalleryActivity extends BaseMvpActivity<GalleryPresenter, GalleryModel> implements GalleryContract.View {

    private TextView mTvCancel;
    private TextView mTvTitle;
    private TextView mTvDelete;
    private ImageView mIvAdd;
    private RecyclerView mRcPhotos;
    private boolean isDelete = false;
    private FamilyPhotoAdapter mPhotoAdapter;
    private int mIndexPage = 1;
    private int mPageCount = 100;
    private List<String> imgIds;
    private List<DayPhoteInfoBean> mDatas;
    private Map<String, List<PhoneCameraBean>> mMap;

    @Override
    public int onBindLayout() {
        return R.layout.gallery_activity_family_gallery;
    }

    @Override
    public void initView() {
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvTitle = findViewById(R.id.tv_title);
        mTvDelete = findViewById(R.id.tv_delete);
        mIvAdd = findViewById(R.id.img_add);
        mRcPhotos = findViewById(R.id.rcPhotos);
        mDatas = new ArrayList<>();
        mPhotoAdapter = new FamilyPhotoAdapter(mContext, mDatas);
    }

    @Override
    public void initData() {
        imgIds = new ArrayList<>();
        mMap = new LinkedHashMap<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mPhotoAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        mRcPhotos.setLayoutManager(layoutManager);
        mRcPhotos.setItemAnimator(new DefaultItemAnimator());
        mRcPhotos.setHasFixedSize(true);
        mRcPhotos.setAdapter(mPhotoAdapter);
        if (NetUtil.checkNet()) {
            getNetFamilyGallery();
        } else {
            getLocalFamilyGallery();
        }

    }


    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initListener() {
        super.initListener();
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelete && !FastClickUtils.isFastClick()) {
                    sendDelete();
                }
            }
        });
        mTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelete) {
                    selectOnStatus();
                } else {
                    List<DayPhoteInfoBean> beans = mPhotoAdapter.getData();
                    if (beans.size() == 0) {
                        Toast.makeText(mContext, "暂无可删除数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    deleteStatus(beans);
                }


            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelete) {
                    List<DayPhoteInfoBean> beans = mPhotoAdapter.getData();
                    for (DayPhoteInfoBean bean : beans) {
                        for (PhoneCameraBean phoneCameraBean :
                                bean.getList()) {
                            phoneCameraBean.setSelectStatus(0);
                        }

                    }
                    cancelDeleteStatus();

                } else {
                    finish();
                }
            }
        });

        mPhotoAdapter.setOnItemClickListener(new FamilyPhotoAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(PhoneCameraBean bean) {
                if (imgIds.contains(bean.getId())) {
                    imgIds.remove(bean.getId());
                } else {
                    imgIds.add(bean.getId());
                }
                if (imgIds.size() > 0) {
                    mIvAdd.setVisibility(View.VISIBLE);
                } else {
                    mIvAdd.setVisibility(View.GONE);
                }
                mTvTitle.setText(getResources().getString(R.string.gallery_delete_size, String.valueOf(imgIds.size())));
            }
        });
    }

    /**
     * 发送删除请求
     */
    private void sendDelete() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < imgIds.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            builder.append(imgIds.get(i));
        }
        mPresenter.deleteFamilyPhoto(builder.toString());
    }

    /**
     * 进入删除状态
     *
     * @param beans
     */
    private void deleteStatus(List<DayPhoteInfoBean> beans) {
        for (DayPhoteInfoBean bean : beans) {
            for (PhoneCameraBean phoneCameraBean : bean.getList()) {
                phoneCameraBean.setSelectStatus(1);
            }
        }
        mTvCancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        Resources resources = getResources();
        mTvCancel.setText(resources.getString(R.string.gallery_family_cancel));
        mPhotoAdapter.notifyDataSetChanged();
        mTvTitle.setText(resources.getString(R.string.gallery_delete_size, "0"));
        mTvDelete.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        mTvDelete.setText("全选");
        mIvAdd.setVisibility(View.GONE);
        isDelete = true;
    }

    /**
     * 选中状态
     */
    private void selectOnStatus() {
        String text = mTvDelete.getText().toString();
        if ("全选".equals(text)) {
            List<DayPhoteInfoBean> beans = mPhotoAdapter.getData();
            for (DayPhoteInfoBean bean : beans) {
                for (PhoneCameraBean phoneCameraBean : bean.getList()) {
                    phoneCameraBean.setSelectStatus(2);
                    String id = phoneCameraBean.getId();
                    if (!imgIds.contains(id)) {
                        imgIds.add(id);
                    }
                }
            }
            mIvAdd.setVisibility(View.VISIBLE);
            mTvDelete.setText("全不选");
        } else if ("全不选".equals(text)) {
            List<DayPhoteInfoBean> beans = mPhotoAdapter.getData();
            for (DayPhoteInfoBean bean : beans) {
                for (PhoneCameraBean phoneCameraBean : bean.getList()) {
                    phoneCameraBean.setSelectStatus(1);
                    String id = phoneCameraBean.getId();
                    if (imgIds.contains(id)) {
                        imgIds.remove(id);
                    }
                }
            }
            mIvAdd.setVisibility(View.GONE);
            mTvDelete.setText("全选");
        }
        mTvTitle.setText(getResources().getString(R.string.gallery_delete_size, String.valueOf(imgIds.size())));
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 取消删除状态
     */
    private void cancelDeleteStatus() {
        Resources resources = getResources();
        mTvCancel.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.life_back, 0, 0, 0);
        mTvCancel.setText("");
        mPhotoAdapter.notifyDataSetChanged();
        mTvTitle.setText(resources.getString(R.string.gallery_family_gallery));
        mTvDelete.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.gallery_photo_delete, 0, 0, 0);
        mTvDelete.setText("");
        mIvAdd.setVisibility(View.GONE);
        if (imgIds.size() > 0) {
            imgIds.clear();
        }
        isDelete = false;
    }

    private void getLocalFamilyGallery() {
        String familyGalleryData = (String) SPUtils.get(mContext, Constant.PHOTO.FAMILY_GALLERY_INFO, "");
        if (!TextUtils.isEmpty(familyGalleryData)) {
            FamilyPhotoResponse familyPhotoRespons = GsonUtil.GsonToBean(familyGalleryData, FamilyPhotoResponse.class);
            handleFamilyGalleryData(familyPhotoRespons);

        }
    }

    private void getNetFamilyGallery() {
        FamilyPhotoRequest request = new FamilyPhotoRequest();
        request.setCurrentPage(mIndexPage);
        request.setPageSize(mPageCount);
        mPresenter.getFamilyPhoto(request);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveEvent(EventMessage message) {
        //相册变化通知
        if (message.getCode() == TCPConfig.MessageType.ALBUM_CHANGE) {
            LogUtils.i("收到相册变化通知");
            mIndexPage=1;
            cancelDeleteStatus();
            getNetFamilyGallery();
        }
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
        ToastUtils.show(response.getMessage());

    }

    @Override
    public void uploadPhotoFailure(BaseResponse response) {

    }


    @Override
    public void getFamilyPhotoSuccess(BaseResponse<FamilyPhotoResponse> response) {
        if (mIndexPage == 1) {
            mMap.clear();
        }
        FamilyPhotoResponse photoResponse = response.getData();
        // 处理家庭相册数据
        handleFamilyGalleryData(photoResponse);
        // 缓存数据到本地
        String familyPhotoInfo = GsonUtil.GsonString(photoResponse);
        SPUtils.put(mContext, Constant.PHOTO.FAMILY_GALLERY_INFO, familyPhotoInfo);
    }

    private void handleFamilyGalleryData(FamilyPhotoResponse photoResponse) {
        if (photoResponse != null) {
            List<PhoneCameraBean> beanList = photoResponse.getItems();
            for (PhoneCameraBean bean : beanList) {
                String uploadData = bean.getDateCreate();
                String formatData = DateUtils.dealDateFormat(uploadData);
                if (mMap.containsKey(formatData)) {
                    List<PhoneCameraBean> tempList = mMap.get(formatData);
                    tempList.add(bean);
                } else {
                    List<PhoneCameraBean> tempList = new ArrayList<>();
                    tempList.add(bean);
                    mMap.put(formatData, tempList);
                }
            }
            mDatas.clear();
            // 遍历map,重组数据
            for (String key : mMap.keySet()) {
                List<PhoneCameraBean> list = mMap.get(key);
                DayPhoteInfoBean bean = new DayPhoteInfoBean(key, "", list);
                mDatas.add(bean);
            }
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteFamilyPhotoSuccess(BaseResponse response) {
        ToastUtils.show("删除成功");
    }

    @Override
    public void showErrorTip(String msg) {

    }


}