package com.hemaapp.xaar.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 13:37
 * 名称:BaseThreaPoolManager
 * 注释:线程池管理者
 *******************************/
public class BaseThreaPoolManager {

    private static BaseThreaPoolManager poolManager;
    private ExecutorService httpServerce;
    private ThreadPoolExecutor imageServerce;
    private LinkedBlockingQueue<Runnable> mImageQueue;

    public static BaseThreaPoolManager getPoolManager() {
        if (poolManager == null) {
            synchronized (BaseThreaPoolManager.class) {
                if (poolManager == null) {
                    poolManager = new BaseThreaPoolManager();
                }
            }
        }
        return poolManager;
    }

    private BaseThreaPoolManager() {

    }


    //创建联网的线程池
    public ExecutorService createHttpPool() {
        if (httpServerce == null) {
            httpServerce = Executors.newFixedThreadPool(1);
        }
        return httpServerce;
    }

    //创建加载图片的线程池
    public ThreadPoolExecutor createImagePool() {
        if (imageServerce == null) {
            int coreSize = Runtime.getRuntime().availableProcessors() * 2;
            mImageQueue = new LinkedBlockingQueue<>();
            imageServerce = new ThreadPoolExecutor(coreSize, coreSize, 0, TimeUnit.MILLISECONDS, mImageQueue);
        }
        return imageServerce;
    }

    //清除图片的加载队列
    public void clearImageQueue() {
        if (mImageQueue != null && mImageQueue.size() > 0) {
            mImageQueue.clear();
        }
    }


}
