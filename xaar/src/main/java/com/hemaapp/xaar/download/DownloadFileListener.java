package com.hemaapp.xaar.download;

import java.io.File;

public interface DownloadFileListener {
    boolean isDownloadWithNoWifi();


    void downloadError(String msg);


    void downloadSucess(File file);


    void downloadUpdate(int finished);
}
