package com.hemaapp.xaar.base;

/******************************
 * 作者:邢佩凯
 * 日期:2016/10/9 15:10
 * 名称:PermissionListener
 * 注释:权限检测的接口
 *******************************/
public interface PermissionListener {
    void permissionPermit(String permission);

    void permissionRefused(String permission);
}
