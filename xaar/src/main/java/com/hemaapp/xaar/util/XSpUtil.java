package com.hemaapp.xaar.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 邢佩凯 on 2016/2/16.
 * sharedpreference工具
 */
public class XSpUtil {

    public static final String NAME = "config";

    public static boolean getBoolean(Context context, String key, boolean defaultBoolean) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(NAME, context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultBoolean);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(NAME, context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(NAME, context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
