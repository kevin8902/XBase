package com.hemaapp.xaar.http;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by 邢佩凯 on 2016/2/17.
 * 任务
 */
public class TaskInfo {
    protected Context mContext;

    public TaskInfo(Context context) {
        this.mContext = context;
    }

    protected HttpTask createTask(HttpInformation httpInformation, HashMap<String, String> params, Class<? extends Object> c) {
        HttpInformation information = httpInformation;
        HttpTask task = new HttpTask(information, params, c);
        return task;

    }

    protected HttpTask createTask(HttpInformation httpInformation, HashMap<String, String> params, Class<? extends Object> c, long cacheTime) {
        HttpInformation information = httpInformation;
        HttpTask task = new HttpTask(information, params, c, cacheTime);
        return task;

    }

    protected HttpTask createTask(HttpInformation httpInformation, HashMap<String, String> params, HashMap<String, String> files, Class<? extends Object> c) {
        HttpInformation information = httpInformation;
        HttpTask task = new HttpTask(information, params, files, c);
        return task;
    }


}
