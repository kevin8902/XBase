package com.hemaapp.xaar.base;

import android.content.Context;

import com.hemaapp.xaar.XConfig;
import com.hemaapp.xaar.bean.User;
import com.hemaapp.xaar.http.BaseResult;
import com.hemaapp.xaar.http.HttpEngine;
import com.hemaapp.xaar.http.HttpExecuteListener;
import com.hemaapp.xaar.http.HttpTask;

import java.util.ArrayList;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/24 10:51
 * 名称:BaseModel
 * 注释:获取数据
 *******************************/
public abstract class BaseModel<T extends Presenter> implements HttpExecuteListener {

    private HttpEngine engine;
    private ArrayList<HttpTask> failedTasks;
    protected T mPresenter;
    protected Context mContext;


    public void Create(Context context, T presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
    }

    protected HttpEngine getEngine() {
        if (engine == null) {
            engine = HttpEngine.getEngine(mContext);
        }
        return engine;
    }

    //执行任务
    public void doTask(HttpTask task) {
        getEngine().doPost(task, this);
    }


    @Override
    public void httpExecuteError(HttpTask task) {
        ExecuteError(task);
    }

    @Override
    public void httpExecuteBefore(HttpTask task) {
        ExecuteBefore(task);
    }


    @Override
    public void httpExecuteAfter(HttpTask task) {
        ExecuteAfter(task);
    }


    @Override
    public void httpExecuteSuccess(HttpTask task, BaseResult baseResult) {
        int id = task.getHttpInformation().getId();
        if (id == XConfig.LOGIN_ID || id == XConfig.THIRD_ID) {
            if (failedTasks != null && failedTasks.size() > 0) {
                User mUser = (User) baseResult.getmObjects().get(0);
                XConfig.Token = mUser.getToken();
                for (HttpTask httpTask : failedTasks) {
                    if (httpTask.getParams().containsKey("token")) {
                        httpTask.getParams().put("token", XConfig.Token);
                    }
                    doTask(task);
                }
                failedTasks.clear();
            } else {
                ExecuteSuccess(task, baseResult);
            }
        } else {
            ExecuteSuccess(task, baseResult);
        }
    }


    @Override
    public void httpExecuteFailed(HttpTask task, BaseResult baseResult) {
        int id = task.getHttpInformation().getId();
        if (id == XConfig.LOGIN_ID || id == XConfig.THIRD_ID) {
            if (failedTasks != null && failedTasks.size() > 0) {
                for (HttpTask httpTask : failedTasks) {
                    httpTask.setError_msg("操作失败");
                    ExecuteError(httpTask);
                }
                failedTasks.clear();
            } else {
                ExecuteFailed(task, baseResult);
            }

        } else {
            //200是token过期的返回码
            if (200 == baseResult.getErrorCode()) {
                if (failedTasks == null) {
                    failedTasks = new ArrayList<>();
                } else {
                    failedTasks.clear();
                }
                failedTasks.add(task);
                doTask(autoLogin());
            } else {
                ExecuteFailed(task, baseResult);
            }
        }
    }

    protected abstract void ExecuteError(HttpTask httpTask);

    protected abstract void ExecuteBefore(HttpTask task);

    protected abstract void ExecuteAfter(HttpTask task);

    public abstract HttpTask autoLogin();

    protected abstract void ExecuteSuccess(HttpTask task, BaseResult baseResult);

    protected abstract void ExecuteFailed(HttpTask task, BaseResult result);
}
