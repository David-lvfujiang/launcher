package com.fenda.settings.activity;

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
import com.fenda.common.router.RouterPath;
import com.fenda.common.util.ImageUtils;
import com.fenda.common.util.LogUtil;
import com.fenda.settings.R;
import com.fenda.settings.bean.SettingsContractsInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/8/31 15:35
 */
@Route(path = RouterPath.SETTINGS.SettingsDeviceContractsActivity)
public class SettingsDeviceContractsActivity extends BaseMvpActivity{
    private static final String TAG = "SettingsDeviceContractsActivity";

    RecyclerView recyclerView;
    TextView cancel;
    private ImageView addContacts;

    private MyContractAdapter mAadapter;
    private List<SettingsContractsInfoBean> mContractList;
//    private Uri mUri = Uri.parse(ContentProviderManager.BASE_URI + "/user");
    @Override
    protected void initPresenter() {

    }

    @Override
    public int onBindLayout() {
        return R.layout.settings_device_cantracts_layout;
    }

    @Override
    public void initView() {
        recyclerView = findViewById(R.id.bind_contacts_listview);
        cancel = findViewById(R.id.bind_contacts_back_iv);
        addContacts = findViewById(R.id.add_contacts_tv);

        mContractList = new ArrayList<>();
//        mContractList = ContentProviderManager.getInstance(getApplicationContext(), mUri).queryUser(null, null);

        addContacts.bringToFront();


        //设置为垂直5列
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL));
        //设置默认动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAadapter = new MyContractAdapter(getLayoutInflater(), mContractList);
        recyclerView.setAdapter(mAadapter);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void showErrorTip(String msg) {

    }

    public class MyContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater inflater;
        List<SettingsContractsInfoBean> list;

        public MyContractAdapter(LayoutInflater inflater, List<SettingsContractsInfoBean> list) {
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
            final SettingsContractsInfoBean ConstactsListBean = mContractList.get(position);
            final ViewHolder holder1= (ViewHolder) holder;
            if(ConstactsListBean != null){

                String iconPath = ConstactsListBean.getIcon();
                ImageUtils.loadImg(getApplicationContext(), holder1.contractIcon, iconPath);

                if(position == 0){
                    holder1.contractName.setText(ConstactsListBean.getUserName() + "(管理员)");
                } else {
                    holder1.contractName.setText(ConstactsListBean.getUserName());
                }

                holder1.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String clickedName = (String) holder1.contractName.getText();
                        LogUtil.d(TAG, "clicked contract name  = " + clickedName + " = " + ConstactsListBean.getUserName());
                        LogUtil.d(TAG, "clicked contract icon = " + ConstactsListBean.getIcon());
//                        Intent ContractInfoIntent = new Intent(SettingsDeviceContractsActivity.this, ChangeContractsNicknameAvtivity.class);
//                        ContractInfoIntent.putExtra("ContractName", clickedName);
//                        startActivity(ContractInfoIntent);
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
