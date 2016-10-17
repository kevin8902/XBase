package com.hemaapp.xaar.download;

import android.os.Parcel;
import android.os.Parcelable;

/******************************
 * 作者:邢佩凯
 * 日期:2016/8/25 13:32
 * 名称:DownloadFileInfo
 * 注释:下载的文件信息
 *******************************/
public class DownloadFileInfo implements Parcelable {

    private String url;//地址
    private String name;//文件名
    private String dir;//文件保存路径
    private long length;//文件长度
    private long finished;//文件完成的长度

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }


    public DownloadFileInfo(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeString(this.dir);
        dest.writeLong(this.length);
        dest.writeLong(this.finished);
    }

    protected DownloadFileInfo(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.dir = in.readString();
        this.length = in.readLong();
        this.finished = in.readLong();
    }

    public static final Creator<DownloadFileInfo> CREATOR = new Creator<DownloadFileInfo>() {
        @Override
        public DownloadFileInfo createFromParcel(Parcel source) {
            return new DownloadFileInfo(source);
        }

        @Override
        public DownloadFileInfo[] newArray(int size) {
            return new DownloadFileInfo[size];
        }
    };
}
