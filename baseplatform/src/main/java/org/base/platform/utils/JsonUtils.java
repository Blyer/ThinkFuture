package org.base.platform.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by YinShengyi on 2016/12/13.
 */
public class JsonUtils {
    public static <T> T jsonToObj(String content, Class<T> cls) {
        return JSONObject.parseObject(content, cls);
    }

    public static String objToJson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    public static <T> List<T> jsonToList(String content, Class<T> cls) {
        return JSONArray.parseArray(content, cls);
    }

    public static <T> String listToJson(List<T> list) {
        return JSONArray.toJSONString(list);
    }
}
