package org.base.platform.enums;

/**
 * Created by YinShengyi on 2016/12/19.
 */
public enum CacheType {
    FILE("cache_files", 50 * 1024 * 1024), // 文件缓存目录，大小50MB
    ERROR_LOG("error_log", 2 * 1024 * 1024), // 错误日志保存目录，大小2MB
    PICTURE("cache_pictures", 50 * 1024 * 1024); // 图片缓存目录，大小50MB

    private String cacheDirectory;
    private long cacheSize;

    CacheType(String directory, int size) {
        cacheDirectory = directory;
        cacheSize = size;
    }

    public String getCacheDirectory() {
        return cacheDirectory;
    }

    public long getCacheSize() {
        return cacheSize;
    }
}
