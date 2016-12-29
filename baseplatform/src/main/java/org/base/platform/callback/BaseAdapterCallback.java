package org.base.platform.callback;

import java.util.List;

/**
 * Created by YinShengyi on 2016/12/29.
 */
public interface BaseAdapterCallback<T> {
    /**
     * 清空数据源，重新复位为data的数据
     */
    void clearTo(List<T> data);

    /**
     * 将data中的数据添加到数据源中
     */
    void append(List<T> data);

    /**
     * 获取数据源
     */
    List<T> getData();
}
