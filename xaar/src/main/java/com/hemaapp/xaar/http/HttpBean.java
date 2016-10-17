package com.hemaapp.xaar.http;

/**
 * Created by 邢佩凯 on 2016/2/16.
 */
public class HttpBean {
    private BaseResult baseResult;
    private HttpTask task;
    private HttpExecuteListener listener;

    public HttpBean(BaseResult baseResult, HttpTask task, HttpExecuteListener listener) {
        this.baseResult = baseResult;
        this.task = task;
        this.listener = listener;
    }

    public BaseResult getBaseResult() {
        return baseResult;
    }


    public HttpTask getTask() {
        return task;
    }


    public HttpExecuteListener getListener() {
        return listener;
    }
}
