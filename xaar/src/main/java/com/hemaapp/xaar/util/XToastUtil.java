package com.hemaapp.xaar.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者:邢佩凯
 * 2015/9/24 19:14
 * Toast统一管理类
 */
public class XToastUtil {
    public static Toast mToast;

    /**
     * 长时间显示toast
     */

    public static void showLong(Context mContext, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_LONG);

        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    public static void showLong(Context mContext, int message) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_LONG);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    /**
     * 短时间显示Toast
     */

    public static void showShort(Context mContext, String message) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    public static void showShort(Context mContext, int message) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext.getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    /**
     * 隐藏Toast
     */
    public static void hideToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
