package org.base.platform.enums;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    JSON("JSON");

    private String methods;

    HttpMethod(String methods) {
        this.methods = methods;
    }
}
