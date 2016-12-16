package org.base.platform.bean;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class ResponseResult {
    private int Code = -1;
    private String Message = "";
    private String ServerTime = "";
    private String Data = "";

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
