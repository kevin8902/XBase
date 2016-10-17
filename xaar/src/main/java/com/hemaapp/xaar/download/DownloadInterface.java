package com.hemaapp.xaar.download;


import java.util.List;

/*************************
 * 作者:邢佩凯
 * 时间:16/5/22 下午4:13
 * 文件名:DownloadInterface.java
 * 注释:下载文件操作数据库的接口
 *************************/
public interface DownloadInterface {
    /**
     * 向数据库中插入一条下载信息
     *
     * @param downloadInfo
     */
    public void insertDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 数据库中删除一条信息
     *
     * @param url
     */
    public void deleteDownloadInfo(String url);

    /**
     * 更新信息
     *
     * @param downloadInfo
     */
    public void updateDownloadInfo(DownloadInfo downloadInfo);

    /**
     * 查询数据库中的信息
     *
     * @param url
     * @return
     */
    public List<DownloadInfo> getDownloadInfos(String url);

    /**
     * 判断数据库中是否已存在下载信息
     *
     * @param url
     * @return
     */
    public boolean isExist(String url, String id);
}
