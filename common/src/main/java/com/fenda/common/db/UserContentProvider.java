package com.fenda.common.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class UserContentProvider extends ContentProvider {
    private final static String DB_USER = "user";


    private CommonOpenHelper helper;
    private SQLiteDatabase db;
    private UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int CODE_USER = 0;

    //主机名
    private static final String authority = "com.fenda.ai";
    private static final Uri BASE_URI = Uri.parse("content://"+authority);


    //添加该主机名的匹配规则
    {
        matcher.addURI(authority,"user",CODE_USER);
    }


    //对外提供的URI
    public interface URI{
        Uri USER_CONTENT_URI = Uri.parse(BASE_URI+"/user");

    }

    /**
     * 获取表名
     * @param uri
     * @return
     */
    private String getTableName(Uri uri){
        String tableName = "";
        switch (matcher.match(uri)){
            case CODE_USER:
                tableName = DB_USER;
                break;
        }
        return tableName;
    }

    @Override
    public boolean onCreate() {
        helper = CommonOpenHelper.getInstance(getContext());
        db = helper.getWritableDatabase();
        return false;
    }

    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)){
            throw new IllegalArgumentException("非法Uri:"+uri);
        }
        Cursor cursor = db.query(tableName,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),BASE_URI);
        return cursor;
    }

    @Override
    public String getType( Uri uri) {
        return null;
    }

    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)){
            throw new IllegalArgumentException("非法Uri:"+uri);
        }
        long rowId = db.insert(tableName,null,values);
        if (rowId == -1){
            return null;
        }else {
            //发送内容更新广播
            getContext().getContentResolver().notifyChange(BASE_URI,null);
            return ContentUris.withAppendedId(uri,rowId);
        }
    }

    @Override
    public int delete( Uri uri,  String selection,  String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)){
            throw new IllegalArgumentException("非法Uri:"+uri);
        }
        int number = db.delete(tableName,selection,selectionArgs);
        //发送内容更新广播
        getContext().getContentResolver().notifyChange(BASE_URI,null);

        return number;
    }

    @Override
    public int update( Uri uri,  ContentValues values,  String selection,  String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)){
            throw new IllegalArgumentException("非法Uri:"+uri);
        }
        int number = db.update(tableName,values,selection,selectionArgs);
        //发送内容更新广播
        getContext().getContentResolver().notifyChange(BASE_URI,null);
        return number;
    }

}
