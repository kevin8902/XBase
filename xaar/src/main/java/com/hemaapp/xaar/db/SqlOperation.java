package com.hemaapp.xaar.db;

import android.content.Context;


import com.hemaapp.xaar.util.XSQLiteUtil;

import java.util.List;

/**
 * Created by 邢佩凯 on 2016/2/17.
 * 操作数据库
 */
public class SqlOperation {

    private Object object;
    private Class<? extends Object> c;
    private BaseSQLiteOpenHelper helper;
    private Context context;
    private String tableName;


    public SqlOperation(Context context, Object object, String tableName) {
        this.context = context;
        this.object = object;
        this.tableName = tableName;
        helper = new BaseSQLiteOpenHelper(context);
    }

    public SqlOperation(Context context, Class<? extends Object> c, String tableName) {
        this.context = context;
        this.c = c;
        this.tableName = tableName;
        helper = new BaseSQLiteOpenHelper(context);
    }


    //判断是否存在数据
    public boolean isExist() {
        if (c == null)
            c = this.object.getClass();
        List<Object> mObjects = XSQLiteUtil.QueryByV(c, tableName, helper, "_id", "1");
        if (mObjects != null && mObjects.size() > 0)
            return true;
        else
            return false;
    }

    public void insertOne() {
        if (!isExist())
            insert();
        else
            update("_id", "1");
    }


    public void insertMore() {
        XSQLiteUtil.inserMore((List<Object>) object, tableName, helper);
    }


    /**
     * 插入一条数据
     */
    public void insert() {
        XSQLiteUtil.insert(object, tableName, helper);
    }

    /**
     * 更新数据
     *
     * @param condition 条件字段
     * @param v         条件值
     */
    public void update(String condition, String v) {
        XSQLiteUtil.upDate(object, tableName, helper, condition, v);
    }

    public void update(String[] condition, String[] v) {
        XSQLiteUtil.upDate(object, tableName, helper, condition, v);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<? extends Object> query() {
        return XSQLiteUtil.Query(c, tableName, helper);
    }


    /**
     * 按条件查询数据
     *
     * @param condition 条件字段
     * @param v         条件值
     * @return
     */
    public List<? extends Object> queryByV(String condition, String v) {
        return XSQLiteUtil.QueryByV(c, tableName, helper, condition, v);
    }

    public List<? extends Object> queryByV(String[] condition, String[] v) {
        return XSQLiteUtil.QueryByV(c, tableName, helper, condition, v);
    }

    /**
     * 删除所有数据
     */
    public void delete() {
        XSQLiteUtil.delete(tableName, helper);
    }

    /**
     * 删除某条数据
     *
     * @param condition 条件字段
     * @param v         条件值
     */
    public void deleteByV(String condition, String v) {
        XSQLiteUtil.delete(tableName, helper, condition, v);
    }

    public void deleteByV(String[] condition, String[] v) {
        XSQLiteUtil.delete(tableName, helper, condition, v);
    }


}
