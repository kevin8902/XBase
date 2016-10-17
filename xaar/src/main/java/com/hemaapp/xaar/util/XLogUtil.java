package com.hemaapp.xaar.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 11:04
 * 名称:XLogUtil
 * 注释:log的工具类
 *******************************/
public class XLogUtil {
    public static final int STATE_DEBUG = 2;
    public static final int STATE_FORMAL = 1;
    private static String TAG = "xpk";
    private static int current_state = STATE_DEBUG;

    //设置为正式,不输出日志
    public static void setFormal() {
        current_state = STATE_FORMAL;
    }


    public static void i(String msg) {
        if (STATE_DEBUG == current_state) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.i(TAG, "**************************************************************");
            Log.i(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
            Log.i(TAG, getPrettyJson(msg));
            Log.i(TAG, "**************************************************************");
        }
    }

    //显示json信息
    public static void i(String msg, String json) {
        if (STATE_DEBUG == current_state) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.i(TAG, "**************************************************************");
            Log.i(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
            Log.i(TAG, msg);
            Log.i(TAG, getPrettyJson(json));
            Log.i(TAG, "**************************************************************");
        }
    }


    public static void e(String msg) {
        if (STATE_DEBUG == current_state) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.e(TAG, "**************************************************************");
            Log.e(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
            Log.e(TAG, getPrettyJson(msg));
            Log.e(TAG, "**************************************************************");
        }
    }


    public static void e(String msg, String json) {
        if (STATE_DEBUG == current_state) {
            StackTraceElement targetStackTraceElement = getTargetStackTraceElement();
            Log.e(TAG, "**************************************************************");
            Log.e(TAG, "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")");
            Log.e(TAG, msg);
            Log.e(TAG, getPrettyJson(json));
            Log.e(TAG, "**************************************************************");
        }
    }


    private static StackTraceElement getTargetStackTraceElement() {
        // find the target invoked method
        StackTraceElement targetStackTrace = null;
        boolean shouldTrace = false;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            boolean isLogMethod = stackTraceElement.getClassName().equals(XLogUtil.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetStackTrace;
    }


    private static String getPrettyJson(String jsonStr) {
        try {
            jsonStr = jsonStr.trim();
            if (jsonStr.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(jsonStr);
                return jsonObject.toString(2);
            }
            if (jsonStr.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonStr);
                return jsonArray.toString(2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }


}
