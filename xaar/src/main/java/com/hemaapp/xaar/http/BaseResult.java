package com.hemaapp.xaar.http;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 邢佩凯 on 2016/2/16.
 * 结果集
 */
public class BaseResult {

    private boolean sucess;
    private String msg;
    private int errorCode;
    private String value;

    private List<Object> mObjects;
    private Object mObject;

    public BaseResult(JSONObject object, Class<? extends Object> c) {
        if (object != null) {
            try {
                if (!object.isNull("success"))
                    sucess = object.getBoolean("success");
                msg = object.getString("msg");
                if (!object.isNull("error_code"))
                    errorCode = object.getInt("error_code");
                if (!object.isNull("infor") && !TextUtils.isEmpty(object.getString("infor"))) {
                    Object json = new JSONTokener(object.getString("infor")).nextValue();
                    if (c == null) {
                        value = object.getString("infor");
                        return;
                    }
                    if (json instanceof JSONArray) {
                        if (mObjects == null) {
                            mObjects = new ArrayList<>();
                        }
                        if (mObjects.size() > 0)
                            mObjects.clear();
                        JSONArray array = (JSONArray) json;
                        for (int i = 0; i < array.length(); i++) {
                            Object o = new Gson().fromJson(array.get(i).toString(), c);
                            mObjects.add(o);
                        }
                    } else {
                        mObject = new Gson().fromJson(json.toString(), c);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                sucess = false;
                msg = "数据解析异常";
            }
        }
    }

    public boolean isSucess() {
        return sucess;
    }

    public String getMsg() {
        return msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getValue() {
        return value;
    }

    public Object getmObject() {
        return mObject;
    }

    public List<? extends Object> getmObjects() {
        return mObjects;
    }
}
