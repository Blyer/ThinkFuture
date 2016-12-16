package org.base.platform.utils.photoselect.inner;

import java.io.Serializable;

/**
 * 图片bean
 */
public class PhotoBean implements Serializable {

    private int photoId; // 该图片对应的id
    private boolean select; // 是否已选择
    private String path; // 图片的路径

    public PhotoBean(int photoId, String path) {
        this.photoId = photoId;
        select = false;
        this.path = path;
    }

    public PhotoBean(int photoId, boolean flag) {
        this.photoId = photoId;
        select = flag;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

}
