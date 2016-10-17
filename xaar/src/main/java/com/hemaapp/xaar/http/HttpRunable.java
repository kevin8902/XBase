package com.hemaapp.xaar.http;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.hemaapp.xaar.XConfig;
import com.hemaapp.xaar.util.XConnectUtil;
import com.hemaapp.xaar.util.XLogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 13:42
 * 名称:HttpRunable
 * 注释:联网任务
 *******************************/
public class HttpRunable implements Runnable {


    private HttpTask task;
    private HttpFileCache fileCache;
    private HttpExecuteListener listener;
    private Handler mHandler;
    private Context mContext;


    public HttpRunable(HttpTask task, HttpFileCache fileCache, HttpExecuteListener listener, Context context, MainHandler handler) {
        this.task = task;
        this.fileCache = fileCache;
        this.listener = listener;
        this.mContext = context;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        before();
        BaseResult baseResult = null;
        //判断是否需要缓存在本地
        if (task.isNeedCache()) {
            String result = fileCache.loadFromCache(task.getHttpInformation().getUrlPath(), task.getCacheTime());
            if (!TextUtils.isEmpty(result)) {
                XLogUtil.i("http缓存获取数据:" + task.getHttpInformation().getDesc(), result);
                try {
                    baseResult = task.parse(new JSONObject(result));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (baseResult != null) {
            Message message = mHandler.obtainMessage();
            HttpBean bean = new HttpBean(baseResult, task, listener);
            message.what = XConfig.HTTP_SUCCESS;
            message.obj = bean;
            mHandler.sendMessage(message);
            return;
        }

        if (XConnectUtil.isNetWorkConnected(mContext)) {
            doTask();
        } else {
            error("网络连接错误");
        }
    }


    private void doTask() {
        String result = null;
        BaseResult baseResult = null;

        if (task.getFiles() != null) {
            result = HttpEvent.doTaskWithFile(task);
        } else {
            result = HttpEvent.doTaskNoFile(task);
        }

        if (TextUtils.isEmpty(result)) {
            error(null);
        } else {
            XLogUtil.i("http网络获取数据:" + task.getHttpInformation().getDesc(), result);
            if (task.isNeedCache()) {
                fileCache.saveToCache(task.getHttpInformation().getUrlPath(), result, task.getCacheTime());
            }
            try {
                baseResult = task.parse(new JSONObject(result));
            } catch (JSONException e) {
                e.printStackTrace();
                XLogUtil.e("string转化为jsonobject error=" + e.getMessage());
            }
            if (baseResult != null) {
                if (baseResult.isSucess()) {
                    Message message = mHandler.obtainMessage();
                    HttpBean bean = new HttpBean(baseResult, task, listener);
                    message.what = XConfig.HTTP_SUCCESS;
                    message.obj = bean;
                    mHandler.sendMessage(message);
                } else {
                    Message message = mHandler.obtainMessage();
                    HttpBean bean = new HttpBean(baseResult, task, listener);
                    message.what = XConfig.HTTP_FAILED;
                    message.obj = bean;
                    mHandler.sendMessage(message);
                }
            } else {
                error("baseresult==null");
            }
        }
    }


    private void before() {
        Message message = mHandler.obtainMessage();
        HttpBean bean = new HttpBean(null, task, listener);
        message.what = XConfig.HTTP_BEFORE;
        message.obj = bean;
        mHandler.sendMessage(message);
    }


    private void error(String msg) {
        if (!TextUtils.isEmpty(msg))
            task.setError_msg(msg);
        Message message = mHandler.obtainMessage();
        HttpBean bean = new HttpBean(null, task, listener);
        message.what = XConfig.HTTP_ERROR;
        message.obj = bean;
        mHandler.sendMessage(message);
    }


}
