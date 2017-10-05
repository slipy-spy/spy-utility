package com.spy.utility.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by shipy on 2017/10/5..
 */

public class Test2 {
    public static void main(String[] args) {
        String url = "http://www.meinvtupianku.com/xgmote/20171004/18209_2.html";
        try {
            Connection connect = Jsoup.connect(url);
            Document document = connect.get();
            Elements h2_tags = document.select("h2.main-title");
            for (Element h2_tag : h2_tags) {
                System.out.println(h2_tag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
