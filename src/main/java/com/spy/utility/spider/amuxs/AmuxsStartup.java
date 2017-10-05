package com.spy.utility.spider.amuxs;

import org.apache.commons.logging.Log;

import java.io.File;

import static org.apache.commons.logging.LogFactory.getLog;

/**
 * 启动类
 * Created by shipy on 2017/10/5..
 */

public class AmuxsStartup {
    private static final Log log = getLog(AmuxsStartup.class);

    private static final String b002 = "http://www.amuxs.net/reader/82/93879.html";

    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            public void run() {
                File folder = new File(AmuxsHandler.PATH); // 目录
                if (!folder.exists()) {
                    folder.mkdirs();
                }
//                AmuxsHandler.readSection(AmuxsHandler.NET_URL);
                AmuxsHandler.readSection(b002);
            }
        };
        Thread t1 = new Thread(r1);
        t1.start();
    }
}
