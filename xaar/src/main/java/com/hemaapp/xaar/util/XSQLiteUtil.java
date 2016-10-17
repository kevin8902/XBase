package com.hemaapp.xaar.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.hemaapp.xaar.XConfig;
import com.hemaapp.xaar.db.BaseSQLiteOpenHelper;
import com.hemaapp.xaar.download.DownloadInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 邢佩凯 on 2016/2/17.
 * 数据库的工具类
 */
public class XSQLiteUtil {

    /**
     * 创建数据库
     *
     * @param name       数据库名
     * @param tableNames 所以表名的数组
     * @param classes    所有实体类的数组
     */
    public static void createDB(Context context, String name, String[] tableNames, Class<? extends Object>[] classes, BaseSQLiteOpenHelper.onSqliteUpdateListener listener) {

        XConfig.DATABASENAME = name;
        if (XConfig.TABLES.size() > 0)
            XConfig.TABLES.clear();
        for (int i = 0; i < tableNames.length; i++) {
            XConfig.TABLES.add(tableNames[i]);
        }
        //创建文件下载的数据库
        XConfig.TABLES.add("download");
        if (XConfig.mClasses.size() > 0)
            XConfig.mClasses.clear();

        for (int i = 0; i < classes.length; i++) {
            XConfig.mClasses.add(classes[i]);
        }
        XConfig.mClasses.add(DownloadInfo.class);
        BaseSQLiteOpenHelper helper = new BaseSQLiteOpenHelper(context.getApplicationContext());
        helper.setOnSqliteUpdateListener(listener);
        SQLiteDatabase db = helper.getReadableDatabase();
        db.close();
    }


    /**
     * 像数据库中添加一条信息
     *
     * @param object    实体类
     * @param tableName 表名
     * @param helper    和表名相关的数据库帮助类继承SQLiteOpenHelper
     */
    public static void insert(Object object, String tableName,
                              SQLiteOpenHelper helper) {
        ContentValues values = getContentValues(object);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }


    /**
     * 数据库中插入多条数据
     */

    public static void inserMore(List<Object> objects, String tableName,
                                 SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values;
        Object o;
        for (int i = 0; i < objects.size(); i++) {
            o = objects.get(i);
            values = getContentValues(o);
            db.insert(tableName, null, values);
        }
        db.close();

    }


    /**
     * 更新操作
     *
     * @param object
     * @param tableName
     * @param helper
     * @param condition
     * @param v         修改where condition=v
     */

