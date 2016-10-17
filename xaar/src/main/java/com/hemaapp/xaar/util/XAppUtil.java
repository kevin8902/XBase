package com.hemaapp.xaar.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 13:13
 * 名称:XAppUtil
 * 注释:和app相关的工具
 *******************************/
public class XAppUtil {

    //获取app版本号
    public static int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
