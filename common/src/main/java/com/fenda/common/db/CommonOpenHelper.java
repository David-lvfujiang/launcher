package com.fenda.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommonOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "fenda_ai.db";

    private static final String DB_TABE_USER = "user";

    private static CommonOpenHelper helper;

    public static CommonOpenHelper getInstance(Context mContext) {
        if (helper == null) {
            helper = new CommonOpenHelper(mContext, DB_NAME, null, 1);
        }
        return helper;
    }


    private CommonOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userSql = "create table " + DB_TABE_USER + " ( _id integer primary key autoincrement,userId varchar(30),icon varchar(100),name varchar(30),phone varchar(30),email varchar(200),user_type integer,register_time varchar(100),update_time varchar(100))";
        String[] sqls = {userSql};
        for (String sql : sqls) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
