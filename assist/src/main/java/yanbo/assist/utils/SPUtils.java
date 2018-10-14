package yanbo.assist.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 共享参数类
 */
public class SPUtils {

    private static SPUtils instance;
    private final SharedPreferences sp;

    private SPUtils(Context context) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SPUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtils.class) {
                if (instance == null) {
                    instance = new SPUtils(context);
                }
            }
        }
        return instance;
    }

    public SharedPreferences.Editor getEditor() {
        return sp.edit();
    }

    public void clear() {
        getEditor().clear().apply();
    }

    public <T> void save(String keyword, T value) {
        SharedPreferences.Editor editor = getEditor();
        if (value == null) {
            editor.remove(keyword).apply();
        } else if (value instanceof String) {
            editor.putString(keyword, (String) value).apply();
        } else if (value instanceof Integer) {
            editor.putInt(keyword, (Integer) value).apply();
        } else if (value instanceof Boolean) {
            editor.putBoolean(keyword, (Boolean) value).apply();
        } else if (value instanceof Long) {
            editor.putLong(keyword, (Long) value).apply();
        } else if (value instanceof Float) {
            editor.putFloat(keyword, (Float) value).apply();
        }
    }

    public <T> T get(String keyword, T defValue) {
        T value;
        if (defValue instanceof String) {
            String s = sp.getString(keyword, (String) defValue);
            value = (T) s;
        } else if (defValue instanceof Integer) {
            Integer i = sp.getInt(keyword, (Integer) defValue);
            value = (T) i;
        } else if (defValue instanceof Long) {
            Long l = sp.getLong(keyword, (Long) defValue);
            value = (T) l;
        } else if (defValue instanceof Float) {
            Float f = sp.getFloat(keyword, (Float) defValue);
            value = (T) f;
        } else if (defValue instanceof Boolean) {
            Boolean b = sp.getBoolean(keyword, (Boolean) defValue);
            value = (T) b;
        } else {
            value = defValue;
        }
        return value;
    }


}
