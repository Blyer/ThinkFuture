package org.base.platform.bean;

import java.io.Serializable;

/**
 * Created by YinShengyi on 2016/12/20.
 * 总线消息传递对象
 */
public class MessageEvent implements Serializable {
    public int id;
    public Object data;
    public Object extraData;
}
