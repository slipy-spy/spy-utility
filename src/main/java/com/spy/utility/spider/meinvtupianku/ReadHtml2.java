package com.spy.utility.spider.meinvtupianku;

import com.spy.utility.spider.bean.ImgBean;
import com.spy.utility.spider.cache.SpiderCache;

import org.apache.commons.logging.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * 读取网页源代码
 * Created by shipy on 2017/9/29..
 */

public class ReadHtml2 {
    private static final Log log           = getLog(ReadHtml2.class);
    public static        int num_zhuwangye = 0; // 主网页个数
    public static        int num_biaotiye  = 0; // 标题页个数
    public static        int num_imgobj    = 0; // 缓存的图片对象的个数

    public static void main(String[] args) {
        ImgBean imgBean = new ImgBean(
                "",
                "http://www.meinvtupianku.com/xgmote/20171004/18209.html",
                "");
        getImgBeanFromTitle(imgBean);
    }

    /**
     * 获取该网页的document对象，存入缓存
     *
     * @param url 网站的入口，即分页的第一页的url
     */
    public static void getMainDocument(String url) {
        if (url == null || "".equals(url)) {
            return;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Connection connect = Jsoup.connect(url);
            Document document = null;
            document = connect.get();

            num_zhuwangye++;

            log.info("将分页网页的Document对象存入缓存： " + url + "   num_zhuwangye:" + num_zhuwangye);
            SpiderCache.setMainDocList(document); // 存入缓存

            Elements pageClicks = document.select("div#pagenavi > *");
            String nextUrl = null;
            boolean flag = false;
            for (Element pageClick : pageClicks) {
                if (flag) {
                    nextUrl = pageClick.attr("href");
                    break;
                }
                boolean isSpan = pageClick.is("span"); // 当前页
                if (isSpan) {
                    flag = true;
                }
            }
            if (nextUrl != null) {
                getMainDocument(nextUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 每个主页面，点击标题图片，进入的网址的缓存
     *
     * @param document 分页网页的Document对象
     */
    public static void getTitileDocument(Document document) {
        if (document == null) {
            return;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 分离出html下的<a>...</a>之间的所有东西
        Elements links = document.select("div.pin-coat > a.imageLink");
        for (Element link : links) {
            // 得到<a>...</a>里面的网址
            String linkHref = link.attr("href");
            Elements img = link.select("img");
            String titleUrl = img.attr("original"); // 标题图片
            String titleName = img.attr("alt"); // 标题

            num_biaotiye++;

            log.info("将  " + titleName + "  放入标题网址缓存" + "    num_biaotiye:" + num_biaotiye);

            // 以标题为名创建文件夹
            File folder = new File(ReadHtml.local_path + titleName); // 文件夹
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String path = ReadHtml.local_path + titleName + File.separator; // 文件夹路径

            // 将标题入口图片放入待下载图片集合缓存
            ImgBean imgBean = new ImgBean(ReadHtml.net_url, titleUrl, path);
            SpiderCache.setImgBeanList(imgBean);

            // 将标题入口的点击进入的网页的对象放入标题集合缓存
            ImgBean titleBean = new ImgBean("", linkHref, path);
            SpiderCache.setTitleBeanList(titleBean);
        }
    }

    /**
     * 解析标题点击进入的网页，获取图片对象，放入图片缓存集合
     *
     * @param titleBean 标题点击进入的网页的对象
     */
    public static void getImgBeanFromTitle(ImgBean titleBean) {
        if (titleBean == null) {
            return;
        }
        try {
            Document doc = getUrl(titleBean);
            if (doc == null) {
                return;
            }
            // 获取页码栏<div>的第一个直接子元素<span>，即当前页码
            Element cur_span = doc.select("div.link_pages > span").first();
            // 获取下一页的<a>
            Element next_page_a = cur_span.nextElementSibling();
            if (next_page_a == null) {
                return;
            }
            String aHref = next_page_a.attr("href");
            //                    getUrl(titleBean);
            ImgBean iib2 = new ImgBean(titleBean.getDomain(), aHref, titleBean.getPath());
            getImgBeanFromTitle(iib2);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读分页的图片（图片下方的页码）
     *
     * @param titleBean 标题点击进入的网页参数对象
     */
    public static Document getUrl(ImgBean titleBean) {
        if (titleBean == null) {
            return null;
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = titleBean.getUrl();
        String path = titleBean.getPath(); // 图片保存的目录
        Connection connect = null;
        try {
            connect = Jsoup.connect(url);
        } catch (Exception e) {
            log.error(url);
            e.printStackTrace();
            return null;
        }
        Document doc = null;
        while (doc == null) {
            try {
                doc = connect.get();
            } catch (SocketTimeoutException e1) {
                doc = null;
                e1.printStackTrace();
            } catch (ConnectException e2) {
                doc = null;
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        }
        try {
            // 获取当前图片元素
            Element main_body = doc.select("div.main-body").first();
            Element img = main_body.select("img").first();
            String src = img.attr("src");

            num_imgobj++;
            log.info("当前线程：" + Thread.currentThread().getName()
                    + "   缓存的图片对象个数num_imgobj：" + num_imgobj
                    + "   " + src);

            // 将图片对象放入待下载图片集合缓存
            ImgBean imgBean = new ImgBean(ReadHtml.net_url, src, path);
            SpiderCache.setImgBeanList(imgBean);
            return doc;
        } catch (Exception e) {
            log.error(url);
            e.printStackTrace();
        }
        return null;
    }

}
