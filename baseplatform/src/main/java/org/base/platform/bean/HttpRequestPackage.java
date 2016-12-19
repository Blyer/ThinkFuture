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
     * 本次请求的标识ID
     */
    public int id;
    /**
     * 本次请求的地址
     */
    public String url = "";
    /**
     * 请求的方式
     */
    public HttpMethod method = HttpMethod.JSON;
    /**
     * 是否静默请求，即不弹出加载中对话框
     */
    public boolean isSilentRequest = false;
    /**
     * 本次请求的数据缓存时间，单位为毫秒
     * 如果为0，则意味着不缓存
     */
    public int cacheTime = 0;
    /**
     * 字符串参数
     */
    public HashMap<String, Object> params = new HashMap<>();
    /**
     * 待上传文件的路径集合
     */
    public ArrayList<String> filePaths = new ArrayList<>();

    /**
     * 标识此请求的唯一键值
     */
    public int uniKey;

    /**
     * 此请求是否有限制，默认没有
     */
    public boolean isLimit = true;

    public void calculateUnikey() {
        int result = id;
        result = 31 * result + url.hashCode();
        result = 31 * result + params.hashCode();
        result = 31 * result + filePaths.hashCode();
        uniKey = result;
    }
}
