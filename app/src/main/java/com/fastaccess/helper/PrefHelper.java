package com.fastaccess.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.App;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by kosh20111 on 10/7/2015.
 */
public class PrefHelper {

    private static Context context;

    public static void init(@NonNull Context context) {
        PrefHelper.context = context.getApplicationContext();
    }

    /**
     * @param key
     *         ( the Key to used to retrieve this data later  )
     * @param value
     *         ( any kind of primitive values  )
     *         <p/>
     *         non can be null!!!
     */
    public static void set(@NonNull String key, @NonNull Object value) {
        if (InputHelper.isEmpty(key) || InputHelper.isEmpty(value)) {
            throw new NullPointerException("Key || Value must not be null! (key = " + key + "), (value = " + value + ")");
        }
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (float) value);
        } else {
            edit.putString(key, App.gson().toJson(value));
        }
        edit.apply();
    }

    @Nullable public static <T> T getJsonObject(@NonNull String key, @NonNull Class<T> type) {
        String value = getString(key);
        if (!InputHelper.isEmpty(value)) {
            return App.gson().fromJson(value, type);
        }
        return null;
    }

    @Nullable public static <T> List<T> getJsonArray(@NonNull String key, final @NonNull Class<T[]> type) {
        String value = getString(key);
        if (!InputHelper.isEmpty(value)) {
            return Arrays.asList(App.gson().fromJson(value, type));
        }
        return null;
    }

    @Nullable public static String getString(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }

    public static boolean getBoolean(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    public static int getInt(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

    public static long getLong(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, 0);
    }

    public static float getFloat(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, 0);
    }

    public static void clearKey(@NonNull String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply();
    }

    public static boolean isExist(@NonNull String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void clearPrefs() {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

    public static Map<String, ?> getAll() {
        return PreferenceManager.getDefaultSharedPreferences(context).getAll();
    }
}
