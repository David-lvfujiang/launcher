package io.rong.callkit.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.rong.callkit.bean.UserInfoBean;

public class ContentProviderManager {

    public static final String BASE_URI = "content://com.fenda.ai";
    private Context mContext;
    private Uri mUri;
    private ContentResolver mContentResolver;

    private ContentProviderManager(Context mContext, Uri mUri) {
        this.mContext = mContext;
        this.mUri = mUri;
        mContentResolver = mContext.getContentResolver();
    }

    private static volatile ContentProviderManager mInstance;

    public static ContentProviderManager getInstance(Context context, Uri uri) {
        if (mInstance == null) {
            synchronized (ContentProviderManager.class) {
                if (mInstance == null) {
                    mInstance = new ContentProviderManager(context, uri);
                }
            }
        }
        return mInstance;
    }

    /**
     * 插入单个联系人
     */
    public void insertUser(UserInfoBean bean) {
        if (!isExist(bean.getUserId())) {
            ContentValues values = new ContentValues();
            values.put("userId", bean.getUserId());
            values.put("icon", bean.getIcon());
            values.put("name", bean.getUserName());
            values.put("phone", bean.getMobile());
            values.put("email", bean.getEmail());
            values.put("user_type", bean.getUserType());
            values.put("register_time", getDate());
            values.put("update_time", getDate());
            mContentResolver.insert(mUri, values);
        }
    }

    /**
     * 批量插入联系人
     */
    public void insertUsers(List<UserInfoBean> users) {
        if (users != null && !users.isEmpty()) {
            for (UserInfoBean user : users) {
                insertUser(user);
            }
        }
    }

    public boolean isExist(String userId) {
        boolean result = false;
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(mUri, null, "userId = ? ", new String[]{userId}, null);
        if (cursor != null && cursor.moveToNext()) {
            result = true;
        }
        return result;
    }

    public UserInfoBean queryUserById(String id) {
        Cursor cursor = mContentResolver.query(mUri, null, "phone = ?", new String[]{id}, null);
        UserInfoBean userInfoBean = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String userId = cursor.getString(cursor.getColumnIndex("userId"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                int userType = cursor.getInt(cursor.getColumnIndex("user_type"));
                String registerTime = cursor.getString(cursor.getColumnIndex("register_time"));
                String updateTime = cursor.getString(cursor.getColumnIndex("update_time"));
                userInfoBean = new UserInfoBean(_id, userId, name, phone, userType, icon, email, registerTime, updateTime);
            }

        }
        return userInfoBean;
    }

    public List<UserInfoBean> queryUser(String selection, String[] selectionArgs) {
        Cursor cursor = mContentResolver.query(mUri, null, selection, selectionArgs, null);
        List<UserInfoBean> beanList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String userId = cursor.getString(cursor.getColumnIndex("userId"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                int userType = cursor.getInt(cursor.getColumnIndex("user_type"));
                String registerTime = cursor.getString(cursor.getColumnIndex("register_time"));
                String updateTime = cursor.getString(cursor.getColumnIndex("update_time"));
                UserInfoBean bean = new UserInfoBean(id, userId, name, phone, userType, icon, email, registerTime, updateTime);
                beanList.add(bean);
            }

        }
        return beanList;
    }


    public int updateUser(UserInfoBean bean) {
        ContentValues values = new ContentValues();
        values.put("userId", bean.getUserId());
        values.put("name", bean.getUserName());
        values.put("phone", bean.getMobile());
        values.put("icon", bean.getIcon());
        values.put("email", bean.getEmail());
        values.put("user_type", bean.getUserType());
        values.put("register_time", bean.getRegisterTime());
        values.put("update_time", getDate());
        return mContentResolver.update(mUri, values, "userId = ? ", new String[]{bean.getUserId()});
    }


    public int deleteUser(String userId) {
        return mContentResolver.delete(mUri, "userId = ? ? ", new String[]{userId});

    }

    public int clear() {
        return mContentResolver.delete(mUri, null, null);

    }

    private String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date());
        return date;
    }

    public void registerContentObserver(Uri uri, Handler handler) {
        mContentResolver.registerContentObserver(uri, false, new ContentObserver(handler) {
                    @Override
                    public boolean deliverSelfNotifications() {
                        return super.deliverSelfNotifications();
                    }

                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                    }

                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                    }
                }
        );
    }
}