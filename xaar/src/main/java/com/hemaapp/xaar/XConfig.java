package com.hemaapp.xaar;

import java.util.ArrayList;

/******************************
*作者:邢佩凯
*日期:2016/10/11 13:10
*名称:XConfig
*注释:设置
*******************************/
public class XConfig {
    //是否开启数字验签
    public static boolean DIGITAL_CHECK = false;
    //DATAKEY为服务器分配的项目唯一字符串
    public static String DATAKEY = "";

    //基本配置
    public static final int LOGIN_ID = 1;//登录的id
    public static final int THIRD_ID = 2;//三方登录id

    public static String Token = "";//定义一个全局token,避免频繁操作数据库
    //缓存文件夹的名称
    public static String CacheName = "demo";
    //http缓存文件夹大小
    public static long httpCacheSize = 1024 * 1024 * 10;//默认10M大小
    //系统版本号
    public static int AppVersionCode = 1000;//主要是在磁盘缓存的时候使用



    public static final int HTTP_SUCCESS = 0x100001;
    public static final int HTTP_FAILED = 0x100002;
    public static final int HTTP_BEFORE = 0x100003;
    public static final int HTTP_ERROR = 0x100004;


    /***************
     * 关于数据库的配置参数
     ********************/
    //程序数据库名字
    public static String DATABASENAME;
    //数据库表名
    public static ArrayList<String> TABLES = new ArrayList<>();
    //数据库对应的实体类
    public static ArrayList<Class<? extends Object>> mClasses = new ArrayList<>();
    //数据库版本号
    public static int DbVersion = 1;

    /****************
     * 图片的配置
     ******************/
    // 图片压缩的最大宽度
    public static final int IMAGE_WIDTH = 640;
    //图片压缩的最大高度
    public static final int IMAGE_HEIGHT = 3000;
    //图片压缩的失真率
    public static final int IMAGE_QUALITY = 100;
    //保存路径
    public static String save_path = "";
    //图片压缩最大大小
    public static int image_size = 200;

}
