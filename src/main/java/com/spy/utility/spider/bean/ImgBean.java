package com.spy.utility.spider.bean;

/**
 * 网络图片对象
 * Created by shipy on 2017/9/29..
 */

public class ImgBean {
    private String domain; // 域名,http://www.baidu.com
    private String url; // 图片的url
    private String path; // 本地保存的文件夹的路径

    public ImgBean(String domain, String url, String path) {
        this.domain = domain;
        this.url = url;
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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
}
