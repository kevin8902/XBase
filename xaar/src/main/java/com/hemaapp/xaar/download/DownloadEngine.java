package com.hemaapp.xaar.download;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;


import com.hemaapp.xaar.XConfig;
import com.hemaapp.xaar.util.XConnectUtil;

import java.io.File;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/25 13:21
 * 名称:DownloadEngine
 * 注释:下载
 *******************************/
public class DownloadEngine implements DownloadListener {


    private static DownloadEngine engine;

    private File downlaodDir;

    private Context mContext;

    private DownloadEvent event;

    private DownloadFileListener listener;

    private ServiceConnection mServiceConnection;

    private boolean isUnbind = true;

    public static DownloadEngine getEngine() {
        if (engine == null) {
            synchronized (DownloadEngine.class) {
                if (engine == null) {
                    engine = new DownloadEngine();
                }
            }
        }
        return engine;
    }

    private DownloadEngine() {

    }


    //停止下载
    public void stopDownload() {
        if (event != null) {
            event.is_pause = true;
            if (isUnbind) {
                mContext.unbindService(mServiceConnection);
                mServiceConnection = null;
                isUnbind = false;
            }

        }
    }


    public void startDownload(Context context, String url, String fileName, DownloadFileListener listener) {
        this.mContext = context;
        boolean canDownload = true;
        this.listener = listener;
        //判断是不是无线连接 提示一下用户
        if (!XConnectUtil.isWifiConnected(mContext)) {
            canDownload = isDownloadWithNoWifi();
        }
        downlaodDir = getDiskCacheDir(context);
        if (!canDownload) {
            listener.downloadError("非WIFI下不允许下载");
            return;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        //下载地址
        DownloadFileInfo info = new DownloadFileInfo(url, fileName);
        info.setDir(downlaodDir.getAbsolutePath());
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.setAction(DownloadService.DOWNLOAD_ACTION_START);
        intent.putExtra("info", info);
        //mContext.startService(intent);
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                DownloadService service = ((DownloadService.downloadBinder) iBinder).getService();
                service.setOnDownloadListener(DownloadEngine.this);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

    }


    //获取本地缓存路径
    private File getDiskCacheDir(Context context) {
        //判断是否有sd卡
        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + XConfig.CacheName + File.separator + "download");
    }


    @Override
    public boolean isDownloadWithNoWifi() {
        return listener.isDownloadWithNoWifi();
    }

    @Override
    public void downloadError(String msg) {
        if (isUnbind) {
            mContext.unbindService(mServiceConnection);
            mServiceConnection = null;
            isUnbind = false;
        }

        listener.downloadError(msg);
    }

    @Override
    public void downloadSucess(File file) {
        if (isUnbind) {
            mContext.unbindService(mServiceConnection);
            mServiceConnection = null;
            isUnbind = false;
        }
        listener.downloadSucess(file);
    }

    @Override
    public void downloadUpdate(int finished) {
        listener.downloadUpdate(finished);
    }

    @Override
    public void downloadStart(DownloadEvent event) {
        this.event = event;
    }
}
