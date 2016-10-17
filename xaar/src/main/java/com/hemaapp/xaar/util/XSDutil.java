package com.hemaapp.xaar.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/22 13:53
 * 名称:XSDutil
 * 注释:关于存储的util
 *******************************/
public class XSDutil {

    public static String getCacheDir(Context context) {
        //判断是否有sd卡
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }
}
