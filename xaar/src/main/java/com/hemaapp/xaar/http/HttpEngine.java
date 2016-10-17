package com.hemaapp.xaar.http;

import android.content.Context;

import com.hemaapp.xaar.manager.BaseThreaPoolManager;


/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 10:33
 * 名称:HttpEngine
 * 注释:联网的主类
 *******************************/
public class HttpEngine {
    private static HttpEngine mEngine;
    private Context mContext;
    private HttpFileCache fileCache;//本地缓存
    private MainHandler mainHandler;


    public static HttpEngine getEngine(Context mContext) {
        if (mEngine == null) {
            synchronized (HttpEngine.class) {
                if (mEngine == null) {
                    mEngine = new HttpEngine(mContext.getApplicationContext());
                }
            }
        }
        return mEngine;
    }

    //设为私有的 防止外面直接new
    private HttpEngine(Context mContext) {
        this.mContext = mContext;
        fileCache = new HttpFileCache(mContext);
        mainHandler = new MainHandler();
    }

    //主要方法
    public void doPost(HttpTask task, HttpExecuteListener listener) {
        BaseThreaPoolManager.getPoolManager().createHttpPool().execute(new HttpRunable(task, fileCache, listener, mContext,mainHandler));
    }


}
