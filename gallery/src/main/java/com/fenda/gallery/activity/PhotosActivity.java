package com.fenda.gallery.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.util.DensityUtil;
import com.fenda.common.util.ImageUtils;
import com.fenda.common.util.ToastUtils;
import com.fenda.gallery.R;
import com.fenda.gallery.adapter.CatalogAdapter;
import com.fenda.gallery.adapter.PhoneCameraAdapter;
import com.fenda.gallery.bean.PhoneCameraBean;
import com.fenda.gallery.contract.GalleryContract;
import com.fenda.gallery.http.Constant;
import com.fenda.gallery.http.FamilyPhotoResponse;
import com.fenda.gallery.http.UploadPhotoRequest;
import com.fenda.gallery.model.GalleryModel;
import com.fenda.gallery.presenter.GalleryPresenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PhotosActivity extends BaseMvpActivity<GalleryPresenter, GalleryModel> implements GalleryContract.View, View.OnClickListener {

    private ImageView back;
    private TextView title;
    private ImageView ivSend;
    private RecyclerView rcPhonePic;
    private RelativeLayout rela;

    public static final int PERMISSION_REQ = 0x123456;
    private final String[] IMAGE_PROJECT = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.SIZE,
    };

    private String[] mPermission = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE


    };
    private List<String> mRequestPermission = new ArrayList<String>();

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    private ArrayList<PhoneCameraBean> photoAllInfoList;
    private HashMap<String, ArrayList<PhoneCameraBean>> cameraBeanMap;
    private ArrayList<String> catalogList;
    private PhoneCameraAdapter adapter;
    private List<String> selectPath;
    private int position;
    private PopupWindow window;

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public int onBindLayout() {
        return R.layout.activity_photo;
    }

    @Override
    public void initView() {
        back = findViewById(R.id.img_back);
        title = findViewById(R.id.tv_title);
        ivSend = findViewById(R.id.iv_send);
        rcPhonePic = findViewById(R.id.rc_phone_pic);
        rela = findViewById(R.id.rela);
    }

    @Override
    public void initListener() {
        super.initListener();
        back.setOnClickListener(this);
        title.setOnClickListener(this);
        ivSend.setOnClickListener(this);
    }

    @Override
    public void initData() {
        photoAllInfoList = new ArrayList<>();
        cameraBeanMap = new HashMap<>();
        selectPath = new ArrayList<>();
        catalogList = new ArrayList<>();
        adapter = new PhoneCameraAdapter(new ArrayList<PhoneCameraBean>(), this);
        rcPhonePic.setHasFixedSize(true);
        rcPhonePic.setItemAnimator(new DefaultItemAnimator());
        rcPhonePic.setLayoutManager(new GridLayoutManager(this, 4));
        rcPhonePic.setItemAnimator(new DefaultItemAnimator());
        rcPhonePic.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String one : mPermission) {
                if (PackageManager.PERMISSION_GRANTED != this.checkPermission(one, Process.myPid(), Process.myUid())) {
                    mRequestPermission.add(one);
                }
            }
            if (!mRequestPermission.isEmpty()) {
                this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
            } else {
                initPhoto();
            }
        } else {

            initPhoto();
        }
    }

    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_send) {
            //上传图片
            List<String> mList = adapter.getSelectList();
            if (mList != null && mList.isEmpty()) {
                ToastUtils.show(R.string.gallery_select_photo);
                return;
            }
            List<MultipartBody.Part> partList = new ArrayList<>();
            for (String path : mList) {
                File mFile = new File(path);
                File pathFile = new File(Constant.PHOTO.DirectoryPath + mFile.getName());
                if (pathFile.exists()) {
                    //如果已经压缩直接添加压缩后的图片
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), pathFile);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("photos", pathFile.getName(), requestBody);
                    partList.add(part);
                } else {
                    int size = (int) (mFile.length() / 1024);
                    if (size > 200) {
                        //大于200KB压缩图片
                        try {
                            File comFile = ImageUtils.compressImage(mFile, 640, 480, Bitmap.CompressFormat.JPEG, 100, mFile.getName());
                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), comFile);
                            MultipartBody.Part part = MultipartBody.Part.createFormData("photos", comFile.getName(), requestBody);
                            partList.add(part);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), mFile);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("photos", mFile.getName(), requestBody);
                        partList.add(part);
                    }
                }
            }
            UploadPhotoRequest request = new UploadPhotoRequest();
            request.setRequestBody(partList);
            mPresenter.uploadPhoto(request);

        } else if (resId == R.id.img_back) {
            finish();

        } else if (resId == R.id.tv_title) {
            if (window == null || !window.isShowing()) {
                showCatalog();
            } else {
                window.dismiss();
                window = null;
            }
        }

    }


    private void initPhoto() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                CursorLoader cursorLoader = new CursorLoader(PhotosActivity.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECT, null, null, IMAGE_PROJECT[2] + " DESC");
                Cursor data = cursorLoader.loadInBackground();
                if (data != null) {
                    int count = data.getCount();
                    data.moveToFirst();
                    if (count > 0) {
                        do {
                            String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECT[0]));
                            String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECT[1]));
                            long time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECT[2]));
                            int size = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECT[4]));
                            PhoneCameraBean photoInfo = new PhoneCameraBean();
                            photoInfo.setPhotos(path);
                            photoInfo.setId(name);
                            photoInfo.setTime(time);
                            photoInfo.setSize(size);
                            photoInfo.setSelectStatus(1);
                            photoAllInfoList.add(photoInfo);
                            int lastIndex = path.lastIndexOf("/");
                            if (lastIndex != -1) {
                                String catalog;
                                if (lastIndex == 0) {
                                    catalog = "/";
                                } else {
                                    int secondLast = path.lastIndexOf("/", lastIndex - 1);
                                    catalog = path.substring(secondLast + 1, lastIndex);
                                }
                                if (cameraBeanMap.containsKey(catalog)) {
                                    cameraBeanMap.get(catalog).add(photoInfo);
                                } else {
                                    ArrayList<PhoneCameraBean> mItem = new ArrayList<>();
                                    mItem.add(photoInfo);
                                    cameraBeanMap.put(catalog, mItem);
                                    catalogList.add(catalog);
                                }
                            }
                        } while (data.moveToNext());
                    }
                    adapter.setNewData(photoAllInfoList);
                    adapter.notifyDataSetChanged();
                    data.close();
                    catalogList.add(0, "全部图片");
                    cameraBeanMap.put("全部图片", photoAllInfoList);
                }


            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // 版本兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (requestCode == PERMISSION_REQ) {
            for (int i = 0; i < grantResults.length; i++) {
                for (String one : mPermission) {
                    if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mRequestPermission.remove(one);
                    }
                }
            }
            initPhoto();
        }

    }

    private void showCatalog() {
        View mView = getLayoutInflater().inflate(R.layout.popup_catalog, null);
        int screenHeight = DensityUtil.getScreenHeight(this);

        int popupHeight = (int) (screenHeight * 0.4);

        window = new PopupWindow(mView, RecyclerView.LayoutParams.MATCH_PARENT, popupHeight);
        RecyclerView rcCatalog = mView.findViewById(R.id.rc_catalog);
        rcCatalog.setLayoutManager(new LinearLayoutManager(this));
        rcCatalog.setItemAnimator(new DefaultItemAnimator());
        rcCatalog.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        CatalogAdapter catalogAdapter = new CatalogAdapter(catalogList, this, position, cameraBeanMap);
        rcCatalog.setAdapter(catalogAdapter);
        catalogAdapter.setOnClickCatalogListener(new CatalogAdapter.onClickCatalogListener() {
            @Override
            public void catalogListener(String catalog, int position) {
                PhotosActivity.this.position = position;
                ArrayList<PhoneCameraBean> cameraBeans = cameraBeanMap.get(catalog);
                adapter.setNewData(cameraBeans);
                title.setText(catalog);
                if (window != null) {
                    window.dismiss();
                    window = null;
                }
            }
        });
        //设置window背景
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.setFocusable(true);
        window.showAsDropDown(rela);

    }


    @Override
    public void getFamilyPhotoSuccess(BaseResponse<FamilyPhotoResponse> response) {

    }

    @Override
    public void deleteFamilyPhotoSuccess(BaseResponse response) {

    }

    @Override
    public void uploadPhotoSuccess(BaseResponse request) {
        ToastUtils.show(R.string.gallery_upload_success);
        setResult(GalleryCategoryActivity.FAMILY_UPLOAD_RESULT);
        this.finish();
    }

    @Override
    public void getFamilyPhotoFailure(BaseResponse<FamilyPhotoResponse> response) {

    }

    @Override
    public void deleteFamilyPhotoFailure(BaseResponse response) {

    }

    @Override
    public void uploadPhotoFailure(BaseResponse response) {

    }

    @Override
    public void showErrorTip(String msg) {

    }


//    @Override
//    public void getFamilyPhotoSuccess(BaseResponse<FamilyPhotoResponse> response) {
//
//    }
//
//    @Override
//    public void deleteFamilyPhotoSuccess(BaseResponse response) {
//
//    }
//
//    @Override
//    public void uploadPhotoSuccess(BaseResponse request) {
//        ToastUtils.show(R.string.upload_success);
//        setResult(Constant.FAMILY_UPLOAD_RESULT);
//        this.finish();
//    }
//
//    @Override
//    public void getFamilyPhotoFailure(BaseResponse<FamilyPhotoResponse> response) {
//
//    }
//
//    @Override
//    public void deleteFamilyPhotoFailure(BaseResponse response) {
//
//    }
//
//    @Override
//    public void uploadPhotoFailure(BaseResponse response) {
//        if (response.getCode() == 613) {
//            ToastUtils.show("上传照片超过设置数量");
//        }
//    }
}
