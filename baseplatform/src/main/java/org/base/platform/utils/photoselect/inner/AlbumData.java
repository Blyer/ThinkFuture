package org.base.platform.utils.photoselect.inner;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumData {

    private static AlbumBean currentSelectedAlbum; // 当前选择的相册

    // 设置获取图片的字段信息
    private static final String[] STORE_IMAGES = {MediaStore.Images.Media.DISPLAY_NAME, // 显示的名称
            MediaStore.Images.Media.DATA, // 文件
            MediaStore.Images.Media.DATE_ADDED, // 文件写入的时间
            MediaStore.Images.Media._ID, // id
            MediaStore.Images.Media.BUCKET_ID, // dir id 目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字
    };

    /**
     * 获取当前选择的相册bean
     */
    public static AlbumBean getCurrentSelectedAlbum() {
        if (currentSelectedAlbum == null) {
            currentSelectedAlbum = new AlbumBean();
        }
        return currentSelectedAlbum;
    }

    /**
     * 设置当前选择的相册
     */
    public static void setCurrentSelectedAlbum(AlbumBean bean) {
        currentSelectedAlbum = bean;
    }

    /**
     * 获取相册列表
     */
    public static List<AlbumBean> getAlbumList(Context context) {
        List<AlbumBean> albumList = new ArrayList<AlbumBean>();
        try {
            Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null, MediaStore.Images.Media.DATE_ADDED + " desc");
            Map<String, AlbumBean> countMap = new HashMap<String, AlbumBean>();
            AlbumBean pa = null;
            while (cursor.moveToNext()) {
                String path = cursor.getString(1);
                String id = cursor.getString(3);
                String dir_id = cursor.getString(4);
                String dir = cursor.getString(5);
                if (!countMap.containsKey(dir_id)) {
                    pa = new AlbumBean();
                    pa.setName(dir);
                    pa.setPhotoId(Integer.parseInt(id));
                    pa.setPhotoPath(path);
                    pa.setCount("1");
                    pa.getPhotoList().add(new PhotoBean(Integer.parseInt(id), path));
                    countMap.put(dir_id, pa);
                } else {
                    pa = countMap.get(dir_id);
                    pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
                    pa.getPhotoList().add(new PhotoBean(Integer.parseInt(id), path));
                }
            }
            cursor.close();
            Iterable<String> it = countMap.keySet();
            int pos = 0; // 截图插入的位置
            for (String key : it) {
                AlbumBean albumBean = countMap.get(key);
                String str = albumBean.getName();
                if (str.toLowerCase().contains("camera")) {
                    albumBean.setName("相册");
                    albumList.add(0, albumBean);
                    ++pos;
                } else if (str.toLowerCase().contains("pictures")) {
                    albumBean.setName("图片");
                    albumList.add(pos, albumBean);
                    ++pos;
                } else if (str.toLowerCase().contains("screenshots")) {
                    albumBean.setName("截图");
                    albumList.add(pos, albumBean);
                    ++pos;
                } else if (str.toLowerCase().contains("sdcard")) {
                    albumBean.setName("存储卡根目录");
                    albumList.add(pos, albumBean);
                    ++pos;
                } else {
                    albumList.add(albumBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            albumList.clear();
        }
        return albumList;
    }

}
