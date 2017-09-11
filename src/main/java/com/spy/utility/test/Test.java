package com.spy.utility.test;

import java.io.File;

/**
 * @author shipy on 2017年7月12日 下午11:51:03
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String fPath = "E:/迅雷下载/www.7kk.com/";
		String fName = "爆乳比基尼美女性感妩媚诱惑私房写真/0a31649640304619b2be84c52bda8.jpg";
		File file = new File(fPath + fName);
		System.out.println("getName:	" + file.getName());
		System.out.println("getAbsolutePath:	" + file.getAbsolutePath());
		System.out.println("getPath:	" + file.getPath());
	}

}
