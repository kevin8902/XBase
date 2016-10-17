package com.hemaapp.xaar.http;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hemaapp.xaar.XConfig;


/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 14:19
 * 名称:MainHandler
 * 注释:
 *******************************/
public class MainHandler extends Handler {


    public MainHandler() {
        super(Looper.getMainLooper());
    }


    @Override
    public void handleMessage(Message msg) {
        HttpBean bean = (HttpBean) msg.obj;
        if (msg.what == XConfig.HTTP_BEFORE) {
            bean.getListener().httpExecuteBefore(bean.getTask());
        } else if (msg.what == XConfig.HTTP_SUCCESS) {
            bean.getListener().httpExecuteSuccess(bean.getTask(), bean.getBaseResult());
            bean.getListener().httpExecuteAfter(bean.getTask());
        } else if (msg.what == XConfig.HTTP_FAILED) {
            bean.getListener().httpExecuteFailed(bean.getTask(), bean.getBaseResult());
            bean.getListener().httpExecuteAfter(bean.getTask());
        } else if (msg.what == XConfig.HTTP_ERROR) {
            bean.getListener().httpExecuteError(bean.getTask());
        }
        super.handleMessage(msg);
    }
}
