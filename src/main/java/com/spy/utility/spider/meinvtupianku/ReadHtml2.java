package com.spy.utility.spider.meinvtupianku;

import com.spy.utility.spider.SavePicture;
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
import java.util.Date;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * 读取网页源代码
 * Created by shipy on 2017/9/29..
 */

public class ReadHtml2 {
    private static final Log log = getLog(ReadHtml2.class);

    public static void main(String[] args) {
        Date begindate = new Date();
        log.info("===========开始爬虫 " + ReadHtml.sdf.format(begindate) + "=============");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                log.info("开始解析，获取主网页对象并缓存");
                getMainDocument(ReadHtml.net_url);
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                while (SpiderCache.getMainDocListSize() < 1) {
                    log.info("主网页个数小于1，先睡1s");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("开始解析主网页，获取标题对象，主网页个数：" + SpiderCache.getMainDocListSize());
                while (SpiderCache.getMainDocListSize() > 0) {
                    Document document = SpiderCache.removeFirstMainDoc();
                    getTitileDocument(document);
                }
            }
        });
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            public void run() {
                while (SpiderCache.getTitleBeancListSize() < 1) {
                    log.info("标题对象个数小于1，先睡1s");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("开始解析标题对象，获取图片对象，标题对象个数：" + SpiderCache.getTitleBeancListSize());
                while (SpiderCache.getTitleBeancListSize() > 0) {
                    ImgBean titleBean = SpiderCache.removeFirstTitleBean();
                    getImgBeanFromTitle(titleBean);
                }
            }
        });
        t3.start();

        Thread t4 = new Thread(new Runnable() {
            public void run() {
                while (SpiderCache.getImgBeanListSize() < 1) {
                    log.info("图片对象个数小于1，先睡1s");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("开始解析图片对象，保存图片，图片对象个数：" + SpiderCache.getImgBeanListSize());
                while (SpiderCache.getImgBeanListSize() > 0) {
                    ImgBean imgBean = SpiderCache.removeFirstImgBean();
                    if (imgBean == null) {
                        continue;
                    }
                    String domain = imgBean.getDomain();
                    String url = imgBean.getUrl();
                    String path = imgBean.getPath();
                    try {
                        log.info("保存图片： " + url);
                        SavePicture.save(domain, url, path);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t4.start();
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
            Connection connect = Jsoup.connect(url);
            Document document = null;
            document = connect.get();

            log.info("将分页网页的Document对象存入缓存： " + url);
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
        // 分离出html下的<a>...</a>之间的所有东西
        Elements links = document.select("div.pin-coat > a.imageLink");
        for (Element link : links) {
            // 得到<a>...</a>里面的网址
            String linkHref = link.attr("href");
            Elements img = link.select("img");
            String titleUrl = img.attr("original"); // 标题图片
            String titleName = img.attr("alt"); // 标题
            log.info("将  " + titleName + "  放入标题网址缓存");

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
            // 获取页码栏元素，遍历
            Elements linkPageses = doc.select("div.link_pages");
            for (Element linkPages : linkPageses) {
                // 获取<a>...</a>
                Elements as = linkPages.select("a");
                for (Element a : as) {
                    String aHref = a.attr("href");
                    getUrl(titleBean);
                }
            }
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
        String url = titleBean.getUrl();
        String path = titleBean.getPath(); // 图片保存的目录
        try {
            Connection connect = Jsoup.connect(url);
            Document doc = connect.get();
            // 获取当前图片元素
            Elements main_body = doc.select("div.main-body");
            for (Element element : main_body) {
                Elements imgs = element.select("img");
                for (Element img : imgs) {
                    String src = img.attr("src");
                    // 将图片对象放入待下载图片集合缓存
                    ImgBean imgBean = new ImgBean(ReadHtml.net_url, src, path);
                    SpiderCache.setImgBeanList(imgBean);
                }
            }
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
