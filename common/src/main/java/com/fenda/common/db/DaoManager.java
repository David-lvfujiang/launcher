package com.fenda.common.db.manager;

import android.content.Context;

import com.fenda.common.gen.DaoMaster;
import com.fenda.common.gen.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;


/**
 * Created by 梁想想 on 2017/12/7.
 */

public class DaoManager {

    private static final String DB_NAME = "R03.db";
    private static final String TAG = DaoManager.class.getSimpleName();

    private static volatile DaoManager daoManager;  //多线程 使用单例模式
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper helper;
    private Context context;


    /*
     * 使用单例模式 保证数据库的安全
     * */
    public static DaoManager getInstance() {
        DaoManager instance = null;
        if (daoManager == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager();
                }
                daoManager = instance;
            }
        }
        return daoManager;
    }

    /*
     * 初始化daomanager
     * */

    public void init(Context context) {
        this.context = context;
    }


    /**
     * 判断是否存在数据库，如果没有则创建数据库
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 完成对数据库的添加 修改 查询 的操作 仅仅是一个接口
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    /*
     * 是否打印 数据库的日志信息 默认不开启
     * */
    public void setDebug(Boolean isDebug) {
        if (isDebug) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        } else {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }

    }

    /*
     * 关闭 help
     * */
    public void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }


    /*
     * 关闭 会话
     * */

    public void closeSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }

    /*
     *   关闭所有的操作 数据库用完的时候必须关闭 节省资源
     */
    public void closeConnection() {
        closeHelper();
        closeSession();
    }


}
