package org.base.platform.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by YinShengyi on 2016/12/13.
 */
public class JsonUtils {

    /**
     * 将json字符串映射成Java对象
     *
     * @param content json字符串内容
     * @param cls     待映射的类对象
     */
    public static <T> T jsonToObj(String content, Class<T> cls) {
        return JSONObject.parseObject(content, cls);
    }

    /**
     * 将Java对象映射成json字符串
     *
     * @param obj 待映射的Java对象
     */
    public static String objToJson(Object obj) {
        return JSONObject.toJSONString(obj);
    }

    /**
     * 将json字符串映射成List集合对象
     *
     * @param content 待映射的json字符串内容
     * @param cls     待映射的类对象
     */
    public static <T> List<T> jsonToList(String content, Class<T> cls) {
        return JSONArray.parseArray(content, cls);
    }

    /**
     * 将List集合对象映射成json字符串
     *
     * @param list 待映射的集合对象
     */
    public static <T> String listToJson(List<T> list) {
        return JSONArray.toJSONString(list);
    }
}
