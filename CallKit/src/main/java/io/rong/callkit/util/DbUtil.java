package io.rong.callkit.util;

import android.content.Context;

import com.fenda.common.BaseApplication;
import com.fenda.common.constant.Constant;
import com.fenda.common.db.ContentProviderManager;

import java.util.ArrayList;
import java.util.List;

import io.rong.callkit.bean.CallRecoderBean;
import io.rong.callkit.db.SqliteManager;
import io.rong.callkit.gen.CallRecoderBeanDao;
import io.rong.callkit.manager.DaoManager;


/**
 * Created by 梁想想 on 2017/12/7.
 */

public class DbUtil {
    private DaoManager daoManager;


    /*
     * 构造方法 初始化daoManager
     * */
    private DbUtil(Context context) {

        daoManager = DaoManager.getInstance();
        daoManager.init(context);
    }

    private static volatile DbUtil mInstance;

    public static DbUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DbUtil.class) {
                if (mInstance == null) {
                    mInstance = new DbUtil(context);
                }
            }
        }

        return mInstance;
    }


    /*
     * 插入一条数据对象
     * */
    public Boolean insertCallRecoder(CallRecoderBean callRecoderBean) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().insert(callRecoderBean);
            flag = true;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return flag;
    }

    /*
     * 插入多条数据对象
     * 可能会存在耗时 操作 所以new 一个线程
     * */
    public Boolean insertMultCallRecoder(final List<CallRecoderBean> callRecoderBeans) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (CallRecoderBean callRecoderBean : callRecoderBeans) {
                        daoManager.getDaoSession().insert(callRecoderBean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return flag;
    }


    /*
     * 修改一条数据
     * 根据id 修改
     * */
    public boolean updateCallRecoder(CallRecoderBean callRecoderBean) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().update(callRecoderBean);
            flag = true;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return flag;
    }

    /*
     * 删除一条数据
     * 根据_id 删除
     * */

    public boolean deleteCallRecoder(CallRecoderBean callRecoderBean) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().delete(callRecoderBean);
            flag = true;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return flag;
    }
    /*
     * 删除一条数据
     * 根据_id 删除
     * */

    public boolean deleteAllCallRecoder() {
        boolean flag = false;
        try {
            daoManager.getDaoSession().deleteAll(CallRecoderBean.class);
            flag = true;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return flag;
    }

    /*
     * 查询已存在联系人通话记录数据
     *
     *
     * */

    public List<CallRecoderBean> listAllCallRecoderByPhone() {
        List<CallRecoderBean> list = new ArrayList<>();
        List<CallRecoderBean> tempList = daoManager.getDaoSession().queryBuilder(CallRecoderBean.class).orderDesc(CallRecoderBeanDao.Properties._id).list();
        if (tempList != null && !tempList.isEmpty()) {
            for (CallRecoderBean callRecoderBean : tempList) {
                String phone = callRecoderBean.getPhone();
                if (ContentProviderManager.getInstance(BaseApplication.getContext(), Constant.Common.URI).isExistPhone(phone)) {
                    list.add(callRecoderBean);
                }
            }
        }
        return list;
    }
}
