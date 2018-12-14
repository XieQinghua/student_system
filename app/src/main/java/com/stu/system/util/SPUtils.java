package com.stu.system.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.stu.system.app.StuSystemApplication;

import java.util.ArrayList;
import java.util.List;

public class SPUtils {
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private static SPUtils single = null;

    public static SPUtils getInstance(Context context) {
        if (single == null) {
            single = new SPUtils(context);
        }
        return single;
    }

    public static SPUtils getInstance() {
        return getInstance(StuSystemApplication.getInstance());
    }

    private SPUtils(Context context) {
        mSp = context.getSharedPreferences("student_system", Context.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    /**
     * 取出string
     */
    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    /**
     * 取出int
     */
    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    /**
     * 取出boolean
     */
    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    /**
     * 取出boolean
     */
    public long getLong(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    public <T> List<T> getDataList(String key, Class<T> cls, String value) {
        List<T> dataList = new ArrayList<T>();
        String strJson = mSp.getString(key, value);
        if (TextUtils.isEmpty(strJson)) {
            return dataList;
        }
        Gson gson = new Gson();
        try {
            //dataList = new Gson().fromJson(strJson, new TypeToken<List<T>>() {
            //}.getType());
            JsonArray array = new JsonParser().parse(strJson).getAsJsonArray();
            for (JsonElement elem : array) {
                dataList.add(gson.fromJson(elem, cls));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 存入string
     */
    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    /**
     * 存入int
     */
    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    /**
     * 存入boolean
     */
    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    /**
     * 存入long
     */
    public void putLong(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    /**
     * 存入数组
     *
     * @param key
     * @param datalist
     * @param <T>
     */
    public <T> void putDataList(String key, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0) {
            return;
        }
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        mEditor.putString(key, strJson);
        mEditor.commit();
    }

    /**
     * 移除
     *
     * @param key
     * @return
     */
    public boolean removeValue(String key) {
        mEditor.remove(key);
        return mEditor.commit();
    }
}
