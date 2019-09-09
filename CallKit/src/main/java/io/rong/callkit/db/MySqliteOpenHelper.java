package io.rong.callkit.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "fenda_call.db";

    public static final String DB_TABE= "call_recorder";

    private static MySqliteOpenHelper helper;

    public static MySqliteOpenHelper getInstance(Context mContext) {
        if (helper == null) {
            helper = new MySqliteOpenHelper(mContext, DB_NAME, null, 1);
        }
        return helper;
    }


    private MySqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String userSql = "create table " + DB_TABE + " ( _id integer primary key autoincrement,phone varchar(30),icon varchar(100),name varchar(30),time integer,callType integer)";
        String[] sqls = {userSql};
        for (String sql : sqls) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
