package com.spy.utility.spider.amuxs;

import com.spy.utility.rw.WriteFile;

import org.apache.commons.logging.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * 阿木小说
 * Created by shipy on 2017/10/5..
 */

public class AmuxsHandler {
    private static final Log log = getLog(AmuxsHandler.class);

    // 第一章url
    public static final String NET_URL0 = "http://www.amuxs.net/";
    public static final String NET_URL  = "http://www.amuxs.net/reader/92/99113.html";
    // 本地目录
    public static final String PATH     = "G:\\AAA_Data\\www.amuxs.net";

    public static void main(String[] args) {
        File folder = new File(PATH); // 目录
        if (!folder.exists()) {
            folder.mkdirs();
        }
        readSection(NET_URL);
    }

    /**
     * 读章节，从第一章开始
     */
    public static void readSection(String url) {
        Connection connect = Jsoup.connect(url);
        Document document = null;
        try {
            document = connect.get();
        } catch (IOException e) {
            log.error("url: " + url);
            e.printStackTrace();
            return;
        }
        // 获取标题所在的div
        Element readtitle_div = document.select("div.readtitle").first();
        if (readtitle_div == null) {
            return;
        }
        // 获取标题span
        Element title_span = readtitle_div.select("span").first();
        if (title_span == null) {
            return;
        }
        // 得到标题
        String title = title_span.text();

        // 章节标题
        String section_title = document.select("div.list_read_title>h1").first().text();
        log.info(title + "--- " + section_title + " : " + url);

        // 得到内容span
        Element content_span = document.select("div.content>span").first();
        // 文本内容
        String content = content_span.text();

        // 将标题加入文本
        content = section_title + content;
        // TODO 换行
        content = content.replaceAll("  ", System.getProperty("line.separator"));
        // 写文件
        WriteFile.write3(PATH + File.separator + title + ".txt", content);

        // 下一章
        Element next_a = document.select("div.jump>a").last();
        String next_href = next_a.attr("href");
        next_href = NET_URL0 + next_href;
        readSection(next_href);
    }
}
