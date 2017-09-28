package com.spy.utility.spider.cache;

import com.spy.utility.spider.bean.ImgBean;

import org.apache.commons.logging.Log;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * 缓存类
 * Created by shipy on 2017/9/28..
 */

public class SpiderCache {
    private static final Log log = getLog(SpiderCache.class);

    /**
     * 首页的html解析对象list，存放的是所有分页的html解析对象（即：下一页。。。）
     */
    private static List<Document> mainDocList   = null;
    /**
     * 点击各图片标题所进入的网页的url的集合
     */
    private static List<ImgBean> titleBeanList = null;
    /**
     * 尚未下载的图片对象集合
     */
    private static List<ImgBean>  imgBeanList   = null;

    static {
        mainDocList = new ArrayList<Document>();
        titleBeanList = new ArrayList<ImgBean>();
        imgBeanList = new ArrayList<ImgBean>();
    }

    //-----mainDocList----------------------------------------------------
    //-----mainDocList----------------------------------------------------
    public synchronized static int getMainDocListSize() {
        if (mainDocList == null) {
            return 0;
        }
        return mainDocList.size();
    }

    public synchronized static List<Document> getMainDocList() {
        return mainDocList;
    }

    public synchronized static Document removeFirstMainDoc() {
        if (mainDocList == null || mainDocList.size() < 1) {
            return null;
        }
        Document doc = mainDocList.remove(0);
        return doc;
    }

    public synchronized static void setMainDocList(List<Document> mainDocList) {
        SpiderCache.mainDocList = mainDocList;
    }

    public synchronized static void setMainDocList(Document document) {
        if (mainDocList == null) {
            mainDocList = new ArrayList<Document>();
        }
        mainDocList.add(document);
    }

    //-----titleDocList----------------------------------------------------
    //-----titleDocList----------------------------------------------------
    public synchronized static int getTitleBeancListSize() {
        if (titleBeanList == null) {
            return 0;
        }
        return titleBeanList.size();
    }

    public synchronized static List<ImgBean> getTitleBeanList() {
        return titleBeanList;
    }

    public synchronized static ImgBean removeFirstTitleBean() {
        if (titleBeanList == null || titleBeanList.size() < 1) {
            return null;
        }
        ImgBean bean = titleBeanList.remove(0);
        return bean;
    }

    public synchronized static void setTitleBeanList(List<ImgBean> titleBeanList) {
        SpiderCache.titleBeanList = titleBeanList;
    }

    public synchronized static void setTitleBeanList(ImgBean titleBean) {
        if (titleBeanList == null) {
            titleBeanList = new ArrayList<ImgBean>();
        }
        titleBeanList.add(titleBean);
    }

    //-----imgBeanList----------------------------------------------------
    //-----imgBeanList----------------------------------------------------
    public synchronized static int getImgBeanListSize() {
        if (imgBeanList == null) {
            return 0;
        }
        return imgBeanList.size();
    }

    public synchronized static List<ImgBean> getImgBeanList() {
        return imgBeanList;
    }

    public synchronized static ImgBean removeFirstImgBean() {
        if (imgBeanList == null || imgBeanList.size() < 1) {
            return null;
        }
        ImgBean imgBean = imgBeanList.remove(0);
        return imgBean;
    }

    public synchronized static void setImgBeanList(List<ImgBean> imgBeanList) {
        SpiderCache.imgBeanList = imgBeanList;
    }

    public synchronized static void setImgBeanList(ImgBean imgBean) {
        if (imgBeanList == null) {
            imgBeanList = new ArrayList<ImgBean>();
        }
        imgBeanList.add(imgBean);
    }
}
