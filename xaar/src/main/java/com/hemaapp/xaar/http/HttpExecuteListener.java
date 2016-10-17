package com.hemaapp.xaar.http;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/18 13:57
 * 名称:HttpExecuteListener
 * 注释:联网返回数据的接口
 *******************************/
public interface HttpExecuteListener {
    //联网失败
    void httpExecuteError(HttpTask task);

    //操作之前
    void httpExecuteBefore(HttpTask task);

    //操作之后
    void httpExecuteAfter(HttpTask task);

    //操作成功
    void httpExecuteSuccess(HttpTask task, BaseResult baseResult);

    //操作失败
    void httpExecuteFailed(HttpTask task, BaseResult baseResult);
}
