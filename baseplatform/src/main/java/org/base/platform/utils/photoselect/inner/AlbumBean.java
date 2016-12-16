package org.base.platform.utils.photoselect.inner;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册bean
 */
public class AlbumBean {

    private String name; // 相册名字
    private String count; // 相册图片数量
    private int photoId; // 首张图片的缩略图的id
    private String photoPath; // 首张图片的缩略图路径
    private List<PhotoBean> photoList = new ArrayList<PhotoBean>(); // 相册中的所有图片bean集合

    public AlbumBean() {
    }

    public AlbumBean(String name, String count, int photoId, String photoPath) {
        super();
        this.name = name;
        this.count = count;
        this.photoId = photoId;
        this.photoPath = photoPath;
    }

    public List<PhotoBean> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<PhotoBean> bitList) {
        this.photoList = bitList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
