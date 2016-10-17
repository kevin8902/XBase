package com.hemaapp.xaar.base;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/9 14:20
 * 名称:Presenter
 * 注释:mvp中p要实现的接口
 *******************************/
public interface Presenter {
    //显示加载提示框
    void showProgressDialog(String msg);

    //隐藏加载提示框
    void hideProgressDialog();

    //显示文字提示框
    void showTextDialog(String msg);

    //操作
    void operate(int type, Object object);

    //关闭界面
    void finishActivity();

}
