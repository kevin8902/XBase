package com.hemaapp.xaar.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by 邢佩凯 on 2016/2/3.
 * 判断网络情况的工具类
 */
public class XConnectUtil {


    /**判断当前是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context == null)
            return false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null)
            return info.isAvailable();
        return false;
    }


    /**判断当前网络是否为wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context == null || !isNetWorkConnected(context))
            return false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI)
            return true;
        return false;
    }


    /**判断当前网络是否为手机网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context == null || !isNetWorkConnected(context))
            return false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE)
            return true;
        return false;
    }


    /**打开wifi设置界面
     *
     * @param activity
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        activity.startActivity(intent);
    }


}
