package com.hemaapp.xaar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hemaapp.xaar.XConfig;

import java.lang.reflect.Field;

/**
 * Created by 邢佩凯 on 2016/2/17.
 * 创建数据库
 */
public class BaseSQLiteOpenHelper extends SQLiteOpenHelper {
    public BaseSQLiteOpenHelper(Context context) {
        super(context, XConfig.DATABASENAME, null, XConfig.DbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //System.out.println(BaseConfig.mClasses.size());
        for (int i = 0; i < XConfig.mClasses.size(); i++) {
            String sql = "";
            Class<? extends Object> c = XConfig.mClasses.get(i);
            // 获取所有的属性
            Field[] fields = c.getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                // 获取属性名
                if (field.toString().contains("private")) {
                    String name = field.getName();
                    sql = sql + name + " text,";
                }

            }
            sql = sql.substring(0, sql.length() - 1);
            sql = "create table " + XConfig.TABLES.get(i) + " (_id integer primary key autoincrement," + sql + ")";
            db.execSQL(sql);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (listener != null) {
            listener.onSqliteUpdate(db, oldVersion, newVersion);
        }
    }


    private onSqliteUpdateListener listener;

    public void setOnSqliteUpdateListener(onSqliteUpdateListener listener) {
        this.listener = listener;
    }

    public interface onSqliteUpdateListener {
        void onSqliteUpdate(SQLiteDatabase db, int oldVersion, int newVersion);
    }


}
