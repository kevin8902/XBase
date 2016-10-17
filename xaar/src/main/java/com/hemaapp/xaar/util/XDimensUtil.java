package com.hemaapp.xaar.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;

/*************************
 * 作者:邢佩凯
 * 时间:16/5/19 下午7:48
 * 文件名:XDimensUtil.java
 * 注释:关于长度的工具类
 *************************/
public class XDimensUtil {

    /**
     * dp转化为px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dpToPx(Context context, int dipValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f * (dipValue >= 0 ? 1 : -1));
    }

    /**
     * px转化为dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxToDp(Context context, int pxValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f * (pxValue >= 0 ? 1 : -1));
    }

    /**
     * sp转化为px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int spToPx(Context context, int spValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * px转化为sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxToSp(Context context, int pxValue) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }


    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }



    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */

    private final static String STATUS_BAR_DEF_PACKAGE = "android";
    private final static String STATUS_BAR_DEF_TYPE = "dimen";
    private final static String STATUS_BAR_NAME = "status_bar_height";
    private static int STATUS_BAR_HEIGHT = -1;

    public static synchronized int getStatusBarHeight(Context context) {
        if (-1 == STATUS_BAR_HEIGHT) {
            //获取资源id
            int resourceId = context.getResources().
                    getIdentifier(STATUS_BAR_NAME, STATUS_BAR_DEF_TYPE, STATUS_BAR_DEF_PACKAGE);
            if (resourceId > 0) {
                STATUS_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return STATUS_BAR_HEIGHT;
    }


    /**
     * 获取navigation栏的高度
     *
     * @param context
     * @return
     */

    private final static String NAVIGATION_BAR_NAME = "navigation_bar_height";
    private static int NAVIGATION_BAR_HEIGHT = -1;

    public static synchronized int getNavigationBarHeight(Context context) {
        if (-1 == NAVIGATION_BAR_HEIGHT) {
            //获取资源id
            int resourceId = context.getResources().
                    getIdentifier(NAVIGATION_BAR_NAME, STATUS_BAR_DEF_TYPE, STATUS_BAR_DEF_PACKAGE);
            if (resourceId > 0) {
                NAVIGATION_BAR_HEIGHT = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return NAVIGATION_BAR_HEIGHT;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getDisplayHeight(Activity context) {
        int screen_h = 0;
        int ver = Build.VERSION.SDK_INT;
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = context.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        if (ver < 13) {
            screen_h = dm.heightPixels;
        } else if (ver == 13) {
            try {
                Method mt = display.getClass().getMethod("getRealHeight");
                screen_h = (Integer) mt.invoke(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ver > 13) {
            try {
                Point point = new Point();
                display.getRealSize(point);
                screen_h = point.y;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screen_h;
    }

}
