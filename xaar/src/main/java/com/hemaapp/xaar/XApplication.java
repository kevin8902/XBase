package com.hemaapp.xaar;

import android.app.Application;
import android.content.Context;

import com.hemaapp.xaar.util.XLogUtil;


/******************************
 * 作者:邢佩凯
 * 日期:2016/10/11 13:17
 * 名称:XApplication
 * 注释:application基类
 *******************************/
public abstract class XApplication<T extends ConfigHelper> extends Application {
    private static Context instance;


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化配置
        try {
            getConfigHelpClass().newInstance().initConfig(this);
        } catch (Exception e) {
            e.printStackTrace();
            XLogUtil.e("初始化配置error:" + e.getMessage());
        }
        instance = this;
        //设置闪退的处理逻辑
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    }


    /**
     * @return 实现configHelper的类
     */
    protected abstract Class<T> getConfigHelpClass();


    public static Context getApplicationInstance() {
        return instance;
    }


    //可以禁掉闪退出现的提示对话框
    class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            //在这可以添加处理逻辑 比如重新打开(intent.setflags(Intent.FLAG_ACTIVITY_NEW_TASK))


            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }
}
