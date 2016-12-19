package org.base.platform.utils;

import org.base.platform.enums.CacheType;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YinShengyi on 2016/12/17.
 */
public class CrashLogUtils {

    public static void init() {
        final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                saveException(ex);
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }

    private static final void saveException(Throwable ex) {
        Writer writer = null;
        PrintWriter printWriter = null;
        String stackTrace = "";
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            stackTrace = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String currentTimeStamp = format.format(new Date());
        String filename = currentTimeStamp + ".txt";
        FileCacheUtils fileCacheUtils = new FileCacheUtils();
        fileCacheUtils.open(CacheType.ERROR_LOG);
        fileCacheUtils.write(filename, stackTrace);
        fileCacheUtils.flush();
        fileCacheUtils.close();
    }
}
