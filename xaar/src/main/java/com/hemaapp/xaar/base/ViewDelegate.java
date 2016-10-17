package com.hemaapp.xaar.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/9 14:14
 * 名称:ViewDelegate
 * 注释:mvp中v的代理接口
 *******************************/
public interface ViewDelegate<P extends Presenter> {
    //创建
    void onCreate(LayoutInflater inflater, ViewGroup contanier, Bundle savedInstanceState, P presenter);

    //获取根布局
    View getRootView();

    //初始化控件
    void initWidget(Intent intent);

    //初始化事件
    void initEvent(View.OnClickListener listener);


}
