package io.rong.callkit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.rong.callkit.bean.CallRecoder;

public class SqliteManager {
    //对SqlitHelper的引用
    private MySqliteOpenHelper sqlitHelper = null;
    //数据库具体操作类，其中封装insert、update delete、query等操作
    private SQLiteDatabase sqLiteDatabase = null;
    private Context context;

    //上下文对象，在外部初始化此类时传入的Context，保证在是在同一环境下的操作    Context context;
    //构造方法
    public SqliteManager(Context context) {
        this.context = context;
        this.sqlitHelper = MySqliteOpenHelper.getInstance(context);
    }

    /**
     * 打开数据库链接
     */
    public void openWriteConnection() {
        this.sqLiteDatabase = sqlitHelper.getWritableDatabase();
    }

    public void openReadConnection() {
        this.sqLiteDatabase = sqlitHelper.getWritableDatabase();
    }

    /**
     * 关闭数据库链接
     */
    public void releaseConnection() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    /**
     * 插入数据方法
     *
     * @param table（必填）
     * @param nullColumnHack
     * @param values         要插入的ContentValues键值对：参考android API 文档）     * @return 插入成功与否的标志
     */
    public boolean insert(String table, String nullColumnHack, ContentValues values) {
        boolean flag = false;
        openWriteConnection();
        long count = sqLiteDatabase.insert(table, null, values);
        flag = (count > 0 ? true : false);
        releaseConnection();
        return flag;
    }

    /**
     * 删除记录
     *
     * @param table       表名（必填）
     * @param whereClause 条件（如：where id=？）
     * @param whereArgs   条件值（如：new String[]{1}）对应where条件
     * @return 插入成功与否的标志
     */
    public boolean delete(String table, String whereClause, String[] whereArgs) {
        boolean flag = false;
        openWriteConnection();
        int count = sqLiteDatabase.delete(table, whereClause, whereArgs);
        flag = (count > 0 ? true : false);
        releaseConnection();
        return flag;
    }

    /**
     * 更新表数据
     *
     * @param table       表名（必填）
     * @param values      所要更改的ContentValues键值对（参考android API 文档）
     * @param whereClause 条件（如：where id=？
     * @param whereArgs   条件值（如：new String[]{1}）对应where条件
     * @return 插入成功与否的标志
     */
    public boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        boolean flag = false;
        openWriteConnection();
        int count = sqLiteDatabase.update(table, values, whereClause, whereArgs);
        flag = (count > 0 ? true : false);
        releaseConnection();
        return flag;
    }

    /**
     * 查找，返回所有记录的集合，可通过多种条件查找
     *
     * @param table             表名（必填）
     * @param columns           列名（可为null）
     * @param selection         所选字段（可为null）
     * @param selectionArgs     字段值（可为null）
     * @param groupBy           分组（可为null）
     * @param having            （可为null）
     * @param orderBy（排序可为null）
     * @return 返回所查询条件所满足的记录集合
     */
    public List<CallRecoder> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<CallRecoder> list = new ArrayList<CallRecoder>();
        openReadConnection();
        Cursor cursor = sqLiteDatabase.query(table, null, selection, selectionArgs, groupBy, having, orderBy);
        while (cursor.moveToNext()) {
            CallRecoder recoder = new CallRecoder();
            recoder.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            recoder.setName(cursor.getString(cursor.getColumnIndex("name")));
            recoder.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            recoder.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            recoder.setCallType(cursor.getInt(cursor.getColumnIndex("callType")));
            list.add(recoder);
        }
        releaseConnection();
        return list;
    }

    public boolean isRecorderExist(String table, String id) {
        boolean flag = false;
        openReadConnection();
        Cursor cursor = sqLiteDatabase.query(table, null, "phone = ?", new String[]{id}, null, null, null);
        while (cursor.moveToNext()) {
            flag = true;
        }
        releaseConnection();
        return flag;
    }

    /**
     * 参数简化版查找，除了表名，其它参数至null
     * 查找，返回所有记录的集合
     *
     * @param table
     * @return 返回所查询条件所满足的记录集合
     */
    public List<CallRecoder> query(String table) {
        List<CallRecoder> list = new ArrayList<CallRecoder>();
        openReadConnection();
        Cursor cursor = sqLiteDatabase.query(table, null, null, null, null, null, "_id desc");
        while (cursor.moveToNext()) {
            CallRecoder recoder = new CallRecoder();
            recoder.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            recoder.setName(cursor.getString(cursor.getColumnIndex("name")));
            recoder.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            recoder.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            recoder.setCallType(cursor.getInt(cursor.getColumnIndex("callType")));
            list.add(recoder);
        }
        releaseConnection();
        return list;
    }


}
