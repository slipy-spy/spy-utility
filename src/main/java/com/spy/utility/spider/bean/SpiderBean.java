package com.spy.utility.spider.bean;

/**
 * Created by shipy on 2017/10/5..
 */

public class SpiderBean {
    private int    flag; // 0-图片；1-下一级链接
    private String imgUrl; // 图片url
    private String url; // 链接url
    private String path; // 图片保存的路径
    private String name; // 图片或文件夹的名称

    public SpiderBean(int flag, String imgUrl, String url, String path, String name) {
        this.flag = flag;
        this.imgUrl = imgUrl;
        this.url = url;
        this.path = path;
        this.name = name;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
