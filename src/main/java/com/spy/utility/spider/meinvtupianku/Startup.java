package com.spy.utility.spider.meinvtupianku;

import com.spy.utility.spider.SavePicture;
import com.spy.utility.spider.bean.ImgBean;
import com.spy.utility.spider.cache.SpiderCache;

import org.apache.commons.logging.Log;
import org.jsoup.nodes.Document;

import java.util.Date;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * 启动类
 * Created by shipy on 2017/10/5..
 */

public class Startup {
    private static final Log log = getLog(Startup.class);

    public static void main(String[] args) {
        Date begindate = new Date();
        log.info("===========开始爬虫 " + ReadHtml.sdf.format(begindate) + "=============");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                log.info("开始解析，获取主网页对象并缓存");
                ReadHtml2.getMainDocument(ReadHtml.net_url);
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                boolean stop = false; // 本线程停止标志
                int num = 0; // 大于5时，说明确实没有为遍历的主网页了，停止此线程
                log.info("开始解析主网页，获取标题对象，主网页个数：" + SpiderCache.getMainDocListSize());
                while (true) {
                    if (SpiderCache.getMainDocListSize() > 0) {
                        num = 0; // 重置标志
                        Document document = SpiderCache.removeFirstMainDoc();
                        ReadHtml2.getTitileDocument(document);
                    } else {
                        if (num >= 5) {
                            stop = true;
                            continue;
                        }
                        num++;
                        log.info("没有缓存的主网页，睡10秒");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t2.start();

        Runnable r3 = new Runnable() {
            public void run() {
                boolean stop = false; // 本线程停止标志
                int num = 0; // 大于5时，说明确实没有为遍历的主网页了，停止此线程
                log.info("开始解析标题对象，获取图片对象，标题对象个数：" + SpiderCache.getTitleBeancListSize());
                while (true) {
                    if (SpiderCache.getTitleBeancListSize() > 0) {
                        num = 0; // 重置标志
                        ImgBean titleBean = SpiderCache.removeFirstTitleBean();
                        ReadHtml2.getImgBeanFromTitle(titleBean);
                    } else {
                        if (num >= 5) {
                            stop = true;
                            continue;
                        }
                        num++;
                        log.info("没有缓存的标题网页对象，先睡20s");
                        try {
                            Thread.sleep(20000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread t3_1 = new Thread(r3);
        t3_1.start();
        Thread t3_2 = new Thread(r3);
        t3_2.start();

        Runnable r4 = new Runnable() {
            public void run() {
                boolean stop = false; // 本线程停止标志
                int num = 0; // 大于5时，说明确实没有为遍历的主网页了，停止此线程
                log.info("开始解析图片对象，保存图片，图片对象个数：" + SpiderCache.getImgBeanListSize());
                while (!stop) {
                    if (SpiderCache.getImgBeanListSize() > 0) {
                        num = 0; // 重置标志
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
                            log.error("保存图片失败：" + url);
                            e.printStackTrace();
                        }
                    } else {
                        if (num >= 5) {
                            stop = true;
                            continue;
                        }
                        num++;
                        log.info("没有缓存的图片对象，先睡30s");
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread t4_1 = new Thread(r4);
        t4_1.start();
        Thread t4_2 = new Thread(r4);
        t4_2.start();
        Thread t4_3 = new Thread(r4);
        t4_3.start();
    }
}
