package com.hemaapp.xaar.download;

/*************************
 * 作者:邢佩凯
 * 时间:16/5/22 下午4:07
 * 文件名:DownloadInfo.java
 * 注释:文件下载时,数据库保存信息
 *************************/
public class DownloadInfo implements Cloneable {
    private String id;
    private String url;//文件地址
    private String start;//文件开始的位置
    private String end;//文件结束的位置
    private String finshed;//文件完成的长度


    public DownloadInfo(){

    }

    public DownloadInfo(String id, String url, String start, String end, String finshed) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finshed = finshed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getFinshed() {
        return finshed;
    }

    public void setFinshed(String finshed) {
        this.finshed = finshed;
    }


    @Override
    public DownloadInfo clone() {
        DownloadInfo mDownloadInfo = null;
        try {
            mDownloadInfo = (DownloadInfo) super.clone();
            mDownloadInfo.setId(this.id);
            mDownloadInfo.setUrl(this.url);
            mDownloadInfo.setStart(this.start);
            mDownloadInfo.setEnd(this.end);
            mDownloadInfo.setFinshed(this.finshed);
            return mDownloadInfo;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;

    }
}
