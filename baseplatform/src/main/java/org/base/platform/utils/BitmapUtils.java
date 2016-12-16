package org.base.platform.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapUtils {

    /**
     * @param path   图片的本地路径
     * @param width  要的宽度
     * @param height 要的高度
     */
    public static Bitmap getBitmap(String path, int width, int height) {
        if (path == null || "".equals(path.trim())) {
            return null;
        } else if (!path.startsWith("/")) {
            return null;
        }
        Options option = new Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);
        option.inSampleSize = calculateSampleSize(option, width, height);

        option.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, option);
        return rotateBitmapByDegree(bm, getBitmapDegree(path));
    }

    private static int calculateSampleSize(Options option, int width, int height) {
        int sampleSize = 1;
        if (option.outWidth > width && option.outHeight > height) {
            int widthRate = Math.round((float) option.outWidth / (float) width);
            int heightRate = Math.round((float) option.outHeight / (float) height);

            sampleSize = Math.max(widthRate, heightRate);
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
        }
        return sampleSize;
    }

    /**
     * @param path 图片的本地地址
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        if (bm == null) {
            return null;
        }
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        //将原始图片按照旋转矩阵进行旋转，并得到新的图片
        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (returnBm == null) {
            returnBm = bm;
        }

        return returnBm;
    }

}
