package com.hemaapp.xaar.http;


import com.hemaapp.xaar.XConfig;
import com.hemaapp.xaar.util.Md5Util;
import com.hemaapp.xaar.util.XLogUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by 邢佩凯 on 2016/2/16.
 * 联网的主要执行类
 */
public class HttpEvent {

    public static String sessionID = null;
    private static String end = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "*****";


    //没有文件的联网操作
    public static String doTaskNoFile(HttpTask task) {
        //获取路径
        String path = task.getHttpInformation().getUrlPath();
        //获取参数
        HashMap<String, String> params = task.getParams();
        StringBuilder sb = HandleParams(params, path);
        //参数显示
        XLogUtil.i(task.getHttpInformation().getDesc() + "参数:", "{\""+sb.toString().replace("=","\":\"").replace("&","\",\"")+"\"}");
        HttpURLConnection conn = null;
        byte[] entry = sb.toString().getBytes();
        try {
            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);//是否输入参数
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(entry.length));
            if (sessionID != null)
                conn.setRequestProperty("Cookie", sessionID);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(entry);
            dos.flush();
            dos.close();
            String cookie = conn.getHeaderField("set-cookie");
            if (cookie != null)
                sessionID = cookie.substring(0, cookie.indexOf(";"));
            int code = conn.getResponseCode();
            InputStream in = (code == HttpURLConnection.HTTP_OK) ? conn
                    .getInputStream() : null;
            if (in == null) {
                XLogUtil.e(task.getHttpInformation().getDesc() + "code:" + code);
                task.setError_msg("code:" + code);
                return null;
            }
            String result = convertStreamToString(in);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            XLogUtil.e(task.getHttpInformation().getDesc() + "错误:" + e.getMessage());
            task.setError_msg("连接服务器失败");
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }


    //处理参数
    private static StringBuilder HandleParams(HashMap<String, String> params, String path) {
        StringBuilder sb = new StringBuilder();
        String value;
        if (params != null && !params.isEmpty()) {
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey());
                sb.append("=");
                if (entry.getValue() != null) {
                    value = entry.getValue().replace("&", "%26");
                    value = value.toString().replace("+", "%2B");
                } else {
                    value = entry.getValue();
                }
                sb.append(value);
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);

            if (XConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = getCurrentTime("yyyy-MM-dd HH:mm:ss");
                sb.append("&").append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = Md5Util.getMd5(XConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                sb.append("sign=").append(sign);
            }
        } else {
            if (XConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = getCurrentTime("yyyy-MM-dd HH:mm:ss");
                sb.append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = Md5Util.getMd5(XConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                sb.append("sign=").append(sign);
            }
        }
        return sb;
    }


    /**
     * 处理参数集
     *
     * @param dos      数据输出流
     * @param params   参数集
     * @param encoding 编码方式
     * @throws IOException
     */
    private static void writeParams(String path, DataOutputStream dos,
                                    HashMap<String, String> params, String encoding) throws IOException {
        StringBuilder data = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // 方便查看发送参数，无实际意义
                data.append(entry.getKey()).append("=");
                data.append(entry.getValue());
                data.append("&");
                if (entry.getValue() != null) {
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + entry.getKey() + "\"" + end);
                    dos.writeBytes(end);
                    dos.write(entry.getValue().getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                    dos.writeBytes(end);
                    // dos.writeBytes(TWOHYPHENS + BOUNDARY + TWOHYPHENS + END);
                }
            }
            data.deleteCharAt(data.length() - 1);
            if (XConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = getCurrentTime("yyyy-MM-dd HH:mm:ss");
                data.append("&").append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = Md5Util.getMd5(XConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                data.append("sign=").append(sign);
                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"datetime\"" + end);
                dos.writeBytes(end);
                dos.write(datetime.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(end);

                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"sign\"" + end);
                dos.writeBytes(end);
                dos.write(sign.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(end);
            }

        } else {
            if (XConfig.DIGITAL_CHECK) {  //开启数字签名
                String datetime = getCurrentTime("yyyy-MM-dd HH:mm:ss");
                data.append("datetime=").append(datetime).append("&");
                String[] tempPath = path.split("/");
                String sign = Md5Util.getMd5(XConfig.DATAKEY + "|" + datetime + "|" + tempPath[tempPath.length - 1]);
                data.append("sign=").append(sign);

                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"datetime\"" + end);
                dos.writeBytes(end);
                dos.write(datetime.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(end);

                dos.writeBytes(twoHyphens + boundary + end);
                dos.writeBytes("Content-Disposition: form-data; "
                        + "name=\"sign\"" + end);
                dos.writeBytes(end);
                dos.write(sign.getBytes(encoding));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                dos.writeBytes(end);
            }
        }
    }

    //带有文件的联网操作
    public static String doTaskWithFile(HttpTask task) {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        try {
            URL url = new URL(task.getHttpInformation().getUrlPath());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            if (sessionID != null)
                conn.setRequestProperty("Cookie", sessionID);// 设置cookie
            dos = new DataOutputStream(conn.getOutputStream());
            /*if (task.getParams() != null && !task.getParams().isEmpty()) {
                for (HashMap.Entry<String, String> entry : task.getParams().entrySet()) {
                    if (entry.getValue() != null) {
                        dos.writeBytes(twoHyphens + boundary + end);
                        dos.writeBytes("Content-Disposition: form-data; "
                                + "name=\"" + entry.getKey() + "\"" + end);
                        dos.writeBytes(end);
                        dos.write(entry.getValue().getBytes("UTF-8"));// writeBytes方法默认以ISO-8859-1编码,此处易出现汉字乱码问题
                        dos.writeBytes(end);
                    }
                }
            }*/
            writeParams(task.getHttpInformation().getUrlPath(), dos, task.getParams(), "UTF-8");


            if (task.getFiles() != null && !task.getFiles().isEmpty()) {
                for (HashMap.Entry<String, String> entry : task.getFiles().entrySet()) {
                    FileInputStream fis = null;
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos.writeBytes("Content-Disposition: form-data; " + "name=\""
                            + entry.getKey() + "\";filename=\"" + entry.getValue()
                            + "\"" + end);
                    String filetype = FileTypeUtil.getFileTypeByPath(entry
                            .getValue());// 获取文件类型
                    dos.writeBytes("Content-type: " + filetype + end);
                    dos.writeBytes(end);
                    int bufferSize = 1024 * 10;
                    byte[] buffer = new byte[bufferSize];
                    int length = -1;
                    File file = new File(entry.getValue());
                    fis = new FileInputStream(file);
                    while ((length = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, length);
                    }
                    dos.writeBytes(end);
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            dos.close();
            String cookie = conn.getHeaderField("set-cookie");
            if (cookie != null)
                sessionID = cookie.substring(0, cookie.indexOf(";"));// 获取sessionID
            String data = convertStreamToString(conn.getInputStream());
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            XLogUtil.e("带文件上传出现错误" + e.getMessage());
            return null;
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                XLogUtil.e("带文件的上传finally出现" + e.getMessage());
            }
        }
    }


    /**
     * 将流转成字符串
     *
     * @param is 输入流
     * @return
     * @throws Exception
     */
    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取系统当前时间
     *
     * @param format 时间格式yyyy-MM-dd HH:mm:ss
     * @return String
     */
    private static String getCurrentTime(String format) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format,
                Locale.getDefault());
        return dateFormat.format(date);
    }


}
