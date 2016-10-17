package com.hemaapp.xaar.download;

import android.content.Context;


import com.hemaapp.xaar.db.SqlOperation;

import java.util.List;


/*************************
 * 作者:邢佩凯
 * 时间:16/5/22 下午7:00
 * 文件名:DownloadDB.java
 * 注释:下载数据库的操作
 *************************/
public class DownloadDB implements DownloadInterface {
    private Context mContext;

    public DownloadDB(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void insertDownloadInfo(DownloadInfo downloadInfo) {
        SqlOperation operation = new SqlOperation(mContext, downloadInfo, "download");
        operation.insert();
    }

    @Override
    public void deleteDownloadInfo(String url) {
        SqlOperation operation = new SqlOperation(mContext, DownloadInfo.class, "download");
        operation.deleteByV("url", url);
    }

    @Override
    public void updateDownloadInfo(DownloadInfo downloadInfo) {
        SqlOperation operation = new SqlOperation(mContext, downloadInfo, "download");
        operation.update(new String[]{"id", "url"}, new String[]{downloadInfo.getId(), downloadInfo.getUrl()});
    }

    @Override
    public List<DownloadInfo> getDownloadInfos(String url) {
        SqlOperation operation = new SqlOperation(mContext, DownloadInfo.class, "download");
        return (List<DownloadInfo>) operation.queryByV("url", url);
    }

    @Override
    public boolean isExist(String url, String id) {
        return false;
    }
}
