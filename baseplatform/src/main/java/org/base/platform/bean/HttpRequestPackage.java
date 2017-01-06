package org.base.platform.bean;

import org.base.platform.enums.HttpMethod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class HttpRequestPackage implements Serializable {
    /**
     * 本次请求的地址
     */
    public String url = "";
    /**
     * 请求的方式
     */
    public HttpMethod method = HttpMethod.JSON;
    /**
     * 字符串参数
     */
    public HashMap<String, Object> params = new HashMap<>();
    /**
     * 待上传文件的路径集合
     */
    public ArrayList<String> filePaths = new ArrayList<>();

}
