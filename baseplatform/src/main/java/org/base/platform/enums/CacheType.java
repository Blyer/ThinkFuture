package org.base.platform.enums;

/**
 * Created by YinShengyi on 2016/12/19.
 */
public enum CacheType {
    FILE("cache_files"), // 文件缓存目录
    ERROR_LOG("error_log"), // 错误日志保存目录
    PICTURE("cache_pictures"); // 图片缓存目录

    private String cacheDirectory;

    CacheType(String directory) {
        cacheDirectory = directory;
    }

    public String getCacheDirectory() {
        return cacheDirectory;
    }
}
