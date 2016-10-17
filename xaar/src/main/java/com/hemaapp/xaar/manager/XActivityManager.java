package com.hemaapp.xaar.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Stack;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/11 16:51
 * 名称:XActivityManager
 * 注释:activity的管理类
 *******************************/
public class XActivityManager {

    private static XActivityManager instance;
    private static Stack<Activity> mActivities;

    public static XActivityManager getInstance() {
        if (instance == null) {
            synchronized (XActivityManager.class) {
                if (instance == null) {
                    instance = new XActivityManager();
                }
            }
        }
        return instance;
    }


    private XActivityManager() {

    }


    public Activity currentActivity() {
        Activity activity = mActivities.lastElement();
        return activity;
    }


    public void addActivity(Activity activity) {
        if (activity == null)
            return;
        if (mActivities == null)
            mActivities = new Stack<>();
        if (!mActivities.contains(activity))
            mActivities.add(activity);
    }


    public void removeActivity(Activity activity) {
        if (activity == null)
            return;
        if (mActivities != null && mActivities.contains(activity))
            mActivities.remove(activity);
    }


    public void closeAllActivity() {
        if (mActivities != null) {
            ArrayList<Activity> tempActivitys = new ArrayList<>();
            tempActivitys.addAll(mActivities);
            for (Activity tempActivity : tempActivitys) {
                tempActivity.finish();
            }
            mActivities.clear();
            mActivities = null;
            tempActivitys.clear();
            tempActivitys = null;
        }
    }


}
