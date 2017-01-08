package org.base.platform.utils;

import android.util.SparseArray;

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

    private static int[] sUnifyProcessCodes = new int[]{1, 2, 3}; // 统一处理的返回码

    private SparseArray<Callback.Cancelable> mRequests = new SparseArray<>(); // 正在进行中的请求

    public Callback.Cancelable request(final HttpRequestPackage httpRequestPackage, final OnRequestListener listener) {
        cancelSameRequest(httpRequestPackage);
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
                mRequests.remove(httpRequestPackage.hashCode());
                if (listener != null) {
                    listener.finish();
                }
            }

            private boolean isCodeUnifyProcess(int code) {
                for (int i = 0; i < sUnifyProcessCodes.length; ++i) {
                    if (sUnifyProcessCodes[i] == code) {
                        return true;
                    }
                }
                return false;
            }

            private void handleResult(String result) {
                if (!StringUtils.isNull(result)) {
                    LogUtils.json(result);
                    ResponseResult bean = JSONObject.parseObject(result, ResponseResult.class);
                    if (bean != null) {
                        if (listener != null) {
                            if (isCodeUnifyProcess(bean.getCode())) {
                                MessageEvent event = new MessageEvent();
                                event.id = MsgEventConstants.NET_REQUEST_RESULT;
                                event.data = bean;
                                MessageEventUtils.post(event);
                                listener.failed(bean.getMessage());
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
        mRequests.put(httpRequestPackage.hashCode(), cancelable);
        return cancelable;
    }

    /**
     * 取消已存在的相同的正在进行中的请求
     */
    private void cancelSameRequest(HttpRequestPackage httpRequestPackage) {
        Callback.Cancelable cancelable = mRequests.get(httpRequestPackage.hashCode());
        if (cancelable != null) {
            if (!cancelable.isCancelled()) {
                cancelable.cancel();
            }
        }
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

        void finish();
    }
}
