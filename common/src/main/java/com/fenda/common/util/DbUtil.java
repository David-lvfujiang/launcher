package com.fenda.common.util;

import android.content.Context;

import com.fenda.common.bean.CallRecoderBean;
import com.fenda.common.gen.CallRecoderBeanDao;

import java.util.List;


/**
 * Created by 梁想想 on 2017/12/7.
 */

public class DbUtil {
    private com.fenda.common.db.manager.DaoManager daoManager;


    /*
     * 构造方法 初始化daoManager
     * */
    private DbUtil(Context context) {

        daoManager = com.fenda.common.db.manager.DaoManager.getInstance();
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
     *
     * 根据id 修改名字
     * */
    public boolean updateNameByUserId(String name, String userId) {
        boolean flag = false;
        try {
            List<CallRecoderBean> callRecoderBeans = daoManager.getDaoSession().queryBuilder(CallRecoderBean.class).where(CallRecoderBeanDao.Properties.UserId.eq(userId)).list();
            for (CallRecoderBean bean : callRecoderBeans) {
                bean.setName(name);
            }
            daoManager.getDaoSession().getCallRecoderBeanDao().updateInTx(callRecoderBeans);
            flag = true;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return flag;
    }
    /*
     *
     * 根据id 修改头像
     * */
    public boolean updateIconByUserId(String icon, String userId) {
        boolean flag = false;
        try {
            List<CallRecoderBean> callRecoderBeans = daoManager.getDaoSession().queryBuilder(CallRecoderBean.class).where(CallRecoderBeanDao.Properties.UserId.eq(userId)).list();
            for (CallRecoderBean bean : callRecoderBeans) {
                bean.setIcon(icon);
            }
            daoManager.getDaoSession().getCallRecoderBeanDao().updateInTx(callRecoderBeans);
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
     * 根据userId删除
     *  */

    public boolean deleteCallRecoderByUserId(String userId) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().queryBuilder(CallRecoderBean.class).where(CallRecoderBeanDao.Properties.UserId.eq(userId)).buildDelete().executeDeleteWithoutDetachingEntities();
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
        return daoManager.getDaoSession().queryBuilder(CallRecoderBean.class).orderDesc(CallRecoderBeanDao.Properties._id).list();
    }
}
