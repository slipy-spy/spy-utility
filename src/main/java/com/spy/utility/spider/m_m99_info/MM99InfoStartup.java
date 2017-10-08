package com.spy.utility.spider.m_m99_info;

import com.spy.utility.spider.SavePicture;
import com.spy.utility.spider.bean.ImgBean;
import com.spy.utility.spider.cache.SpiderCache;
import com.spy.utility.spider.meinvtupianku.ReadHtml;

import org.apache.commons.logging.Log;
import org.jsoup.nodes.Document;

import java.util.Date;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * Created by shipy on 2017/10/8..
 */

class MM99InfoStartup {
    private static final Log log = getLog(MM99InfoStartup.class);

    public static void main(String[] args) {
        Date begindate = new Date();
        log.info("===========开始爬虫 " + ReadHtml.sdf.format(begindate) + "=============");
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                log.info("开始解析，获取主网页对象并缓存");
                MM99InfoHandler.step1ReadNextPage(MM99InfoHandler.NET_URL);
            }
        });
        t1.start();

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                boolean stop = false; // 本线程停止标志
                int num = 0; // 大于5时，说明确实没有为遍历的主网页了，停止此线程
                log.info("开始解析主网页，获取标题对象，主网页个数：" + SpiderCache.getMainDocListSize());
                while (true) {
                    if (SpiderCache.getMainDocListSize() > 0) {
                        num = 0; // 重置标志
                        Document document = SpiderCache.removeFirstMainDoc();
                        MM99InfoHandler.step2ReadTitlePic(document);
                    } else {
                        if (num >= 5) {
                            stop = true;
                            continue;
                        }
                        num++;
                        log.info("没有缓存的主网页，睡5分钟");
                        try {
                            Thread.sleep(5 * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        t2.start();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Runnable r3 = new Runnable() {
            public void run() {
                boolean stop = false; // 本线程停止标志
                int num = 0; // 大于5时，说明确实没有为遍历的主网页了，停止此线程
                log.info("开始解析标题对象，获取图片对象，标题对象个数：" + SpiderCache.getTitleBeancListSize());
                while (true) {
                    if (SpiderCache.getTitleBeancListSize() > 0) {
                        num = 0; // 重置标志
                        ImgBean titleBean = SpiderCache.removeFirstTitleBean();
                        MM99InfoHandler.step3ReadEachTitlePage(titleBean);
                    } else {
                        if (num >= 5) {
                            stop = true;
                            continue;
                        }
                        num++;
                        log.info("没有缓存的标题网页对象，先睡4分钟");
                        try {
                            Thread.sleep(4 * 60 * 1000);
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

        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

                            Thread.sleep(3 * 1000); // 防止频繁访问网站
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
                        log.info("没有缓存的图片对象，先睡1分钟");
                        try {
                            Thread.sleep(1 * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Thread t4_1 = new Thread(r4);
        t4_1.start();
    }
}
