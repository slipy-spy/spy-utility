package com.spy.utility.spider.m_m99_info;

import com.spy.utility.spider.bean.ImgBean;
import com.spy.utility.spider.cache.SpiderCache;

import org.apache.commons.logging.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * m.m99.info/cat1 诱惑网
 * Created by shipy on 2017/10/8..
 */

public class MM99InfoHandler {
    private static final Log    log          = getLog(MM99InfoHandler.class);
    // 保存路径
    public static        String local_path   = "G:\\AAA_Data\\m.m99.info\\";
    // 第一页URl
    public static final  String NET_URL      = "http://m.m99.info/cat1";
    // 网页页码
    private static       int    page_num     = 0;
    // 标题页个数
    private static       int    num_biaotiye = 0;

    public static void main(String[] args) {
        step1ReadNextPage(NET_URL);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Document document = SpiderCache.removeFirstMainDoc();
        step2ReadTitlePic(document);
    }

    /**
     * 读取每一页，将页面Document对象存入缓存
     *
     * @param url
     */
    public static void step1ReadNextPage(String url) {
        if (url == null || "".equals(url)) {
            return;
        }
        try {
            Connection connect = Jsoup.connect(url);
            Document document = null;
            document = connect.get();

            page_num++;

            log.info("将分页网页的Document对象存入缓存： " + url + "   page_num:" + page_num);
            SpiderCache.setMainDocList(document); // 存入缓存

            // 进入下一页的<a>...</a>
            Element nextPage_a = document.select("a.nextp").first();
            if (nextPage_a == null) {
                log.warn("nextPage_a==null");
                log.warn(document.body());
                return;
            }
            // 下一页的url
            String nextPage_href = nextPage_a.attr("href");
            if (StringUtil.isBlank(nextPage_href)) {
                log.warn("StringUtil.isBlank(nextPage_href)");
                log.warn(document.body());
                return;
            }
            // 回调，先睡会，防止网站会禁止频繁访问
            //            try {
            //                Thread.sleep(5 * 1000);
            //            } catch (InterruptedException e) {
            //                e.printStackTrace();
            //            }
            //            step1ReadNextPage(nextPage_href);
        } catch (IOException e) {
            log.error("url:" + url);
            e.printStackTrace();
        }
    }

    /**
     * 读取标题图片点击的url
     *
     * @param document
     */
    public static void step2ReadTitlePic(Document document) {
        if (document == null) {
            return;
        }
        // 分离出html下的<a>...</a>之间的所有东西
        Elements links = document.select("ul.listpic>li>a");
        for (Element link : links) {
            // 得到<a>...</a>里面的网址
            String linkHref = link.attr("href");
            Elements img = link.select("img");
            String titleUrl = img.attr("src"); // 标题图片
            String titleName = img.attr("alt"); // 标题

            num_biaotiye++;

            log.info("将  " + titleName + "  放入标题网址缓存" + "    num_biaotiye:" + num_biaotiye);

            // 以标题为名创建文件夹
            File folder = new File(local_path + titleName); // 文件夹
            if (!folder.exists()) {
                folder.mkdirs();
            } else {
                // 已存在，则跳过，不再解析此标签内的图片
                return;
            }

            String path = local_path + titleName + File.separator; // 文件夹路径

            // 将标题入口图片放入待下载图片集合缓存
            ImgBean imgBean = new ImgBean(null, titleUrl, path);
            SpiderCache.setImgBeanList(imgBean);

            // 将标题入口的点击进入的网页的对象放入标题集合缓存
            ImgBean titleBean = new ImgBean("", linkHref, path);
            SpiderCache.setTitleBeanList(titleBean);
        }
    }

    /**
     * 读取点击标题进入的网页，缓存其中的每个图片
     *
     * @param titleBean
     */
    public static void step3ReadEachTitlePage(ImgBean titleBean) {
        if (titleBean == null) {
            return;
        }
        String url = titleBean.getUrl();
        try {
            Connection connect = Jsoup.connect(url);
            Document document = null;
            document = connect.get();

            Element pbox_div = document.getElementById("pbox");
            Elements imgs = pbox_div.select("img");
            for (Element img : imgs) {
                String img_src = img.attr("src"); // 图片url
                String img_alt = img.attr("alt"); // 图片名称

                // 将图片对象放入待下载图片集合缓存
                ImgBean imgBean = new ImgBean(null, img_src, titleBean.getPath());
                SpiderCache.setImgBeanList(imgBean);
                return;
            }
        } catch (Exception e) {
            log.error("url:" + url);
            e.printStackTrace();
        }
    }

}
