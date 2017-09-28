package com.spy.utility.spider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 读取网页
 * create by shipy on 2017/9/28 0:54
 */
public class ReadHtml {
    private static Log log = LogFactory.getLog(ReadHtml.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
    //    private static String local_path = "G:\\AAA_Data\\www.meinvtupianku.com\\"; // 保存路径
    private static String local_path = "/Users/shipy/Desktop/spy/AAA_Data/www.meinvtupianku.com/"; // 保存路径
    private static String net_url = "http://www.meinvtupianku.com"; // 网址

    public static void main(String[] args) {
        getUrl(net_url);
    }

    /**
     * 输入网址，解析网页
     *
     * @param url 原网址
     */
    public static void getUrl(String url) {
        try {
            // 开始时间
            Date beginDate = new Date();
            log.info("开始爬---" + sdf.format(beginDate));

            Connection connect = Jsoup.connect(url);
            Document document = connect.get();
            // 分离出html下的<a>...</a>之间的所有东西
            Elements links = document.select("div.pin-coat > a.imageLink");
            for (Element link : links) {
                // 得到<a>...</a>里面的网址
                String linkHref = link.attr("href");
                Elements img = link.select("img");
                String titleUrl = img.attr("original"); // 标题图片
                String titleName = img.attr("alt"); // 标题
                getUrl(titleName, titleUrl, linkHref);
//                return;
            }
            Date endDate = new Date();
            log.info("爬完了---" + sdf.format(endDate));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 二级网址
     *
     * @param name   标题
     * @param imgUrl 标题图片
     * @param url    点击链接
     */
    public static void getUrl(String name, String imgUrl, String url) {
        try {
            // 以标题为名创建文件夹
            File folder = new File(local_path + name); // 文件夹
            if (!folder.exists()) {
                folder.mkdirs();
            }
            SavePicture.save(net_url, imgUrl, local_path + name + File.separator);
            // 开始解析点击进入的网页
            try {
                Document doc = getUrl(name, url);
                // 获取页码栏元素，遍历
                Elements linkPageses = doc.select("div.link_pages");
                for (Element linkPages : linkPageses) {
                    // 获取<a>...</a>
                    Elements as = linkPages.select("a");
                    for (Element a : as) {
                        String aHref = a.attr("href");
                        getUrl(name, aHref);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("遍历" + name + "结束");
    }

    /**
     * 读分页的图片（图片下方的页码）
     *
     * @param name 文件夹名
     * @param url  页码的url
     */
    public static Document getUrl(String name, String url) {
        try {
            Connection connect = Jsoup.connect(url);
            Document doc = connect.get();
            // 获取当前图片元素
            Elements main_body = doc.select("div.main-body");
            for (Element element : main_body) {
                Elements imgs = element.select("img");
                for (Element img : imgs) {
                    String src = img.attr("src");
                    SavePicture.save(net_url, src, local_path + name + File.separator);
                }
            }
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
