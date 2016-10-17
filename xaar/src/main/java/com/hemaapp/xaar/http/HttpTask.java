package com.hemaapp.xaar.http;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 邢佩凯 on 2016/2/16.
 * 联网任务
 */
public class HttpTask {

    private HttpInformation httpInformation;
    private HashMap<String, String> params;//参数
    private HashMap<String, String> files;//文件
    private String error_msg;
    private Class<? extends Object> c;
    private boolean needCache = false;
    private long cacheTime = 0;


    public HttpTask(HttpInformation httpInformation, HashMap<String, String> params, Class<? extends Object> c) {
        this.httpInformation = httpInformation;
        this.params = params;
        this.c = c;
    }

    public HttpTask(HttpInformation httpInformation, HashMap<String, String> params, Class<? extends Object> c, long cacheTime) {
        this.httpInformation = httpInformation;
        this.params = params;
        this.c = c;
        this.needCache = true;
        this.cacheTime = cacheTime;
    }


    public HttpTask(HttpInformation httpInformation, HashMap<String, String> params, HashMap<String, String> files, Class<? extends Object> c) {
        this.httpInformation = httpInformation;
        this.params = params;
        this.files = files;
        this.c = c;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public boolean isNeedCache() {
        return needCache;
    }

    public HttpInformation getHttpInformation() {
        return httpInformation;
    }


    public HashMap<String, String> getParams() {
        return params;
    }


    public HashMap<String, String> getFiles() {
        return files;
    }

    //解析结果
    public BaseResult parse(JSONObject object) {
        return new BaseResult(object, c);
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }
}
