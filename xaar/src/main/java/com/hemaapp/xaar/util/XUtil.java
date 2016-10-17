package com.hemaapp.xaar.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 邢佩凯 on 2016/2/16.
 * 工具类
 */
public class XUtil {






    /**
     * 设置是否登录
     *
     * @param context
     * @param login   true or false
     */
    public static void HadLogin(Context context, boolean login) {
        XSpUtil.setBoolean(context, "isLogin", login);
    }

    /**
     * 判断当前是否登录
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        //false为默认未登录
        return XSpUtil.getBoolean(context, "isLogin", false);
    }


    /**
     * 保存用户名密码
     *
     * @param context  上下文
     * @param userName 用户名
     * @param passWord 密码
     */
    public static void saveUserAndPass(Context context, String userName, String passWord) {
        XSpUtil.setString(context, "userName", userName);
        XSpUtil.setString(context, "passWord", passWord);
    }

    /**
     * 获取用户名
     *
     * @param context
     * @return
     */
    public static String getUserName(Context context) {
        return XSpUtil.getString(context, "userName", null);
    }

    public static String getPassWord(Context context) {
        return XSpUtil.getString(context, "passWord", null);
    }

    /**
     * 是否自动登陆
     *
     * @param context
     * @return
     */
    public static boolean isAutoLogin(Context context) {
        return XSpUtil.getBoolean(context, "autoLogin", false);
    }

    public static void setAutoLogin(Context context, boolean autoLogin) {
        XSpUtil.setBoolean(context, "autoLogin", autoLogin);
    }


    /**
     * 是否第一次使用
     *
     * @param context
     * @return
     */
    public static boolean isFirst(Context context) {
        return XSpUtil.getBoolean(context, "first", true);
    }

    public static void setFirst(Context context, boolean first) {
        XSpUtil.setBoolean(context, "first", first);
    }





    /**
     * 将流转成字符串
     *
     * @param is 输入流
     * @return
     * @throws Exception
     */
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * 将文件转成字符串
     *
     * @param file 文件
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }


    /**
     * 获取版本名
     * 获取的为前3位
     *
     * @return
     */
    public static String getVersionName(Context applicationContext) {
        PackageManager manager = applicationContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(applicationContext.getPackageName(), 0);
            return info.versionName.substring(0, info.versionName.lastIndexOf("."));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            XLogUtil.e(e.getMessage());
            return null;
        }


    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getApplicationContext().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            XLogUtil.e(e.getMessage());
            return 0;
        }
    }

    //保存定位状态
    public static void saveLocationSuccess(Context context, boolean success) {
        XSpUtil.setBoolean(context, "location", success);
    }

    //获取定位状态
    public static boolean getLocationSuccess(Context context) {
        return XSpUtil.getBoolean(context, "location", false);
    }


    /**
     * 保存省市区及坐标
     */

    public static void saveLocation(Context context, String province, String city, String district, String lat, String lont) {
        XSpUtil.setString(context, "province", province);
        XSpUtil.setString(context, "city", city);
        XSpUtil.setString(context, "district", district);
        XSpUtil.setString(context, "lat", lat);
        XSpUtil.setString(context, "lont", lont);
    }



    /**
     * 推送存储的数据
     *
     * @param context
     * @param userId
     */
    public static void saveUserId(Context context, String userId) {
        XSpUtil.setString(context, "userId", userId);
    }

    public static void saveChannelId(Context context, String ChannelId) {
        XSpUtil.setString(context, "ChannelId", ChannelId);
    }

    public static String getUserId(Context context) {
        return XSpUtil.getString(context, "userId", "");

    }


    public static String getChannelId(Context context) {
        return XSpUtil.getString(context, "ChannelId", "");

    }


    //判断屏幕
    public static boolean isFullScreen(final Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isTranslucentStatus(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow().getAttributes().flags &
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    //判断是否有虚拟按键
    public static synchronized boolean HaveNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = context.getResources().getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    //底部虚拟键是否显示
    public static boolean bottomIsShow(Activity activity) {
        if (HaveNavigationBar(activity)) {
            Rect rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            if (rect.bottom < XDimensUtil.getDisplayHeight(activity)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }


    public static boolean isMobileNum(String phone) {
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }


    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }


    /**
     * 获取系统当前时间
     *
     * @param format 时间格式yyyy-MM-dd HH:mm:ss
     * @return String
     */
    public static String getCurrentTime(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                Locale.getDefault());
        return dateFormat.format(date);
    }


    //获取文件的大小
    public static String getFile(Context context, File file) {
        return android.text.format.Formatter.formatFileSize(context.getApplicationContext(), getFileSize(file));
    }

    public static long getFileSize(File f) {
        long size = 0;
        File[] flist = f.listFiles();
        if (flist == null) {
            return size;
        }
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }


}