    public static void upDate(Object object, String tableName,
                              SQLiteOpenHelper helper, String condition, String v) {
        ContentValues values = getContentValues(object);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, values, condition + "=?", new String[]{v});
        db.close();
    }

    /**
     * 更新操作
     *
     * @param object
     * @param tableName
     * @param helper
     * @param condition
     * @param v         修改多条件
     */

    public static void upDate(Object object, String tableName,
                              SQLiteOpenHelper helper, String[] condition, String[] v) {
        String item = "";
        for (int i = 0; i < condition.length; i++) {
            item = item + condition[i] + "=? and ";
        }
        item = item.substring(0, item.length() - 4);
        ContentValues values = getContentValues(object);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.update(tableName, values, item, v);
        db.close();
    }

    /**
     * 查询数据库 返回集合
     *
     * @param tableName 表名
     * @param helper
     * @return
     */

    public static ArrayList<Object> Query(Class<? extends Object> c, String tableName,
                                          SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        try {
            ArrayList<Object> list = new ArrayList<Object>();
            Field[] fields = c.getDeclaredFields();
            Object object2 = null;
            while (cursor.moveToNext()) {
                object2 = getObject(cursor, c, fields, object2);
                list.add(object2);
            }
            cursor.close();
            db.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 根据条件查询数据库 返回集合
     *
     * @param tableName
     * @param helper
     * @param condition
     * @param v         where condition=v
     * @return
     */

    public static ArrayList<Object> QueryByV(Class<? extends Object> c, String tableName,
                                             SQLiteOpenHelper helper, String condition, String v) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, condition + "=?", new String[]{v}, null, null, null);
        try {
            ArrayList<Object> list = new ArrayList<Object>();
            Field[] fields = c.getDeclaredFields();
            Object object2 = null;
            while (cursor.moveToNext()) {
                object2 = getObject(cursor, c, fields, object2);
                list.add(object2);
            }
            cursor.close();
            db.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Object> QueryByV(Class<? extends Object> c, String tableName,
                                             SQLiteOpenHelper helper, String[] condition, String[] v) {
        String item = "";
        for (int i = 0; i < condition.length; i++) {
            item = item + condition[i] + "=? and ";
        }
        item = item.substring(0, item.length() - 4);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, item, v, null, null, null);
        try {
            ArrayList<Object> list = new ArrayList<Object>();
            Field[] fields = c.getDeclaredFields();
            Object object2 = null;
            while (cursor.moveToNext()) {
                object2 = getObject(cursor, c, fields, object2);
                list.add(object2);
            }
            cursor.close();
            db.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除符合条件的信息
     *
     * @param tableName
     * @param helper
     * @param condition
     * @param v
     */
    public static void delete(String tableName,
                              SQLiteOpenHelper helper, String condition, String v) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(tableName, condition + "=?", new String[]{v});
        db.close();
    }


    public static void delete(String tableName,
                              SQLiteOpenHelper helper, String[] condition, String[] v) {
        String item = "";
        for (int i = 0; i < condition.length; i++) {
            item = item + condition[i] + "=? and ";
        }
        item = item.substring(0, item.length() - 4);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(tableName, item, v);
        db.close();
    }


    public static void delete(String tableName, SQLiteOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();
    }


    /**
     * 返回contentvalues
     *
     * @param object
     * @return
     */

    private static ContentValues getContentValues(Object object) {
        Class<? extends Object> c = object.getClass();
        // 获取所有的属性
        Field[] fields = c.getDeclaredFields();
        ContentValues values = new ContentValues();
        Object value = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.toString().contains("private")) {
                // 获取属性名
                String name = field.getName();
                // 第一个字母变为大写
                char ch = ToBig(name);
                // 将剩余的字母转化为新字符串
                String leave = name.substring(1, name.length());
                String path = "get" + ch + leave;
                try {
                    // 根据path获得指定的方法
                    Method m = c.getDeclaredMethod(path);
                    // 获取字段类型
                    String type = field.getGenericType().toString();
                    if (type.equals("class java.lang.String")) {
                        value = (String) m.invoke(object);
                        values.put(name, (String) value);
                    } else if (type.equals("int")) {
                        value = (Integer) m.invoke(object);
                        values.put(name, (Integer) value);
                    } else if (type.equals("long")) {
                        value = (Long) m.invoke(object);
                        values.put(name, (Long) value);
                    } else if (type.equals("boolean")) {
                        // 存储到数据库中 false为0,true为1
                        value = (Boolean) m.invoke(object);
                        values.put(name, (Boolean) value);
                    } else if (type.equals("float")) {
                        value = (Float) m.invoke(object);
                        values.put(name, (Float) value);
                    } else if (type.equals("double")) {
                        value = (Double) m.invoke(object);
                        values.put(name, (Double) value);
                    } else if (type.equals("java.util.list<?>")) {

                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }
        return values;
    }


    /**
     * 获取一个附好值的实体类对象
     *
     * @param cursor
     * @param c
     * @param fields
     * @param object2
     * @return
     */
    private static Object getObject(Cursor cursor, Class<? extends Object> c,
                                    Field[] fields, Object object2) {
        try {
            // 构建一个新对象,将获得的值放进去,如果用object 则会所有条目相同
            object2 = c.newInstance();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if (field.toString().contains("private")) {
                    String name = field.getName();
                    // 第一个字母变为大写
                    char ch = ToBig(name);
                    // 将剩余的字母转化为新字符串
                    String leave = name.substring(1, name.length());
                    String path = "set" + ch + leave;
                    // 根据path获得指定的方法
                    Method m = c.getDeclaredMethod(path, field.getType());
                    String type = field.getGenericType().toString();
                    if (type.equals("class java.lang.String")) {
                        m.invoke(object2,
                                cursor.getString(cursor.getColumnIndex(name)));
                    } else if (type.equals("int")) {
                        m.invoke(object2,
                                cursor.getInt(cursor.getColumnIndex(name)));
                    } else if (type.equals("long")) {
                        m.invoke(object2,
                                cursor.getLong(cursor.getColumnIndex(name)));
                    } else if (type.equals("boolean")) {
                        // 存储到数据库中 false为0,true为1
                        if (cursor.getString(cursor.getColumnIndex(name))
                                .equals("0")) {
                            m.invoke(object2, false);
                        } else if (cursor
                                .getString(cursor.getColumnIndex(name)).equals(
                                        "1")) {
                            m.invoke(object2, true);
                        }
                    } else if (type.equals("float")) {
                        m.invoke(object2,
                                cursor.getFloat(cursor.getColumnIndex(name)));
                    } else if (type.equals("double")) {
                        m.invoke(object2,
                                cursor.getDouble(cursor.getColumnIndex(name)));
                    } else if (type.equals("java.util.list<?>")) {
                        XLogUtil.e("集合");
                        continue;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object2;
    }


    /**
     * 把第一个字母变为大写
     *
     * @param name
     * @return
     */
    private static char ToBig(String name) {
        char ch = name.charAt(0);
        ch = Character.toUpperCase(ch);
        return ch;
    }


}
