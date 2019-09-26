package debug;

import android.content.Context;
import android.content.SharedPreferences;

import com.fenda.common.util.SPUtils;

import org.androidannotations.api.sharedpreferences.SharedPreferencesCompat;

/**
 * Created by  Android Studio.
 * Author :   aviva.jiangjing
 * Date:   2019/9/19 11:39
 */
public class SettingsDebugSPUtils {
    private static final String FILE_NAME = "fendasettingsdebug";
    private static final String KEY_SP_BIND_DEVICE = "key_sp_binddevice";
    private static final String KEY_SP_REGISTER_DEVICE= "key_sp_registerdeviceid";

    public static Boolean isBindedDevice(Context context){

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_SP_BIND_DEVICE, false);
    }

    public static void saveBindedDevice(Context context, boolean isBind){

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SP_BIND_DEVICE, isBind).apply();
    }

    public static Boolean isRegisterDevice(Context context){

        SharedPreferences sp1 = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp1.getBoolean(KEY_SP_REGISTER_DEVICE, false);
    }

    public static void saveRegisterDevice(Context context, boolean isRegister){
        SharedPreferences sp2 = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp2.edit().putBoolean(KEY_SP_REGISTER_DEVICE, isRegister).apply();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

}
