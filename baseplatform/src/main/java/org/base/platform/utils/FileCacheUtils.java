package org.base.platform.utils;

import com.apkfuns.logutils.LogUtils;

import org.base.platform.utils.cachehelper.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by YinShengyi on 2016/12/12.
 */
public class FileCacheUtils {

    public static final String FILE_CACHE = "cache_files";
    public static final String ERROR_LOG = "error_log";

    private DiskLruCache mDiskLruCache;

    public FileCacheUtils() {
    }

    /**
     * 开启DiskLruCache工具
     */
    public void open(String cacheDirectory) {
        try {
            String cacheDir = BaseUtils.getCachePath() + File.separator + "lru_cache" + File.separator + cacheDirectory;
            File cacheFile = new File(cacheDir);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheFile, BaseUtils.getAppVersionCode(), 1, 50 * 1024 * 1024);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    /**
     * 关闭DiskCache工具，无法继续使用
     * 一般在onDestroy中调用
     */
    public void close() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.close();
            } catch (Exception e) {
                LogUtils.e(e);
            }
            mDiskLruCache = null;
        }
    }

    /**
     * 将缓存信息同步至日志文件中
     * 一般在onPause中调用
     */
    public void flush() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    }

    /**
     * 获取当前缓存目录的总大小，单位为字节
     */
    public long getCacheSize() {
        if (mDiskLruCache != null) {
            return mDiskLruCache.size();
        }
        return 0;
    }

    /**
     * 清空缓存
     */
    public void clear() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.delete();
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
    }

    /**
     * 写入文本文件
     */
    public void write(String fileName, String content) {
        if (mDiskLruCache == null || StringUtils.isNull(content)) {
            return;
        }
        LogUtils.d("write cache to file '" + fileName + "' : " + content);
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(fileName);
            if (editor != null) {
                out = editor.newOutputStream(0);
                out.write(content.getBytes());
                editor.commit();
            }
        } catch (Exception e) {
            try {
                editor.abort();
            } catch (IOException ioe) {
                LogUtils.e(ioe);
            }
            LogUtils.e(e);
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
    }

    /**
     * 读取文本文件
     */
    public String read(String fileName) {
        String result = "";
        if (mDiskLruCache != null) {
            InputStream in = null;
            ByteArrayOutputStream outStream = null;
            try {
                DiskLruCache.Snapshot snapShot = mDiskLruCache.get(fileName);
                if (snapShot != null) {
                    in = snapShot.getInputStream(0);
                    outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[512];
                    int length = -1;
                    while ((length = in.read(buffer)) != -1) {
                        outStream.write(buffer, 0, length);
                    }
                    result = outStream.toString();
                }
            } catch (Exception e) {
                LogUtils.e(e);
            } finally {
                try {
                    if (outStream != null)
                        outStream.close();
                    if (in != null)
                        in.close();
                } catch (IOException e) {
                    LogUtils.e(e);
                }
            }
        }
        LogUtils.d("read cache : " + result);
        return result;
    }

    /**
     * 移除缓存
     */
    public void remove(String fileName) {
        if (mDiskLruCache == null) {
            return;
        }
        try {
            mDiskLruCache.remove(fileName);
        } catch (IOException e) {
            LogUtils.e(e);
        }
    }
}
