package com.fenda.gallery.activity;

import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.util.DateUtils;
import com.fenda.common.util.LogUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.adapter.FamilyPhotoAdapter;
import com.fenda.gallery.bean.DayPhoteInfoBean;
import com.fenda.gallery.bean.PhoneCameraBean;
import com.fenda.gallery.contract.GalleryContract;
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

public class FamilyGalleryActivity extends BaseMvpActivity<GalleryPresenter, GalleryModel> implements GalleryContract.View {

    private TextView tvCancel;
    private TextView tvTitle;
    private TextView tvDelete;
    private ImageView imgAdd;
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
        return R.layout.activity_family_gallery;
    }

    @Override
    public void initView() {
        tvCancel = findViewById(R.id.tv_cancel);
        tvTitle = findViewById(R.id.tv_title);
        tvDelete = findViewById(R.id.tv_delete);
        imgAdd = findViewById(R.id.img_add);
        mRcPhotos = findViewById(R.id.rcPhotos);
    }

    @Override
    public void initData() {
        imgIds = new ArrayList<>();
        mDatas = new ArrayList<>();
        mMap = new LinkedHashMap<>();
        mPhotoAdapter = new FamilyPhotoAdapter(mContext, mDatas);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(mPhotoAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        mRcPhotos.setLayoutManager(layoutManager);
        mRcPhotos.setItemAnimator(new DefaultItemAnimator());
        mRcPhotos.setHasFixedSize(true);
        mRcPhotos.setAdapter(mPhotoAdapter);
        getFamilyGallery();
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void initListener() {
        super.initListener();
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelete) {
                    sendDelete();
                }
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
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
        tvCancel.setOnClickListener(new View.OnClickListener() {
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
                    imgAdd.setVisibility(View.VISIBLE);
                } else {
                    imgAdd.setVisibility(View.GONE);
                }
                tvTitle.setText(getResources().getString(R.string.gallery_delete_size, String.valueOf(imgIds.size())));
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
        tvCancel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        Resources resources = getResources();
        tvCancel.setText(resources.getString(R.string.gallery_family_cancel));
        mPhotoAdapter.notifyDataSetChanged();
        tvTitle.setText(resources.getString(R.string.gallery_delete_size, "0"));
        tvDelete.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        tvDelete.setText("全选");
        imgAdd.setVisibility(View.GONE);
        isDelete = true;
    }

    /**
     * 选中状态
     */
    private void selectOnStatus() {
        String text = tvDelete.getText().toString();
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
            imgAdd.setVisibility(View.VISIBLE);
            tvDelete.setText("全不选");
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
            imgAdd.setVisibility(View.GONE);
            tvDelete.setText("全选");
        }
        tvTitle.setText(getResources().getString(R.string.gallery_delete_size, String.valueOf(imgIds.size())));
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 取消删除状态
     */
    private void cancelDeleteStatus() {
        Resources resources = getResources();
        tvCancel.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.life_back, 0, 0, 0);
        tvCancel.setText("");
        mPhotoAdapter.notifyDataSetChanged();
        tvTitle.setText(resources.getString(R.string.gallery_family_gallery));
        tvDelete.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.gallery_photo_delete, 0, 0, 0);
        tvDelete.setText("");
        imgAdd.setVisibility(View.GONE);
        if (imgIds.size() > 0) {
            imgIds.clear();
        }
        isDelete = false;
    }

    private void getFamilyGallery() {
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
            cancelDeleteStatus();
            getFamilyGallery();
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
        setResult(GalleryCategoryActivity.FAMILY_DELETE_RESULT);
        mIndexPage = 1;
        ToastUtils.show("删除成功");
        cancelDeleteStatus();
        getFamilyGallery();
    }

    @Override
    public void showErrorTip(String msg) {

    }


}