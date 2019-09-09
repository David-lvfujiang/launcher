package com.fenda.settings.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fenda.common.base.BaseMvpActivity;
import com.fenda.common.base.BaseResponse;
import com.fenda.common.bean.UserInfoBean;
import com.fenda.common.db.ContentProviderManager;
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ImageUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.common.util.ToastUtils;
import com.fenda.protocol.http.RetrofitHelper;
import com.fenda.protocol.http.RxSchedulers;
import com.fenda.settings.R;
import com.fenda.settings.bean.SettingsContractsInfoBean;
import com.fenda.settings.constant.SettingsContant;
import com.fenda.settings.contract.SettingsContract;
import com.fenda.settings.http.SettingsApiService;
import com.fenda.settings.model.SettingsModel;
import com.fenda.settings.model.response.SettingsGetContractListResponse;
import com.fenda.settings.model.response.SettingsQueryDeviceInfoResponse;
import com.fenda.settings.model.response.SettingsRegisterDeviceResponse;
import com.fenda.settings.presenter.SettingsPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:35
 */
@Route(path = RouterPath.SETTINGS.SettingsDeviceContractsActivity)
public class SettingsDeviceContractsActivity extends BaseMvpActivity {
    private static final String TAG = "SettingsDeviceContractsActivity";

    private RecyclerView rvContrants;
    private TextView tvCancel;
    private ImageView ivAddContacts;

    private MyContractAdapter mMyContractAdapter;
    private List<UserInfoBean> mContractList;
    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_device_cantracts_layout;
    }

    @Override
    public void initView() {
        rvContrants = findViewById(R.id.bind_contacts_listview);
        tvCancel = findViewById(R.id.bind_contacts_back_iv);
        ivAddContacts = findViewById(R.id.add_contacts_tv);

        mContractList = new ArrayList<>();
        ivAddContacts.bringToFront();
        mContractList = ContentProviderManager.getInstance(getApplicationContext(), mUri).queryUser(null, null);
        LogUtil.d(TAG, "getContactsListSuccess ContractList = " +mContractList);

        //设置为垂直5列
        rvContrants.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
        //设置默认动画
        rvContrants.setItemAnimator(new DefaultItemAnimator());
        mMyContractAdapter = new MyContractAdapter(getLayoutInflater(), mContractList);
        rvContrants.setAdapter(mMyContractAdapter);
    }

    @SuppressLint("CheckResult")
    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        ivAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(SettingsDeviceContractsActivity.this, SettingsDeviceAddContractsQRActivity.class);
                startActivity(addIntent);
                finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backBindInfoIntent = new Intent(SettingsDeviceContractsActivity.this, SettingsDeviceCenterActivity.class);
                startActivity(backBindInfoIntent);
                finish();
            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }

    public class MyContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater inflater;
        List<UserInfoBean> list;

        public MyContractAdapter(LayoutInflater inflater, List<UserInfoBean> list) {
            this.inflater = inflater;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.settings_device_bind_contracts_item_layout, parent,false);
            return new MyContractAdapter.ViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final UserInfoBean constactsListBean = mContractList.get(position);
            final ViewHolder holder1= (ViewHolder) holder;
            if(constactsListBean != null){

                String iconPath = constactsListBean.getIcon();
                ImageUtils.loadImg(getApplicationContext(), holder1.contractIcon, iconPath);

                if(position == 0){
                    holder1.contractName.setText(constactsListBean.getUserName() + "(管理员)");
                } else {
                    holder1.contractName.setText(constactsListBean.getUserName());
                }

                holder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String clickedName = (String) holder1.contractName.getText();
                        LogUtil.d(TAG, "clicked contract name  = " + clickedName + " = " + constactsListBean.getUserName());
                        LogUtil.d(TAG, "clicked contract icon = " + constactsListBean.getIcon());
                        Intent ContractInfoIntent = new Intent(SettingsDeviceContractsActivity.this, SettingsDeviceContractsNickNameActivity.class);
                        ContractInfoIntent.putExtra("ContractName", clickedName);
                        ContractInfoIntent.putExtra("ContractIcon", constactsListBean.getIcon());
                        startActivity(ContractInfoIntent);
                        finish();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            public TextView contractName;
            public ImageView contractIcon;

            public ViewHolder(View itemView) {
                super(itemView);
                contractName= itemView.findViewById(R.id.bind_contacts_name);
                contractIcon = itemView.findViewById(R.id.bind_contacts_icon_11);
            }
        }
    }
}
