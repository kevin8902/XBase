package com.hemaapp.xaar.http;

import android.content.Context;
import android.os.Environment;


import com.hemaapp.xaar.XConfig;
import com.hemaapp.xaar.util.XLogUtil;
import com.hemaapp.xaar.util.XMD5Utils;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 10:38
 * 名称:HttpFileCache
 * 注释:联网缓存类
 *******************************/
public class HttpFileCache {

    private DiskLruCache mDiskLruCache;
    private long cacheSize;
    private boolean isCreated;
    private File cacheFile;

    public HttpFileCache(Context context) {
        cacheFile = getDiskCacheDir(context);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        //判断设置存储空间大小和可用存储空间大小
        if (XConfig.httpCacheSize >= getUsableSpzce(cacheFile)) {
            cacheSize = XConfig.httpCacheSize;
        } else {
            //若可用空间大小不足,则为可用空间大小的1/4
            cacheSize = getUsableSpzce(cacheFile) / 4;
        }
        try {
            mDiskLruCache = DiskLruCache.open(cacheFile, XConfig.AppVersionCode, 1, cacheSize);
            isCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
            isCreated = false;
            XLogUtil.e("http缓存文件夹error" + e.getMessage());
        }


    }


    //从本地缓存中加载文件
    public String loadFromCache(String url, long time) {
        String result = null;
        if (mDiskLruCache != null && isCreated) {
            String key = XMD5Utils.hashKeyFromUrl(url);
            try {
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                InputStream is = snapshot.getInputStream(0);
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);
                if (time == 0) {
                    result = getString(br);
                } else {
                    long outOfTime = Long.parseLong(br.readLine());
                    if (System.currentTimeMillis() <= outOfTime) {
                        result = getString(br);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    //将数据缓存到文件
    public void saveToCache(String url, String json, long time) {
        if (mDiskLruCache == null || !isCreated) {
            return;
        }
        String key = XMD5Utils.hashKeyFromUrl(url);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            if (editor != null) {
                if (writeFile(key, json, time)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            XLogUtil.e("缓存http文件到文件夹error:" + e.getMessage());
        }
    }

    //br转化为字符串
    private String getString(BufferedReader br) throws IOException {
        String len = null;
        StringWriter sw = new StringWriter();
        while ((len = br.readLine()) != null) {
            sw.write(len);
        }
        return sw.toString();
    }


    //将字符串写到文件
    private boolean writeFile(String key, String json, long time) {
        BufferedWriter bw = null;
        FileWriter fileWriter = null;
        File file = new File(cacheFile, key);
        try {
            fileWriter = new FileWriter(file);
            bw = new BufferedWriter(fileWriter);
            if (time != 0) {
                bw.write((System.currentTimeMillis() + time) + "");
                bw.newLine();
            }
            bw.write(json);
            bw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    //获取本地缓存路径
    private File getDiskCacheDir(Context context) {
        //判断是否有sd卡
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + XConfig.CacheName + File.separator + "httpCache");
    }

    //获取可用空间大小
    private long getUsableSpzce(File path) {
        return path.getUsableSpace();
    }



}
