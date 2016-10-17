package com.hemaapp.xaar.download;

import android.content.Context;


import com.hemaapp.xaar.manager.BaseThreaPoolManager;
import com.hemaapp.xaar.util.XLogUtil;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*************************
 * 作者:邢佩凯
 * 时间:16/5/22 下午6:55
 * 文件名:DownloadEngine.java
 * 注释:执行下载任务
 *************************/
public class DownloadEvent {

    private DownloadFileInfo mDownloadFileInfo;
    private DownloadDB mDownloadDB;
    private Context mContext;
    private long mFinished = 0;
    public boolean is_pause = false;
    private ArrayList<downloadThread> mInfoArrayList;
    private DownloadListener listener;
    private File file;

    public DownloadEvent(Context context, DownloadFileInfo downloadFileInfo, DownloadListener listener) {
        this.mContext = context;
        this.mDownloadFileInfo = downloadFileInfo;
        this.listener = listener;
        mDownloadDB = new DownloadDB(mContext);
    }


    public void download() {
        listener.downloadStart(this);
        List<DownloadInfo> mDownloadInfos = mDownloadDB.getDownloadInfos(mDownloadFileInfo.getUrl());
        if (mDownloadInfos == null || mDownloadInfos.size() == 0) {
            int threadCount = Runtime.getRuntime().availableProcessors() * 2;
            long everyLength = mDownloadFileInfo.getLength() / threadCount;
            DownloadInfo mDownloadInfo = new DownloadInfo(0 + "", mDownloadFileInfo.getUrl(), 0 + "", (everyLength - 1) + "", 0 + "");
            mDownloadDB.insertDownloadInfo(mDownloadInfo);
            mDownloadInfos.add(mDownloadInfo);
            for (int i = 1; i < threadCount; i++) {
                DownloadInfo downloadInfo = mDownloadInfo.clone();
                if (downloadInfo != null) {
                    downloadInfo.setId(i + "");
                    downloadInfo.setStart(everyLength * i + "");
                    downloadInfo.setEnd((i + 1) * everyLength - 1 + "");
                    if (i == threadCount - 1) {
                        downloadInfo.setEnd(mDownloadFileInfo.getLength() + "");
                    }
                    mDownloadDB.insertDownloadInfo(downloadInfo);
                }
                mDownloadInfos.add(downloadInfo);
            }
        }

        if (mInfoArrayList == null) {
            mInfoArrayList = new ArrayList<>();
        } else {
            mInfoArrayList.clear();
        }
        BaseThreaPoolManager.getPoolManager().clearImageQueue();
        for (DownloadInfo info : mDownloadInfos) {
            downloadThread mDownloadThread = new downloadThread(info);
            mFinished = mFinished + Long.parseLong(info.getFinshed());
            BaseThreaPoolManager.getPoolManager().createImagePool().execute(mDownloadThread);
            mInfoArrayList.add(mDownloadThread);
        }


    }


    private class downloadThread implements Runnable {
        private DownloadInfo mDownloadInfo;
        public boolean isFinished = false;

        private downloadThread(DownloadInfo downloadInfo) {
            this.mDownloadInfo = downloadInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream is = null;

            try {
                URL url = new URL(mDownloadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                long start = Long.parseLong(mDownloadInfo.getStart()) + Long.parseLong(mDownloadInfo.getFinshed());
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mDownloadInfo.getEnd());
                //设置文件写入位置
                file = new File(mDownloadFileInfo.getDir(), mDownloadFileInfo.getName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                //Intent intent = new Intent(DownloadService.DOWNLOAD_ACTION_UPDATE);
                if (conn.getResponseCode() == 206) {
                    //读取数据
                    is = conn.getInputStream();
                    byte[] buffer = new byte[1024 * 4];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buffer)) != -1) {
                        //写入文件
                        raf.write(buffer, 0, len);
                        //累加整个文件的完成进度
                        mFinished += len;
                        //累加每个线程的进度
                        mDownloadInfo.setFinshed(Long.parseLong(mDownloadInfo.getFinshed()) + len + "");
                        //把下载进度传回去
                        if (System.currentTimeMillis() - time > 500) {
                            time = System.currentTimeMillis();
                            XLogUtil.i("进度:" + mFinished * 100 / mDownloadFileInfo.getLength());
                            /*intent.putExtra("finished", (int) (mFinished * 100 / mDownloadFileInfo.getLength()));
                            mContext.sendBroadcast(intent);*/
                            listener.downloadUpdate((int) (mFinished * 100 / mDownloadFileInfo.getLength()));
                        }

                        if (is_pause) {
                            mDownloadDB.updateDownloadInfo(mDownloadInfo);
                            return;
                        }
                    }
                    isFinished = true;
                    checkAllFinished();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
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


    private void checkAllFinished() {
        boolean allFinished = true;
        for (downloadThread mThread : mInfoArrayList) {
            if (!mThread.isFinished) {
                allFinished = false;
                break;
            }
        }

        if (allFinished) {
            mDownloadDB.deleteDownloadInfo(mDownloadFileInfo.getUrl());
           /* Intent intent = new Intent(DownloadService.DOWNLOAD_ACTION_FINISHED);
            intent.putExtra(DownloadService.DOWNLOAD_INFO, mDownloadFileInfo);
            mContext.sendBroadcast(intent);*/
            listener.downloadSucess(file);
        }

    }
}
