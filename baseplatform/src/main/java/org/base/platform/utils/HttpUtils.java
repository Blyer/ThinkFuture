package org.base.platform.utils;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.constants.MsgEventConstants;
import org.base.platform.enums.HttpMethod;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.ConnectException;

/**
 * Created by YinShengyi on 2017/1/6.
 */
public class HttpUtils {

    public Callback.Cancelable request(HttpRequestPackage httpRequestPackage, final OnRequestListener listener) {
        LogUtils.d(httpRequestPackage);
        RequestParams requestParams = parseParams(httpRequestPackage);
        org.xutils.http.HttpMethod method;
        if (httpRequestPackage.method == HttpMethod.GET) {
            method = org.xutils.http.HttpMethod.GET;
        } else {
            method = org.xutils.http.HttpMethod.POST;
            if (httpRequestPackage.method == HttpMethod.JSON) {
                requestParams.setAsJsonContent(true);
            }
        }
        Callback.Cancelable cancelable = x.http().request(method, requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                handleResult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e(ex);
                String reason;
                if (ex instanceof HttpException) { // 网络错误
                    reason = "服务器开小差了";
                } else if (ex instanceof ConnectException) {
                    reason = "您的手机网络不太顺畅喔";
                } else {
                    reason = "解析数据失败";
                }
                if (listener != null) {
                    listener.failed(reason);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            private void handleResult(String result) {
                if (!StringUtils.isNull(result)) {
                    LogUtils.json(result);
                    ResponseResult bean = JSONObject.parseObject(result, ResponseResult.class);
                    if (bean != null) {
                        if (listener != null) {
                            if (bean.getCode() == 1) {
                                MessageEvent event = new MessageEvent();
                                event.id = MsgEventConstants.NET_REQUEST_RESULT;
                                event.data = bean;
                                MessageEventUtils.post(event);
                                listener.failed("");
                            } else {
                                listener.success(bean);
                            }
                        }
                    } else {
                        if (listener != null) {
                            listener.failed("解析数据失败");
                        }
                    }
                }
            }
        });
        return cancelable;
    }

    private RequestParams parseParams(HttpRequestPackage httpRequestPackage) {
        RequestParams requestParams = new RequestParams(httpRequestPackage.url);
        for (String key : httpRequestPackage.params.keySet()) {
            Object value = httpRequestPackage.params.get(key);
            if (value != null) {
                requestParams.addBodyParameter(key, value.toString());
            }
        }
        if (httpRequestPackage.filePaths.size() > 0) {
            requestParams.setMultipart(true);
            for (int i = 1; i <= httpRequestPackage.filePaths.size(); ++i) {
                requestParams.addBodyParameter("file" + i, new File(httpRequestPackage.filePaths.get(i)));
            }
        }
        return requestParams;
    }

    public interface OnRequestListener {
        void success(ResponseResult result);

        void failed(String reason);
    }
}
