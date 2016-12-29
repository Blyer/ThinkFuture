package org.base.platform.utils;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;

import org.base.platform.bean.HttpRequestPackage;
import org.base.platform.bean.MessageEvent;
import org.base.platform.bean.ResponseResult;
import org.base.platform.callback.NetRequestProcessCallback;
import org.base.platform.enums.HttpMethod;
import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.base.platform.constants.MsgEventConstants.NET_REQUEST_ERROR;

/**
 * Created by YinShengyi on 2016/11/29.
 */
public class HttpUtils {

    private static final int REQUEST_FINISHED = 1000;

    private NetRequestProcessCallback mNetProcessCallback;
    private Handler mHandler;
    private ArrayList<HttpRequestPackage> mRequestPackages = new ArrayList<>(); // 请求的队列
    private HashMap<Integer, Callback.Cancelable> mProcessingRequests = new HashMap<>(); // 正在进行中的请求
    private int mUnSilentRequestNum = 0; // 连接请求的需要显示加载框的数量

    public HttpUtils(NetRequestProcessCallback netProcessCallback) {
        mNetProcessCallback = netProcessCallback;
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case REQUEST_FINISHED:
                        if (mUnSilentRequestNum <= 0) {
                            mNetProcessCallback.closeLoadingDialog();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 添加一次请求
     */
    public void addRequest(HttpRequestPackage httpRequestPackage) {
        if (httpRequestPackage == null) {
            return;
        }
        httpRequestPackage.calculateUnikey();
        for (HttpRequestPackage v : mRequestPackages) {
            if (v.uniKey == httpRequestPackage.uniKey) {
                return;
            }
        }
        mRequestPackages.add(httpRequestPackage);
    }

    private RequestParams parseParams(HttpRequestPackage httpRequestPackage) {
        RequestParams requestParams = new RequestParams(httpRequestPackage.url);
        for (String key : httpRequestPackage.params.keySet()) {
            Object value = httpRequestPackage.params.get(key);
            if (value != null) {
                requestParams.addBodyParameter(key, value.toString());
            }
        }
        if (httpRequestPackage.cacheTime > 0) {
            requestParams.setCacheMaxAge(httpRequestPackage.cacheTime);
        }
        if (httpRequestPackage.filePaths.size() > 0) {
            requestParams.setMultipart(true);
            for (int i = 1; i <= httpRequestPackage.filePaths.size(); ++i) {
                requestParams.addBodyParameter("file" + i, new File(httpRequestPackage.filePaths.get(i)));
            }
        }
        return requestParams;
    }

    public void request() {
        for (int i = 0; i < mRequestPackages.size(); ++i) {
            HttpRequestPackage httpRequestPackage = mRequestPackages.get(i);
            if (!httpRequestPackage.isSilentRequest) {
                ++mUnSilentRequestNum;
            }
            begin(httpRequestPackage);
        }
        mRequestPackages.clear();
    }

    private void begin(final HttpRequestPackage httpRequestPackage) {
        LogUtils.d(httpRequestPackage);
        if (mUnSilentRequestNum > 0) {
            mNetProcessCallback.showLoadingDialog();
        }
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

        final Callback.Cancelable cancelable = x.http().request(method, requestParams, new Callback.CacheCallback<String>() {

            @Override
            public boolean onCache(String result) {
                handleResult(result, true);
                return false;
            }

            @Override
            public void onSuccess(String result) {
                handleResult(result, false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.e(ex);
                if (ex instanceof HttpException) { // 网络错误
                    ToastUtils.show("您的手机网络不太顺畅喔~");
                } else {
                    ToastUtils.show("解析数据失败");
                }
                MessageEvent event = new MessageEvent();
                event.id = NET_REQUEST_ERROR;
                event.extraData = httpRequestPackage.id;
                MessageEventUtils.post(event);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (!httpRequestPackage.isSilentRequest) {
                    --mUnSilentRequestNum;
                    mHandler.sendEmptyMessage(REQUEST_FINISHED);
                }
                mProcessingRequests.remove(httpRequestPackage.uniKey);
            }

            private void handleResult(String result, boolean isCache) {
                if (!StringUtils.isNull(result)) {
                    LogUtils.json(result);
                    ResponseResult bean = JSONObject.parseObject(result, ResponseResult.class);
                    if (bean != null) {
                        mNetProcessCallback.processNetRequest(httpRequestPackage.id, bean, isCache);
                    } else {
                        if (!isCache) {
                            ToastUtils.show("解析数据失败");
                        }
                    }
                }
            }
        });
        if (httpRequestPackage.isLimit) {
            mProcessingRequests.put(httpRequestPackage.uniKey, cancelable);
        }
    }

    public void cancelAllRequests() {
        for (Integer key : mProcessingRequests.keySet()) {
            if (mProcessingRequests.get(key) != null) {
                mProcessingRequests.get(key).cancel();
            }
        }
        mProcessingRequests.clear();
        mRequestPackages.clear();
        mUnSilentRequestNum = 0;
    }

    /**
     * 取消某个请求
     *
     * @param key 请求包的唯一键值
     */
    public void cancelRequest(int key) {
        Callback.Cancelable cancelable = mProcessingRequests.get(key);
        if (cancelable != null && !cancelable.isCancelled()) {
            cancelable.cancel();
        }
        mProcessingRequests.remove(key);
    }

}
