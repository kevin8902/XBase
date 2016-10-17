package com.hemaapp.xaar.http;

/**
 * Created by 邢佩凯 on 2016/2/16.
 * 联网信息
 */
public interface HttpInformation {
    /**
     * 获取请求信息id
     *
     * @return
     */
    public int getId();

    /**
     * 获取请求地址
     *
     * @return
     */
    public String getUrlPath();

    /**
     * 获取请求描述
     *
     * @return
     */
    public String getDesc();

    /**
     * 判断是否为根路径
     *
     * @return
     */
    public boolean isRootPath();

}
