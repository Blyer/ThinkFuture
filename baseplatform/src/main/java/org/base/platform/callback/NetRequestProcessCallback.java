package org.base.platform.callback;

import org.base.platform.bean.ResponseResult;

/**
 * Created by YinShengyi on 2016/12/9.
 */
public interface NetRequestProcessCallback extends LoadingDialogShowCallback {

    /**
     * 处理网络请求完成后的回调
     *
     * @param id      此次网络请求的ID
     * @param result  请求得到的结果
     * @param isCache 请求的结果是否来自于缓存，true表示是缓存读取，false表示从网络读取
     */
    void processNetRequest(int id, ResponseResult result, boolean isCache);

}
