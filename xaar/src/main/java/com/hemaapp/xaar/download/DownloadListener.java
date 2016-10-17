package com.hemaapp.xaar.download;

import java.io.File;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/25 13:23
 * 名称:DownloadListener
 * 注释:下载的监听
 *******************************/
public interface DownloadListener {


    boolean isDownloadWithNoWifi();


    void downloadError(String msg);


    void downloadSucess(File file);


    void downloadUpdate(int finished);

    void downloadStart(DownloadEvent event);

}
