package com.hemaapp.xaar.download;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


import com.hemaapp.xaar.manager.BaseThreaPoolManager;
import com.hemaapp.xaar.util.XLogUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadService extends Service {
    public static final String DOWNLOAD_ACTION_START = "ACTION_START";//开始

    public static final int error = 1;
    public static final int init = 2;

    private DownloadListener listener;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DownloadFileInfo info = (DownloadFileInfo) msg.obj;
            if (msg.what == error) {
                listener.downloadError("文件下载初始化错误");
            } else if (msg.what == init) {
                File file = new File(info.getDir());
                if (file.getUsableSpace() <= info.getLength()) {
                    listener.downloadError("可用空间不足");
                } else {
                    //开始下载
                    XLogUtil.e("开始下载");
                    DownloadEvent event = new DownloadEvent(getApplicationContext(), info, listener);
                    event.download();
                    //listener.downloadSucess();
                }
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        DownloadFileInfo info = intent.getParcelableExtra("info");
        BaseThreaPoolManager.getPoolManager().createHttpPool().execute(new init(info));
        return new downloadBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        XLogUtil.e("启动");
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        XLogUtil.e("解除绑定");
    }

    public class downloadBinder extends Binder {
        public DownloadService getService() {
            return DownloadService.this;
        }
    }


    public void setOnDownloadListener(DownloadListener listener) {
        this.listener = listener;
    }


    private class init implements Runnable {
        private DownloadFileInfo info;

        private init(DownloadFileInfo fileInfo) {
            this.info = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            try {
                URL url = new URL(info.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int length = -1;
                if (200 == conn.getResponseCode()) {
                    length = conn.getContentLength();
                }
                if (length < 0) {
                    //错误
                    mHandler.obtainMessage(error, info).sendToTarget();
                    return;
                }
                File file = new File(info.getDir());
                if (!file.exists()) {
                    file.mkdir();
                }
                File f = new File(file, info.getName());
                raf = new RandomAccessFile(f, "rwd");
                raf.setLength(length);
                info.setLength(length);
                mHandler.obtainMessage(init, info).sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
                mHandler.obtainMessage(error, info).sendToTarget();
            } finally {
                try {
                    if (raf != null) {
                        raf.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XLogUtil.e("被销毁");
    }
}
