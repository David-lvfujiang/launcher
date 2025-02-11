package com.fenda.common.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.fenda.common.bean.CallRecoderBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CALL_RECODER_BEAN".
*/
public class CallRecoderBeanDao extends AbstractDao<CallRecoderBean, Long> {

    public static final String TABLENAME = "CALL_RECODER_BEAN";

    /**
     * Properties of entity CallRecoderBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property _id = new Property(0, Long.class, "_id", true, "_id");
        public final static Property Count = new Property(1, int.class, "count", false, "COUNT");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Phone = new Property(3, String.class, "phone", false, "PHONE");
        public final static Property Icon = new Property(4, String.class, "icon", false, "ICON");
        public final static Property UserId = new Property(5, String.class, "userId", false, "USER_ID");
        public final static Property CallTime = new Property(6, long.class, "callTime", false, "CALL_TIME");
        public final static Property CommunicateTime = new Property(7, long.class, "communicateTime", false, "COMMUNICATE_TIME");
        public final static Property CallType = new Property(8, int.class, "callType", false, "CALL_TYPE");
        public final static Property CallStatus = new Property(9, int.class, "callStatus", false, "CALL_STATUS");
        public final static Property CallMode = new Property(10, int.class, "callMode", false, "CALL_MODE");
    }


    public CallRecoderBeanDao(DaoConfig config) {
        super(config);
    }
    
    public CallRecoderBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CALL_RECODER_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: _id
                "\"COUNT\" INTEGER NOT NULL ," + // 1: count
                "\"NAME\" TEXT," + // 2: name
                "\"PHONE\" TEXT," + // 3: phone
                "\"ICON\" TEXT," + // 4: icon
                "\"USER_ID\" TEXT," + // 5: userId
                "\"CALL_TIME\" INTEGER NOT NULL ," + // 6: callTime
                "\"COMMUNICATE_TIME\" INTEGER NOT NULL ," + // 7: communicateTime
                "\"CALL_TYPE\" INTEGER NOT NULL ," + // 8: callType
                "\"CALL_STATUS\" INTEGER NOT NULL ," + // 9: callStatus
                "\"CALL_MODE\" INTEGER NOT NULL );"); // 10: callMode
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CALL_RECODER_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CallRecoderBean entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getCount());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(5, icon);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(6, userId);
        }
        stmt.bindLong(7, entity.getCallTime());
        stmt.bindLong(8, entity.getCommunicateTime());
        stmt.bindLong(9, entity.getCallType());
        stmt.bindLong(10, entity.getCallStatus());
        stmt.bindLong(11, entity.getCallMode());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CallRecoderBean entity) {
        stmt.clearBindings();
 
        Long _id = entity.get_id();
        if (_id != null) {
            stmt.bindLong(1, _id);
        }
        stmt.bindLong(2, entity.getCount());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(5, icon);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(6, userId);
        }
        stmt.bindLong(7, entity.getCallTime());
        stmt.bindLong(8, entity.getCommunicateTime());
        stmt.bindLong(9, entity.getCallType());
        stmt.bindLong(10, entity.getCallStatus());
        stmt.bindLong(11, entity.getCallMode());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CallRecoderBean readEntity(Cursor cursor, int offset) {
        CallRecoderBean entity = new CallRecoderBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // _id
            cursor.getInt(offset + 1), // count
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // phone
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // icon
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // userId
            cursor.getLong(offset + 6), // callTime
            cursor.getLong(offset + 7), // communicateTime
            cursor.getInt(offset + 8), // callType
            cursor.getInt(offset + 9), // callStatus
            cursor.getInt(offset + 10) // callMode
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CallRecoderBean entity, int offset) {
        entity.set_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCount(cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIcon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCallTime(cursor.getLong(offset + 6));
        entity.setCommunicateTime(cursor.getLong(offset + 7));
        entity.setCallType(cursor.getInt(offset + 8));
        entity.setCallStatus(cursor.getInt(offset + 9));
        entity.setCallMode(cursor.getInt(offset + 10));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CallRecoderBean entity, long rowId) {
        entity.set_id(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CallRecoderBean entity) {
        if(entity != null) {
            return entity.get_id();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CallRecoderBean entity) {
        return entity.get_id() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
